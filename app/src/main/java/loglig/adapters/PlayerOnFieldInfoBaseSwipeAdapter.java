package loglig.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import loglig.activities.GameMonitoringActivity;
import loglig.enums.NotificationEnum;
import loglig.enums.TeamEnum;
import loglig.handlers.DatabaseChangesListener;
import loglig.is_uptown4.loglig.R;
import loglig.managers.PlayersManager;
import loglig.managers.RealmManager;
import loglig.managers.StatisticsManager;
import loglig.models.Player;
import loglig.notification.LogLigNotification;
import loglig.views.CircularImageView;

/**
 * Created by is_uptown4 on 10/05/16.
 */
public class PlayerOnFieldInfoBaseSwipeAdapter extends BaseSwipeAdapter implements Observer {

    ArrayList<View> items;
    private Context context;
    private StatisticsManager statisticsManager;
    private PlayersManager playersManager;
    private String teamId;
    private TeamEnum teamIdentifier;
    private CircularImageView playerImg;
    private TextView playerNumber;
    private TextView playerInfo;
    private Button openTeamPlayerListButton;
    private List<Player> players;

    // TODO: missing marking of selected player => LL-218

    public PlayerOnFieldInfoBaseSwipeAdapter(Context context, String teamId) {
        super();
        this.context = context;
        this.teamId = teamId;
        this.items = new ArrayList<View>();
        statisticsManager = StatisticsManager.getInstance();
        statisticsManager.register(this);
        playersManager = PlayersManager.getInstance();
        this.teamIdentifier = playersManager.getIdentifierForTeamId(teamId);
        players = PlayersManager.getInstance().playersOnFieldForTeam(teamIdentifier);
        PlayersManager.getInstance().addChangesListener(new DatabaseChangesListener() {
            @Override
            public void onChange() {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        players = PlayersManager.getInstance().playersOnFieldForTeam(teamIdentifier);
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        int resource = R.id.playerSwipeLayoutA;
        if (teamIdentifier.equals(TeamEnum.TEAM_B)) resource = R.id.playerSwipeLayoutB;
        return resource;
    }

    @Override
    public View generateView(final int position, final ViewGroup parent) {
        ArrayList<Player> players = playersManager.playersOnFieldForTeam(teamIdentifier);
        final Player player = players.get(position);
        int layoutResource = R.layout.player_info_list_item_for_team_a;
        int openTeamPlayerListButtonResource = R.id.openTeamAplayerList;
        if (teamIdentifier.equals(TeamEnum.TEAM_B)) {
            layoutResource = R.layout.player_info_list_item_for_team_b;
            openTeamPlayerListButtonResource = R.id.openTeamBplayerList;
        }
        final SwipeLayout swipeLayout = (SwipeLayout) LayoutInflater.from(context).inflate(layoutResource, null);
        if (!player.isOnField()) swipeLayout.setLongClickable(false);
        else swipeLayout.setLongClickable(true);

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
            }

            @Override
            public void onOpen(SwipeLayout layout) {
                ((GameMonitoringActivity) context).closeFullTeamListViews();
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });
        swipeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!playersManager.playersOnFieldForTeam(teamIdentifier).get(position).equals("-1")) {
//                    swipeLayout.setSwipeEnabled(false);
                    ((GameMonitoringActivity) context).closeFullTeamListViews();
                    resetSwipedViewAtPosition(position);
                    ((GameMonitoringActivity) context).resetPlayersListViewItemsBackgroundColor();
                    ((GameMonitoringActivity) context).showPlayerDetailsView(PlayersManager.getInstance().playersOnFieldForTeam(teamIdentifier).get(position));
                }
                return true;
            }
        });
        items.add(position, swipeLayout);

        return swipeLayout;

    }

    @Override
    public void fillValues(final int position, final View convertView) {
        final Player player = players.get(position);
        if (teamIdentifier.equals(TeamEnum.TEAM_A)) {
            playerImg = (CircularImageView) convertView.findViewById(R.id.playerImageA);
            playerNumber = (TextView) convertView.findViewById(R.id.playerNumberA);
            playerInfo = (TextView) convertView.findViewById(R.id.playerInfoA);
            openTeamPlayerListButton = (Button) convertView.findViewById(R.id.openTeamAplayerList);
        }
        // --- --- --- --- --- --- ---
        if (teamIdentifier.equals(TeamEnum.TEAM_B)) {
            playerImg = (CircularImageView) convertView.findViewById(R.id.playerImageB);
            playerNumber = (TextView) convertView.findViewById(R.id.playerNumberB);
            playerInfo = (TextView) convertView.findViewById(R.id.playerInfoB);
            openTeamPlayerListButton = (Button) convertView.findViewById(R.id.openTeamBplayerList);
        }
        // --- --- --- --- --- --- ---
        if (player.getId().startsWith("Empty"))
            playerImg.setVisibility(View.INVISIBLE);
        else playerImg.setVisibility(View.VISIBLE);
        // --- --- --- --- --- --- ---
        if (player.isOnField()) {
            Glide.with(context).load(player.getImageUrl()).asBitmap().placeholder(R.drawable.player_icon_holder).into(playerImg);
            playerNumber.setText(player.getShirtNumber());
            playerInfo.setText(player.getName());
        }
        // --- --- --- --- --- --- ---
        openTeamPlayerListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameMonitoringActivity) context).handleOpenFullTeamListEvent(position, teamIdentifier);
            }
        });
        // --- --- --- --- --- --- ---
        convertView.setLongClickable(true);
        ((SwipeLayout) convertView).setSwipeEnabled(true);
        // --- --- --- --- --- --- ---
        if (!player.isOnField()) ((SwipeLayout) convertView).setLongClickable(false);
        else ((SwipeLayout) convertView).setLongClickable(true);
        ((SwipeLayout) convertView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerId = player.getId().toLowerCase();
                if (!playerId.contains("empty") && player.isOnField()) {
                    ((GameMonitoringActivity) context).closeFullTeamListViews();
                    ((SwipeLayout) convertView).setSwipeEnabled(true);
//                    resetSwipedViewAtPosition(position);
                    ((GameMonitoringActivity) context).resetPlayersListViewItemsBackgroundColor();
                    //COMMENT: CircularImageView background need to be changed too
                    markSelectedView(((SwipeLayout) convertView), true);
                    Player currentPlayer = PlayersManager.getInstance().playersOnFieldForTeam(teamIdentifier).get(position);
                    statisticsManager.setCurrentPlayerSelected(currentPlayer);
                    statisticsManager.addCurrentStatisticTeamId(teamId);
                    statisticsManager.addCurrentStatisticPlayer(currentPlayer);
                    ((GameMonitoringActivity) context).showCurrentStatisticPanel();
                    if (statisticsManager.isCurrentStatisticReady()) {
                        statisticsManager.saveCurrentStatisticToList();
                        ((GameMonitoringActivity) context).refreshPlayByPlayList();
                        ((GameMonitoringActivity) context).removeLocationMarkerFromCourt();
                        ((GameMonitoringActivity) context).hideCurrentStatisticPanel();
                        markSelectedView(((SwipeLayout) convertView), false);
                    }
                }
            }
        });
    }

    private void markSelectedView(SwipeLayout view, boolean isMarked) {
        if (isMarked) {
            view.getChildAt(1).setBackgroundResource(R.drawable.player_list_litem_background_selected);
        } else {
            view.getChildAt(1).setBackgroundResource(R.color.colorBlack);
        }
    }

    public void resetSwipedViewAtPosition(int position) {
        if (position != -1) closeItem(position);
        notifyDataSetChanged();
    }

    public void resetListItemSelection(ListView listView) {
        for (int i = 0; i < listView.getChildCount(); i++) {
            View view = ((SwipeLayout) listView.getChildAt(i)).getChildAt(1);
            view.setBackgroundResource(R.color.colorBlack);
        }
        notifyDataSetChanged();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (((LogLigNotification) data).getAction().equals(NotificationEnum.UPDATE_SELECTED_PLAYER)) {
            resetSwipedViewAtPosition(-1);
        }
    }

    public void markAllPlayersAsOffField() {
        for (Player player : PlayersManager.getInstance().allPlayersForTeam(teamId)) {
            if (!player.equals(RealmManager.getInstance().emptyPlayerForSlot(teamId, player.getOnFieldPosition())))
                RealmManager.getInstance().updatePlayerOnFieldPosition(player.getId(), false, -1);
        }
    }
}