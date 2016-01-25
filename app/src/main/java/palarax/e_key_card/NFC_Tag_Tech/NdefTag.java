package palarax.e_key_card.NFC_Tag_Tech;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author Ilya Thai
 */
public class NdefTag {

    public static final String TAG = "NdefTag";

    public String read(Tag tag) {
        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {

            // NDEF is not supported by this Tag.
            Log.e(TAG, "Not supported");
            return null;
        }
        String message ="";
        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
        Log.e(TAG, "Cache: " + ndefMessage);
        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {
            Log.e(TAG, "Cache: " + ndefRecord.toString());
            //if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    message+= readText(ndefRecord)+", ";
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Unsupported Encoding", e);
                }
            //}
        }
        return message;
    }

    /**
     *
     * @param record
     * @return message from tag
     * @throws UnsupportedEncodingException
     */
    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();
        String UTF8 = "UTF-8";
        String UTF16 = "UTF-16";
        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? UTF8 : UTF16;

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;
        // Get the Text
        Log.i(TAG, textEncoding);
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }


    public boolean writeMessage(String text, Tag tag) throws IOException, FormatException {
        String language = "en";
        //NdefRecord records = NdefRecord.createTextRecord(language,text);
        NdefRecord[] records = {createRecord(text)};

        NdefMessage message = new NdefMessage(records);
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                Log.e(TAG, "Ndef connected");
                if (!ndef.isWritable()) {
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    return false;
                }
                Log.e(TAG, "writing msg" + message);
                ndef.writeNdefMessage(message);
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        return true;
                    } catch (IOException e) {
                        Log.i(TAG, "Error: " + e);
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {

        //create the message in according with the standard
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;

        byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
        return recordNFC;
    }

    public String getSize(Tag tag)
    {
        Ndef ndef = Ndef.get(tag);
        String size = "undefined";
        try{
            ndef.connect();
            size = Integer.toString(ndef.getMaxSize());
            ndef.close();
        }
        catch(Exception e) {
            Log.i(TAG, "Error when reading tag: "+e);
        }

        return size;
    }
}

