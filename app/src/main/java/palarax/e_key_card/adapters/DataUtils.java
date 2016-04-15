package palarax.e_key_card.adapters;

import java.util.Calendar;

/**
 * Created by Ithai on 14/04/2016.
 */
public class DataUtils {
    private static final int CRC_16_POLY = 33800;
    private static final int CRC_32_POLY = -306674912;
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String byteArrayToBinaryString(byte[] arrby) {
        return DataUtils.byteArrayToBinaryString(arrby, null);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static String byteArrayToBinaryString(byte[] arrby, Character c) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < arrby.length) {
            byte by = arrby[n];
            for (int i = 0; i < 8; ++i) {
                int n2 = (by & 1 << 7 - i) > 0 ? 1 : 0;
                stringBuilder.append(n2);
            }
            if (c != null && n < -1 + arrby.length) {
                stringBuilder.append(c);
            }
            ++n;
        }
        return stringBuilder.toString();
    }

    public static String byteArrayToHexString(byte[] arrby) {
        return DataUtils.byteArrayToHexString(arrby, null);
    }

    public static String byteArrayToHexString(byte[] arrby, Character c) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] arrc = new char[2 * arrby.length];
        for (int i = 0; i < arrby.length; ++i) {
            int n = 255 & arrby[i];
            arrc[i * 2] = HEX_ARRAY[n >>> 4];
            arrc[1 + i * 2] = HEX_ARRAY[n & 15];
            stringBuilder.append(HEX_ARRAY[n >>> 4]);
            stringBuilder.append(HEX_ARRAY[n & 15]);
            if (c == null || i >= -1 + arrby.length) continue;
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    public static int byteArrayToInt(byte[] arrby) {
        int n = 0;
        for (int i = 0; i < arrby.length; ++i) {
            int n2 = 8 * (-1 + arrby.length - i);
            n += (255 & arrby[i]) << n2;
        }
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int calculateCRC16CCITT(byte[] arrby) {
        int n = 65535;
        if (arrby.length == 0) {
            return -65536;
        }
        int n2 = arrby.length;
        int n3 = 0;
        while (n3 < n2) {
            byte by = arrby[n3];
            for (int i = 0; i < 8; ++i, by = (byte)(by >> 1)) {
                if ((n & 1 ^ by & 1) > 0) {
                    n = CRC_16_POLY ^ n >>> 1;
                    continue;
                }
                n >>>= 1;
            }
            ++n3;
        }
        return (n & 255) << 8 | n >>> 8;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int calculateCRC32CCITT(byte[] arrby) {
        int n = -1;
        if (arrby.length == 0) {
            return 0;
        }
        int n2 = arrby.length;
        int n3 = 0;
        while (n3 < n2) {
            byte by = arrby[n3];
            for (int i = 0; i < 8; ++i, by = (byte)(by >> 1)) {
                if ((n & 1 ^ by & 1) > 0) {
                    n = -306674912 ^ n >>> 1;
                    continue;
                }
                n >>>= 1;
            }
            ++n3;
        }
        return n << 24 | (65280 & n) << 8 | (16711680 & n) >>> 8 | n >>> 24;
    }

    public static byte[] hexStringToByteArray(String string2) {
        int n = string2.length();
        byte[] arrby = new byte[n / 2];
        for (int i = 0; i < n; i += 2) {
            arrby[i / 2] = (byte)((Character.digit(string2.charAt(i), 16) << 4) + Character.digit(string2.charAt(i + 1), 16));
        }
        return arrby;
    }

    public static String intToBinaryString(int n) {
        return DataUtils.byteArrayToBinaryString(DataUtils.intToByteArray(n));
    }

    public static String intToBinaryString(int n, Character c) {
        return DataUtils.byteArrayToBinaryString(DataUtils.intToByteArray(n), c);
    }

    public static byte[] intToByteArray(int n) {
        return DataUtils.intToByteArray(n, new byte[4]);
    }

    public static byte[] intToByteArray(int n, byte[] arrby) {
        for (int i = -1 + arrby.length; i >= 0; --i) {
            int n2 = 8 * (-1 + arrby.length - i);
            arrby[i] = (byte)((n & 255 << n2) >> n2);
        }
        return arrby;
    }

    public static boolean isLuhnChecksumValid(String string2) {
        String string3 = new StringBuilder(string2).reverse().toString();
        int n = 0;
        for (int i = 0; i < string3.length(); ++i) {
            int n2 = Character.getNumericValue(string3.charAt(i));
            if ((i + 1) % 2 == 0 && (n2 *= 2) > 9) {
                n2 = n2 / 10 + n2 % 10;
            }
            n += n2;
        }
        return n % 10 == 0;
    }

    public static void reverseBytes(byte[] arrby) {
        for (int i = 0; i < arrby.length / 2; ++i) {
            byte by = arrby[i];
            arrby[i] = arrby[-1 + arrby.length - i];
            arrby[-1 + arrby.length - i] = by;
        }
    }

    public static Calendar calculateDateAndTimeOfLastTag(int var0, int var1) {
        Calendar var2 = getEpoch();
        var2.add(Calendar.DAY_OF_MONTH, var0); //5?
        var2.add(Calendar.MINUTE, var1);//12?
        return var2;
    }

    public static Calendar getEpoch() {
        Calendar var0 = Calendar.getInstance();
        var0.clear();
        var0.set(Calendar.YEAR,1980);
        return var0;
    }
}
