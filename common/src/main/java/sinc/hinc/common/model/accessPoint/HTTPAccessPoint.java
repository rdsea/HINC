package sinc.hinc.common.model.accessPoint;


public class HTTPAccessPoint extends AccessPoint {

    public enum HttpMethod{
        GET,
        POST,
        PUT,
        DELETE
    }

    private HttpMethod httpMethod;

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
}
