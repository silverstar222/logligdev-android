package loglig.services;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import loglig.context_provider.ApplicationContextProvider;
import loglig.handlers.GenericHandler;
import loglig.handlers.StatisticServiceHandler;
import loglig.interfaces.INodeObject;
import loglig.managers.RealmManager;
import loglig.models.Statistic;

/**
 * Created by is_uptown4 on 16/05/16.
 */
public class StatisticService {
    private static StatisticService instance;

    private StatisticService() {
    }

    public static StatisticService getInstance() {
        if (instance == null) {
            instance = new StatisticService();
        }
        return instance;
    }

    public void fetchStatisticTypes(final GenericHandler handler) {
       /* if (this.statisticsList == null) {
            this.statisticsList = new ArrayList<StatisticType>();
            this.statisticsList.add(new StatisticType("Stl", "Steal", "Stl", false));
            this.statisticsList.add(new StatisticType("Ast", "Assist", "Ast", false));
            this.statisticsList.add(new StatisticType("+1", "Successful 1 point throw", "+1", false, StatisticCategory.Score, 1));
            this.statisticsList.add(new StatisticType("+2", "Successful 2 point throw", "+2", true, StatisticCategory.Score, 2));
            this.statisticsList.add(new StatisticType("+3", "Successful 3 point throw", "+3", true, StatisticCategory.Score, 3));
            this.statisticsList.add(new StatisticType("Foul", "Personal foul", "Foul", false, StatisticCategory.PlayerFoul));
            this.statisticsList.add(new StatisticType("Tecf", "Technical Foul", "Tecf", false, StatisticCategory.PlayerFoul));
            this.statisticsList.add(new StatisticType("Miss1", "Missed free throws", "Miss1", false));
            this.statisticsList.add(new StatisticType("Miss2", "Missed 2 point attempt.", "Miss2", true));
            this.statisticsList.add(new StatisticType("Miss3", "Missed 3 point attempt", "Miss3", true));
            this.statisticsList.add(new StatisticType("OFoul", "Offensive Foul", "OFoul", false, StatisticCategory.PlayerFoul));
            this.statisticsList.add(new StatisticType("TO", "Turn Over", "TO", false));
            this.statisticsList.add(new StatisticType("Blk", "Block", "Blk"));
            this.statisticsList.add(new StatisticType("OReb", "Offensive Rebound", "OReb"));
            this.statisticsList.add(new StatisticType("DReb", "Defensive Rebound", "DReb", false));
            this.statisticsList.add(new StatisticType("TFoul", "Team Foul", "TFoul", StatisticAssociation.Team, StatisticCategory.TeamFoul));
            this.statisticsList.add(new StatisticType("T", "Team Time Out", "T", StatisticAssociation.Team));
            this.statisticsList.add(new StatisticType("TOT", "Team Over Time", "TOT", StatisticAssociation.Team));
            this.statisticsList.add(new StatisticType("OT", "Over Time", "OT", StatisticAssociation.Team, StatisticCategory.OverTime));
        }
        return this.statisticsList;
        */
        AsyncHttpResponseHandler fetchingStatisticTypeHandler = new JsonHttpResponseHandler() {
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
        performStatisticTypeFetching(fetchingStatisticTypeHandler);
    }

    private void performStatisticTypeFetching(AsyncHttpResponseHandler handler) {
        String url = AppService.concatUrlWithToken(AppService.URL, AppService.STATISTIC_TYE_PATH);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, handler);
    }

    public void fetchStatisticPresetList(final GenericHandler handler) {
        AsyncHttpResponseHandler fetchingStatisticPresetHandler = new JsonHttpResponseHandler() {
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
        // TODO: the function fetchStatisticPresetList() not functional yet, need API for StatisticPreset.
        //performStatisticPresetFetching(fetchingStatisticPresetHandler);
    }

    private void performStatisticPresetFetching(AsyncHttpResponseHandler handler) {
        String url = AppService.concatUrlWithToken(AppService.URL, AppService.STATISTIC_PRESET_PATH);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, handler);
    }

    public void sendStatisticsToServer(Statistic statistic, final StatisticServiceHandler handler) {
        performSendStatisticsToServer(statistic, handler);
    }

    private void performSendStatisticsToServer(INodeObject statistic, final StatisticServiceHandler handler) {
        String url = AppService.concatUrlWithToken(AppService.URL, statistic.endpointPath());
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject json = statistic.jsonRepresentation();
        ByteArrayEntity entity;
        try {
            entity = new ByteArrayEntity(json.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            handler.onFailure(-99, null);
            return;
        }
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        client.post(ApplicationContextProvider.getContext(), url, entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                handler.onFailure(statusCode, throwable);
            }
        });
    }
}
