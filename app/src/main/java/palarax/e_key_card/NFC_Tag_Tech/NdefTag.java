package palarax.e_key_card.NFC_Tag_Tech;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

import java.io.IOException;

/**
 * @author Ilya Thai
 */
public class NdefTag {

    private static final String TAG = NdefTag.class.getSimpleName();

    //TODO: finish Ndef Tag
    public void writeTag(Tag tag, String tagText)
    {
        Ndef tag_Ndef = Ndef.get(tag);
        try {
            tag_Ndef.connect();
        } catch (IOException e) {
            Log.e(TAG, "IOException while closing MifareUltralight...", e);
        } finally {
            try {
                tag_Ndef.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing MifareUltralight...", e);
            }
        }
    }

    public String readTag(Tag tag)
    {
        Log.e(TAG,"Hello");
        NdefFormatable tag_Ndef = NdefFormatable.get(tag);
        /*Log.e(TAG,"Size: "+tag_Ndef.getMaxSize());
        Log.e(TAG, "Type: " + tag_Ndef.getType());
        try {
            extractMessage(tag_Ndef.getNdefMessage());
        } catch (FormatException e) {
            Log.e(TAG, "FormatException while getting Ndef msg...", e);
        } catch (IOException e) {
            Log.e(TAG, "IOException while getting Ndef msg...", e);
        }*/
        return null;
    }

    private void extractMessage(NdefMessage msg) {
        byte[] array = null;
        array = msg.getRecords()[0].getPayload();
        Log.e(TAG,"MSG:"+array);
    }
}
