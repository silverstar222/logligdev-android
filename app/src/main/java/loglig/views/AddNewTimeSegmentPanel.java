package loglig.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
public class AddNewTimeSegmentPanel extends RelativeLayout implements Observer {

    private LayoutInflater inflater;
    private Context context;
    private Button addNewTimeSegmentButton;
    private TextView addNewTimeSegmentTitle;

    public AddNewTimeSegmentPanel(Context context) {
        super(context);
        init(context);
    }

    public AddNewTimeSegmentPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddNewTimeSegmentPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.add_new_time_segment_layout, this, true);
        this.context = context;

        TimerManager.getInstance().register(this);

        this.addNewTimeSegmentTitle = (TextView) findViewById(R.id.addNewTimeSegmentTitle);

        this.addNewTimeSegmentButton = (Button) findViewById(R.id.addNewTimeSegmentButton);
        this.addNewTimeSegmentButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnableAddNewOverTimeButton(false);
                TimerManager.getInstance().processAddNewOverTimeEvent();
            }
        });
        setEnableAddNewOverTimeButton(false);

    }

    private void setEnableAddNewOverTimeButton(boolean enable) {
        this.addNewTimeSegmentButton.setEnabled(enable);
        if (enable) {
            this.addNewTimeSegmentTitle.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            this.addNewTimeSegmentButton.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            this.addNewTimeSegmentButton.setBackgroundResource(R.drawable.add_time_segment_border);
        } else {
            this.addNewTimeSegmentTitle.setTextColor(ContextCompat.getColor(context, R.color.colorLoginFont));
            this.addNewTimeSegmentButton.setTextColor(ContextCompat.getColor(context, R.color.colorLoginFont));
            this.addNewTimeSegmentButton.setBackgroundResource(R.drawable.add_time_segment_border_disabled);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (((LogLigNotification) data).getAction().equals(NotificationEnum.TIMER_INCREMENT)) {
            if (((LogLigNotification) data).getNotificationMessage().equals(TimerManager.timeIsUp)) {
                if(TimerManager.getInstance().isLastTimeSegment() || TimerManager.getInstance().isOverTime())
                    setEnableAddNewOverTimeButton(true);
            }
        }
    }
}