package model;

import java.util.List;

public class Table {
    private Schema[] schema;

    private String id;

    public Schema[] getSchema ()
    {
        return schema;
    }

    public void setSchema (Schema[] schema)
    {
        this.schema = schema;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [schema = "+schema+", id = "+id+"]";
    }
}
