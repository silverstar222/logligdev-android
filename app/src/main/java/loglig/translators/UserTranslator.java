package loglig.translators;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import loglig.managers.UserManager;
import loglig.models.User;

/**
 * Created by is_uptown4 on 08/09/16.
 */
public class UserTranslator {
    private String TAG_EXCEPTION = "TRANSLATOR";

    public static String translateToken(JSONObject object) {
        String token="";
        try {
            token = object.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TAG_EXCEPTION", e.getMessage());
        }
        return token;
    }

    public static User translateUserData(JSONObject object) {
        User user = new User();
        try {
            JSONObject userData = object.getJSONObject("user");
            user.setId(userData.getString("id"));
            user.setUserName(userData.getString("username"));
            // TODO: set to current user user status "admin", sportType "basketball" => LL-222
            // TODO:  user.setUserLevel(serData.getString("userLevel")) => LL-231
            // TODO:  user.setSportType(serData.setSportType("sportType")) => LL-223
            user.setSportType("57d7c43badf9b9a41f308003"); // TODO: remove the line after LL-223 will be done
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TAG_EXCEPTION", e.getMessage());
        }
        return  user;
    }
}
