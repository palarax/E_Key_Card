package palarax.e_key_card.CardReader;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by Ithai on 8/01/2016.
 */
public class nfcCardReader implements NfcAdapter.ReaderCallback {

    private static final String TAG = "CardReader";
    // AID (Application ID) for the E_KEY service
    private static final String E_KEY_AID = "F222222222";

    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]

    private static final String SELECT_APDU_HEADER = "00A40400";

    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};

    // Weak reference to prevent retain loop. mAccountCallback is responsible for exiting
    // foreground mode before it becomes invalid (e.g. during onPause() or onStop()).
    private WeakReference<AccountCallback> mAccountCallback;

    public interface AccountCallback {
        public void onAccountReceived(String account);
    }

    public nfcCardReader(AccountCallback accountCallback) {
        mAccountCallback = new WeakReference<AccountCallback>(accountCallback);
    }

    /**
     * Callback when a new tag is discovered by the system.
     *
     * <p>Communication with the card should take place here.
     *
     * @param tag Discovered tag
     */
    @Override
    public void onTagDiscovered(Tag tag) {
        Log.e(TAG, "New tag discovered");
        //NfcA nfca_tag = NfcA.get(tag);
        Log.e(TAG,tag.toString());
        Log.e(TAG,"Contents: "+tag.describeContents());
        Log.e(TAG,"ID: "+bytesToHexString(tag.getId()));
        Log.e(TAG, "Tech list: " + tag.getTechList());
        mAccountCallback.get().onAccountReceived("ID: " + bytesToHexString(tag.getId()));

       // new String(tag.getTechList(), Charset.forName("US-ASCII"))
        /*try{
            nfca_tag.connect();
            Short s = nfca_tag.getSak();
            byte[] a = nfca_tag.getAtqa();
            String atqa = new String(a, Charset.forName("US-ASCII"));
            Log.e(TAG, atqa);
            nfca_tag.close();
            mAccountCallback.get().onAccountReceived(atqa);
        }
        catch(Exception e){
            Log.e(TAG, "Error when reading tag");
            mAccountCallback.get().onAccountReceived("Error");
        }*/
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }
}
