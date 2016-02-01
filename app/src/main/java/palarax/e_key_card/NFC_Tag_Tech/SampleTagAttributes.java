package palarax.e_key_card.NFC_Tag_Tech;

import java.util.HashMap;

/**
 * @author Ilya Thai
 *
 * This class includes a small subset of standard TAG attributes
 */
public class SampleTagAttributes {

    private static HashMap<String, String> tagType = new HashMap();  //tag type hash map

    //SAK value for tag type
    public static String SAK_MIFARE_MINI = "09";
    public static String SAK_MIFARE_CLASSIC1k = "08"; //1K
    public static String SAK_MIFARE_CLASSIC4k = "18"; //4k
    public static String SAK_MIFARE_PLUS = "08";
    public static String SAK_MIFARE_DESFIRE = "20"; //including all EV1

    //Tag Type map
    static {
        // Sample
        tagType.put(SAK_MIFARE_MINI, "MIFARE Mini");
        tagType.put(SAK_MIFARE_CLASSIC1k, "MIFARE Classic 1k");
        tagType.put(SAK_MIFARE_CLASSIC4k, "MILFARE Classic 4k");
        tagType.put(SAK_MIFARE_PLUS, "MILFARE Plus");
        tagType.put(SAK_MIFARE_DESFIRE, "MILFARE DESFire / DESFire EV1");
    }

    public static String lookup(String SAK, String type) {
        String name = tagType.get(SAK);
        return name == null ? type : name;
    }

}
