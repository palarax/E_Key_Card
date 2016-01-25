package palarax.e_key_card.NFC_Tag_Tech;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by Ithai on 25/01/2016.
 */
public class mifareUltraLight {

    public static final String TAG = "mifareUltraLight";

    private class NdefTagReader extends AsyncTask<Tag, Void, String> {
        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            android.nfc.tech.Ndef ndef = android.nfc.tech.Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                Log.e(TAG, "Not supported");
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();
            Log.e(TAG, "Cache: " + ndefMessage);
            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                Log.e(TAG, "Cache: " + ndefRecord.toString());
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }
            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */
            byte[] payload = record.getPayload();
            String UTF8 = "UTF-8";
            String UTF16 = "UTF-16";
            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? UTF8 : UTF16;

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            Log.e(TAG, textEncoding);
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Post execute");
            if (result != null) {
                //techTextView.setText("Read content: " + result);
            }
        }
    }
}
