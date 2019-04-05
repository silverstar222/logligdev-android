package loglig.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import loglig.is_uptown4.loglig.R;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class StatisticTypeItem extends LinearLayout {

    private Button statisticTypeButton;
    private LayoutInflater inflater;

    public StatisticTypeItem(Context context) {
        super(context);
        init(context);
    }

    public StatisticTypeItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StatisticTypeItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.statistic_type_panle_item_layout, this, true);
        this.statisticTypeButton = (Button) findViewById(R.id.statisticTypeItemButton);
    }

    public void setText(String text) {
        this.statisticTypeButton.setText(text);
    }

    public void setTextColor(int color) {
        this.statisticTypeButton.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    public void setBackground(int resource) {
        this.statisticTypeButton.setBackgroundResource(resource);
    }

    public void setEnabled(boolean setEnabled) {
        this.statisticTypeButton.setEnabled(setEnabled);
    }

    public void setSelected(boolean setSelected) {
        this.statisticTypeButton.setSelected(setSelected);
    }
}