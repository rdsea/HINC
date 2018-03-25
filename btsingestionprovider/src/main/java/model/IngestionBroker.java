package model;

public class IngestionBroker {
    private String port;

    private String[] topics;

    private String username;

    private String host;

    private String password;

    private String clientId;

    public String getPort ()
    {
        return port;
    }

    public void setPort (String port)
    {
        this.port = port;
    }

    public String[] getTopics ()
    {
        return topics;
    }

    public void setTopics (String[] topics)
    {
        this.topics = topics;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getHost ()
    {
        return host;
    }

    public void setHost (String host)
    {
        this.host = host;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getClientId ()
    {
        return clientId;
    }

    public void setClientId (String clientId)
    {
        this.clientId = clientId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [port = "+port+", topics = "+topics+", username = "+username+", host = "+host+", password = "+password+", clientId = "+clientId+"]";
    }
}
