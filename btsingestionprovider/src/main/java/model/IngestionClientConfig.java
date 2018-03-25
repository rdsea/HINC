package model;

public class IngestionClientConfig {
    private String url;
    private IngestionClient sampleConfiguration;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IngestionClient getSampleConfiguration() {
        return sampleConfiguration;
    }

    public void setSampleConfiguration(IngestionClient sampleConfiguration) {
        this.sampleConfiguration = sampleConfiguration;
    }
}
