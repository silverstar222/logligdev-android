package loglig.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;

import loglig.activities.GameMonitoringActivity;
import loglig.enums.TeamEnum;
import loglig.is_uptown4.loglig.R;
import loglig.managers.PlayersManager;
import loglig.models.Player;
import loglig.views.CircularImageView;

/**
 * Created by is_uptown4 on 10/05/16.
 */
public class PlayerNotOnFieldInfoAdapter extends BaseAdapter {

    private PlayersManager playersManager;
    private Context context;
    private String teamId;

    private static class ViewHolder {
        CircularImageView playerImg;
        TextView playerNumber;
        TextView playerInfo;
    }

    public PlayerNotOnFieldInfoAdapter(Context context, String teamId) {
        super();
        this.context = context;
        this.teamId = teamId;
        playersManager = PlayersManager.getInstance();
    }

    @Override
    public int getCount() {
        return playersManager.playersNotOnFieldForTeam(teamId).size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        final Player player = playersManager.playersNotOnFieldForTeam(teamId).get(position);
        LayoutInflater inflater;
        int layoutResource = R.layout.player_info_list_item_for_team_a;
        if (getCurrentTeamIdentifier(parent).equals(TeamEnum.TEAM_B)) {
            layoutResource = R.layout.player_info_list_item_for_team_b;
        }
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.playerImg = (CircularImageView) convertView.findViewById(R.id.playerImageA);
            viewHolder.playerNumber = (TextView) convertView.findViewById(R.id.playerNumberA);
            viewHolder.playerInfo = (TextView) convertView.findViewById(R.id.playerInfoA);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // --- --- --- --- --- --- ---
        if (getCurrentTeamIdentifier(parent).equals(TeamEnum.TEAM_B)) {
            viewHolder.playerImg = (CircularImageView) convertView.findViewById(R.id.playerImageB);
            viewHolder.playerNumber = (TextView) convertView.findViewById(R.id.playerNumberB);
            viewHolder.playerInfo = (TextView) convertView.findViewById(R.id.playerInfoB);
        }
        // --- --- --- --- --- --- ---
        Glide.with(context).load(player.getImageUrl()).asBitmap().placeholder(R.drawable.player_icon_holder).into(viewHolder.playerImg);
        viewHolder.playerImg.setVisibility(View.VISIBLE);
        viewHolder.playerNumber.setText(player.getShirtNumber());
        viewHolder.playerInfo.setText(player.getName());
        // --- --- --- --- --- --- ---
        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((GameMonitoringActivity) context).resetPlayersListViewItemsBackgroundColor();
                playersManager.updatePlayersStatus(
                        playersManager.playersNotOnFieldForTeam(teamId).get(position), teamId);
                ((GameMonitoringActivity) context).refreshPlayByPlayList();
                ((GameMonitoringActivity) context).enablePlayerInfoView();
                ((GameMonitoringActivity) context).updateListView((ListView) parent);
                ((GameMonitoringActivity) context).setFullTeamListViewVisibility(getCurrentTeamIdentifier(parent), false);
            }
        });
        ((SwipeLayout) convertView).setSwipeEnabled(false);
        return convertView;
    }

    private TeamEnum getCurrentTeamIdentifier(View view) {
        TeamEnum team = TeamEnum.NONE;
        if (view.getId() == R.id.fullTeamListA) {
            team = TeamEnum.TEAM_A;
        }
        if (view.getId() == R.id.fullTeamListB) {
            team = TeamEnum.TEAM_B;
        }
        return team;
    }
}