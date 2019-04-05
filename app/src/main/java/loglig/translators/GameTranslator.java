package loglig.translators;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import loglig.models.Game;

/**
 * Created by is_uptown4 on 08/09/16.
 */
public class GameTranslator {
    private String TAG_EXCEPTION = "TRANSLATOR";


    public static ArrayList<Game> translateGameData(JSONArray array) {
        ArrayList<Game> gamesList = new ArrayList<Game>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = null;
                object = array.getJSONObject(i);
                Game game = new Game();
                game.setId(object.getString("id"));
                game.setLeagueName(object.getString("leagueId"));
                game.setGameStatus(object.getString("status"));
                game.setGameLocation(object.getString("location"));
                game.setGameName(object.getString("name"));
                String timeDate[] = convertJSONdateObjectToString(object.getString("date"));
                game.setGameDate(timeDate[0]);
                game.setGameTime(timeDate[1]);
                game.setHomeTeamId(object.getString("homeTeamId"));
                game.setAwayTeamId(object.getString("awayTeamId"));
                gamesList.add(game);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG_EXCEPTION", e.getMessage());
            }
        }
        return gamesList;
    }

    private static String[] convertJSONdateObjectToString(String _time) {
        String result[] = new String[2];
        String strArray[] = _time.split("T");
        String date[] = strArray[0].split("-");
        result[0] = date[2] + "/" + date[1] + "/" + date[0];
        String time[] = strArray[1].split(":");
        result[1] = time[0] + ":" + time[1];
        return result;
    }
}
