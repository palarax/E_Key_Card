package palarax.e_key_card.adapters;

/**
 * @author Ilya Thai
 */
public class CardObject {
    private String ID;
    private String type;
    private String tech;

    public CardObject (String ID, String type, String tech){
        this.ID = ID;
        this.type = type;
        this.tech = tech;
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