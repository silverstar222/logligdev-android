package loglig.interfaces;
import org.json.JSONObject;

/**
 * Created by yuvalg on 14/10/2016.
 */

public interface INodeObject {
    String endpointPath();
    JSONObject jsonRepresentation();
//    <E extends RealmModel> E managedObjectFromJson(JSONObject jsonObject);
//    <E> E unManagedObjectFromJson(JSONObject jsonObject);
}
