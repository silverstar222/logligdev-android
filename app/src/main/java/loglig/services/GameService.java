package loglig.services;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import loglig.handlers.GenericHandler;

/**
 * Created by is_uptown4 on 01/09/16.
 */
public class GameService {
    private static GameService instance;

    private GameService() {
    }

    public static GameService getInstance() {
        if (instance == null) instance = new GameService();
        return instance;
    }

    public void fetchGameList(String sportsTypeId, final GenericHandler handler) {
        AsyncHttpResponseHandler fetchingGameHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                handler.onFailure(statusCode, throwable);
            }

        };
        performGameFetching(sportsTypeId, fetchingGameHandler);
    }

    private void performGameFetching(String sportsTypeId, AsyncHttpResponseHandler handler) {
        //String url = AppService.concatUrlWithInclude(AppService.URL, AppService.GAME_PATH, true, "\"sportsTypeId\" : \"" + sportsTypeId + "\"");
        String url = AppService.concatUrlWithInclude(AppService.URL, AppService.GAME_PATH, true, "");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, handler);
    }


}
