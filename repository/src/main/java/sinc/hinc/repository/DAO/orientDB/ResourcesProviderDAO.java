package sinc.hinc.repository.DAO.orientDB;

import com.orientechnologies.orient.core.record.impl.ODocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sinc.hinc.model.VirtualComputingResource.Capabilities.ControlPoint;
import sinc.hinc.model.VirtualComputingResource.ResourcesProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResourcesProviderDAO extends AbstractDAO<ResourcesProvider> {
    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private AbstractDAO<ControlPoint> controlPointDAO = new AbstractDAO<ControlPoint>(ControlPoint.class);

    public ResourcesProviderDAO() {
        super(ResourcesProvider.class);
    }

    @Override
    public ODocument save(ResourcesProvider provider){
        logger.debug("saving resource provider "+provider.getUuid());
        ODocument document = super.save(provider);

        logger.debug("saving "+provider.getApis().size()+" control points");
        controlPointDAO.saveAll(provider.getApis());

        return document;
    }

    @Override
    public List<ODocument> saveAll(Collection<ResourcesProvider> providers){
        logger.debug("saving "+providers.size()+" resource providers");
        List<ODocument> documents = new ArrayList<>();
        for(ResourcesProvider provider: providers){
            documents.add(this.save(provider));
        }
        return documents;
    }

    // we don't need read because those are directly serialized by the mapper
}
