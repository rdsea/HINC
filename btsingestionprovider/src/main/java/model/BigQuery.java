package model;

public class BigQuery {
    private Tables[] tables;

    private String dataset;

    public Tables[] getTables ()
    {
        return tables;
    }

    public void setTables (Tables[] tables)
    {
        this.tables = tables;
    }

    public String getDataset ()
    {
        return dataset;
    }

    public void setDataset (String dataset)
    {
        this.dataset = dataset;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tables = "+tables+", dataset = "+dataset+"]";
    }
}
