package loglig.handlers;

import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by is_uptown4 on 08/09/16.
 */
public class GenericHandler {

    private static final String LOG_TAG = "GenericHandler";

    /**
     * Fired when the request is started, override to handle in your own code
     */
    public void onStart() {
        AsyncHttpClient.log.w(LOG_TAG, "onStart() was not overriden, but callback was received");
    }

    /**
     * Returns when request succeeds
     *
     * @param statusCode http response status line
     */
    public void onSuccess(int statusCode, JSONObject response) {
        AsyncHttpClient.log.w(LOG_TAG, "onSuccess(int statusCode) was not overriden, but callback was received");
    }

    /**
     * Returns when request succeeds
     *
     * @param statusCode http response status line
     * @param response   parsed response if any
     */
    public void onSuccess(int statusCode, JSONArray response) {
        AsyncHttpClient.log.w(LOG_TAG, "onSuccess(int, Header[], JSONArray) was not overriden, but callback was received");
    }

    /**
     * Returns when request failed
     *
     * @param statusCode http response status line
     * @param throwable  throwable describing the way request failed
     */
    public void onFailure(int statusCode, Throwable throwable) {
        AsyncHttpClient.log.w(LOG_TAG, "onFailure(int, Throwable, JSONArray) was not overriden, but callback was received", throwable);
    }
}
