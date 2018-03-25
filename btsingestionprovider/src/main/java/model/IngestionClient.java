package model;

public class IngestionClient  {
    private String _id;

    private BigQuery bigQuery;

    private String ingestionClientId;

    private String createdAt;

    private String data;

    private IngestionBroker[] brokers;

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public BigQuery getBigQuery ()
    {
        return bigQuery;
    }

    public void setBigQuery (BigQuery bigQuery)
    {
        this.bigQuery = bigQuery;
    }

    public String getIngestionClientId ()
    {
        return ingestionClientId;
    }

    public void setIngestionClientId (String ingestionClientId)
    {
        this.ingestionClientId = ingestionClientId;
    }

    public String getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getData ()
    {
        return data;
    }

    public void setData (String data)
    {
        this.data = data;
    }

    public IngestionBroker[] getBrokers ()
    {
        return brokers;
    }

    public void setBrokers (IngestionBroker[] brokers)
    {
        this.brokers = brokers;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [_id = "+_id+", bigQuery = "+bigQuery+", ingestionClientId = "+ingestionClientId+", createdAt = "+createdAt+", data = "+data+", IngestionBroker = "+brokers+"]";
    }
}
