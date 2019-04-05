package loglig.managers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import loglig.enums.TeamEnum;
import loglig.handlers.DatabaseChangesListener;
import loglig.handlers.PlayerServiceHandler;
import loglig.handlers.TeamServiceHandler;
import loglig.models.Game;
import loglig.models.Player;
import loglig.models.Team;
import loglig.services.PlayerService;
import loglig.translators.TeamTranslator;

/**
 * Created by is_uptown4 on 11/05/16.
 */
public class PlayersManager {

    private static PlayersManager instance;
    private PlayerService playerService;
    private StatisticsManager statisticsManager;
    private GameManager gameManager;
    private ArrayList<Team> teamList;
    private HashMap<String, ArrayList<Player>> onFieldTeamPlayers = new HashMap<>();

    private PlayersManager() {
        playerService = PlayerService.getInstance();
        statisticsManager = StatisticsManager.getInstance();
        gameManager = GameManager.getInstance();
    }

    public static PlayersManager getInstance() {
        if (instance == null) instance = new PlayersManager();
        return instance;
    }

    public static void destroyInstance() {
        //instance = null;
    }

    private String getTeamIdForIdentifier(int identifier) {
        String teamId = "";
        for (Team team : this.teamList) {
            if (team.getTeamIdentifier().getValue() == identifier)
                teamId = team.getTeamID();
        }
        return teamId;
    }

    public TeamEnum getIdentifierForTeamId(String teamId) {
        return getTeamById(teamId).getTeamIdentifier();
    }

    private void loadOnFieldTeamPlayers() {
        for (Team team : getTeamList()) {
            onFieldTeamPlayers.put(team.getTeamID(), RealmManager.getInstance().onFieldTeamPlayers(team.getTeamID()));
        }
    }

    public void fetchTeamList(final TeamServiceHandler handler) {
        TeamServiceHandler teamServiceHandler = new TeamServiceHandler() {
            @Override
            public void onSuccess(int statusCode, JSONArray response) {
                teamList = TeamTranslator.translateTeamData(response);
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
                handler.onFailure(statusCode, throwable);
            }
        };
        playerService.fetchTeamList(teamServiceHandler);
    }

    public void fetchTeamById(String teamId, final Game game, final TeamEnum TeamIdentifier, final TeamServiceHandler handler) {
        TeamServiceHandler teamServiceHandler = new TeamServiceHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Team team = TeamTranslator.translateSingleTeamData(response);
                team.setTeamIdentifier(TeamIdentifier);
                if (!isTeamInList(team.getTeamID())) {
                    if (teamList == null) teamList = new ArrayList<Team>();
                    teamList.add(team);
                }
                game.addTeam(team);
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onSuccess(int statusCode, JSONArray response) {
                super.onSuccess(statusCode, response);
                teamList = (TeamTranslator.translateTeamData(response));
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
                handler.onFailure(statusCode, throwable);
            }
        };
        PlayerService.getInstance().fetchTeamById(teamId, teamServiceHandler);
    }

    public void fetchTeamByGame(final Game game, final TeamServiceHandler handler) {
        TeamServiceHandler teamServiceHandler = new TeamServiceHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                Team team = TeamTranslator.translateSingleTeamData(response);
                if (!isTeamInList(team.getTeamID())) {
                    if (teamList == null) teamList = new ArrayList<Team>();
                    teamList.add(team);
                }
                if (!game.isContainsTeam(team.getTeamID())) {
                    if (game.getTeams().size() < 1) team.setTeamIdentifier(TeamEnum.TEAM_A);
                    else team.setTeamIdentifier(TeamEnum.TEAM_B);
                    game.addTeam(team);
                    gameManager.getGameList().set(gameManager.getGameList().indexOf(game), game);
                    handler.onSuccess(statusCode, response);
                }
            }

            @Override
            public void onSuccess(int statusCode, JSONArray response) {
                super.onSuccess(statusCode, response);
                teamList = (TeamTranslator.translateTeamData(response));
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
                handler.onFailure(statusCode, throwable);
            }
        };
        playerService.fetchTeamByGame(game, teamServiceHandler);
    }

    public void fetchPlayersByGame(final Game game, final PlayerServiceHandler handler) {
        PlayerService.getInstance().fetchPlayersByGame(game, new PlayerServiceHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONArray response) {
                        super.onSuccess(statusCode, response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Player.managedObjectFromJson(response.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.onSuccess(statusCode, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable) {
                        handler.onFailure(statusCode, throwable);
                    }
                }
        );
    }

    public Player playerWithID(String playerId) {
        return RealmManager.getInstance().getPlayer(playerId);
    }

    public ArrayList<Team> getTeamList() {
        return this.teamList;
    }

    public Team getTeamById(TeamEnum teamId) {
        Team team = null;
        for (Team _team : this.teamList)
            if (_team.getTeamID().equals(String.valueOf(teamId.getValue())))
                team = _team;
        return team;
    }

    public Team getTeamById(String teamId) {
        Team team = null;
        for (Team _team : this.teamList)
            if (_team.getTeamID().equals(teamId))
                team = _team;
        return team;
    }

    private boolean isTeamInList(String teamId) {
        if (this.teamList != null) {
            for (Team team : this.teamList)
                if (team.getTeamID().equals(teamId))
                    return true;
        }
        return false;
    }

    public ArrayList<Player> playersOnFieldForTeam(TeamEnum teamIdentifier) {
        String teamId = getTeamIdForIdentifier(teamIdentifier.getValue());
        if (!this.onFieldTeamPlayers.containsKey(teamId))
            loadOnFieldTeamPlayers();

        ArrayList<Player> list = onFieldTeamPlayers.get(teamId);
        if (list.size() > 0) {
            if (!list.get(0).isValid()) {
                loadOnFieldTeamPlayers();
            }
        }

        return onFieldTeamPlayers.get(teamId);
    }

    public ArrayList<Player> playersNotOnFieldForTeam(String teamId) {
        return RealmManager.getInstance().offFieldTeamPlayers(teamId);
    }

    public ArrayList<Player> allPlayersForTeam(String teamId) {
        return RealmManager.getInstance().allTeamPlayers(teamId);
    }

    public void updatePlayersStatus(Player playerOffField, String teamId) {
        // Get the player for the item currently selected in the player list view.
        TeamEnum teamIdentifier = getIdentifierForTeamId(teamId);
        final Team team = getTeamById(teamId);
        final Player playerOnField = statisticsManager.getCurrentPlayerSelected();
        ArrayList<Player> players = playersOnFieldForTeam(teamIdentifier);
        int playerOnFieldIndex = players.indexOf(playerOnField);

        if (playerOnField.isValid() && playerOnFieldIndex != -1) {
            playerOnField.saveOnFieldPosition(-1);
            playerOffField.saveOnFieldPosition(playerOnFieldIndex);
            statisticsManager.createStatisticOffField(playerOnField);
            playersOnFieldForTeam(teamIdentifier).set(playerOnFieldIndex, playerOffField);
            statisticsManager.createStatisticOnField(playerOffField);
        } else {
            int playerOffFieldIndex = team.getPlayersList().indexOf(playerOffField);
            playerOffField.saveOnFieldPosition(playerOnFieldIndex);
            team.getPlayersList().set(playerOffFieldIndex, playerOffField);
            playersOnFieldForTeam(teamIdentifier).set(playerOnFieldIndex, playerOffField);
            statisticsManager.createStatisticOnField(playerOffField);
        }
    }

    public String getCurrentTeamId(TeamEnum teamIdentifier) {
        String teamId = "0";
        String homeTeamId = gameManager.getCurrentGame().getHomeTeamId();
        String awayTeamId = gameManager.getCurrentGame().getAwayTeamId();
        if (getIdentifierForTeamId(homeTeamId).equals(teamIdentifier))
            teamId = homeTeamId;
        else if (getIdentifierForTeamId(awayTeamId).equals(teamIdentifier))
            teamId = awayTeamId;
        return teamId;
    }

    public void addChangesListener(final DatabaseChangesListener handler) {
        final RealmResults<Player> results = RealmManager.getInstance().getRealm().where(Player.class).findAll();
        results.addChangeListener(new RealmChangeListener<RealmResults<Player>>() {
            @Override
            public void onChange(RealmResults<Player> element) {
                handler.onChange();
            }
        });
    }
}
