package sinc.hinc.model.VirtualNetworkResource;

import java.util.Objects;

public class AccessPoint {

    String endpoint;

    public AccessPoint() {
    }

    public AccessPoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.endpoint);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccessPoint other = (AccessPoint) obj;
        if (!Objects.equals(this.endpoint, other.endpoint)) {
            return false;
        }
        return true;
    }

}
