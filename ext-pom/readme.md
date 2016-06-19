# Develop and integrate new plug-in

**Step 1:** Clone entire HINC source code from [main repository](https://github.com/SINCConcept/HINC)

**Step 2:** Create a new module in *ext-pom*

**Step 3:** Add following dependency:
 
```XML
<dependency>
   <groupId>at.ac.tuwien.dsg.hinc</groupId>
   <artifactId>collector</artifactId>
   <version>1.0</version>
</dependency>
```
**Step 4:** Create 2 classes to implement following interface:

- [Provider Adaptor](https://github.com/SINCConcept/HINC/blob/master/collector/src/main/java/sinc/hinc/abstraction/ResourceDriver/ProviderAdaptor.java): To interface with the provider APIs to collect data and send control to resources.
- [Informaion Transformer](https://github.com/SINCConcept/HINC/tree/master/collector/src/main/java/sinc/hinc/abstraction/transformer): To extract the capability and transform to HINC information model.

**Step 5:** Create a *info-source.info* as following:

```XML
{
    "source": [
        {
            "name": "myPlugin",
            "type": "IoT",
            "interval": 0,
            "adaptorClass": "sinc.hinc.transformer.openhab.MyAdaptor",
            "transformerClass": "sinc.hinc.transformer.openhab.MyTransformer",
            "settings": {
                "endpoint": "http://localhost:8080/rest"
            }
        }
    ]
}
```

- name: can take any value to management purpose.
- type: can take value of: IoT, Network or Cloud.
- interval: the interval that HINC will query provider. Set to 0 for one time query or subscription plugin
- adaptorClass: the full name of the class for Adaptor
- transformerClass: the full name of the class for Transformer
- settings: any properties which require in implementing the plugin. This settings are input for the implementation of above interfaces.

**Step 6:** Build the module by Maven command:
```shellscript
$ mvn clean install
```

**Step 6:** Run and test the HINC-local and HINC-global as the [guideline here](http://sincconcept.github.io/HINC/#testbed)

