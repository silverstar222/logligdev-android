package loglig.services;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import loglig.handlers.GenericHandler;

/**
 * Created by is_uptown4 on 16/05/16.
 */
public class UserService extends AppService {

    private static UserService instance;
    private String TAG_EXCEPTION = "TRANSLATOR";

    private UserService() {

    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void loginUser(final GenericHandler handler, String userName, String password) {

        AsyncHttpResponseHandler loginHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                handler.onFailure(statusCode, throwable);
            }

        };
        performUserLogin(loginHandler, userName, password);
    }

    private void performUserLogin(AsyncHttpResponseHandler handler, String userName, String password) {
        String url = concatUrlWithInclude(URL, LOGIN_PATH, false, "user");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", userName);
        params.put("password", password);
        client.post(url, params, handler);
    }
}