package palarax.e_key_card.adapters;

/**
 * @author Ilya Thai
 */
public class CardObject {
    private String ID;
    private String type;
    private String tech;
    private String msg;
    private String size;
    private CharSequence balance;
    private CharSequence serialNumber;


    public CardObject (String ID, String type, String tech, String msg, String size,CharSequence serialNumber,CharSequence balance){
        this.ID = ID;
        this.type = type;
        this.tech = tech;
        this.msg = msg;
        this.size = size;
        this.balance = balance;
        this.serialNumber = serialNumber;
    }

    public CharSequence getBalance() {
        return this.balance;
    }

    public void setBalance(CharSequence balance) {
        this.balance = balance;
    }

    public CharSequence getSerialNumber() {
        return this.serialNumber;
    }

    public void setSerialNumber(CharSequence serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTech() {
        return this.tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }
}