package loglig.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Observable;
import java.util.Observer;

import loglig.enums.NotificationEnum;
import loglig.enums.TeamEnum;
import loglig.is_uptown4.loglig.R;
import loglig.managers.GameManager;
import loglig.managers.PlayersManager;
import loglig.managers.StatisticsManager;
import loglig.managers.TimerManager;
import loglig.notification.LogLigNotification;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class TeamTimeOutPanel extends RelativeLayout implements Observer {

    private LayoutInflater inflater;
    private Button timeOutButton;
    private GameManager gameManager;
    private StatisticsManager statisticsManager;
    private PlayersManager playersManager;

    public TeamTimeOutPanel(Context context) {
        super(context);
        init(context);
    }

    public TeamTimeOutPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TeamTimeOutPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        gameManager = GameManager.getInstance();
        statisticsManager = StatisticsManager.getInstance();
        statisticsManager.register(this);
        playersManager = PlayersManager.getInstance();

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.team_time_out_layout, this, true);

        this.timeOutButton = (Button) findViewById(R.id.timeOutButton);

        updateTimeOutView(String.valueOf(StatisticsManager.getInstance().getTemTimeOutCounter(playersManager.getCurrentTeamId(getCurrentTeamIdentifier()))));

        if (getCurrentTeamIdentifier().equals(TeamEnum.TEAM_A))
            timeOutButton.setTextColor(ContextCompat.getColor(context, R.color.teamACenterColor));
        else timeOutButton.setTextColor(ContextCompat.getColor(context, R.color.teamBCenterColor));

        this.timeOutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TimerManager.getInstance().isTimerOn()) {
                    long timeoutCounter = statisticsManager.getTemTimeOutCounter(playersManager.getCurrentTeamId(getCurrentTeamIdentifier()));
                    if (timeoutCounter < TimerManager.getInstance().getMaximumNumberOfTimeOuts()) {
                        StatisticsManager.getInstance().createTeamTimeOutStatistic(playersManager.getCurrentTeamId(getCurrentTeamIdentifier()));
                        TimerManager.getInstance().pauseTimer();
                    }
                }
            }
        });
    }

    private TeamEnum getCurrentTeamIdentifier() {
        TeamEnum team = TeamEnum.NONE;
        if (this.getId() == R.id.teamATTimeout) {
            team = TeamEnum.TEAM_A;
        }
        if (this.getId() == R.id.teamBTTimeout) {
            team = TeamEnum.TEAM_B;
        }
        return team;
    }

    public void updateTimeOutView(String timeout) {
        this.timeOutButton.setText("T " + timeout + "/" + StatisticsManager.getInstance().getMaxNumberOfTimeOuts());
        if (timeout == String.valueOf(StatisticsManager.getInstance().getMaxNumberOfTimeOuts())) {
            this.timeOutButton.setEnabled(false);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (((LogLigNotification) data).getAction().equals(NotificationEnum.TEAM_STATISTIC_UPDATE)) {
            String id = playersManager.getCurrentTeamId(getCurrentTeamIdentifier());
            if (!id.equals("0")) {
                long timeoutCounter = statisticsManager.getTemTimeOutCounter(id);
                updateTimeOutView(String.valueOf(timeoutCounter));
            }
        }
    }

    public void setBackground(int resource) {
        this.timeOutButton.setBackgroundResource(resource);
    }

    public void setEnabled(boolean setEnabled) {
        this.timeOutButton.setEnabled(setEnabled);
    }

    public void setSelected(boolean setSelected) {
        this.timeOutButton.setSelected(setSelected);
    }
}
