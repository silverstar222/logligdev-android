package loglig.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Observable;
import java.util.Observer;

import loglig.is_uptown4.loglig.R;
import loglig.managers.TimerManager;
import loglig.notification.LogLigNotification;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class PlayPauseClockPanel extends RelativeLayout implements Observer {

    private Context context = null;
    private LayoutInflater inflater;
    private Button startPauseClockButton, stopClockButton;

    public PlayPauseClockPanel(Context context) {
        super(context);
        init(context);
    }

    public PlayPauseClockPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayPauseClockPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context _context) {
        context = _context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.play_pause_clock_layout, this, true);
        TimerManager.getInstance().register(this);

        this.startPauseClockButton = (Button) findViewById(R.id.startPauseClockButton);

        this.startPauseClockButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable.ConstantState viewBackground = v.getBackground().getConstantState();
                Drawable.ConstantState playButtonBackground = ContextCompat.getDrawable(context, R.drawable.clock_play_button).getConstantState();
                if (playButtonBackground.equals(viewBackground)) {
                    ((Button) v).setBackgroundResource(R.drawable.clock_pause_button);
                    TimerManager.getInstance().startTimer();
                } else {
                    ((Button) v).setBackgroundResource(R.drawable.clock_play_button);
                    TimerManager.getInstance().pauseTimer();
                }
            }
        });
    }

    @Override
    public void update(Observable observable, Object data) {
        switch ((((LogLigNotification) data).getAction())) {
            case TIMER_UPDATE:
                resetStartPauseClockButton();
                break;
            case SEGMENT_END:
                if (TimerManager.getInstance().isOverTime()) {
                    startPauseClockButton.setEnabled(false);
                    startPauseClockButton.setAlpha((float) 0.2);
                }
                break;
            case ADD_NEW_OVER_TIME_SEGMENT:
                startPauseClockButton.setEnabled(true);
                startPauseClockButton.setAlpha((float) 1.0);
                break;
            default:
                break;
        }
    }

    public void resetStartPauseClockButton() {
        if (TimerManager.getInstance().isTimerOn() || TimerManager.getInstance().isPaused())
            this.startPauseClockButton.setBackgroundResource(R.drawable.clock_pause_button);
        else this.startPauseClockButton.setBackgroundResource(R.drawable.clock_play_button);
    }

}
