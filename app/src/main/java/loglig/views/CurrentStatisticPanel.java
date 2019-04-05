package loglig.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import loglig.activities.GameMonitoringActivity;
import loglig.enums.NotificationEnum;
import loglig.enums.TeamEnum;
import loglig.is_uptown4.loglig.R;
import loglig.managers.PlayersManager;
import loglig.managers.RealmManager;
import loglig.managers.StatisticsManager;
import loglig.managers.TimerManager;
import loglig.models.Statistic;
import loglig.notification.LogLigNotification;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class CurrentStatisticPanel extends RelativeLayout implements Observer {

    private Context context;
    private PlayersManager playersManager;
    private StatisticsManager statisticsManager;
    private Button closeButton;
    private TextView playerNumber, statTimeSegment, statGameTime, statisticTypeItem;
    private LayoutInflater inflater;
    private boolean isVisible;

    public CurrentStatisticPanel(Context context) {
        super(context);
        this.context = context;
        init();
        initialHideCurrentStatisticPanel();
    }

    public CurrentStatisticPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurrentStatisticPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        playersManager = PlayersManager.getInstance();
        statisticsManager = StatisticsManager.getInstance();
        isVisible = true;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.current_statistic_layout, this, true);
        this.closeButton = (Button) findViewById(R.id.closeButton);
        this.playerNumber = (TextView) findViewById(R.id.currPlayerNumber);
        this.statisticTypeItem = (TextView) findViewById(R.id.currStatType);
        this.statTimeSegment = (TextView) findViewById(R.id.currentStatTimeSegmentText);
        this.statGameTime = (TextView) findViewById(R.id.currentStatTimeStampText);
        StatisticsManager.getInstance().register(this);
        setInitialFontColor();
    }

    public Button getCloseButton() {
        return closeButton;
    }

    private void setInitialFontColor() {
        this.playerNumber.setTextColor(ContextCompat.getColor(context, R.color.colorLoginFont));
        this.statTimeSegment.setTextColor(ContextCompat.getColor(context, R.color.colorLoginFont));
        this.statGameTime.setTextColor(ContextCompat.getColor(context, R.color.colorLoginFont));
        this.statisticTypeItem.setTextColor(ContextCompat.getColor(context, R.color.colorLoginFont));
    }

    public void initialHideCurrentStatisticPanel() {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.initial_close_panel);
        this.startAnimation(animation);
        isVisible = false;
    }

    public void resetCurrentStatisticPanelContent() {
        Statistic statistic = statisticsManager.getCurrentStatistic();
        if (statistic != null) {
            if (statistic.getStatisticType() == null) {
                setStatisticTypeData(getResources().getString(R.string.statistic_default));
                this.statisticTypeItem.setTextColor(ContextCompat.getColor(context, R.color.colorLoginFont));
            }
            if (RealmManager.getInstance().getPlayer(statistic.getPlayerID()) == null) {
                this.playerNumber.setTextColor(ContextCompat.getColor(context, R.color.colorLoginFont));
                this.playerNumber.setText(R.string.number_default);
            }
            if (statistic.getGameTime() == 0) {
                this.statGameTime.setTextColor(ContextCompat.getColor(context, R.color.colorLoginFont));
                this.statGameTime.setText(R.string.timer_setup_for_0_0);
                this.statTimeSegment.setTextColor(ContextCompat.getColor(context, R.color.colorLoginFont));
                this.statTimeSegment.setText(R.string.time_segment);
            }
        }
    }

    public void showCurrentStatisticPanel() {
        resetCurrentStatisticPanelContent();
        if (!isCurrentStatisticPanelVisible()) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.open_panel);
            this.startAnimation(animation);
            isVisible = true;
            setGameTime();
            setTimeSegment();
        }
        long timeStamp = TimerManager.getInstance().getGameTimestamp();
        setTimeSegment();
        setGameTime();
    }

    public void hideCurrentStatisticPanel() {
        if (isCurrentStatisticPanelVisible()) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.close_panel);
            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ((GameMonitoringActivity) context).setPlayByPlayContainerVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            this.startAnimation(animation);
            isVisible = false;
            //
        }
    }

    public boolean isCurrentStatisticPanelVisible() {
        if (isVisible) return true;
        else return false;
    }

    public void setPlayerData() {
        Statistic statistic = statisticsManager.getCurrentStatistic();
        if (playersManager.getIdentifierForTeamId(statistic.getTeamId()).equals(TeamEnum.TEAM_A)) {
            this.playerNumber.setTextColor(ContextCompat.getColor(context, R.color.teamACenterColor));
        } else if (playersManager.getIdentifierForTeamId(statistic.getTeamId()).equals(TeamEnum.TEAM_B)) {
            this.playerNumber.setTextColor(ContextCompat.getColor(context, R.color.teamBEndColor));
        } else {
            this.playerNumber.setTextColor(ContextCompat.getColor(context, R.color.colorNeutralStatistic));
        }
        playerNumber.setText(RealmManager.getInstance().getPlayer(statistic.getPlayerID()).getShirtNumber());
    }

    public void setStatisticTypeData(String statistic_type) {
        this.statisticTypeItem.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        this.statisticTypeItem.setText(statistic_type);
    }

    public void setTimeSegment() {
        this.statTimeSegment.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        this.statTimeSegment.setText(statisticsManager.getCurrentStatistic().getTimeSegmentName());
    }

    public void setGameTime() {
        this.statGameTime.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        this.statGameTime.setText(TimerManager.getInstance().getStringRepresentationOfTimeStamp(
                statisticsManager.getCurrentStatistic().getGameTime()));
        //TODO: replace the above line by following commented line:
        /* this.statGameTime.setText(TimerManager.getInstance().convertTimeToString(
                statisticsManager.getCurrentStatistic().getGameTime()));
                COMMENT: the function convertTimeToString() is implemented/refactored on branch LL-192
        */

    }

    public void updateGameTime() {
        this.statGameTime.setText(TimerManager.getInstance().getUpdatedStringRepresentationOfTimeStamp());
    }

    public void clearCurrentStatisticPanel() {
        setInitialFontColor();
        this.playerNumber.setText(R.string.number_default);
        this.statisticTypeItem.setText(R.string.statistic_default);
        this.statTimeSegment.setText(R.string.time_segment);
        this.statGameTime.setText(R.string.init_timer);
    }

    private void upadatePanelContext() {
        setGameTime();
        setTimeSegment();
        setStatisticTypeData(statisticsManager.getCurrentStatistic().getAbbreviation());
        setPlayerData();
        hideCurrentStatisticPanel();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (((LogLigNotification) data).getAction().equals(NotificationEnum.UPDATE_SELECTED_PLAYER)) {
            Statistic currentStatistic = statisticsManager.getCurrentStatistic();
            if (currentStatistic != null) {
                if (currentStatistic.getAbbreviation().equals("onField") || currentStatistic.getAbbreviation().equals("offField")) {
                    upadatePanelContext();
                } else {
                    setPlayerData();
                }
            }
        }
    }
}
