/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sinc.hinc.dummyprovider.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author hungld
 */
public class DummyMetadataItem {

    String id = null;
    String name = DummyMetadataItem.generateFullName();
    String description = generateDescription();
    String type = generateData(DATA_TYPE);
    String unit = generateData(UNIT_TYPE);

    public static void main(String[] args) throws JsonProcessingException {
        DummyMetadataItem item = new DummyMetadataItem();
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(item));
    }

    public DummyMetadataItem() {
        this.id = UUID.randomUUID().toString();
    }

    public DummyMetadataItem(String id) {
        this.id = id;
    }

    private static final String[] DATA_TYPE = {"temperature", "humidity", "light", "CO2", "water",
        "flow", "electricity", "movement"};
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

    public static String generateData(String[] array) {
        return array[rand.nextInt(array.length)];
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

}
