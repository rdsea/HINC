package sinc.hinc.transformer.btssensor.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class SensorDescription {

    private String name;
    private String url;
    private String communication;
    private String format;
    private String measurement;
    private String unit;
    @JsonSerialize(as = SensorConfig.class)
    private SensorConfig sampleConfiguration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCommunication() {
        return communication;
    }

    public void setCommunication(String communication) {
        this.communication = communication;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public SensorConfig getSampleConfiguration() {
        return sampleConfiguration;
    }

    public void setSampleConfiguration(SensorConfig sampleConfig) {
        this.sampleConfiguration = sampleConfig;
    }
}
