package palarax.e_key_card.NFC_Tag_Tech;

import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import palarax.e_key_card.adapters.DataUtils;
import palarax.e_key_card.adapters.RequestHandler;

/**
 * @author Ilya Thai
 */
public class IsoDepTag {

    //TODO: finish IsoDep Tag
    public static final byte AUTHENTICATE = -86;
    public static final byte GET_ADDITIONAL_FRAME = -81;
    public static final byte GET_APPLICATION_DIRECTORY = 106;
    public static final byte GET_FILES = 111;
    public static final byte GET_FILE_SETTINGS = -11;
    public static final byte GET_KEY_SETTINGS = 69;
    public static final byte GET_KEY_VERSON = 100;
    public static final byte GET_MANUFACTURING_DATA = 96;
    public static final byte READ_DATA = -67;
    public static final byte READ_RECORD = -69;
    private static final byte[] READ_WHOLE_FILE;
    public static final byte SELECT_APPLICATION = 90;
    public static final String TAG;
    public static final byte WRITE_DATA = 61;
    private IsoDep mTagTech;

    //Opal
    private static final String DESFIRE_APP_NAME = "SE1";


    static {
        TAG = IsoDep.class.getName();
        READ_WHOLE_FILE = new byte[]{0, 0, 0, 0, 0, 0};
    }

    public IsoDepTag(IsoDep isoDep) throws IOException {
        mTagTech=isoDep;
        selectApp(DataUtils.byteArrayToInt(DESFIRE_APP_NAME.getBytes()));
    }

    //Select which application of the App to use
    public void selectApp(int n) {
        byte[] arrby = new byte[3];
        arrby = DataUtils.intToByteArray(n, arrby);
        DataUtils.reverseBytes(arrby);
        sendRequest(SELECT_APPLICATION, arrby);
    }

    public byte[] readFile(int n) throws IOException {
        return read(READ_DATA, n);
    }

    private byte[] read(byte var1, int var2) throws IOException {
        ByteArrayOutputStream var3 = new ByteArrayOutputStream();
        var3.write((byte)var2);
        var3.write(READ_WHOLE_FILE);
        return sendRequest(var1, var3.toByteArray());
    }



    //Communicate with the tag to get access to application
    //Sends requests in forms of bytes
    private byte[] sendRequest(byte by, byte[] arrby) {

        try {
            return new RequestHandler(this.mTagTech, by, arrby).start();
        } catch (Exception e) {
            Log.e(TAG,"exception in RequestHandler");
        }
        return null;
    }

}
