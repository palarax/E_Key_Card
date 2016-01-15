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

    //Card and Recycler layout
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        super.onCreate(savedInstanceState);
        mAdapter = new RecyclerAdapter_Scroller(getDataSet());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView");
        mainView = inflater.inflate(viewID, container, false);

        if(viewID==R.layout.nfc_write_fragment) {
            mRecyclerView = (RecyclerView) mainView.findViewById(R.id.my_recycler_view);
            //mRecyclerView.setHasFixedSize(true);
            //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.setAdapter(mAdapter);
        }
            idTextView = (TextView) mainView.findViewById(R.id.tagID_text);
            techTextView = (TextView) mainView.findViewById(R.id.techList_text);
            typeTextView = (TextView) mainView.findViewById(R.id.tagType_text);


        //creates a new cardReader object
        cardReader = new nfcCardReader(this);
        // Disable Android Beam and register our card reader callback
        enableReaderMode();

        return mainView;
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
        rootView.addView(mainView);

        /*
        if(viewID == R.layout.nfc_write_fragment ) {
        mAdapter = new RecyclerAdapter_Scroller(getDataSet());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView = (RecyclerView) mainView.findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

        }else
        {
            idTextView = (TextView) mainView.findViewById(R.id.tagID_text);
            techTextView = (TextView) mainView.findViewById(R.id.techList_text);
            typeTextView = (TextView) mainView.findViewById(R.id.tagType_text);
        }*/

    }

    /**
     * sets the new view ID
     * @param ID view ID
     */
    public void setviewID(int ID)
    {
        viewID=ID;
    }


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

    private ArrayList<CardObject> getDataSet() {
        //Replace this code with the scanned data
        ArrayList results = new ArrayList<CardObject>();
        for (int index = 0; index < 20; index++) {
            CardObject obj = new CardObject("ID: " + index,
                    "Type: " + index,"Tech: "+index);
            results.add(index, obj);
        }
        return results;
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
        String[] techList = tag.getTechList(); //list of all Tag techs

        final String tech = techList(tag);
        final String ID = Long.toString(bytesToDec(tag.getId()));
        nfcATag tag_nfcA = new nfcATag(tag);
        final String type = tag_nfcA.getTagType();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                idTextView.setText(ID);
                techTextView.setText(tech);
                typeTextView.setText(type);
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
