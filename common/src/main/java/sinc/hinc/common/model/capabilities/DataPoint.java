package sinc.hinc.common.model.capabilities;

public class DataPoint {
    // add more as needed
    public enum DataType{
        STRING,
        INT,
        FLOAT,
    }

    private String name;
    private DataType dataType;
    private String unit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
