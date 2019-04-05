package loglig.translators;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import loglig.managers.PlayersManager;
import loglig.models.Player;
import loglig.models.Team;

/**
 * Created by is_uptown4 on 08/09/16.
 */
public class TeamTranslator {
    private String TAG_EXCEPTION = "TRANSLATOR";


    public static ArrayList<Team> translateTeamData(JSONArray array) {
        ArrayList<Team> teamList = new ArrayList<Team>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = null;
                object = array.getJSONObject(i);
                Team team = new Team();
                team.setTeamID(object.getString("id"));
                team.setTeamName(object.getString("name"));
                team.setTeamLogoUri(object.getString("teamLogoUrl"));
                teamList.add(team);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG_EXCEPTION", e.getMessage());
            }
        }
        return teamList;
    }

    public static Team translateSingleTeamData(JSONObject object) {
        Team team = new Team();
        try {
            team.setTeamID(object.getString("id"));
            team.setTeamName(object.getString("name"));
            team.setTeamLogoUri(object.getString("teamLogoUrl"));
            team.setPlayersList(PlayersManager.getInstance().allPlayersForTeam(team.getTeamID()));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("TAG_EXCEPTION", e.getMessage());
        }
        return team;
    }

    public static ArrayList<Player> translatePlayerData(String teamId, JSONArray array) {
        ArrayList<Player> playerList = new ArrayList<Player>();
        String playerId = "";
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = null;
                object = array.getJSONObject(i);
                if (teamId.equals(object.getString("teamId"))) {
                    Player player = new Player();
                    player.setId(object.getString("id"));
                    player.setName(object.getString("name"));
                    player.setImageUrl(object.getString("imageUrl"));
                    player.setShirtNumber(Integer.toString(object.getInt("playerNumber")));
                    player.setTeamId(object.getString("teamId"));
                    playerList.add(player);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG_EXCEPTION", e.getMessage());
            }
        }
        return playerList;
    }

}
