package model;


public class Dataset {
    private Table[] tables;

    private String _id;

    private String createdAt;

    private String datasetId;

    public Table[] getTables ()
    {
        return tables;
    }

    public void setTables (Table[] tables)
    {
        this.tables = tables;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getDatasetId ()
    {
        return datasetId;
    }

    public void setDatasetId (String datasetId)
    {
        this.datasetId = datasetId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tables = "+tables+", _id = "+_id+", createdAt = "+createdAt+", datasetId = "+datasetId+"]";
    }



}
