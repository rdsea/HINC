package model;

public class Tables {
    private String id;

    private String[] topics;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String[] getTopics ()
    {
        return topics;
    }

    public void setTopics (String[] topics)
    {
        this.topics = topics;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", topics = "+topics+"]";
    }
}
