package palarax.e_key_card.adapters;

import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Ithai on 14/04/2016.
 */
public class RequestHandler {
    private static final byte ADDITIONAL_FRAME = -81;
    private static final byte APPLICATION_INTERNAL_ERROR = -95;
    private static final byte APPLICATION_NOT_FOUND = -96;
    private static final byte AUTHENTICATION_ERROR = -82;
    private static final byte BOUNDARY_ERROR = -66;
    private static final byte CLA = -112;
    private static final byte COMMAND_LENGTH_ERROR = 126;
    private static final byte COMMAND_PARAMETER_ERROR = -98;
    private static final byte DESFIRE_RESPONSE_CODE = -111;
    private static final byte FILE_INTEGRITY_ERROR = -15;
    private static final byte FILE_NOT_FOUND = -16;
    private static final byte ILLEGAL_COMMAND_CODE = 28;
    private static final byte LE = 0;
    private static final int MAX_COMMAND_PARAMETERS_LENGTH = 59;
    private static final byte NO_SUCH_KEY = 64;
    private static final byte OPERATION_OK = 0;
    private static final byte P1 = 0;
    private static final byte P2 = 0;
    private static final byte PERMISSION_DENIED = -99;
    private static final byte PICC_DISABLED = -51;
    private static final byte PICC_INTERNAL_ERROR = -63;
    private static final byte PREVIOUS_COMMAND_ABORTED = -54;
    public static final String TAG = RequestHandler.class.getName();
    private final byte mCommand;
    private boolean mOperationComplete;
    private final byte[] mParameters;
    private int mParametersOffset;
    protected IsoDep mTag;

    public RequestHandler(IsoDep isoDep, byte by, byte[] arrby) {
        this.mTag = isoDep;
        this.mCommand = by;
        this.mParameters = arrby;
        this.mParametersOffset = 0;
        this.mOperationComplete = false;
    }


    private byte[] getNextFrameParameters() {
        if (this.mParameters == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(this.mParameters, this.mParametersOffset, Math.min(this.mParameters.length - this.mParametersOffset, MAX_COMMAND_PARAMETERS_LENGTH));
        this.mParametersOffset = MAX_COMMAND_PARAMETERS_LENGTH + this.mParametersOffset;
        return byteArrayOutputStream.toByteArray();
    }

    protected byte[] onAdditionalFrameReceived(ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        byte by = this.mCommand;
        byte[] var3 = null;
        if (by == 61) {
            var3 = getNextFrameParameters();
        }
        return this.mTag.transceive(this.wrapInstruction(ADDITIONAL_FRAME, var3));
    }

    public byte[] start() throws IOException {
        return this.start(null);
    }

    public byte[] start(SecureMessagingHandler var1) throws IOException {
        ByteArrayOutputStream var2 = new ByteArrayOutputStream();
        byte[] var3 = this.getNextFrameParameters();
        if(var1 != null) {
            label65: {
                ByteArrayOutputStream var4 = new ByteArrayOutputStream(1 + var3.length);
                var4.write(this.mCommand);
                var4.write(var3);

                Object var5;
                try {
                    var1.calculateCMAC(var4);
                    break label65;
                } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchPaddingException var20) {
                    var5 = var20;
                }

                Log.e(TAG, "CMAC calculation failed", (Throwable)var5);
            }
        }

        byte[] var7 = this.wrapInstruction(this.mCommand, var3);
        Log.v(TAG, "Raw command: " + DataUtils.byteArrayToHexString(var7));
        byte[] var9 = this.mTag.transceive(var7);

        do {
            Log.v(TAG, "Raw response: " + DataUtils.byteArrayToHexString(var9));
            byte var11;
            if(var9.length < 2) {
                var11 = 0;
            } else {
                var11 = var9[-2 + var9.length];
            }

            if(var11 != DESFIRE_RESPONSE_CODE) {
                Log.e(TAG, "Invalid APDU response code");
            }

            var2.write(var9, 0, -2 + var9.length);
            byte var12 = var9[-1 + var9.length];
            switch(var12) {
                case -81:
                    var9 = this.onAdditionalFrameReceived(var2);
                    break;
                case 0:
                    this.mOperationComplete = true;
                    break;
                default:
                    Log.e(TAG,"Such exception, many WOW");
            }
        } while(!this.mOperationComplete);

        if(var1 != null) {
            Object var13;
            try {
                return var1.decryptMessage(var2.toByteArray());
            } catch (InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException var16) {
                var13 = var16;
            }

            Log.e(TAG, "Message decryption failed", (Throwable)var13);
        }

        return var2.toByteArray();
    }

    protected byte[] wrapInstruction(byte by, byte[] arrby) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(CLA);
        byteArrayOutputStream.write((int)by);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(0);
        if (arrby != null && arrby.length > 0) {
            byteArrayOutputStream.write((int)((byte)arrby.length));
            byteArrayOutputStream.write(arrby);
        }
        byteArrayOutputStream.write(0);
        return byteArrayOutputStream.toByteArray();
    }
}
