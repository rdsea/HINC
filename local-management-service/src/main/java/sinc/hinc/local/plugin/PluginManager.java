package sinc.hinc.local.plugin;

import org.reflections.Reflections;
import org.slf4j.Logger;
import sinc.hinc.abstraction.ResourceDriver.PluginDataRepository;
import sinc.hinc.abstraction.ResourceDriver.ProviderListenerAdaptor;
import sinc.hinc.abstraction.ResourceDriver.ProviderQueryAdaptor;
import sinc.hinc.abstraction.ResourceDriver.ServiceDetector;
import sinc.hinc.abstraction.transformer.*;
import sinc.hinc.common.metadata.HINCMessageType;
import sinc.hinc.common.metadata.HincMessageTopic;
import sinc.hinc.common.utils.HincConfiguration;
import sinc.hinc.communication.processing.HincMessage;
import sinc.hinc.local.IoTUnitUpdateProcessor;
import sinc.hinc.local.PropertiesManager;
import sinc.hinc.local.communication.LocalCommunicationManager;
import sinc.hinc.local.watch.PluginWatcher;
import sinc.hinc.model.API.WrapperMicroserviceArtifact;
import sinc.hinc.model.SoftwareArtifact.MicroserviceArtifact;
import sinc.hinc.repository.DAO.orientDB.AbstractDAO;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static sinc.hinc.local.LocalManagementService.DEFAULT_SOURCE_SETTINGS;

public class PluginManager {
    static Logger logger = HincConfiguration.getLogger();

    private HashSet<String> enabledPlugins;

    private Map<String, ProviderQueryAdaptor> adaptors = new ConcurrentHashMap<>();
    private Map<String, ProviderListenerAdaptor> listeners = new ConcurrentHashMap<>();
    private Map<String, IoTUnitTransformer> ioTUnitTransformers = new ConcurrentHashMap<>();
    private Map<String, DataPointTransformer> dataPointTransformers = new ConcurrentHashMap<>();
    private Map<String, ControlPointTransformer> controlPointTransformers = new ConcurrentHashMap<>();
    private Map<String, ExecutionEnvironmentTransformer> executionEnvironmentTransformers = new ConcurrentHashMap<>();
    private Map<String, ConnectivityTransformater> connectivityTransformaters = new ConcurrentHashMap<>();
    private Map<String, ServiceDetector> serviceDetectors = new ConcurrentHashMap<>();

    private LocalCommunicationManager localCommunicationManager;

    public PluginManager(HashSet<String> enabledPlugins, LocalCommunicationManager localCommunicationManager){
        File pluginFolder = new File(PluginWatcher.PLUGIN_FOLDER);
        File[] listOfFiles = pluginFolder.listFiles();
        List<URL> urls = new ArrayList<>();
        for(File plugin: listOfFiles){
            try {
                urls.add(plugin.toURI().toURL());
            } catch (MalformedURLException e) {
                logger.error("failed to add jar file "+plugin.getAbsolutePath());
            }
        }
        URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[urls.size()]));
        Reflections reflections = new Reflections(loader);
        try {
            this.loadClasses(reflections);
        } catch (Exception e) {
            logger.error("failed to load initial plugins");
        }

        this.enabledPlugins = enabledPlugins;

    }

    public void loadPlugin(URL url) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        URLClassLoader loader = new URLClassLoader(new URL[]{url});

        Reflections reflections = new Reflections(loader);
        this.loadClasses(reflections);


    }

    private void loadClasses(Reflections reflections) throws IllegalAccessException, InstantiationException {
        Set<Class<? extends ProviderQueryAdaptor>> adaptorClasses = reflections.getSubTypesOf(ProviderQueryAdaptor.class);
        Set<Class<? extends ProviderListenerAdaptor>> listenerClasses = reflections.getSubTypesOf(ProviderListenerAdaptor.class);

        Set<Class<? extends IoTUnitTransformer>> iotUnitClazz = reflections.getSubTypesOf(IoTUnitTransformer.class);
        Set<Class<? extends DataPointTransformer>> datapointsClazz = reflections.getSubTypesOf(DataPointTransformer.class);
        Set<Class<? extends ControlPointTransformer>> controlpointsClazz = reflections.getSubTypesOf(ControlPointTransformer.class);
        Set<Class<? extends ExecutionEnvironmentTransformer>> executionEnvClazz = reflections.getSubTypesOf(ExecutionEnvironmentTransformer.class);
        Set<Class<? extends ConnectivityTransformater>> connectivityClazz = reflections.getSubTypesOf(ConnectivityTransformater.class);
        Set<Class<? extends ServiceDetector>> serviceDetectorClazz = reflections.getSubTypesOf(ServiceDetector.class);

        logger.info(listenerClasses.size()+" "+ProviderListenerAdaptor.class.getSimpleName()+"s found");

        logger.info(adaptorClasses.size()+" "+ProviderQueryAdaptor.class.getSimpleName()+"s found");
        for(Class<? extends ProviderQueryAdaptor> clazz: adaptorClasses){
            ProviderQueryAdaptor adaptor = clazz.newInstance();
            adaptors.put(adaptor.getName(), adaptor);
        }

        logger.info(iotUnitClazz.size()+" "+IoTUnitTransformer.class.getSimpleName()+"s found");
        for (Class<? extends IoTUnitTransformer> clazz : iotUnitClazz) {
            IoTUnitTransformer transformer = clazz.newInstance();
            ioTUnitTransformers.put(transformer.getName(), transformer);
        }

        logger.info(datapointsClazz.size()+" "+DataPointTransformer.class.getSimpleName()+"s found");
        for (Class<? extends DataPointTransformer> clazz : datapointsClazz) {
            DataPointTransformer transformer = clazz.newInstance();
            dataPointTransformers.put(transformer.getName(), transformer);
        }

        logger.info(controlpointsClazz.size()+" "+ControlPointTransformer.class.getSimpleName()+"s found");
        for (Class<? extends ControlPointTransformer> clazz : controlpointsClazz) {
            ControlPointTransformer transformer = clazz.newInstance();
            controlPointTransformers.put(transformer.getName(), transformer);
        }

        logger.info(executionEnvClazz.size()+" "+ExecutionEnvironmentTransformer.class.getSimpleName()+"s found");
        for (Class<? extends ExecutionEnvironmentTransformer> clazz : executionEnvClazz) {
            ExecutionEnvironmentTransformer transformer = clazz.newInstance();
            executionEnvironmentTransformers.put(transformer.getName(), transformer);
        }

        logger.info(connectivityClazz.size()+" "+ConnectivityTransformater.class.getSimpleName()+"s found");
        for (Class<? extends ConnectivityTransformater> clazz : connectivityClazz) {
            ConnectivityTransformater transformater = clazz.newInstance();
            connectivityTransformaters.put(transformater.getName(),transformater);
        }

        logger.info(serviceDetectorClazz.size()+" "+ServiceDetector.class.getSimpleName()+"s found");
        for (Class<? extends ServiceDetector> clazz : serviceDetectorClazz) {
            ServiceDetector detector = clazz.newInstance();
            if(!enabledPlugins.contains(detector.getName().trim())) continue;

            WrapperMicroserviceArtifact wrapper = new WrapperMicroserviceArtifact();
            logger.info("Detecting service : " + detector.getName());
            MicroserviceArtifact mService = detector.detect(PropertiesManager.getSettings(detector.getName().trim(), DEFAULT_SOURCE_SETTINGS));
            mService.setHostID(HincConfiguration.getMyUUID());
            // save to DB
            AbstractDAO<MicroserviceArtifact> serviceDAO = new AbstractDAO(MicroserviceArtifact.class);
            serviceDAO.save(mService);
            wrapper.getmServices().add(mService);
            serviceDetectors.put(detector.getName(),detector);
            if (!wrapper.getmServices().isEmpty()) {
                String groupTopic = HincMessageTopic.getBroadCastTopic(HincConfiguration.getGroupName());
                HincMessage updateMsg = new HincMessage(HINCMessageType.UPDATE_INFORMATION_MICRO_SERVICE.toString(), HincConfiguration.getMyUUID(), groupTopic, "", wrapper.toJson());
                localCommunicationManager.sendToGlobal(updateMsg);
            }
        }

        for (Class<? extends ProviderListenerAdaptor> clazz : listenerClasses) {
            ProviderListenerAdaptor listener = clazz.newInstance();
            if(!enabledPlugins.contains(listener.getName().trim())) continue;

            listeners.put(listener.getName(), listener);
            IoTUnitTransformer unitTrans = ioTUnitTransformers.get(listener.getName());
            listener.listen(
                    PropertiesManager.getSettings(listener.getName(), DEFAULT_SOURCE_SETTINGS),
                    unitTrans, new IoTUnitUpdateProcessor(HincConfiguration.getMyUUID()));
        }
    }

    public IoTUnitTransformer getIoTUnitTranformerByName(String name) {
        return ioTUnitTransformers.get(name);
    }

    public DataPointTransformer getDatapointTransformerByName(String name) {
        return dataPointTransformers.get(name);
    }

    public ControlPointTransformer getControlpointTransformerByName(String name) {
        return controlPointTransformers.get(name);
    }

    public ExecutionEnvironmentTransformer getExecutionEnvTransformerByName(String name) {
        return executionEnvironmentTransformers.get(name);
    }

    public ConnectivityTransformater getConnectivityTransformerByName(String name) {
        return connectivityTransformaters.get(name);
    }

    public ProviderQueryAdaptor getAdaptorByName(String name){
        return adaptors.get(name);
    }

    public Set<ServiceDetector> getServiceDetectors() {
        return new HashSet<>(serviceDetectors.values());
    }

    public Set<ProviderQueryAdaptor> getAdaptors() {
        return new HashSet<>(adaptors.values());
    }

    public Set<ProviderListenerAdaptor> getListeners() {
        return new HashSet<>(listeners.values());
    }

    public void scanAdaptors(){
        for(ProviderQueryAdaptor adaptor: this.getAdaptors()){
            if(!enabledPlugins.contains(adaptor.getName().trim())) continue;

            String aName = adaptor.getName();
            logger.info("Querying provider: " + aName);
            ProviderDataRepository repository = new ProviderDataRepository();
            try{
                adaptor.createResources(repository, PropertiesManager.getSettings(aName, DEFAULT_SOURCE_SETTINGS));
            }catch(Exception e){
                logger.error("failed to create resources from "+adaptor.getName());
                e.printStackTrace();
            }
        }
    }

}

