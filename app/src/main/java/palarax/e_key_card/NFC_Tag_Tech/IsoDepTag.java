package palarax.e_key_card.NFC_Tag_Tech;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;

/**
 * @author Ilya Thai
 */
public class IsoDepTag {

    //TODO: finish IsoDep Tag

    private static final String TAG = IsoDep.class.getSimpleName();

    public void writeTag(Tag tag, String tagText) {
        IsoDep isoDepTag = IsoDep.get(tag);
        /*
        try {
            isoDepTag.connect();
            ultralight.writePage(4, "abcd".getBytes(Charset.forName("US-ASCII")));
            ultralight.writePage(5, "efgh".getBytes(Charset.forName("US-ASCII")));
            ultralight.writePage(6, "ijkl".getBytes(Charset.forName("US-ASCII")));
            ultralight.writePage(7, "mnop".getBytes(Charset.forName("US-ASCII")));
        } catch (IOException e) {
            Log.e(TAG, "IOException while closing MifareUltralight...", e);
        } finally {
            try {
                ultralight.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing MifareUltralight...", e);
            }
        }*/
    }


    public String readTag(Tag tag) {
        IsoDep tag_IsoDep = IsoDep.get(tag);
        /*
        try {
            mifare.connect();
            byte[] payload = mifare.readPages(4);
            return new String(payload, Charset.forName("US-ASCII"));
        } catch (IOException e) {
            Log.e(TAG, "IOException while writing MifareUltralight
                    message...", e);
        } finally {
            if (mifare != null) {
                try {
                    mifare.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "Error closing tag...", e);
                }
            }
        }*/
        return null;
    }
}
