package loglig.models;

import org.json.JSONException;
import org.json.JSONObject;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import loglig.enums.SyncStatusEnum;
import loglig.interfaces.INodeObject;
import loglig.managers.GameManager;
import loglig.managers.PlayersManager;
import loglig.managers.StatisticsManager;

/**
 * Created by is_uptown4 on 11/05/16.
 */
public class Statistic extends RealmObject implements Comparable<Statistic>, INodeObject {

    @PrimaryKey
    private String id;
    private String statisticTypeId;
    private String playerID;
    private String teamId;
    private String gameId;
    private Point location;
    private String abbreviation;
    private long gameTime;
    private long timestamp;
    private String category;
    private String timeSegmentName;
    private int timeSegment;
    private String syncStatus;
    private String reporterId;
    private int segmentTimeStamp;
    private String note;

    public Statistic() {
        this.id = "";
        this.statisticTypeId = null;
        this.playerID = "";
        this.gameId = "";
        this.location = null;
        this.abbreviation = "";
        this.gameTime = 0;
        this.timestamp = 0;
        this.category = "";
        this.timeSegmentName = "";
        this.timeSegment = 0;
        this.syncStatus = SyncStatusEnum.NOT_SYNCED.toString();
        this.reporterId = "";
        this.note = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StatisticType getStatisticType() {
        return StatisticsManager.getInstance().getStatisticType(getStatisticTypeId());
    }

    public String getStatisticTypeId() {
        return statisticTypeId;
    }

    public void setStatisticTypeId(String statisticTypeId) {
        this.statisticTypeId = statisticTypeId;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public long getGameTime() {
        return gameTime;
    }

    public void setGameTime(long gameTime) {
        this.gameTime = gameTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTimeSegmentName() {
        return timeSegmentName;
    }

    public void setTimeSegmentName(String timeSegmentName) {
        this.timeSegmentName = timeSegmentName;
    }

    public int getTimeSegment() {
        return timeSegment;
    }

    public void setTimeSegment(int timeSegment) {
        this.timeSegment = timeSegment;
    }

    public int getSegmentTimeStamp() {
        return segmentTimeStamp;
    }

    public void setSegmentTimeStamp(int segmentTimeStemp) {
        this.segmentTimeStamp = segmentTimeStemp;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int compareTo(Statistic statistic) {
        Long original = statistic.gameTime;
        Long local = this.gameTime;
        int compare = (int) local.compareTo(original);
        return compare;
    }

    @Override
    public String endpointPath() {
        return "statistics";
    }

    @Override
    public JSONObject jsonRepresentation() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", getAbbreviation());
            obj.put("timeSegment", getTimeSegment());
            if (getPlayerID().length() > 0)
                obj.put("player", PlayersManager.getInstance().playerWithID(getPlayerID()).jsonRepresentation());
            obj.put("playerId", "123"); //TEMP
            obj.put("timeStamp", getTimestamp());
            obj.put("gameId", getGameId());
            obj.put("game", GameManager.getInstance().gameWithID(getGameId()).jsonRepresentation());
            obj.put("locationOnField", getLocation().toString());
            obj.put("type", getStatisticType().jsonRepresentation());
            obj.put("segmentTimeStamp", getSegmentTimeStamp());
            if (getNote().length() > 0)
                obj.put("note", getNote());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}