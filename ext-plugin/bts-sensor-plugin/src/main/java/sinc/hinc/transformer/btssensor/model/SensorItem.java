package sinc.hinc.transformer.btssensor.model;

import java.util.List;

public class SensorItem {

    private SensorDescription description;
    private List<Sensor> sensors;

    public SensorDescription getDescription() {
        return description;
    }

    public void setDescription(SensorDescription description) {
        this.description = description;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }
}
