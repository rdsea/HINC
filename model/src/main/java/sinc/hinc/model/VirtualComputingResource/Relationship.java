package sinc.hinc.model.VirtualComputingResource;

public class Relationship {

    private IoTUnit source;
    private IoTUnit target;
    private RelationshipType type;

    public Relationship() {
    }

    public Relationship(IoTUnit source, IoTUnit target, RelationshipType type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public IoTUnit getSource() {
        return source;
    }

    public void setSource(IoTUnit source) {
        this.source = source;
    }

    public IoTUnit getTarget() {
        return target;
    }

    public void setTarget(IoTUnit target) {
        this.target = target;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

}
