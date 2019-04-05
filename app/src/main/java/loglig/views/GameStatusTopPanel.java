package loglig.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import loglig.is_uptown4.loglig.R;


/**
 * Created by is_uptown4 on 07/06/16.
 */
public class GameStatusTopPanel extends RelativeLayout {

    private LayoutInflater inflater;

    public GameStatusTopPanel(Context context) {
        super(context);
        init(context);
    }

    public GameStatusTopPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameStatusTopPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.game_status_panel_layout, this, true);
    }
}