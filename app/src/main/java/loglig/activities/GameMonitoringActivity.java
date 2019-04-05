package loglig.activities;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import loglig.adapters.PlayByPlayListAdapter;
import loglig.adapters.PlayerNotOnFieldInfoAdapter;
import loglig.adapters.PlayerOnFieldInfoBaseSwipeAdapter;
import loglig.enums.NotificationEnum;
import loglig.enums.TeamEnum;
import loglig.interfaces.OnHidePlayerDetailsViewListener;
import loglig.is_uptown4.loglig.R;
import loglig.managers.GameManager;
import loglig.managers.PlayersManager;
import loglig.managers.StatisticsManager;
import loglig.managers.TimerManager;
import loglig.models.Player;
import loglig.models.Point;
import loglig.models.StatisticType;
import loglig.notification.LogLigNotification;
import loglig.views.CurrentStatisticPanel;
import loglig.views.DurationPicker;
import loglig.views.PlayByPlayPanel;
import loglig.views.PlayerDetailsView;
import loglig.views.StatisticTypePanel;

public class GameMonitoringActivity extends Activity {

    private static final int POINT_MARK_SIZE = 25;
    private Context context;
    private PlayersManager playersManager;
    private StatisticsManager statisticsManager;
    private GameManager gameManager;
    private PlayByPlayListAdapter playByPlayListAdapter;
    private PlayByPlayPanel playByPlayPanel;
    private CurrentStatisticPanel currentStatisticPanel;
    private RelativeLayout playByPlayContainer;
    private RelativeLayout courtContainer;
    private ImageView courtImage;
    private ListView teamList1, fullTeamListA, teamList2, fullTeamListB;
    private SwipeLayout currentSelectedPlayerItem;
    private PlayerOnFieldInfoBaseSwipeAdapter playerOnFieldTeamAswipeAdapter, playerOnFieldTeamBswipeAdapter;
    private PlayerNotOnFieldInfoAdapter playerNotOnFieldTeamAadapter, playerNotOnFieldTeamBadapter;
    private RelativeLayout currentStatisticContainer, playerDetailsViewContainer;
    private PlayerDetailsView playerDetailsView;
    private PopupWindow playerPopupWindow;
    private RelativeLayout gameMonitoringLayout;
    private List<Observer> observers = new ArrayList<>();
    private OnHidePlayerDetailsViewListener onHidePlayerDetailsViewListener;
    private StatisticTypePanel statisticTypePanel;

    public void hideCurrentStatisticPanel() {
        currentStatisticPanel.hideCurrentStatisticPanel();
    }

    public void removeLocationMarkerFromCourt() {
        int childCount = courtContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = (ImageView) courtContainer.getChildAt(i);
            if (!childView.equals(courtImage)) {
                courtContainer.removeView(childView);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (playerOnFieldTeamAswipeAdapter != null && playerOnFieldTeamBswipeAdapter != null) {
            playerOnFieldTeamAswipeAdapter.notifyDataSetChanged();
            playerOnFieldTeamBswipeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_monitoring);
        this.context = this;
        playersManager = PlayersManager.getInstance();
        gameManager = GameManager.getInstance();
        statisticsManager = StatisticsManager.getInstance();
        this.gameMonitoringLayout = (RelativeLayout) findViewById(R.id.gameMonitoringLayout);
        // --- --- ---
        this.playerOnFieldTeamAswipeAdapter = new PlayerOnFieldInfoBaseSwipeAdapter(this, gameManager.getCurrentGame().getHomeTeamId());
        this.playerOnFieldTeamBswipeAdapter = new PlayerOnFieldInfoBaseSwipeAdapter(this, gameManager.getCurrentGame().getAwayTeamId());
        // --- --- ---
        this.teamList1 = (ListView) findViewById(R.id.teamListA);
        this.fullTeamListA = (ListView) findViewById(R.id.fullTeamListA);
        this.teamList2 = (ListView) findViewById(R.id.teamListB);
        this.fullTeamListB = (ListView) findViewById(R.id.fullTeamListB);
        // --- --- ---
        teamList1.setAdapter(playerOnFieldTeamAswipeAdapter);
        playerOnFieldTeamAswipeAdapter.setMode(Attributes.Mode.Single);
        teamList2.setAdapter(playerOnFieldTeamBswipeAdapter);
        playerOnFieldTeamBswipeAdapter.setMode(Attributes.Mode.Single);
        // --- --- ---
        this.playerNotOnFieldTeamAadapter = new PlayerNotOnFieldInfoAdapter(this, gameManager.getCurrentGame().getHomeTeamId());
        this.fullTeamListA.setAdapter(playerNotOnFieldTeamAadapter);
        this.playerNotOnFieldTeamBadapter = new PlayerNotOnFieldInfoAdapter(this, gameManager.getCurrentGame().getAwayTeamId());
        this.fullTeamListB.setAdapter(playerNotOnFieldTeamBadapter);
        // --- --- ---
        this.currentStatisticContainer = (RelativeLayout) findViewById(R.id.currentStatisticContainer);
        // --- --- ---
        this.currentStatisticPanel = new CurrentStatisticPanel(this);
        this.currentStatisticPanel.getCloseButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeLocationMarkerFromCourt();
                hideCurrentStatisticPanel();
                clearCurrentStatisticPanel();
                statisticsManager.clearCurrentStatistic();
                currentStatisticPanel.resetCurrentStatisticPanelContent();
                resetPlayersListViewItemsBackgroundColor();
                removeLocationMarkerFromCourt();
            }
        });
        this.currentStatisticContainer.addView(currentStatisticPanel, panelLayoutParams());
        // --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---
        this.playByPlayContainer = (RelativeLayout) findViewById(R.id.playByPlayContainer);
        this.playByPlayPanel = new PlayByPlayPanel(this, playByPlayContainer);
        this.playByPlayPanel.getOpenClosePlayByPlayPanelButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playByPlayPanel.isPlayByPlayPanelOpened())
                    closePlayByPlayPanel();
                else showPlayByPlayPanel();
            }
        });
        this.playByPlayListAdapter = new PlayByPlayListAdapter(this);
        this.playByPlayPanel.getPlayByPlayPanelListView().setAdapter(playByPlayListAdapter);
        this.playByPlayContainer.addView(playByPlayPanel, panelLayoutParams());
        this.playByPlayContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!view.equals(playByPlayPanel) && playByPlayPanel.isPlayByPlayPanelOpened()) {
                    closePlayByPlayPanel();
                }
            }
        });
        // --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---
        this.courtContainer = (RelativeLayout) findViewById(R.id.courtContainer);
        this.courtImage = (ImageView) findViewById(R.id.courtImage);
        this.courtImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    removeLocationMarkerFromCourt();
                    addStatisticLocation(new Point(event.getX(), event.getY()));
                    ImageView pointMark = new ImageView(getApplicationContext());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(POINT_MARK_SIZE, POINT_MARK_SIZE);
                    params.leftMargin = (int) event.getX() - POINT_MARK_SIZE / 2;
                    params.topMargin = (int) event.getY() - POINT_MARK_SIZE / 2;
                    pointMark.setImageResource(R.drawable.field_point_x);
                    courtContainer.addView(pointMark, params);
                }
                return true;
            }
        });
        // --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---
        statisticTypePanel = (StatisticTypePanel) findViewById(R.id.statisticTypePanel);
        recoverCurrentGameState();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int height = this.playByPlayContainer.getHeight();
        this.playByPlayPanel.setPlayByPlayContainerHeight(height);
        this.playByPlayPanel.initialClosePlayByPlayPanel();
    }

    @Override
    public void onBackPressed() {
        if (TimerManager.getInstance().isTimerOn()) {
            TimerManager.getInstance().pauseTimer();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private RelativeLayout.LayoutParams panelLayoutParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        return params;
    }

    public void enablePlayerInfoView() {
        if (this.currentSelectedPlayerItem != null) {
            this.currentSelectedPlayerItem.close();
//            this.currentSelectedPlayerItem.setBackgroundResource(R.drawable.player_info_list_item_effect);
            this.currentSelectedPlayerItem = null;
        }
    }

    public void setFullTeamListViewVisibility(TeamEnum teamId, boolean visibility) {
        if (teamId.equals(TeamEnum.TEAM_A)) {
            if (visibility) {
                this.fullTeamListB.setVisibility(View.INVISIBLE);
                this.fullTeamListA.setVisibility(View.VISIBLE);
            } else {
                this.fullTeamListA.setVisibility(View.INVISIBLE);
            }
        } else {
            if (visibility) {
                this.fullTeamListA.setVisibility(View.INVISIBLE);
                this.fullTeamListB.setVisibility(View.VISIBLE);
            } else {
                this.fullTeamListB.setVisibility(View.INVISIBLE);
            }
        }
        this.playerNotOnFieldTeamAadapter.notifyDataSetChanged();
        this.playerNotOnFieldTeamBadapter.notifyDataSetChanged();
    }

    public void updateListView(ListView listView) {
        if (listView.equals(fullTeamListA)) {
            playerOnFieldTeamAswipeAdapter.notifyDataSetChanged();
            playerNotOnFieldTeamAadapter.notifyDataSetChanged();
        }
        if (listView.equals(fullTeamListB)) {
            playerOnFieldTeamBswipeAdapter.notifyDataSetChanged();
            playerNotOnFieldTeamBadapter.notifyDataSetChanged();
        }
    }

    public void addStatisticLocation(Point point) {
        statisticsManager.addCurrentStatisticLocation(point);
        showCurrentStatisticPanel();

        if (statisticsManager.isCurrentStatisticReady()) {
            statisticsManager.saveCurrentStatisticToList();
        }

        refreshPlayByPlayList();
        hideCurrentStatisticPanel();
        resetPlayersListViewItemsBackgroundColor();
        removeLocationMarkerFromCourt();

    }

    public void addStatisticType(final View view) {
        if (TimerManager.getInstance().isTimerOn()) {
            statisticTypePanel.highlightPressedButton(view);

            String statisticAbbreviation = ((Button) view).getText().toString();
            StatisticType statisticType = statisticsManager.getStatisticTypeByAbbreviation(statisticAbbreviation);
            statisticsManager.addCurrentStatisticType(statisticType);
            currentStatisticPanel.setStatisticTypeData(statisticAbbreviation);
            showCurrentStatisticPanel();

            if (statisticsManager.isCurrentStatisticReady()) {
                statisticsManager.saveCurrentStatisticToList();
            }

            refreshPlayByPlayList();
            hideCurrentStatisticPanel();
            resetPlayersListViewItemsBackgroundColor();
            removeLocationMarkerFromCourt();
        }
    }

    public void showCurrentStatisticPanel() {
        this.currentStatisticPanel.showCurrentStatisticPanel();
    }

    public void setPlayByPlayContainerVisibility(final int visibility) {
        playByPlayContainer.setVisibility(visibility);
    }

    public void clearCurrentStatisticPanel() {
        this.currentStatisticPanel.clearCurrentStatisticPanel();
    }

    public void showPlayByPlayPanel() {
        if (statisticsManager.getCurrentStatisticsListSize() > 0) {
            if (!currentStatisticPanel.isCurrentStatisticPanelVisible()) {
                hideCurrentStatisticPanel();
                refreshPlayByPlayList();
                //statisticsManager.sortCurrentStatisticsList();
                this.playByPlayPanel.openPlayByPlayPanelPanel();
            }
        }
    }

    public void closePlayByPlayPanel() {
        playByPlayPanel.closePlayByPlayPanelPanel();
    }

    public void refreshPlayByPlayList() {
        playByPlayListAdapter.updateStatisticList();
        playByPlayListAdapter.notifyDataSetChanged();
    }

    public void showPlayerDetailsView(Player player) {
        this.fullTeamListA.setVisibility(View.INVISIBLE);
        this.fullTeamListB.setVisibility(View.INVISIBLE);

        this.playerDetailsView = new PlayerDetailsView(context, player);
        onHidePlayerDetailsViewListener = this.playerDetailsView;
        this.playerDetailsView.getClosePlayerDetailsButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHidePlayerDetailsViewListener.onHide();
                hidePlayerDetailsView();
            }
        });

        playerPopupWindow = new PopupWindow(playerDetailsView,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        playerPopupWindow.setFocusable(true);
        playerPopupWindow.update();
        playerPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.player_details_popup_window_background));
        playerPopupWindow.showAtLocation(gameMonitoringLayout, Gravity.CENTER, 0, 0);
    }

    public void hidePlayerDetailsView() {
        playerPopupWindow.dismiss();
    }

    public void animateSwipedPlayer(TeamEnum teamId, int playerPositionInList) {
        View viewToAnimate = null;

        if (teamId.equals(TeamEnum.TEAM_A))
            viewToAnimate = this.teamList1.getChildAt(playerPositionInList);
        else viewToAnimate = this.teamList2.getChildAt(playerPositionInList);

        ObjectAnimator swipeAnimation = ObjectAnimator.ofObject(viewToAnimate, "backgroundColor",
                new ArgbEvaluator(), ContextCompat.getColor(this, R.color.colorTransparent20), Color.BLACK);
        swipeAnimation.setDuration(350);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(swipeAnimation);
        animatorSet.start();
    }

    public void handleOpenFullTeamListEvent(int position, TeamEnum teamId) {
        Player player = null;
        if (teamId.equals(TeamEnum.TEAM_A)) {
            if (this.currentSelectedPlayerItem != null && ((ListView) this.currentSelectedPlayerItem.getParent()).getId() == teamList2.getId()) {
                this.currentSelectedPlayerItem.close();
            }
            setFullTeamListViewVisibility(TeamEnum.TEAM_A, true);
            this.currentSelectedPlayerItem = (SwipeLayout) this.teamList1.getChildAt(position);

            player = playersManager.playersOnFieldForTeam(TeamEnum.TEAM_A).get(position);

        } else {
            if (this.currentSelectedPlayerItem != null && ((ListView) this.currentSelectedPlayerItem.getParent()).getId() == teamList1.getId()) {
                this.currentSelectedPlayerItem.close();
            }
            setFullTeamListViewVisibility(TeamEnum.TEAM_B, true);
            this.currentSelectedPlayerItem = (SwipeLayout) this.teamList2.getChildAt(position);

            player = playersManager.playersOnFieldForTeam(TeamEnum.TEAM_B).get(position);

        }
        statisticsManager.setCurrentPlayerSelected(player);
    }

    public void closeFullTeamListViews() {
        this.fullTeamListA.setVisibility(View.INVISIBLE);
        this.fullTeamListB.setVisibility(View.INVISIBLE);

    }

    public void resetPlayersListViewItemsBackgroundColor() {
        this.playerOnFieldTeamAswipeAdapter.resetListItemSelection(this.teamList1);
        this.playerOnFieldTeamBswipeAdapter.resetListItemSelection(this.teamList2);
    }

    public void refreshTeamList(TeamEnum teamId) {
        if (teamId.equals(TeamEnum.TEAM_A))
            this.playerOnFieldTeamAswipeAdapter.notifyDataSetChanged();
        else this.playerOnFieldTeamBswipeAdapter.notifyDataSetChanged();
    }

    public void endGame(String note) {
        TimerManager.getInstance().stopTimer();
        StatisticsManager.getInstance().createGameOverStatistic(note);
        playerOnFieldTeamAswipeAdapter.markAllPlayersAsOffField();
        playerOnFieldTeamBswipeAdapter.markAllPlayersAsOffField();
//        gameManager.markAllPlayersAsOffField();// TODO handle thread
        //gameManager.deleteAllStatistics();
        gameManager.setCurrentGame(null);
        //AppService.destroyManagers();

        Intent intent = new Intent(context, GameSelectionActivity.class);
        startActivity(intent);
        finish();
    }

    public void register(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers(LogLigNotification notification) {
        for (Observer observer : observers) {
            observer.update(null, notification);
        }
    }

    private void recoverCurrentGameState() {
        LogLigNotification notification = new LogLigNotification();
        notification.setAction(NotificationEnum.SCORE_UPDATE);
        notifyAllObservers(notification);
    }

    public void setNewTime(View view) {
        DurationPicker timePickerDialog = new DurationPicker(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TimerManager.getInstance().stopTimer();
                TimerManager.getInstance().updateTimer(hourOfDay, minute);
                TimerManager.getInstance().startTimer();
            }
        }, 10, 0);
        timePickerDialog.show();
    }

    public float getCourtContainerScale() {
        return courtContainer.getWidth() / courtContainer.getY();
    }
}

