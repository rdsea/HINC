package sinc.hinc.model.VirtualComputingResource;

public class Relationship {

    private VirtualResource source;
    private VirtualResource target;
    private RelationshipType type;

    public Relationship() {
    }

    public Relationship(VirtualResource source, VirtualResource target, RelationshipType type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }

    public VirtualResource getSource() {
        return source;
    }

    public void setSource(VirtualResource source) {
        this.source = source;
    }

    public VirtualResource getTarget() {
        return target;
    }

    public void setTarget(VirtualResource target) {
        this.target = target;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
    }

}
