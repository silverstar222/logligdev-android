package loglig.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import loglig.adapters.GameSelectionAdapter;
import loglig.enums.TeamEnum;
import loglig.handlers.GameServiceHandler;
import loglig.handlers.PlayerServiceHandler;
import loglig.handlers.TeamServiceHandler;
import loglig.is_uptown4.loglig.R;
import loglig.managers.GameManager;
import loglig.managers.PlayersManager;
import loglig.managers.UserManager;
import loglig.models.Game;

/**
 * Created by is_uptown4 on 10/05/16.
 */
public class GameSelectionActivity extends Activity {

    private Context context;
    private ListView games;
    private GameSelectionAdapter gameAdapter;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);
        this.context = this;
        // --- --- --- --- ---
        this.games = (ListView) findViewById(R.id.gamesListView);
        this.gameAdapter = new GameSelectionAdapter(this.context);
        // --- --- --- --- ---
        final TeamServiceHandler teamServiceHandler = new TeamServiceHandler() {
            @Override
            public void onSuccess(int statusCode, JSONArray response) {
                super.onSuccess(statusCode, response);
                refreshGameListData();
            }

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                super.onSuccess(statusCode, response);
                for (Game game : GameManager.getInstance().getGameList()) {
                    PlayersManager.getInstance().fetchPlayersByGame(game, new PlayerServiceHandler() {
                        @Override
                        public void onSuccess(int statusCode, JSONArray response) {
                            super.onSuccess(statusCode, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, JSONObject response) {
                            super.onSuccess(statusCode, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable throwable) {
                            super.onFailure(statusCode, throwable);
                        }
                    });
                    if (game.getTeams().size() >= 2) refreshGameListData();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
                super.onFailure(statusCode, throwable);
            }
        };
        // --- --- --- --- ---
        final GameServiceHandler gameServiceHandler = new GameServiceHandler() {
            @Override
            public void onSuccess(int statusCode, JSONArray response) {
                for (Game game : GameManager.getInstance().getGameList()) {
                    //PlayersManager.getInstance().fetchTeamByGame(game, teamServiceHandler);
                    PlayersManager.getInstance().fetchTeamById(game.getHomeTeamId(), game, TeamEnum.TEAM_A, teamServiceHandler);
                    PlayersManager.getInstance().fetchTeamById(game.getAwayTeamId(), game, TeamEnum.TEAM_B, teamServiceHandler);
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
            }
        };
        GameManager.getInstance().fetchGameList(UserManager.getInstance().getCurrentUser().getSportType(), gameServiceHandler);
        // --- --- --- --- --- --- ---
    }

    private void refreshGameListData() {
        if (games.getAdapter() == null) games.setAdapter(gameAdapter);
        else gameAdapter.notifyDataSetChanged();
    }

}
