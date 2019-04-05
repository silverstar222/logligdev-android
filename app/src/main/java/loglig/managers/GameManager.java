package loglig.managers;
import org.json.JSONArray;
import java.util.ArrayList;
import loglig.handlers.GameServiceHandler;
import loglig.handlers.GenericHandler;
import loglig.models.Game;
import loglig.models.Player;
import loglig.models.Team;
import loglig.services.GameService;
import loglig.translators.GameTranslator;

/**
 * Created by is_uptown4 on 01/09/16.
 */
public class GameManager {
    private static GameManager instance;
    private ArrayList<Game> gameList;
    private Game currentGame;

    private GameManager() {
        this.currentGame = new Game();
    }

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    public void fetchGameList(String sportsTypeId, final GenericHandler handler) {
        GameServiceHandler gameServiceHandler = new GameServiceHandler() {
            @Override
            public void onSuccess(int statusCode, JSONArray response) {
                gameList = RealmManager.getInstance().saveGames(GameTranslator.translateGameData(response));

                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
                handler.onFailure(statusCode, throwable);
            }
        };
        GameService.getInstance().fetchGameList(sportsTypeId, gameServiceHandler);
    }

    public ArrayList<Game> getGameList() {
        return this.gameList;
    }

    public int getGameListCount() {
        return this.gameList.size();
    }

    public Game getGameIndex(int index) {
        return this.gameList.get(index);
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public Game gameWithID(String id){
        return RealmManager.getInstance().getGame(id);
    }

    public void deleteAllStatistics(){
        RealmManager.getInstance().deleteAllStatistics();
    }
}
