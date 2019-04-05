package loglig.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import loglig.enums.NotificationEnum;
import loglig.is_uptown4.loglig.R;
import loglig.managers.TimerManager;
import loglig.notification.LogLigNotification;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class TimerPanel extends RelativeLayout implements Observer {

    private TextView gameTimerView;
    private Context context;
    private LayoutInflater inflater;


    public TimerPanel(Context context) {
        super(context);
        init(context);
    }

    public TimerPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimerPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private String segmentDurationStr() {
        int segmentDuration = TimerManager.getInstance().getSegmentDuration();
        if (TimerManager.getInstance().isPaused()) {
            segmentDuration = ((int)TimerManager.getInstance().getCountDownStartTime() / 1000);
        }
        return String.format("%02d:%02d", segmentDuration / 60, segmentDuration % 60);
    }

    private void init(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.timer_layout, this, true);
        this.gameTimerView = (TextView) findViewById(R.id.timer);
        this.gameTimerView.setText(segmentDurationStr());
        TimerManager.getInstance().register(this);
    }

    private void updateTimerView(String time) {
        this.gameTimerView.setText(time);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (((LogLigNotification) data).getAction().equals(NotificationEnum.SCORE_UPDATE)) {
            updateTimerView(((LogLigNotification) data).getNotificationMessage());
        }

        if (((LogLigNotification) data).getAction().equals(NotificationEnum.TIMER_INCREMENT)) {
            updateTimerView(((LogLigNotification) data).getNotificationMessage().toString());
        }

        if (((LogLigNotification) data).getAction().equals(NotificationEnum.SEGMENT_END)) {
            if (TimerManager.getInstance().isTimeSegment()) {
                updateTimerView(segmentDurationStr());
            }
        }

        if (((LogLigNotification) data).getAction().equals(NotificationEnum.ADD_NEW_OVER_TIME_SEGMENT)) {
            if (TimerManager.getInstance().isOverTime()) {
                updateTimerView(segmentDurationStr());
            }
        }
    }
}