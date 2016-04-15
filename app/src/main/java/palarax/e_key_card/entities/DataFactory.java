package palarax.e_key_card.entities;

import android.util.Log;

import com.tomgibara.bits.BitVector;
import com.tomgibara.bits.ByteArrayBitReader;

import java.util.Calendar;

import palarax.e_key_card.adapters.DataUtils;


/**
 * @author Ilya Thai
 */
public class DataFactory {
    private static final int CHECK_DIGIT_LENGTH = 4;
    private static final int CRC_LENGTH = 16;
    private static final int CURRENT_BALANCE_LENGTH = 21;
    private static final int DATE_OF_LAST_TAG_LENGTH = 15;
    private static final int FREQUENCY_OF_USE_LENGTH = 4;
    private static final int MODE_OF_LAST_TAG_LENGTH = 3;
    private static final int SERIAL_NUMBER_LENGTH = 32;
    private static final int SMART_CARD_TRANSACTION_SEQ_LENGTH = 16;
    private static final int TIME_OF_LAST_TAG_LENGTH = 11;
    private static final int USAGE_OF_LAST_TAG_LENGTH = 4;
    public static final String TAG = DataFactory.class.getName();

    public static OpalCard parseData(byte[] arrby) {
        int n = -2 + arrby.length;
        byte[] arrby2 = new byte[n];
        System.arraycopy(arrby, 0, arrby2, 0, n);
        DataUtils.reverseBytes(arrby);
        //display(arrby);
        ByteArrayBitReader byteArrayBitReader = new ByteArrayBitReader(arrby);
        int n2 = byteArrayBitReader.read(SMART_CARD_TRANSACTION_SEQ_LENGTH);
        if (!DataFactory.verifyCRC(arrby2, n2)) {
             Log.e(TAG,"Card data CRC failed");
        }
        int n3 = byteArrayBitReader.read(CHECK_DIGIT_LENGTH);
        boolean bl = byteArrayBitReader.readBoolean();
        byteArrayBitReader.read(FREQUENCY_OF_USE_LENGTH);
        byteArrayBitReader.read(MODE_OF_LAST_TAG_LENGTH);

        int n4 = byteArrayBitReader.read(TIME_OF_LAST_TAG_LENGTH);
        Calendar calendar = DataUtils.calculateDateAndTimeOfLastTag(byteArrayBitReader.read(DATE_OF_LAST_TAG_LENGTH), n4);
        int n5 = twosComplement(DataUtils.intToByteArray(byteArrayBitReader.read(CURRENT_BALANCE_LENGTH)), CURRENT_BALANCE_LENGTH);
        int n6 = byteArrayBitReader.read(CRC_LENGTH);
        boolean bl2 = !byteArrayBitReader.readBoolean();
        int n7 = byteArrayBitReader.read(CHECK_DIGIT_LENGTH);
        Object[] arrobject = new Object[]{Integer.valueOf(byteArrayBitReader.read(SERIAL_NUMBER_LENGTH))};
        return new OpalCard(String.format("%09d", arrobject), n7, bl2, n6, n5, calendar, bl, n3, n2);
    }

    private static int twosComplement(byte[] var0, int var1) {
        BitVector var2 = new BitVector(DataUtils.byteArrayToBinaryString(var0).substring(8 * var0.length - var1));
        if(var2.getBit(var1 - 1)) {
            var2.flip();
            return -(1 + DataUtils.byteArrayToInt(var2.toByteArray()));
        } else {
            return DataUtils.byteArrayToInt(var0);
        }
    }

    private static boolean verifyCRC(byte[] var0, int var1) {
        byte[] var2 = DataUtils.intToByteArray(DataUtils.calculateCRC16CCITT(var0), new byte[2]);
        DataUtils.reverseBytes(var2);
        return DataUtils.byteArrayToInt(var2) == var1;
    }

    private static void display(byte[] c)
    {
        byte hexToBin[] = c;
        int[] allBits = new int[32];
        int a =0;

        for (int i =0; i < hexToBin.length ; i++)
        {
            byte eachByte = hexToBin[i];
            String s2 = String.format("%8s", Integer.toBinaryString((eachByte)& 0xFF)).replace(' ', '0');
            System.out.println(s2);
            char [] totalCharArr = s2.toCharArray();
            for (int k=0; k <8; k++)
            {
                allBits[k+a]= totalCharArr[k];
            }
            a= a+8;
        }

        for (int b=0; b<32;b++)
        {
            System.out.print(allBits[b]);
        }
    }

}
