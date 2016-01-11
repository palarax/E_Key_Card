package palarax.e_key_card.CardReader;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * @author Ilya Thai
 */
public class nfcCardReader implements NfcAdapter.ReaderCallback {

    private static final String TAG = "CardReader";

    // Weak reference to prevent retain loop. mAccountCallback is responsible for exiting
    // foreground mode before it becomes invalid (e.g. during onPause() or onStop()).
    private WeakReference<AccountCallback> mAccountCallback;

    public interface AccountCallback {
         void onAccountReceived(String account, String techlist);
    }

    public nfcCardReader(AccountCallback accountCallback) {
        mAccountCallback = new WeakReference<>(accountCallback);
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
        Log.e(TAG,tag.toString());
        Log.e(TAG,"Contents: "+tag.describeContents());
        Log.e(TAG,"ID (hex): "+bytesToHexString(tag.getId()));
        mAccountCallback.get().onAccountReceived(Long.toString(bytesToDec(tag.getId())),techList(tag));
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
     * bytes to Hex converter
     * @param bytes data in bytes
     * @return data in Hex returned as a string
     */
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
