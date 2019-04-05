package loglig.models;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import loglig.interfaces.INodeObject;
import loglig.interfaces.RealmTransaction;
import loglig.managers.RealmManager;

/**
 * Created by is_uptown4 on 15/05/16.
 */
public class Player extends RealmObject implements Comparable<Player>, INodeObject {
    @PrimaryKey
    private String id;
    private String name;
    private String shirtNumber;
    private String imgUrl;
    private String foulType;        //Offence / Defense.
    private int foul_counter;
    private int steal_ball_counter;
    private boolean onField;
    private int onFieldPosition;
    private String teamId;

    public Player() {
        this.id = "";
        this.name = "";
        this.shirtNumber = "";
        this.imgUrl = "";
        this.foulType = "";
        this.foul_counter = 0;
        this.steal_ball_counter = 0;
        this.onField = false;
        this.teamId = "0";
    }

    public Player(String id, String name, String number, String ingUrl) {
        this();
        this.id = id;
        this.name = name;
        this.shirtNumber = number;
        this.imgUrl = ingUrl;
        this.onFieldPosition = -1;
    }

    public Player(String id, String name, String shirtNumber, boolean onField, String teamId) {
        this();
        this.id = id;
        this.name = name;
        this.shirtNumber = shirtNumber;
        this.onField = onField;
        this.onFieldPosition = -1;
        this.teamId = teamId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShirtNumber() {
        return shirtNumber;
    }

    public void setShirtNumber(String shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public boolean isOnField() {
        return onField;
    }

    public void setOnField(boolean onField) {
        this.onField = onField;
    }

    public int getOnFieldPosition() {
        return onFieldPosition;
    }

    public void setOnFieldPosition(int onFieldPosition) {
        this.onFieldPosition = onFieldPosition;
    }

    public String getIngUrl() {
        return imgUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imgUrl = imageUrl;
    }

    public String getImageUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPlayerType() {
        return foulType;
    }

    public void setPlayerType(String foulType) {
        this.foulType = foulType;
    }

    public void incrementFoulCounter() {
        this.foul_counter++;
        if (foul_counter >= 4) {
            // change color to red
        } else {
            // change color to green
        }
    }

    public void decrementFoulCounter() {
        this.foul_counter--;
        if (foul_counter >= 4) {
            // change color to red
        } else {
            // change color to ...
        }
    }

    public int getFoulCounter() {
        return this.foul_counter;
    }

    public void incrementStealCount() {
        this.steal_ball_counter++;
    }

    public void decrementStealCount() {
        this.steal_ball_counter--;
    }

    public int getStealBallCounter() {
        return this.steal_ball_counter;
    }

    public String getPlayerInfo() {
        String str = "";
        str += id + ", " + name + ", " + shirtNumber;
        return str;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public void saveOnFieldPosition(final int position)
    {
        final Player player = this;
        RealmManager.getInstance().transactionExecute(new RealmTransaction() {
            @Override
            public void execute() {
                player.setOnField(position > -1);
                player.setOnFieldPosition(position);
            }
        });
    }

    public String toString() {
        String str = "";
        str += this.id + ", " + this.name + ", " + this.shirtNumber + ", " + this.imgUrl + "" +
                ", " + this.foulType + ", " + this.foul_counter + ", " + this.steal_ball_counter + "" +
                ", " + this.onField;
        return str;
    }

    @Override
    public int compareTo(Player anotherPlayer) {
        int res = this.shirtNumber.compareTo(anotherPlayer.getShirtNumber());
        return res;
    }

    @Override
    public String endpointPath() {
        return "Player";
    }

    @Override
    public JSONObject jsonRepresentation() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", getName());
            obj.put("imageUrl", getImageUrl());
            obj.put("teamId", getTeamId());
            obj.put("playerNumber", getShirtNumber());
            obj.put("id", getId());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;
    }


    static public <E extends RealmModel> E managedObjectFromJson(JSONObject jsonObject) {

        final Player player = Player.unManagedObjectFromJson(jsonObject);

        final Player managedObj = RealmManager.getInstance().getPlayer(player.getId());
        if (managedObj != null)
        {
            player.setOnFieldPosition(managedObj.getOnFieldPosition());
            player.setOnField(managedObj.isOnField());
            RealmManager.getInstance().transactionExecute(new RealmTransaction() {
                @Override
                public void execute() {managedObj.deleteFromRealm();}
                }
            );
        }

        return (E) RealmManager.getInstance().managedObject(player);
    }

    static public <E> E unManagedObjectFromJson(JSONObject jsonObject) {
        try {
            Player player = new Player();
            player.setId(jsonObject.getString("id"));
            player.setName(jsonObject.getString("name"));
            player.setImageUrl(jsonObject.getString("imageUrl"));
            player.setShirtNumber(Integer.toString(jsonObject.getInt("playerNumber")));
            player.setTeamId(jsonObject.getString("teamId"));
            player.setOnFieldPosition(-1);
            return (E) player;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}