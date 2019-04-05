package loglig.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Observable;
import java.util.Observer;

import loglig.activities.GameMonitoringActivity;
import loglig.enums.NotificationEnum;
import loglig.enums.TeamEnum;
import loglig.is_uptown4.loglig.R;
import loglig.managers.PlayersManager;
import loglig.managers.StatisticsManager;
import loglig.managers.TimerManager;
import loglig.notification.LogLigNotification;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class TimeSegmentPanel extends RelativeLayout implements Observer {

    private TimerManager timerManager;
    private StatisticsManager statisticsManager;
    private PlayersManager playersManager;
    private LayoutInflater inflater;
    private Context context;
    private LinearLayout timeSegmentContainer;
    private HorizontalScrollView timeSegmentsScrollViewContainer;

    public TimeSegmentPanel(Context context) {
        super(context);
        init(context);
    }

    public TimeSegmentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimeSegmentPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.time_segment_panel_layout, this, true);
        this.context = context;

        playersManager = PlayersManager.getInstance();
        statisticsManager = StatisticsManager.getInstance();
        statisticsManager.register(this);
        timerManager = TimerManager.getInstance();
        timerManager.register(this);

        if (context instanceof GameMonitoringActivity) {
            GameMonitoringActivity activity = (GameMonitoringActivity) context;
            activity.register(this);
        } else {
            throw new IllegalArgumentException();
        }

        this.timeSegmentContainer = (LinearLayout) findViewById(R.id.timeSegmentContainer);
        this.timeSegmentsScrollViewContainer = (HorizontalScrollView) findViewById(R.id.timeSegmentsScrollViewContainer);

        int maxNumberOfTimeSegments = timerManager.getMaximumNumberOfTimeSegments();
        for (int i = 0; i < maxNumberOfTimeSegments; i++) {
            TimeSegmentItem timeSegment = new TimeSegmentItem(context);
            timeSegment.setQuarterNumber((i + 1) + "");
            if (i == timerManager.getCurrentQuarter() - 1)
                timeSegment.setTimeSegmentBackground(R.drawable.time_segment_background);
            else timeSegment.setTimeSegmentBackground(R.drawable.time_segment_border);
            timeSegmentContainer.addView(timeSegment);
        }
        for (int i = maxNumberOfTimeSegments; i < timerManager.getCurrentTimeSegment(); i++) {
            TimeSegmentItem timeSegment = new TimeSegmentItem(context);
            timeSegment.setQuarterNumber("OT" + String.valueOf((i + 1) - maxNumberOfTimeSegments));
            if (i + 1 == timerManager.getCurrentTimeSegment())
                timeSegment.setTimeSegmentBackground(R.drawable.time_segment_background);
            else timeSegment.setTimeSegmentBackground(R.drawable.time_segment_border);
            timeSegmentContainer.addView(timeSegment);
        }
        updateTimeSegmentsScrollViewContainer();

    }

    private void updateTimeSegmentScore() {
        int currentTimeSegment = timerManager.getCurrentTimeSegment();
        View currentTimeSegmentView = this.timeSegmentContainer.getChildAt(currentTimeSegment - 1);

        String teamAscore = String.valueOf(statisticsManager.calculateScoreByTimeSegmentNumber(
                playersManager.getCurrentTeamId(TeamEnum.TEAM_A), timerManager.getTimeSegmentName())
        );
        String teamBscore = String.valueOf(statisticsManager.calculateScoreByTimeSegmentNumber(
                playersManager.getCurrentTeamId(TeamEnum.TEAM_B), timerManager.getTimeSegmentName())
        );
        if (currentTimeSegmentView != null)
            ((TimeSegmentItem) currentTimeSegmentView).setTimeSegmentScore(teamAscore + "-" + teamBscore);
    }

    private void updateTimeSegmentTeamFouls() {
        int currentTimeSegment = timerManager.getCurrentTimeSegment();
        View currentTimeSegmentView = this.timeSegmentContainer.getChildAt(currentTimeSegment - 1);

        String teamAfouls = String.valueOf(
                statisticsManager.calculateFoulsByTimeSegmentNumber(
                        playersManager.getCurrentTeamId(TeamEnum.TEAM_A),
                        timerManager.getTimeSegmentName())
        );
        String teamBfouls = String.valueOf(
                statisticsManager.calculateFoulsByTimeSegmentNumber(
                        playersManager.getCurrentTeamId(TeamEnum.TEAM_B),
                        timerManager.getTimeSegmentName())
        );
        ((TimeSegmentItem) currentTimeSegmentView).setTimeSegmentFouls(teamAfouls + "-" + teamBfouls);
    }

    private void createNewTimeSegment() {
        TimeSegmentItem overTime = new TimeSegmentItem(context);
        overTime.setQuarterNumber("OT" + String.valueOf(
                timerManager.getCurrentTimeSegment() -
                        (timerManager.getMaximumNumberOfTimeSegments()))
        );
        overTime.setTimeSegmentBackground(R.drawable.time_segment_background);
        this.timeSegmentContainer.addView(overTime);
        updateTimeSegmentsScrollViewContainer();

        TimeSegmentItem timeSegmentItem = ((TimeSegmentItem) this.timeSegmentContainer.getChildAt(timerManager.getCurrentTimeSegment() - 2));
        if (timeSegmentItem != null) {
            timeSegmentItem.setTimeSegmentBackground(R.drawable.time_segment_border);
        }
    }

    private void updateTimeSegmentsScrollViewContainer() {
        this.timeSegmentsScrollViewContainer.post(new Runnable() {
            @Override
            public void run() {
                timeSegmentsScrollViewContainer.fullScroll(View.FOCUS_RIGHT);
            }
        });
    }

    private void removeBackgrounOfLastTimeSegment() {
        TimeSegmentItem timeSegmentItem = ((TimeSegmentItem) this.timeSegmentContainer.getChildAt(timerManager.getCurrentTimeSegment() - 1));
        if (timeSegmentItem != null) {
            timeSegmentItem.setTimeSegmentBackground(R.drawable.time_segment_border);
        }
    }

    private void setBackgroundToCurrentTimeSegment() {

        TimeSegmentItem timeSegmentItem = ((TimeSegmentItem) this.timeSegmentContainer.getChildAt(timerManager.getCurrentTimeSegment()));

        if (timeSegmentItem != null) {
            timeSegmentItem.setTimeSegmentBackground(R.drawable.time_segment_background);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (((LogLigNotification) data).getAction().equals(NotificationEnum.ADD_NEW_OVER_TIME_SEGMENT)) {
            createNewTimeSegment();
        }
        if (((LogLigNotification) data).getAction().equals(NotificationEnum.TIMER_INCREMENT)) {
            if (((LogLigNotification) data).getNotificationMessage().equals(TimerManager.timeIsUp)) {
                if (timerManager.isTimeSegment() && !timerManager.isLastTimeSegment()) {
                    removeBackgrounOfLastTimeSegment();
                    setBackgroundToCurrentTimeSegment();
                }
            }
        }
        if (((LogLigNotification) data).getAction().equals(NotificationEnum.SCORE_UPDATE)
                || ((LogLigNotification) data).getAction().equals(NotificationEnum.TEAM_STATISTIC_UPDATE)) {
            updateTimeSegmentScore();
            updateTimeSegmentTeamFouls();
        }
        Log.d("ACTION", ((LogLigNotification) data).getAction().toString());
    }
}