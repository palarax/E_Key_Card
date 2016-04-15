package palarax.e_key_card.CardReader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import palarax.e_key_card.NFC_Tag_Tech.IsoDepTag;
import palarax.e_key_card.NFC_Tag_Tech.NdefTag;
import palarax.e_key_card.NFC_Tag_Tech.nfcATag;
import palarax.e_key_card.R;
import palarax.e_key_card.adapters.CardObject;
import palarax.e_key_card.adapters.RecyclerAdapter_Scroller;
import palarax.e_key_card.adapters.helper.OnStartDragListener;
import palarax.e_key_card.adapters.helper.SimpleItemTouchHelperCallback;
import palarax.e_key_card.adapters.newDialog;
import palarax.e_key_card.entities.DataFactory;
import palarax.e_key_card.entities.OpalCard;

/**
 * @author Ilya Thai
 */
public class nfcCard extends Fragment implements nfcCardReader.AccountCallback,View.OnClickListener, palarax.e_key_card.CardReader.writeOptionDialog.dialogDoneListener, OnStartDragListener {



    private ItemTouchHelper mItemTouchHelper;
    public static final String TAG = "NFC_Card";
    public static final int MSG_RECORD = 1;
    public static final int MSG_OVERWRITE = 2;
    public static final int MSG_CLEAR = 0;
    private static final int FREE_READ_FILE_NUMBER = 7;

    private View mainView;          //Main view displayed
    private int viewID,msgOption;
    private RecyclerAdapter_Scroller cardInfo ;

    private String msgType, msgRecord;

    private boolean overwriteMsg = false; //boolean to check if a "overwrite" message is ready to be sent

    private FragmentManager fm ;
    private newDialog editNameDialog;

    // Recommend NfcAdapter flags for reading from other Android devices. Indicates that this
    // activity is interested in NFC-A devices (including other Android devices), and that the
    // system should not check for the presence of NDEF-formatted data (e.g. Android Beam).
    //| NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B | NfcAdapter.FLAG_READER_NFC_F | NfcAdapter.FLAG_READER_NFC_V;
    public nfcCardReader cardReader;

    /**
     * Called when sample is created. Displays generic UI with welcome text.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        cardInfo = new RecyclerAdapter_Scroller(getDataSet(),this);
        cardInfo.clearAll();
        super.onCreate(savedInstanceState);
    }

    private ArrayList<CardObject> getDataSet() {
        //Replace this code with the scanned data
        ArrayList results = new ArrayList<>();
        CardObject obj = new CardObject("-1", "-1","-1","-1","-1");
        results.add(0, obj);
        return results;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");
        mainView = inflater.inflate(viewID, container, false);
        //choose view
        choosingView(inflater, container);
        //creates a new cardReader object
        cardReader = new nfcCardReader(this);

        // Disable Android Beam and register our card reader callback
        enableReaderMode();

        return mainView;
    }

    /**
     * choosing scan or write view, handles recycler view
     * @param inflater inflate for the views
     * @param container holds the view group
     */
    private void choosingView(LayoutInflater inflater, ViewGroup container)
    {
        if(viewID==R.layout.nfc_details_fragment) {

            mainView = inflater.inflate(R.layout.nfc_details_fragment, container, false);
            RecyclerView mRecyclerView = (RecyclerView) mainView.findViewById(R.id.recyclerList);
            //LinearLayoutManager llm = new LinearLayoutManager(getContext());
            mRecyclerView.setHasFixedSize(true);
            //llm.setOrientation(LinearLayoutManager.VERTICAL);
            //mRecyclerView.setLayoutManager(llm);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(cardInfo);
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(cardInfo);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
            overwriteMsg = false;

            //synchorize view to update the layout
            synchronized(this) {
                FloatingActionButton fab = (FloatingActionButton) mainView.findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar = Snackbar.make(view, "Clear all tags", Snackbar.LENGTH_LONG)
                                .setAction("Erase", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cardInfo.clearAll();
                                        Toast.makeText(getContext(), "Cleared all", Toast.LENGTH_SHORT).show();
                                        //notify layout that the data has changed and it needs to update
                                        cardInfo.notifyDataSetChanged();
                                    }
                                });
                        // Changing message text color
                        snackbar.setActionTextColor(Color.RED);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                });
            }

        }else if(viewID==R.layout.nfc_write_fragment)
        {
            mainView = inflater.inflate(R.layout.nfc_write_fragment, container, false);
            fm = getFragmentManager();
            mainView.findViewById(R.id.vcard).setOnClickListener(this);
            mainView.findViewById(R.id.plainMessage).setOnClickListener(this);
            mainView.findViewById(R.id.teleNumber).setOnClickListener(this);
            mainView.findViewById(R.id.link).setOnClickListener(this);

            mainView.findViewById(R.id.overwriteNFCbtn).setOnClickListener(this);
            mainView.findViewById(R.id.writeRecordBtn).setOnClickListener(this);
            mainView.findViewById(R.id.clearBtn).setOnClickListener(this);
        } else {
            Log.e(TAG,"ViewID null");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.link:
                displayOption("URL:");
                msgType="URI";
                break;

            case R.id.vcard:
                displayOption("MAIL:");
                msgType="VCARD";
                break;

            case R.id.plainMessage:
                displayOption("TEXT:");
                msgType="TEXT";
                break;

            case R.id.teleNumber:
                displayOption("MOBILE:");
                msgType="MOBILE";
                break;

            case R.id.clearBtn:
                    msgOption = MSG_CLEAR;
                    overwriteMsg = true;
                    showEditDialog("Cancel", "Waiting for tag");
                    break;

            case R.id.writeRecordBtn:
                    msgOption = MSG_RECORD;
                    overwriteMsg = true;
                    showEditDialog("Cancel", "Waiting for tag");
                    break;

            case R.id.overwriteNFCbtn:
                    msgOption = MSG_OVERWRITE;
                    overwriteMsg = true;
                    showEditDialog("Cancel", "Waiting for tag");
                    break;
        }
    }

    /**
     * displays dialog to get user data
     * @param mid_text  value of the middle line
     */
    private void displayOption(String mid_text)
    {
        palarax.e_key_card.CardReader.writeOptionDialog writeOptionDialog = new writeOptionDialog();
        writeOptionDialog.setView(mid_text);
        writeOptionDialog.setListener(this);
        writeOptionDialog.show(fm, "option_dialog");

    }

    //What happens once Finish button is pressed
    @Override
    public void onDone(String inputText[]) {
        if(!inputText[0].equals(""))
        {
            msgRecord = inputText[0];
        }
        if(msgType.equals("VCARD"))
        {
            msgRecord = "BEGIN:VCARD" +"\n"+ "VERSION:2.1" +"\n" + "N:" + inputText[2] + "\n" + "TEL:"+inputText[0] +"\n"+ "EMAIL:"+inputText[0] +"\n"+"END:VCARD";
        }
        if(msgType.equals("MOBILE"))
        {
            msgRecord =inputText[0];
        }

    }


    /**
     * Showed a dialog box when writing to tag
     * @param message message to be displayed
     * @param btnT text on the button
     */
    private void showEditDialog(String btnT, String message) {
        try{
            if(editNameDialog.getDialog().isShowing())
            {
                editNameDialog.getDialog().dismiss();
            }
        }catch (NullPointerException e){Log.i(TAG,"Error: "+e);}
        editNameDialog = new newDialog();
        editNameDialog.getMsg(btnT, message);
        editNameDialog.show(fm, "fragment_edit_name");
        editNameDialog.setBtnResult(!overwriteMsg);
        }


    /**
     * sets the view of the selected menu item ( scan or write)
     * @param id ID of the view
     */
    public void setViewLayout(int id){
        Log.e(TAG, "setting view");
        viewID=id;
        //create a root view to change the main view
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, ((ViewGroup)getView().getParent()), false);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        choosingView(inflater, rootView);
        rootView.addView(mainView);

    }

    /**
     * sets the new view ID
     * @param ID view ID
     */
    public void setviewID(int ID) {viewID=ID; }


    /**
     * Enables the devices to scan the tag
     */
    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        Activity activity = getActivity(); //user defined fragment
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.enableReaderMode(activity, cardReader, READER_FLAGS, null);
        }
    }

    /**
     * Disables the devices to scan the tag
     */
    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.disableReaderMode(activity);
        }
    }

    /**
     * receives data after nfc card has been found
     * @param tag tag scanned
     */
    @Override
    public void onAccountReceived(Tag tag) throws Exception {
        Log.i(TAG,"AccountReceived");
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.

        String[] techList = tag.getTechList(); //list of all Tag techs

        String tech = techList(tag);
        String ID = bytesToHexString(tag.getId());
        nfcATag tag_nfcA = new nfcATag(tag);
        String type = tag_nfcA.getTagType();
        ArrayList msgRecords = new ArrayList<>();
        String tagSize = "";
        String message = "";
        //Look through tech
        for (String singleTech : techList) {
            //selected only Ndef and NdefFormatable techs
            if (singleTech.equals(Ndef.class.getName()) || singleTech.equals(NdefFormatable.class.getName())) {
                NdefTag ndef = new NdefTag();
                tagSize = ndef.getSize(tag);
                if(msgOption==MSG_RECORD) {
                    msgRecords = ndef.read(tag);
                    //if it's NdefFormatable, then don't try to read the message (there is none)
                    if(msgRecords.isEmpty() && msgOption != MSG_OVERWRITE)
                    {
                        Toast.makeText(getContext(), "Tag has to be formatted by overwriting", Toast.LENGTH_LONG).show();
                        overwriteMsg=false;
                    }
                }

                //Ndef - read,write,add messages
                //NdefFormatable - overwrite/format message
                try {
                    if (overwriteMsg && !editNameDialog.getBtnResult()) {
                        //write
                        msgRecords.add(msgRecord); //add users message to records
                        ndef.writeMessage(msgRecords, tag, msgOption,msgType);
                        overwriteMsg=false;
                        showEditDialog("Complete","Tag write successful");
                    }
                }catch (Exception e){ Log.i(TAG,"Exception: "+e);}
                Log.e(TAG,"SIZE: "+tagSize);
                //read message, if it's null then catch the error and make it ""
                try {
                    message = ndef.read(tag).toString();
                }catch (NullPointerException e) {message = "";}
                break;
            }

            if (singleTech.equals(IsoDep.class.getName())) {
                IsoDep isoDep = IsoDep.get(tag);
                if (tag.getId().length != 7) {
                    throw new Exception("Verify UID length failed. ");
                }
                try {
                    // connect to isoDep
                    isoDep.connect();
                    //set up isoDep
                    IsoDepTag isotech = new IsoDepTag(isoDep);
                    //get data
                    OpalCard opalcard = DataFactory.parseData(isotech.readFile(FREE_READ_FILE_NUMBER));
                    opalcard.print();
                }catch (Exception e){Log.e(TAG,"problems with isoDep connection");}
                finally {
                    isoDep.close();
                }
            }

        }
        //update information in Scan cards
        updateCard(message, ID, tech, type, tagSize);
    }


    /**
     * Updates tag information in card view
     * @param message   message from tag
     * @param identification    tag ID
     * @param technology    tech in the tag
     * @param tagType   type of tag
     * @param tagSize   tag size
     */
    private void updateCard(String message,String identification, String technology, String tagType, String tagSize)
    {
        final String tech = technology;
        final String ID = identification;
        final String type = tagType;
        final String msg = message;
        final String size = tagSize;

        //Update information
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int position = cardInfo.exists(ID);
                if (position == -2) {
                    //doest the card exist
                    cardInfo.addItem(new CardObject(ID, type, tech, msg, size), cardInfo.getIndexCount());
                } else {
                    //update object
                    cardInfo.updateCard(position, ID, msg, tech, type, size);
                    cardInfo.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Find all the technologies utilized by the {@code Tag}
     * @param tag scanned tag
     * @return list of technologies utilized by the tag
     */
    private String techList(Tag tag)
    {
        StringBuilder list= new StringBuilder();
        String prefix = "android.nfc.tech."; //prefex of the tech
        for (String tech : tag.getTechList()) {
            list.append(tech.substring(prefix.length()));
            list.append(", ");
        }
        list.delete(list.length() - 2, list.length());
        return list.toString();
    }


    /**
     * Converts bytes to hex string
     * @param bytes bytes to be converted
     * @return a string of hex values
     */
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= bytes.length - 1; i++) {
            int b = bytes[i] & 0xff; //1111 1111
            if (b < 0x10)
                sb.append('0');
            if (i > 0) {
                sb.append(":");
            }
            sb.append(Integer.toHexString(b));
        }
        return sb.toString().toUpperCase();
    }

    @Override
    public void onPause() {
        super.onPause();
        disableReaderMode();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableReaderMode();
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
