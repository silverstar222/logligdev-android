package loglig.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import org.json.JSONObject;

import loglig.handlers.StatisticServiceHandler;
import loglig.managers.StatisticsManager;

/**
 * Created by is_uptown4 on 26/09/16.
 */
public class StatisticIntentService extends IntentService {
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "DownloadService";

    public StatisticIntentService() {
        super(StatisticIntentService.class.getName());

    }

    public StatisticIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();
        // Service Started
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);

        // Status Finished
        String statisticExtra = intent.getParcelableExtra("statistic");
        StatisticServiceHandler statisticServiceHandler = new StatisticServiceHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject response) {

            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {

            }
        };
       // StatisticsManager.getInstance().sendSingleStatisticsToServer(statisticExtra, statisticServiceHandler);

        //bundle.putStringArray("result", results);
        receiver.send(STATUS_FINISHED, bundle);
        //Sending error message back to activity
        bundle.putString(Intent.EXTRA_TEXT, "Error message here..");
        receiver.send(STATUS_ERROR, bundle);

    }

}
