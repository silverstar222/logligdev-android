package loglig.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;

import loglig.enums.TeamEnum;
import loglig.is_uptown4.loglig.R;
import loglig.managers.PlayersManager;
import loglig.managers.RealmManager;
import loglig.models.Player;

/**
 * Created by is_uptown4 on 10/05/16.
 */
public class PlayerSelectionAdapter extends BaseAdapter {

    private PlayersManager playersManager;
    private ListView mContext;
    private String teamId;
    private TeamEnum teamIdentifier;
    private ArrayList<Player> players;

    private SparseBooleanArray playersInitialStates;

    public PlayerSelectionAdapter(ListView context, String teamId) {
        this.mContext = context;
        this.playersManager = PlayersManager.getInstance();
        this.teamId = teamId;
        this.teamIdentifier = playersManager.getIdentifierForTeamId(teamId);
        this.players = PlayersManager.getInstance().allPlayersForTeam(teamId);
        this.playersInitialStates = new SparseBooleanArray();
        mContext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean newState = !playersInitialStates.get(position);
                // updating player's position causes to multiple issues with players state.
//                if (newState) {
//                    RealmManager.getInstance().updatePlayerOnFieldPosition(getItem(position).getId(), newState, position);
//                }else {
//                    RealmManager.getInstance().updatePlayerOnFieldPosition(getItem(position).getId(), newState, -1);
//                }
                playersInitialStates.put(position, newState);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public Player getItem(int position) {
        return players.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        Player player = players.get(position);

        int layoutToInflate = R.layout.player_list_item_a;

        if (teamIdentifier.equals(TeamEnum.TEAM_B))
            layoutToInflate = R.layout.player_list_item_b;

        if (convertView == null) {
            inflater = (LayoutInflater) mContext.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutToInflate, parent, false);
        }

        CheckBox playerInfo = (CheckBox) convertView.findViewById(R.id.playerNameCheckboxA);
        if (teamIdentifier.equals(TeamEnum.TEAM_B)) {
            playerInfo = (CheckBox) convertView.findViewById(R.id.playerNameCheckboxB);
        }
        playerInfo.setText(player.getShirtNumber() + " " + player.getName());
        final int realPosition = position;
        if (playersInitialStates.indexOfKey(realPosition) < 0){
            // updating player's position causes to multiple issues with players state.
//            RealmManager.getInstance().updatePlayerOnFieldPosition(getItem(position).getId(), true, position);
            setViewChecked(playerInfo, true);
            playersInitialStates.put(realPosition, true);
        } else {
            setViewChecked(playerInfo, playersInitialStates.get(realPosition));
        }

        return convertView;
    }

    private void setViewChecked(CheckBox view, boolean isChecked) {
        view.setChecked(isChecked);
        if (isChecked) {
            view.setTextColor(ContextCompat.getColor(mContext.getContext(), R.color.playerFontSelected));
        } else {
            view.setTextColor(ContextCompat.getColor(mContext.getContext(), R.color.playerFontNotSelected));
        }
    }
}