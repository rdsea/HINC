package sinc.hinc.transformer.btssensor.model;

public class SensorItem {

    Sensor sensor;
    SensorMetadata metadata;

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public SensorMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(SensorMetadata metadata) {
        this.metadata = metadata;
    }
}
