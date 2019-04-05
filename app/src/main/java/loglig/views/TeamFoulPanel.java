package loglig.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import loglig.enums.TeamEnum;
import loglig.is_uptown4.loglig.R;
import loglig.managers.PlayersManager;
import loglig.managers.StatisticsManager;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class TeamFoulPanel extends LinearLayout {

    private Button teamFoulButton;
    private LayoutInflater inflater;
    PlayersManager playersManager;

    public TeamFoulPanel(Context context) {
        super(context);
        init(context);
    }

    public TeamFoulPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TeamFoulPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        playersManager = PlayersManager.getInstance();

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.team_foul_layout, this, true);
        this.teamFoulButton = (Button) findViewById(R.id.teamFoulButton);
        if (getCurrentTeamIdentifier().equals(TeamEnum.TEAM_A))
            teamFoulButton.setTextColor(ContextCompat.getColor(context, R.color.teamACenterColor));
        else teamFoulButton.setTextColor(ContextCompat.getColor(context, R.color.teamBCenterColor));
        this.teamFoulButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsManager.getInstance().createTeamFoulStatistic(playersManager.getCurrentTeamId(getCurrentTeamIdentifier()));
            }
        });
    }

    private TeamEnum getCurrentTeamIdentifier() {
        TeamEnum team = TeamEnum.NONE;
        if (this.getId() == R.id.teamATFoul) {
            team = TeamEnum.TEAM_A;
        }
        if (this.getId() == R.id.teamBTFoul) {
            team = TeamEnum.TEAM_B;
        }
        return team;
    }

}