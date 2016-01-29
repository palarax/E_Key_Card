package palarax.e_key_card.NFC_Tag_Tech;

import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Ilya Thai
 */
public class NdefTag {

    private static final String TAG = "NdefTag";
    private static final int MSG_RECORD = 1;
    private static final int MSG_OVERWRITE = 2;
    private static final int MSG_CLEAR = 0;



    public ArrayList read(Tag tag) {
        ArrayList<String> msgRecords = new ArrayList<>();
        Ndef ndef = Ndef.get(tag);
        if (ndef == null) {

            // NDEF is not supported by this Tag.
            Log.e(TAG, "NDEF is not supported by this Tag");
            return null;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
        Log.e(TAG, "Cache: " + ndefMessage);
        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records) {

            //if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {

                    //message+= readText(ndefRecord)+", ";
                    msgRecords.add(readText(ndefRecord));
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Unsupported Encoding", e);
                }
        }
        return msgRecords;
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


    public boolean writeMessage(ArrayList<String> readRecords, Tag tag, int msgOption) throws IOException, FormatException {
        Log.e(TAG,"write message");
        //String language = "en";

        NdefRecord[] messageRecords = new NdefRecord[readRecords.size()];
        NdefMessage message = null;
        int size=0;

        switch (msgOption) {
            case MSG_CLEAR:         message = new NdefMessage(new NdefRecord(NdefRecord.TNF_EMPTY, null, null, null));
                                    break;

            case MSG_RECORD:
                                    Log.e(TAG,"Records size: "+readRecords.size());

                                    for(int i=0 ; i<readRecords.size();i++)
                                    {
                                        messageRecords[i] = createRecord(readRecords.get(i));
                                    }
                                    message = new NdefMessage(messageRecords);
                                    size = message.toByteArray().length;

                                    break;

            case MSG_OVERWRITE:     NdefRecord[] records = {createRecord(readRecords.get(0))};
                                    message = new NdefMessage(records);
                                    size = message.toByteArray().length;
                                    break;
        }
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    return false;
                }
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

    private NdefRecord recordTypes(String data, String type) throws UnsupportedEncodingException
    {

        switch(type)
        {
            case "URI":     return NdefRecord.createUri(data); // = NdefRecord.createUri("http://www.linkedin.com/in/ilyathai");


            case "VCARD":   return createVcard(data);


            case "APPLICATION": return NdefRecord.createApplicationRecord(data);


            case "NUMBER":  return NdefRecord.createMime(type, data.getBytes()); //mime


            default : return createRecord(data);
        }

    }


    private NdefRecord createNumber(String text)  {

        //text/vcard
        //new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/vcard".getBytes(), new byte[0], payload);
        return null;
    }

    private NdefRecord createVcard(String text) {

        //  text/vcard
        //new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/vcard".getBytes(), new byte[0], payload);
        return null;
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

