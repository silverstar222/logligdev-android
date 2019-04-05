package loglig.models;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import loglig.enums.TeamEnum;
import loglig.managers.RealmManager;

/**
 * Created by is_uptown4 on 16/05/16.
 */
public class Team implements Comparable<Team> {
    private String teamID;
    private String teamName;
    private Player teamManager;
    private ArrayList<Player> playersList;
    private String teamLogoUri;
    private TeamEnum teamIdentifier;

    public Team() {
        this.teamID = "";
        this.teamName = "";
        RealmManager.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                teamManager = realm.createObject(Player.class);
            }
        });
        this.playersList = new ArrayList<Player>();
        this.teamLogoUri = "";
        this.teamIdentifier = TeamEnum.NONE;
    }

    public Team(String teamID, String teamName) {
        this();
        this.teamID = teamID;
        this.teamName = teamName;
    }

    public Team(String teamID, String teamName, ArrayList<Player> playersList) {
        this();
        this.teamID = teamID;
        this.teamName = teamName;
        this.playersList = playersList;
    }

    public Team(String teamID, String teamName, ArrayList<Player> playersList, String teamLogoUri) {
        this(teamID, teamName, playersList);
        this.teamLogoUri = teamLogoUri;
    }

    public Team(String teamID, String teamName, Player teamManager, HashMap<String, Player> playersList) {
        this(teamID, teamName);
        this.teamManager = teamManager;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Player getTeamManager() {
        return teamManager;
    }

    public void setTeamManager(Player teamManager) {
        this.teamManager = teamManager;
    }

    public ArrayList<Player> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(ArrayList<Player> playersList) {
        this.playersList = playersList;
    }

    public String getTeamLogoUri() {
        if (teamLogoUri != null) {
            return teamLogoUri;
        }else {
            return "";
        }
    }

    public void setTeamLogoUri(String teamLogoUri) {
        this.teamLogoUri = teamLogoUri;
    }

    public TeamEnum getTeamIdentifier() {
        return teamIdentifier;
    }

    public void setTeamIdentifier(TeamEnum teamIdentifier) {
        this.teamIdentifier = teamIdentifier;
    }

    public String toString() {
        String str = "";
        for (Player player : playersList) {
            str = player.toString();
        }
        return str;
    }

    @Override
    public int compareTo(Team team) {
        int compare = this.teamName.compareTo(team.teamName);
        return compare;
    }
}