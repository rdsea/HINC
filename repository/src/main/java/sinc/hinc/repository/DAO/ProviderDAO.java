package sinc.hinc.repository.DAO;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.BasicQuery;
import sinc.hinc.common.model.ResourceProvider;

import java.util.List;

public class ProviderDAO extends AbstractDAO<ResourceProvider>{

    public ProviderDAO(){
        super(ResourceProvider.class);
    }

    @Override
    public List<ResourceProvider> query(String jsonQuery) {

        Document document = Document.parse(jsonQuery);

        // if there is uuid field, we need to change it to _id for mongodb
        if(document.containsKey("uuid")){
            document.append("_id", document.get("uuid"));
            document.remove("uuid");
        }
        String elemMatch = "{\"availableResources\":{$elemMatch:"+document.toJson()+"}}";
        BasicQuery query = new BasicQuery(elemMatch);
        List<Document> docs = mongoTemplate.find(query, Document.class, collection);

        return deserializeDocuments(docs);
    }

}
