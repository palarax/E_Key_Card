package palarax.e_key_card.CardReader;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import palarax.e_key_card.NFC_Tag_Tech.nfcATag;
import palarax.e_key_card.R;

/**
 * @author Ilya Thai
 */
public class nfcCard extends Fragment implements nfcCardReader.AccountCallback {

    public static final String TAG = "NFC_Card";
    private TextView idTextView; //ID text box
    private TextView techTextView; //tech text box

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nfc_details_fragment, container, false);
        idTextView = (TextView) v.findViewById(R.id.tagID_text);
        techTextView = (TextView) v.findViewById(R.id.techList_text);

        //creates a new cardReader object
        cardReader = new nfcCardReader(this);
        // Disable Android Beam and register our card reader callback
        enableReaderMode();

        return v;
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

    /**
     * receives data after nfc card has been found
     * @param tag tag scanned
     */
    @Override
    public void onAccountReceived(Tag tag) {

        // This callback is run on a background thread, but updates to UI elements must be performed
        // on the UI thread.
        final String tech = techList(tag);
        final String ID = Long.toString(bytesToDec(tag.getId()));
        Log.i(TAG,"Creating tag object");
        nfcATag tag_nfcA = new nfcATag(tag);
        final String type = tag_nfcA.getTagType();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                idTextView.setText(ID);
                techTextView.setText(tech);
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
