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
    private String tagType;

    /**
     * Gets the tag information
     * @param tag tag scanned
     */
    public nfcATag(Tag tag) {
        NfcA tag_nfcA = NfcA.get(tag);
        try{
            Log.i(TAG,"NFCA connecting");
            tag_nfcA.connect();
            Short tagTypeShort = tag_nfcA.getSak();//Return tag type ( SAK byte)
            this.tagType = getSampleType(Integer.toHexString(tagTypeShort.intValue())); //convert to short to hex string and get the card type
            Log.i(TAG,"NFCA closing");
            tag_nfcA.close();
        }
        catch(Exception e) {
            Log.i(TAG, "Error when reading tag: "+e);
        }
    }

    private String getSampleType(String SAK)
    {
        String unknownTypeString = "Unknown type";
        Log.i(TAG,"getting sample");
        return SampleTagAttributes.lookup(SAK,unknownTypeString); //looks up if app knows the type
    }

    /**
     * Type information getter
     * @return tag type
     */
    public String getTagType()
    {
        return tagType;
    }


    //The primary NFC-A I/O operation is transceive(byte[])
    //TODO: finish nfcA Tag transceiver
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

    /**
     * Converts bytes to hex string
     * @param bytes bytes to be converted
     * @return a string of hex values
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
