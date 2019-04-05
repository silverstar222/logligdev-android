package loglig.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import loglig.interfaces.INodeObject;

/**
 * Created by is_uptown4 on 16/05/16.
 */
public class Game extends RealmObject implements Comparable<Game>, INodeObject {
    @PrimaryKey
    private String id;
    private String gameName;
    private String gameTime;
    private String gameDate;
    private String leagueName;
    @Ignore
    private ArrayList<String> referees;
    @Ignore
    private ArrayList<Team> teams;
    private String homeTeamId;
    private String awayTeamId;
    private String gameLocation;        // stadium
    private String gameClockSettings;   // 10 min per quarter
    private String audienceInfo;
    private int gameScore;
    private String gameStatus;          //Start, In progress, End

    public Game() {
        this.id = "";
        this.gameName = "";
        this.gameTime = "";
        this.gameDate = "";
        this.referees = new ArrayList<String>();
        this.teams = new ArrayList<Team>();
        this.homeTeamId = "";
        this.awayTeamId = "";
        this.gameLocation = "";            // stadium
        this.gameClockSettings = "";
        this.audienceInfo = "";
        this.gameScore = 0;
    }

    public Game(String gameId) {
        this();
        this.id = gameId;
    }

    public Game(String gameId, String gameName, String gameTime, String gameDate) {
        this(gameId);
        this.gameName = gameName;
        this.gameTime = gameTime;
        this.gameDate = gameDate;
    }

    public Game(String gameId, String gameName, String gameTime, String gameDate, String gameLocation) {
        this(gameId, gameName, gameTime, gameDate);
        this.gameLocation = gameLocation;
    }

    public Game(String gameId, String gameName, String gameTime, String gameDate, ArrayList<String> referees, ArrayList<Team> teams, String gameLocation) {
        this(gameId, gameName, gameTime, gameDate);
        this.referees = referees;
        this.teams = teams;
        this.gameLocation = gameLocation;
    }

    public Game(String gameId, String teamAid, String teamBid, String gameTime, String gameDate, String gameLocation) {
        this(gameId);
        this.gameLocation = gameLocation;
        this.gameTime = gameTime;
        this.gameDate = gameDate;
        this.teams = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public ArrayList<String> getReferees() {
        return referees;
    }

    public void setReferees(ArrayList<String> referees) {
        this.referees = referees;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public boolean isContainsTeam(String teamId) {
        for (Team team : this.teams)
            if (team.getTeamID().equals(teamId))
                return true;
        return false;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public void removeTeam(Team team) {
        this.teams.remove(team);
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getGameLocation() {
        return gameLocation;
    }

    public void setGameLocation(String gameLocation) {
        this.gameLocation = gameLocation;
    }

    public String getGameClockSettings() {
        return gameClockSettings;
    }

    public void setGameClockSettings(String gameClockSettings) {
        this.gameClockSettings = gameClockSettings;
    }

    public String getAudienceInfo() {
        return audienceInfo;
    }

    public void setAudienceInfo(String audienceInfo) {
        this.audienceInfo = audienceInfo;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public int getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String toString() {
        String str = "";
        str += "id: " + this.id + "\nname: " + this.gameName + "\ntime: " + this.gameTime + "\ndate:" + this.gameDate + "\n" +
                "referees: " + toStringReferees() + "\nteams: " + toStringTeams() + "\nlocation: " + this.gameLocation + "\n" +
                "game clock settings: " + this.gameClockSettings + "\naudience info: " + this.audienceInfo + "\ngame score:" + this.gameScore;
        return str;
    }

    private String toStringReferees() {
        String str = "";
        if (referees != null || referees.size() > 0) {
            for (String item : referees)
                str += item;
        }
        return str;
    }

    private String toStringTeams() {
        String str = "";
        if (teams != null || teams.size() > 0) {
            for (Team team : teams)
                str += team.toString();
        }
        return str;
    }

    @Override
    public int compareTo(Game game) {
        int compare = this.gameName.compareTo(game.gameName);
        return compare;
    }

    @Override
    public String endpointPath() {
        return "games";
    }

    @Override
    public JSONObject jsonRepresentation() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", getGameName());
            obj.put("date", getGameDate());
            obj.put("location", getGameLocation());
            obj.put("audienceInfo", getAudienceInfo());
            obj.put("homeTeamId", getHomeTeamId());
            obj.put("awayTeamId", getAwayTeamId());
            obj.put("totalGameTime", getGameDate());
            obj.put("leagueId", getLeagueName());
            obj.put("score", getGameScore());
            obj.put("status", getGameStatus());
            obj.put("referees", getReferees());
            obj.put("id", getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}