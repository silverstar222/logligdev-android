package loglig.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import loglig.is_uptown4.loglig.R;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class TimeSegmentItem extends RelativeLayout implements Observer {

    private TextView timeSegmentNumber, timeSegmentScore, timeSegmentFoul;
    private LayoutInflater inflater;

    public TimeSegmentItem(Context context) {
        super(context);
        init(context);
    }

    public TimeSegmentItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimeSegmentItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.time_segment_item_layout, this, true);

        this.timeSegmentNumber = (TextView) findViewById(R.id.timeSegmentNumber);
        this.timeSegmentScore = (TextView) findViewById(R.id.timeSegmentScore);
        this.timeSegmentFoul = (TextView) findViewById(R.id.timeSegmentFoul);
    }

    public void setTimeSegmentBackground(int backgroundResource) {
        this.timeSegmentNumber.setBackgroundResource(backgroundResource);
    }

    public void setQuarterNumber(String quarterNumber) {
        this.timeSegmentNumber.setText(quarterNumber);
    }

    public void setTimeSegmentScore(String score) {
        this.timeSegmentScore.setText(score);
    }

    public void setTimeSegmentFouls(String fouls) {
        this.timeSegmentFoul.setText(fouls);
    }

    @Override
    public void update(Observable observable, Object data) {
    }
}