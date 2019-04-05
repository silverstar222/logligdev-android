package loglig.services;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import loglig.enums.TeamEnum;
import loglig.handlers.PlayerServiceHandler;
import loglig.handlers.TeamServiceHandler;
import loglig.models.Game;

/**
 * Created by is_uptown4 on 23/05/16.
 */
public class PlayerService {

    private static PlayerService instance;

    private PlayerService() {
    }

    public static PlayerService getInstance() {
        if (instance == null) instance = new PlayerService();
        return instance;
    }

    public void fetchTeamList(final TeamServiceHandler handler) {
        AsyncHttpResponseHandler fetchingTeamHandler = new JsonHttpResponseHandler() {
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
        performTeamFetching(fetchingTeamHandler);
    }

    private void performTeamFetching(AsyncHttpResponseHandler handler) {
        String url = AppService.concatUrlWithToken(AppService.URL, AppService.TEAM_PATH);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, handler);
    }

    public void fetchTeamByGame(Game game, final TeamServiceHandler handler) {
        AsyncHttpResponseHandler fetchingTeamHandler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                handler.onSuccess(statusCode, response);
            }

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
        performTeamByIdFetching(game.getHomeTeamId(), fetchingTeamHandler);
        performTeamByIdFetching(game.getAwayTeamId(), fetchingTeamHandler);
    }

    public void fetchTeamById(String teamId, final TeamServiceHandler handler) {
        AsyncHttpResponseHandler fetchingTeamHandler = new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                handler.onSuccess(statusCode, response);
            }

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
        performTeamByIdFetching(teamId, fetchingTeamHandler);
    }

    private void performTeamByIdFetching(String teamId, AsyncHttpResponseHandler handler) {
        String url = AppService.concatUrlWithToken(AppService.URL, AppService.TEAM_PATH + "/" + teamId);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, handler);
    }

    public void fetchPlayersByGame(final Game game, final PlayerServiceHandler handler) {
        String url = AppService.concatUrlWithToken(AppService.URL, AppService.PLAYER_PATH);
        AsyncHttpClient client = new AsyncHttpClient();

        // TODO: uncomment line bellow after getPlaersByTeam on API will be added.
        //String url = AppService.concatUrlWithToken(AppService.URL, AppService.PLAYER_PATH + "/" + teamId);
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                handler.onSuccess(statusCode, response);
            }

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
        });
    }
}