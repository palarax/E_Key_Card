package palarax.e_key_card.CardReader;

import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import palarax.e_key_card.NFC_Tag_Tech.nfcATag;
import palarax.e_key_card.R;
import palarax.e_key_card.adapters.CardObject;
import palarax.e_key_card.adapters.RecyclerAdapter_Scroller;

/**
 * @author Ilya Thai
 */
public class nfcCard extends Fragment implements nfcCardReader.AccountCallback {

    public static final String TAG = "NFC_Card";
    private TextView idTextView; //ID text box
    public TextView techTextView; //tech text box
    private TextView typeTextView; //manufacturer text box
    private View mainView;          //Main view displayed
    private ViewGroup rootView;     // "container" of where mainView is located
    private int viewID;             //ID of the view used
    private RecyclerAdapter_Scroller cardInfo ;
    private int index;

    //Card and Recycler layout
    private RecyclerView mRecyclerView;

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
        cardInfo = new RecyclerAdapter_Scroller(getDataSet());
        index = 0;
        super.onCreate(savedInstanceState);
    }

    private ArrayList<CardObject> getDataSet() {
        //Replace this code with the scanned data
        ArrayList results = new ArrayList<CardObject>();
        CardObject obj = new CardObject("-1", "-1","-1");
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
            mRecyclerView = (RecyclerView) mainView.findViewById(R.id.recyclerList);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(llm);
            mRecyclerView.setAdapter(cardInfo);
        }else if(viewID==R.layout.nfc_write_fragment)
        {
            mainView = inflater.inflate(R.layout.nfc_write_fragment, container, false);
            idTextView = (TextView) mainView.findViewById(R.id.tagID_text);
            techTextView = (TextView) mainView.findViewById(R.id.techList_text);
            typeTextView = (TextView) mainView.findViewById(R.id.tagType_text);

        } else {
            Log.e(TAG,"ViewID null");
        }
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
        rootView = (ViewGroup) getView();
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
    public void onAccountReceived(Tag tag) {
        Log.i(TAG,"AccountReceived");
        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        //String[] techList = tag.getTechList(); //list of all Tag techs
        final String tech = techList(tag);
        final String ID = bytesToHexString(tag.getId());
        final nfcATag tag_nfcA = new nfcATag(tag);
        final String type = tag_nfcA.getTagType();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!cardInfo.exists(ID)) {
                    cardInfo.addItem(new CardObject(ID,type, tech), index);
                    index++;
                }
            }
        });

        //Look through tech

        String searchedTech = Ndef.class.getName();

        /*for (String tech : techList) {
            if (searchedTech.equals(tech)) {
                Log.e(TAG, "Tech");
                //new Ndef().execute(tag);
                break;
            }
        }*/

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
     * bytes to Dec converter
     * @param bytes data in bytes
     * @return data as a long
     */
    private long bytesToDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (byte aByte : bytes) {
            long value = aByte & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
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


}
