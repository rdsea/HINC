/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hungld
 */
public class DummyMetadataItem {

    static Logger logger = LoggerFactory.getLogger("Dummy");

    String id = null;
    String name = DummyMetadataItem.generateFullName();
    String description = generateDescription();
    String type = generateData(DATA_TYPE);
    String unit = generateData(UNIT_TYPE);
    Map<String, String> metadata = new HashMap<>();

    public static void main(String[] args) throws JsonProcessingException {
        DummyMetadataItem item = new DummyMetadataItem();
        ObjectMapper mapper = new ObjectMapper();
        logger.debug(mapper.writeValueAsString(item));
    }

    // keep UUID and name, regenerate others
    public void reGenerateMetadata() {
        this.description = generateDescription();
        metadata.clear();
        metadata.put("rate", generateInt(3, 60) + "");
        metadata.put("accuracy", generateData("good", "medium", "low"));
        switch (this.type) {
            case "temperature":
                metadata.put("unit", generateData("celsius", "fahrenheit"));
                break;
            case "humidity":
                metadata.put("unit", generateData("percent", "level"));
                break;
            case "light":
                metadata.put("level", generateData("high", "low", "medium"));
                break;
            case "movement":
                metadata.put("sensitive", generateData("high", "low", "medium"));
                break;
        }
    }

    public DummyMetadataItem() {
        this.id = UUID.randomUUID().toString();
    }

    public DummyMetadataItem(String id) {
        this.id = id;
    }

    // more: "CO2", "water", "flow", "electricity"
    private static final String[] DATA_TYPE = {"temperature", "humidity", "light", "movement"};
    private static final String[] UNIT_TYPE = {"celsius", "fahrenheit", "percent", "max", "min", "high", "middle", "low"};

    private static final String[] Beginning = {"Kr", "Ca", "Ra", "Mrok", "Cru",
        "Ray", "Bre", "Zed", "Drak", "Mor", "Jag", "Mer", "Jar", "Mjol",
        "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro",
        "Mar", "Luk"};
    private static final String[] Middle = {"air", "ir", "mi", "sor", "mee", "clo",
        "red", "cra", "ark", "arc", "miri", "lori", "cres", "mur", "zer",
        "marac", "zoir", "slamar", "salmar", "urak"};
    private static final String[] End = {"d", "ed", "ark", "arc", "es", "er", "der",
        "tron", "med", "ure", "zur", "cred", "mur"};

    private static final Random rand = new Random();

    public static String generateName() {
        return Beginning[rand.nextInt(Beginning.length)]
                + Middle[rand.nextInt(Middle.length)]
                + End[rand.nextInt(End.length)];
    }

    public static String generateFullName() {
        return generateName() + "_" + generateName();
    }

    public static String generateDescription() {
        return generateName() + " " + generateName() + " " + generateName() + " " + generateName() + " " + generateName();
    }

    public static String generateData(String... array) {
        return array[rand.nextInt(array.length)];
    }

    public static int generateInt(int min, int max) {
        return rand.nextInt(max) + min;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

}
