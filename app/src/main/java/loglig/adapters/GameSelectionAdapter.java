package loglig.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import loglig.activities.PlayersSelectionActivity;
import loglig.is_uptown4.loglig.R;
import loglig.managers.GameManager;
import loglig.managers.PlayersManager;
import loglig.models.Game;
import loglig.models.Team;

/**
 * Created by is_uptown4 on 10/05/16.
 */
public class GameSelectionAdapter extends BaseAdapter {

    private Context context;
    private GameManager gameManager;

    private static class ViewHolder {
        SimpleDraweeView teamALogo;
        SimpleDraweeView teamBLogo;
        TextView teamAName;
        TextView teamBName;
        TextView gameInfo;
    }

    public GameSelectionAdapter(Context context) {
        this.context = context;
        gameManager = GameManager.getInstance();
    }

    @Override
    public int getCount() {
        return gameManager.getGameListCount();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final Game game = gameManager.getGameIndex(position);
        ArrayList<Team> teamList = new ArrayList<>();
        Team teamA = PlayersManager.getInstance().getTeamById(game.getHomeTeamId());
        Team teamB = PlayersManager.getInstance().getTeamById(game.getAwayTeamId());
        teamList.add(teamA);
        teamList.add(teamB);
        game.setTeams(teamList);

        LayoutInflater inflater;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.game_selection_list_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.teamALogo = (SimpleDraweeView) convertView.findViewById(R.id.gameSelectionTeamLogoA);
            viewHolder.teamBLogo = (SimpleDraweeView) convertView.findViewById(R.id.gameSelectionTeamLogoB);
            viewHolder.teamAName = (TextView) convertView.findViewById(R.id.gameSelectionTeamNameA);
            viewHolder.teamBName = (TextView) convertView.findViewById(R.id.gameSelectionTeamNameB);
            viewHolder.gameInfo = (TextView) convertView.findViewById(R.id.gameSelectionGameInfo);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setTeamLogo(viewHolder.teamALogo, teamA.getTeamLogoUri());
        setTeamLogo(viewHolder.teamBLogo, teamB.getTeamLogoUri());
        viewHolder.teamAName.setText(game.getTeams().get(0).getTeamName());
        viewHolder.teamBName.setText(game.getTeams().get(1).getTeamName());
        viewHolder.gameInfo.setText(game.getGameTime() + " " + game.getGameDate() + "\n" + game.getGameLocation());

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gameManager.setCurrentGame(game);
                Intent intent = new Intent(context, PlayersSelectionActivity.class);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private void setTeamLogo(SimpleDraweeView logoImage, String imageUri) {
        Uri uri = Uri.parse(imageUri);
        logoImage.setImageURI(uri);
    }
}