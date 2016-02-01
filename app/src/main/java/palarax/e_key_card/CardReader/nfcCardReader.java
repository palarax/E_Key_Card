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
        mAccountCallback.get().onAccountReceived(tag);
    }



}
