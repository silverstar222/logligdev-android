package loglig.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import loglig.is_uptown4.loglig.R;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class PlayByPlayPanel extends RelativeLayout {

    private Context context;
    private Button openClosePanelButton;
    private LayoutInflater inflater;
    private ListView currentGameStatisticsListView;
    private View container;
    private int containerHeight;


    public PlayByPlayPanel(Context context, View container) {
        super(context);
        this.context = context;
        this.container = container;
        this.setVisibility(INVISIBLE);
        init();
    }

    public PlayByPlayPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayByPlayPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.play_by_play_panel_layout, this, true);
        this.openClosePanelButton = (Button) findViewById(R.id.openCloseCurrentGameStatisticsListButton);
        this.currentGameStatisticsListView = (ListView) findViewById(R.id.currentGameStatisticsListView);
    }

    public Button getOpenClosePlayByPlayPanelButton() {
        return openClosePanelButton;
    }

    public void setPlayByPlayContainerHeight(int height) {
        this.containerHeight = height;
    }

    public ListView getPlayByPlayPanelListView() {
        return this.currentGameStatisticsListView;
    }

    public void initialClosePlayByPlayPanel() {
        this.openClosePanelButton.setText("Play By Play");
        ObjectAnimator initialCloseAnimator = ObjectAnimator.ofFloat(container, "translationY", 0, (containerHeight - convertDpIntoInt(50)));
        initialCloseAnimator.setDuration(0);
        initialCloseAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setPanelVisibility();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        initialCloseAnimator.start();
    }

    public void closePlayByPlayPanelPanel() {
        this.openClosePanelButton.setText("Play By Play");
        ObjectAnimator closeAnimator = ObjectAnimator.ofFloat(container, "translationY", 0, (container.getHeight() - convertDpIntoInt(50)));
        closeAnimator.setDuration(500);
        closeAnimator.start();
        container.setBackgroundColor(0x00000000);
    }

    public void openPlayByPlayPanelPanel() {
        this.openClosePanelButton.setText("Close");
        ObjectAnimator openAnimator = ObjectAnimator.ofFloat(container, "translationY", (container.getHeight()), 0);
        openAnimator.setDuration(500);
        openAnimator.start();
        container.setBackgroundResource(R.color.colorTransparent20);
    }

    public boolean isPlayByPlayPanelOpened() {
        String text = this.openClosePanelButton.getText().toString();
        if (text.equals("Play By Play"))
            return false;
        else return true;
    }

    private int convertDpIntoInt(int dp) {
        Resources r = getResources();
        int intNumber = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return intNumber;
    }

    private void setPanelVisibility() {
        this.setVisibility(VISIBLE);
    }
}
