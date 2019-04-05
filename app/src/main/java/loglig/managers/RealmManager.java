package loglig.managers;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import loglig.context_provider.ApplicationContextProvider;
import loglig.interfaces.RealmTransaction;
import loglig.models.Game;
import loglig.models.Player;
import loglig.models.Statistic;
import loglig.models.StatisticType;

/**
 * Created by is_uptown4 on 11/05/16.
 */
public class RealmManager extends Observable {

    private static RealmManager instance;
    private Realm realm;

    private RealmManager() {
        initRealm();
    }

    public static RealmManager getInstance() {
        if (instance == null) {
            instance = new RealmManager();
        }
        if (instance.realm.isClosed()) {
            instance = new RealmManager();
        }
        return instance;
    }

    public static void destroyInstance() {
        getInstance().realm.close();
        instance = null;
    }

    private void initRealm() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(ApplicationContextProvider.getContext())
                .deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfig);
        this.realm = Realm.getDefaultInstance();
    }

    public Realm getRealm() {
        return this.realm;
    }

    public void saveStatisticRealmDb(final Statistic statistic) {
        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(statistic);
            }
        });
    }

    public ArrayList<Statistic> getAllStatisticsFromDb() {
        ArrayList<Statistic> list = new ArrayList<Statistic>();
        RealmResults<Statistic> realmResults = realm.where(Statistic.class).findAll().sort("timestamp", Sort.ASCENDING);
        for (Statistic statistic : realmResults) {
            list.add(statistic);
        }
        return list;
    }

    public ArrayList<StatisticType> getAllStatisticsTypesFromDb() {
        ArrayList<StatisticType> list = new ArrayList<>();
        RealmResults<StatisticType> realmResults = realm.where(StatisticType.class).findAll();
        for (StatisticType type : realmResults) {
            list.add(type);
        }
        return list;
    }

    public ArrayList<Statistic> getStatisticsBySyncStatus(String syncStatus) {
        ArrayList<Statistic> list = new ArrayList<>();
        RealmResults<Statistic> realmResults = realm.where(Statistic.class).equalTo("syncStatus", syncStatus).findAll().sort("timestamp", Sort.ASCENDING);
        for (Statistic statistic : realmResults) {
            list.add(statistic);
        }
        return list;
    }

    public ArrayList<Statistic> getStatisticsByTimeSegmentName(String timeSegment) {
        ArrayList<Statistic> list = new ArrayList<Statistic>();
        RealmResults<Statistic> realmResults = realm.where(Statistic.class).equalTo("timeSegmentName", timeSegment).findAll().sort("timestamp", Sort.ASCENDING);
        for (Statistic statistic : realmResults) {
            list.add(statistic);
        }
        return list;
    }

    public ArrayList<Statistic> getStatisticsByGameId(String gameId) {
        ArrayList<Statistic> list = new ArrayList<Statistic>();
        if (!gameId.equals("")) {
            RealmResults<Statistic> realmResults = this.realm.where(Statistic.class).equalTo("gameId", gameId).findAll();
            for (Statistic statistic : realmResults) {
                list.add(statistic);
            }
        }
        return list;
    }

    public void deleteStatisticItemByGameId(String gameId, String statId) {
        realm.beginTransaction();
        if (!TextUtils.isEmpty(gameId)) {
            RealmResults<Statistic> realmResults = this.realm.where(Statistic.class)
                    .equalTo("gameId", gameId)
                    .contains("id", statId)
                    .findAll();
            realmResults.deleteAllFromRealm();
        }
        realm.commitTransaction();
    }

    public void updatePlayerOnFieldPosition(final String playerId, final boolean isOnField, final int onFieldPosition) {
        final Player player = this.realm.where(Player.class).equalTo("id", playerId).findFirst();
        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                player.setOnField(isOnField);
                player.setOnFieldPosition(onFieldPosition);
            }
        });
    }

    public void updateStatisticSyncStatus(final String statisticId, final String syncStatus) {
        final Statistic statistic = this.realm.where(Statistic.class).equalTo("id", statisticId).findFirst();
        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                statistic.setSyncStatus(syncStatus);
            }
        });
    }

    public ArrayList<Player> savePlayers(final ArrayList<Player> players) {
        this.realm.beginTransaction();
        ArrayList<Player> managedList = new ArrayList<>();
        for (Player player : players) {
            player.setOnFieldPosition(-1);
            player.setOnField(false);
            Player savedPlayer = realm.where(Player.class).equalTo("id", player.getId()).findFirst();

            if (savedPlayer != null) {
                if (savedPlayer.getOnFieldPosition() > -1) {
                    player.setOnField(true);
                    player.setOnFieldPosition(savedPlayer.getOnFieldPosition());
                }
                savedPlayer.deleteFromRealm();
            }

            managedList.add(realm.copyToRealm(player));
        }
        this.realm.commitTransaction();
        return managedList;
    }

    public Player getPlayer(String playerID) {
        return realm.where(Player.class).equalTo("id", playerID).findFirst();
    }

    public void transactionExecute(final RealmTransaction transaction) {
        this.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                transaction.execute();
            }
        });
    }

    public ArrayList<StatisticType> saveStatisticTypes(final ArrayList<StatisticType> statisticTypes) {
        this.realm.beginTransaction();
        ArrayList<StatisticType> managedList = new ArrayList<>();
        for (StatisticType statisticType : statisticTypes) {
            StatisticType obj = realm.where(StatisticType.class).equalTo("id", statisticType.getId()).findFirst();
            if (obj != null)
                obj.deleteFromRealm();

            managedList.add(realm.copyToRealm(statisticType));
        }
        this.realm.commitTransaction();
        return managedList;
    }

    public StatisticType getStatisticType(String statisticTypeID) {
        return realm.where(StatisticType.class).equalTo("id", statisticTypeID).findFirst();
    }

    public ArrayList<Game> saveGames(final ArrayList<Game> games) {
        this.realm.beginTransaction();
        ArrayList<Game> managedList = new ArrayList<>();
        for (Game game : games) {
            Game obj = realm.where(Game.class).equalTo("id", game.getId()).findFirst();
            if (obj != null)
                obj.deleteFromRealm();

            managedList.add(realm.copyToRealm(game));
        }
        this.realm.commitTransaction();
        return managedList;
    }

    public Game getGame(String gameId) {
        return realm.where(Game.class).equalTo("id", gameId).findFirst();
    }

    public Player emptyPlayerForSlot(String teamID, int slotNo) {
        String playerId = String.format("Empty%02d", slotNo);
        Player player = realm.where(Player.class).equalTo("id", playerId).findFirst();

        realm.beginTransaction();

        if (player != null) {
            player.setTeamId(teamID);
        } else {
            Player newPlayer = new Player();
            newPlayer.setId(playerId);
            newPlayer.setShirtNumber("Swipe to");
            newPlayer.setName("add player");
            newPlayer.setTeamId(teamID);
            newPlayer.setOnField(true);
            newPlayer.setOnFieldPosition(slotNo);
            player = realm.copyToRealm(newPlayer);
        }
        realm.commitTransaction();
        return player;
    }

    public void deleteAllStatistics() {
        realm.beginTransaction();
        realm.delete(Statistic.class);
        realm.commitTransaction();
    }

    public void deleteAllPlayers() {
        realm.beginTransaction();
        realm.delete(Player.class);
        realm.commitTransaction();
    }

    public ArrayList<Player> onFieldTeamPlayers(String teamId) {
        Log.d("", "onFieldTeamPlayers: " + teamId);
        ArrayList<Player> onFieldPlayers = new ArrayList<>();

        int maxPlayersOnField = 5;
        if (onFieldPlayers.size() < maxPlayersOnField) {
            //int firstIndex = onFieldPlayers.size();
            for (int i = 0; i < maxPlayersOnField; i++) {
                onFieldPlayers.add(emptyPlayerForSlot(teamId, i));
            }
        }

        RealmResults<Player> players = realm.where(Player.class)
                .equalTo("teamId", teamId)
                .greaterThan("onFieldPosition", -1)
                .findAllSorted("onFieldPosition");

        for (Player player : players) {
            if (onFieldPlayers.get(player.getOnFieldPosition()).equals(emptyPlayerForSlot(teamId, player.getOnFieldPosition())))
                onFieldPlayers.set(player.getOnFieldPosition(), player);
        }
        return onFieldPlayers;
    }

    public ArrayList<Player> offFieldTeamPlayers(String teamId) {
        ArrayList<Player> offFieldPlayers = new ArrayList<>();
        RealmResults<Player> players = realm.where(Player.class)
                .equalTo("teamId", teamId)
                .equalTo("onFieldPosition", -1)
                .findAllSorted("shirtNumber");

        for (Player player : players) {
            if (!player.getId().startsWith("Empty"))
                offFieldPlayers.add(player);
        }

        return offFieldPlayers;
    }

    public ArrayList<Player> allTeamPlayers(String teamId) {
        ArrayList<Player> allFieldPlayers = new ArrayList<>();
        RealmResults<Player> players = realm.where(Player.class)
                .equalTo("teamId", teamId)
                .findAllSorted("shirtNumber");

        for (Player player : players) {
            if (!player.getId().startsWith("Empty"))
                allFieldPlayers.add(player);
        }

        return allFieldPlayers;
    }

    public <E extends RealmModel> E managedObject(E object) {
        E managedObj;
        realm.beginTransaction();
        managedObj = realm.copyToRealm(object);
        realm.commitTransaction();
        return managedObj;
    }
}