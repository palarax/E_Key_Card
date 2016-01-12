package palarax.e_key_card.NFC_Tag_Tech;

import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.util.Log;

import java.io.IOException;

/**
 * @author Ilya Thai
 */
public class nfcATag{

    private static final String TAG = nfcATag.class.getSimpleName();

    //The primary NFC-A I/O operation is transceive(byte[])
    //TODO: finish nfcA Tag
    public void transceiveTag(Tag tag, String tagText)
    {
        NfcA tag_nfcA = NfcA.get(tag);
        try {
            tag_nfcA.connect();
        } catch (IOException e) {
            Log.e(TAG, "IOException while closing MifareUltralight...", e);
        } finally {
            try {
                tag_nfcA.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing MifareUltralight...", e);
            }
        }
    }

    public String readTag(Tag tag)
    {
        NfcA tag_nfcA = NfcA.get(tag);

        return null;
    }
}
