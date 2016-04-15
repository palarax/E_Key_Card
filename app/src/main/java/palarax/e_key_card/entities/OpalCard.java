package palarax.e_key_card.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Ilya THai
 */
public class OpalCard implements Serializable{

        static final String OPAL_ISSUER_NUMBER = "308522";
        private final boolean mAutoloadSet;
        private final int mCRC;
        private final boolean mCardEnabled;
        private final int mCheckDigit;
        private final int mCurrentBalance;
        private final Calendar mDateAndTimeOfLastTag;
        private final int mFrequencyOfUse;
        private final String mSerialNumber;
        private final int mTransactionSequenceNumber;

        public OpalCard(String string2, int n, boolean bl, int n2, int n3, Calendar calendar,  boolean bl2, int n4, int n5) {
            this.mSerialNumber = string2;
            this.mCheckDigit = n;
            this.mCardEnabled = bl;
            this.mTransactionSequenceNumber = n2;
            this.mCurrentBalance = n3;
            this.mDateAndTimeOfLastTag = calendar;
            this.mAutoloadSet = bl2;
            this.mFrequencyOfUse = n4;
            this.mCRC = n5;
        }

    public int getCRC() {
        return this.mCRC;
    }

    public String getCardNumber() {
        return OPAL_ISSUER_NUMBER + this.mSerialNumber + this.mCheckDigit;
    }

    public int getCheckDigit() {
        return this.mCheckDigit;
    }

    public int getCurrentBalanceInCents() {
        return this.mCurrentBalance;
    }

    public Calendar getDateAndTimeOfLastTag() {
        return (Calendar)this.mDateAndTimeOfLastTag.clone();
    }

    public int getFrequencyOfUse() {
        return this.mFrequencyOfUse;
    }


    public String getSerialNumber() {
        return this.mSerialNumber;
    }

    public int getTransactionSequenceNumber() {
        return this.mTransactionSequenceNumber;
    }


    public boolean isAutoloadSet() {
        return this.mAutoloadSet;
    }

    public boolean isCardEnabled() {
        return this.mCardEnabled;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Serial Number: " + this.mSerialNumber + "\n");
        stringBuffer.append("Check digit: " + this.mCheckDigit + "\n");
        StringBuilder stringBuilder = new StringBuilder().append("Current card state: ");
        String string2 = this.mCardEnabled ? "ENABLED" : "DISABLED";
        stringBuffer.append(stringBuilder.append(string2).append("\n").toString());
        stringBuffer.append("Transaction sequence number: " + this.mTransactionSequenceNumber + "\n");
        stringBuffer.append("Current balance: " + this.getCurrentBalanceInCents() + "\n");
        StringBuilder stringBuilder2 = new StringBuilder().append("Autoload indicator: ");
        String string3 = this.mAutoloadSet ? "SET" : "NOT SET";
        stringBuffer.append(stringBuilder2.append(string3).append("\n").toString());
        stringBuffer.append("Frequency of use: " + this.mFrequencyOfUse + "\n");
        stringBuffer.append("CRC: " + this.mCRC);
        return stringBuffer.toString();
    }

    public CharSequence getFormattedCardBalance() {
        String var7;
            int var1 = getCurrentBalanceInCents();
            int var2 = Math.abs(var1);
            int var3 = var2 % 100;
            int var4 = var2 / 100;
            Locale var5 = Locale.ENGLISH;
            Object[] var6 = new Object[]{Integer.valueOf(var4), Integer.valueOf(var3)};
            var7 = String.format(var5, "$%01d.%02d", var6);
            if(var1 < 0) {
                return '-' + var7;
            }
        return var7;
    }

    public CharSequence getFormattedCardNumber() {
        String var1 = getCardNumber();
        Locale var2 = Locale.ENGLISH;
        Object[] var3 = new Object[]{var1.substring(0, 4), var1.substring(4, 8), var1.substring(8, 12), var1.substring(12, 16)};
        return String.format(var2, "%s %s %s %s", var3);
    }

}
