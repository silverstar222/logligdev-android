package loglig.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.sql.Time;
import java.util.ArrayList;

import loglig.adapters.PlayerSelectionAdapter;
import loglig.is_uptown4.loglig.R;
import loglig.managers.GameManager;
import loglig.managers.RealmManager;
import loglig.managers.TimerManager;
import loglig.models.Player;

/**
 * Created by is_uptown4 on 10/05/16.
 */
public class PlayersSelectionActivity extends Activity {
    private Context context;
    private GameManager gameManager;
    private ListView teamAListView, teamBListView;
    private PlayerSelectionAdapter playerAdapterForTeamA, playerAdapterForTeamB;
    private SimpleDraweeView teamLogoA, teamLogoB;
    private TextView teamNameA, teamNameB;
    private Button startGameButton, gameSettingsButton;
    private TextView gameInfo, clock;
    private Button addMinutes, reduceMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_selection);
        this.context = this;
        gameManager = GameManager.getInstance();
        // --- --- --- --- --- --- ---
        this.teamAListView = (ListView) findViewById(R.id.teamListView1);
        this.playerAdapterForTeamA = new PlayerSelectionAdapter(teamAListView, gameManager.getCurrentGame().getHomeTeamId());
        teamAListView.setAdapter(playerAdapterForTeamA);
        // --- --- --- --- --- --- ---
        this.teamBListView = (ListView) findViewById(R.id.teamListView2);
        this.playerAdapterForTeamB = new PlayerSelectionAdapter(teamBListView, gameManager.getCurrentGame().getAwayTeamId());
        teamBListView.setAdapter(playerAdapterForTeamB);
        // --- --- --- --- --- --- ---
        this.teamLogoA = (SimpleDraweeView) findViewById(R.id.playerSelectionTeamLogoA);
        Uri uri = Uri.parse(gameManager.getCurrentGame().getTeams().get(0).getTeamLogoUri());
        teamLogoA.setImageURI(uri);
        this.teamLogoB = (SimpleDraweeView) findViewById(R.id.playerSelectionTeamLogoB);
        uri = Uri.parse(gameManager.getCurrentGame().getTeams().get(1).getTeamLogoUri());
        teamLogoB.setImageURI(uri);
        // --- --- --- --- --- --- ---
        this.teamNameA = (TextView) findViewById(R.id.playerSelectionTeamLogoNameA);
        this.teamNameA.setText(gameManager.getCurrentGame().getTeams().get(0).getTeamName());
        this.teamNameB = (TextView) findViewById(R.id.playerSelectionTeamLogoNameB);
        this.teamNameB.setText(gameManager.getCurrentGame().getTeams().get(1).getTeamName());
        // --- --- --- --- --- --- ---
        this.startGameButton = (Button) findViewById(R.id.playerSelectionStartGameButton);
        this.startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StatisticSelectionActivity.class);
                startActivity(intent);
            }
        });
        // --- --- --- --- --- --- ---
        this.gameSettingsButton = (Button) findViewById(R.id.playerSelectionGameSettingsButton);
        this.gameSettingsButton.setVisibility(View.GONE);// TODO remove row when fixed settings screen
        this.gameSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GameSettingsActivity.class);
                startActivity(intent);
            }
        });
        // --- --- --- --- --- --- ---
        this.gameInfo = (TextView) findViewById(R.id.playerSelectionGameInfo);
        //TODO: COMMENT: all relevant data saved in current game in StatisticManager, LL-204
        // TODO: game date, time, location need to be retrieved from currentGame in StatisticManager
        this.gameInfo.setText("20/09/2016\n18:00\nSami-Ofer Haifa");
        // --- --- --- --- --- --- ---
        this.clock = (TextView) findViewById(R.id.playerSelectionClock);
        this.clock.setText(String.format("%02d:%02d", (TimerManager.getInstance().getSegmentDuration())/60, (TimerManager.getInstance().getSegmentDuration())%60));
        // --- --- --- --- --- --- ---
        this.addMinutes = (Button) findViewById(R.id.clockSettingsAddMinutes);
        this.addMinutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerManager.getInstance().setSegmentDuration(TimerManager.getInstance().getQuarterTime() + 0.5);
                clock.setText(String.format("%02d:%02d", (TimerManager.getInstance().getSegmentDuration())/60, (TimerManager.getInstance().getSegmentDuration())%60));
            }
        });
        this.reduceMinutes = (Button) findViewById(R.id.clockSettingsReduceMinutes);
        this.reduceMinutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimerManager.getInstance().getQuarterTime() - 0.5 > 0) {
                    TimerManager.getInstance().setSegmentDuration(TimerManager.getInstance().getQuarterTime() - 0.5);
                    clock.setText(String.format("%02d:%02d", (TimerManager.getInstance().getSegmentDuration()) / 60, (TimerManager.getInstance().getSegmentDuration()) % 60));
                }
            }
        });
    }

    private void setClock(String time) {
        this.clock.setText(time);
    }
}