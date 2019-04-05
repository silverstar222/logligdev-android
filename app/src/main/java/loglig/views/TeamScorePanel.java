package loglig.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import loglig.activities.GameMonitoringActivity;
import loglig.enums.NotificationEnum;
import loglig.enums.TeamEnum;
import loglig.is_uptown4.loglig.R;
import loglig.managers.GameManager;
import loglig.managers.PlayersManager;
import loglig.managers.StatisticsManager;
import loglig.notification.LogLigNotification;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class TeamScorePanel extends RelativeLayout implements Observer {

    private TextView teamScore;
    private LayoutInflater inflater;
    private PlayersManager playersManager;
    private GameManager gameManager;

    public TeamScorePanel(Context context) {
        super(context);
        init(context);
    }

    public TeamScorePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TeamScorePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        playersManager = PlayersManager.getInstance();
        gameManager = GameManager.getInstance();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.team_score_layout, this, true);
        this.teamScore = (TextView) findViewById(R.id.teamScore);
        StatisticsManager.getInstance().register(this);
        ((GameMonitoringActivity)context).register(this);
    }

    public void updateScoreView(String score) {
        this.teamScore.setText(score);
    }

    private TeamEnum getCurrentTeamIdentifier() {
        TeamEnum team = TeamEnum.NONE;
        if (this.getId() == R.id.teamAScore) {
            team = TeamEnum.TEAM_A;
        }
        if (this.getId() == R.id.teamBScore) {
            team = TeamEnum.TEAM_B;
        }
        return team;
    }


    @Override
    public void update(Observable observable, Object data) {
        if (((LogLigNotification) data).getAction().equals(NotificationEnum.SCORE_UPDATE)) {
            String id = playersManager.getCurrentTeamId(getCurrentTeamIdentifier());
            if(!id.equals("0"))
                updateScoreView(String.valueOf(StatisticsManager.getInstance().calculateScore(id)));
        }
    }
}