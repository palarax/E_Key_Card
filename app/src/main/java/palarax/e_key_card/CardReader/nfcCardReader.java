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
         void onAccountReceived(Tag tag);
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
        Log.e(TAG, "Contents: " + tag.describeContents());
        Log.e(TAG, "ID (hex): " + bytesToHexString(tag.getId()));
        mAccountCallback.get().onAccountReceived(tag);
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
