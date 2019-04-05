package loglig.managers;

import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import loglig.enums.NotificationEnum;
import loglig.enums.StatisticCategory;
import loglig.enums.SyncStatusEnum;
import loglig.enums.TeamBasketballStatisticTypeEnum;
import loglig.enums.TeamEnum;
import loglig.handlers.GenericHandler;
import loglig.handlers.StatisticServiceHandler;
import loglig.handlers.StatisticTypeServiceHandler;
import loglig.models.PersonalStatistic;
import loglig.models.Player;
import loglig.models.Point;
import loglig.models.Statistic;
import loglig.models.StatisticAssociation;
import loglig.models.StatisticType;
import loglig.models.StatisticsSet;
import loglig.notification.LogLigNotification;
import loglig.services.StatisticService;
import loglig.translators.StatisticTranslator;

/**
 * Created by is_uptown4 on 11/05/16.
 */
public class StatisticsManager extends Observable implements Observer {

    public static String fullStatisticPresetName = "Full Set";
    private static StatisticsManager instance;
    final Handler handler = new Handler();
    private GameManager gameManager;
    private RealmManager realmManager;
    private TimerManager timerManager;
    private TimerTask syncTimerTask;
    private int syncTime = 5000;
    // --- --- ---
    private ArrayList<StatisticType> statisticTypesList;
    private ArrayList<StatisticType> statisticTypesListForPlayer;
    private ArrayList<StatisticsSet> statisticsPresetList;
    private ArrayList<Statistic> currentStatisticsList;
    private Statistic currentStatistic;
    // --- --- ---
    private String currentPlayerSelected;
    private List<Observer> observers;
    // --- --- ---
    // TODO: the maximum number of timeouts per game need to fetch from GameManager/GlobalManager
    private int maxNumberOfTimeOuts = 3;

    private StatisticsManager() {
        this.observers = new ArrayList<Observer>();
        this.currentStatisticsList = new ArrayList<Statistic>();
        this.statisticsPresetList = new ArrayList<StatisticsSet>();
        gameManager = GameManager.getInstance();
        realmManager = RealmManager.getInstance();
        timerManager = TimerManager.getInstance();
        timerManager.register(this);
        syncTimer();
    }

    public static StatisticsManager getInstance() {
        if (instance == null) {
            instance = new StatisticsManager();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public Player getCurrentPlayerSelected() {
        return PlayersManager.getInstance().playerWithID(currentPlayerSelected);
    }

    public void setCurrentPlayerSelected(Player _currentPlayerSelected) {
        currentPlayerSelected = _currentPlayerSelected.getId();
    }

    public void deleteStatisticPreset() {
        if (!getSelectedSet().getSetName().equals(fullStatisticPresetName))
            this.statisticsPresetList.remove(getSelectedSet());
    }

    public StatisticsSet createNewStatisticPreset(String setName) {
        StatisticsSet newSet = new StatisticsSet(setName);
        statisticsPresetList.add(newSet);
        return newSet;
    }

    public boolean isStatisticPresetNameExist(String setName) {
        boolean status = false;
        setName = setName.toLowerCase();
        for (StatisticsSet set : this.statisticsPresetList) {
            if (set.getSetName().toLowerCase().equals(setName)) status = true;
        }
        return status;
    }

    public void fetchStatisticsPresets(final GenericHandler handler) {
        // TODO: Uncomment code bellow when StatisticPreset API will be ready
        /*StatisticSetHandler statisticSetHandler = new StatisticSetHandler() {
            @Override
            public void onSuccess(int statusCode, JSONArray response) {
                statisticsPresetList = StatisticTranslator.translateStatisticPresetData(response);
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
                handler.onFailure(statusCode, throwable);
            }
        };
        StatisticService.getInstance().fetchStatisticPresetList(statisticSetHandler);*/
        // TODO: Delete code bellow when StatisticPreset API will be ready
        createStatisticPreset();
        handler.onSuccess(200, new JSONArray());

    }

    private void createStatisticPreset() {
        StatisticsSet fullSet = new StatisticsSet(StatisticsManager.fullStatisticPresetName);
        ArrayList<StatisticType> list = new ArrayList<StatisticType>();
        for (StatisticType type : getStatisticTypeList()) {
            if (type.getAssociation() != StatisticAssociation.Team && !type.getCategory().equals(StatisticCategory.OverTime)) {
                StatisticType statisticType = StatisticType.clone(type);
                statisticType.setSelected(true);
                list.add(statisticType);
            }
        }
        fullSet.setSet(list);
        this.statisticsPresetList.add(fullSet);
        // --- --- --- --- --- --- ---
        for (int i = 1; i < 15; i++) {
            StatisticsSet set = new StatisticsSet("Set " + (i + 1));
            set.setSet(createRandomSet());
            this.statisticsPresetList.add(set);
        }
    }

    private ArrayList<StatisticType> createRandomSet() {
        // TODO: Delete the function when StatisticPreset API will be ready
        ArrayList<StatisticType> list = new ArrayList<StatisticType>();
        List<StatisticType> statisticTypeList = new ArrayList<>(getStatisticTypeList());

        for (int i = 0; i < statisticTypeList.size(); i++) {
            StatisticType statisticType = StatisticType.clone(statisticTypeList.get(i));

            if (statisticType.getAssociation() != StatisticAssociation.Team
                    && !statisticType.getCategory().equals(StatisticCategory.OverTime)) {
                statisticType.setSelected(generateRandomBoolean());
                list.add(statisticType);
            }
        }
/*
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
        list.add(getStatisticTypeList().get(generateRandomNumber()));
*/
        return list;
    }

    private int generateRandomNumber() {
        Random random = new Random();
        int number = random.nextInt(15 - 0 + 1) + 0;
        return number;
    }

    private boolean generateRandomBoolean() {
        return new Random().nextBoolean();
    }

    public ArrayList<StatisticsSet> getStatisticsPresetList() {
        return this.statisticsPresetList;
    }

    public void setSelectedPreset(StatisticsSet set) {
        setIsSelectedForAllSets(false);
        statisticsPresetList.get(statisticsPresetList.indexOf(set)).setSelected(true);
    }

    public StatisticsSet findSetByName(String setName) {
        StatisticsSet set = null;
        for (StatisticsSet item : this.statisticsPresetList) {
            if (item.getSetName().equals(setName))
                set = item;
        }
        return set;
    }

    public StatisticsSet getSelectedSet() {
        StatisticsSet set = null;
        for (StatisticsSet item : this.statisticsPresetList) {
            if (item.isSelected())
                set = item;
        }
        if (set == null && !statisticsPresetList.isEmpty()) {
            this.statisticsPresetList.get(0).setSelected(true);
            set = this.statisticsPresetList.get(0);
        }
        return set;
    }

    public void setIsSelectedForAllSets(boolean isSelected) {
        StatisticsSet set = null;
        for (StatisticsSet item : this.statisticsPresetList) {
            item.setSelected(isSelected);
        }
    }

    public void savePresetSelections(ArrayList<String> list) {
        StatisticsSet set = getSelectedSet();
        ArrayList<StatisticType> statisticTypeList = new ArrayList<StatisticType>();
        for (String abbreviation : list) {
            statisticTypeList.add(getStatisticTypeByAbbreviation(abbreviation));
        }
        findSetByName(set.getSetName()).setSet(statisticTypeList);
    }

    public void fetchAllStatisticTypes(final GenericHandler handler) {
        StatisticTypeServiceHandler statisticTypeServiceHandler = new StatisticTypeServiceHandler() {
            @Override
            public void onSuccess(int statusCode, JSONArray response) {
                statisticTypesList = RealmManager.getInstance().saveStatisticTypes(StatisticTranslator.translateStatisticTypeData(response));
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
                handler.onFailure(statusCode, throwable);
            }
        };
        StatisticService.getInstance().fetchStatisticTypes(statisticTypeServiceHandler);
    }

    public ArrayList<StatisticType> getStatisticTypesListForPlayer() {
        if (this.statisticTypesListForPlayer != null
                && this.statisticTypesListForPlayer.size() > 0
                && this.statisticTypesListForPlayer.get(0).isValid()) {
            return this.statisticTypesListForPlayer;
        } else {
            this.statisticTypesListForPlayer = new ArrayList<>();
            for (StatisticType type : getStatisticTypeList()) {
                if (type.getAssociation() == StatisticAssociation.Player)
                    this.statisticTypesListForPlayer.add(type);
            }
            return this.statisticTypesListForPlayer;
        }
    }

    public void addCurrentStatisticLocation(Point location) {
        createNewCurrentStatisticIfNeeded();
        currentStatistic.setLocation(location);
    }

    public void addCurrentStatisticType(StatisticType statisticType) {
        createNewCurrentStatisticIfNeeded();
        currentStatistic.setStatisticTypeId(statisticType.getId());
        currentStatistic.setAbbreviation(statisticType.getAbbreviation());
        currentStatistic.setCategory(statisticType.getCategory());
    }

    public void addCurrentStatisticPlayer(Player player) {
        createNewCurrentStatisticIfNeeded();
        currentStatistic.setPlayerID(player.getId());
        LogLigNotification notification = new LogLigNotification();
        notification.setAction(NotificationEnum.UPDATE_SELECTED_PLAYER);
        notification.setNotificationMessage("");
        notifyAllObservers(notification);
    }

    public void addCurrentStatisticTeamId(String teamId) {
        createNewCurrentStatisticIfNeeded();
        currentStatistic.setTeamId(teamId.toString());
    }

    private void createNewCurrentStatisticIfNeeded() {
        if (currentStatistic == null) {
            currentStatistic = new Statistic();
            currentStatistic.setTimestamp(timerManager.getRealTimestamp());
            currentStatistic.setGameTime(timerManager.getGameTimestamp());
            currentStatistic.setTimeSegmentName(timerManager.getTimeSegmentName());
            currentStatistic.setTimeSegment(timerManager.getCurrentTimeSegment());
            currentStatistic.setGameId(gameManager.getCurrentGame().getId());
            currentStatistic.setSegmentTimeStamp(timerManager.currentSegmentSecond());
        }
    }

    public Statistic getCurrentStatistic() {
        return currentStatistic;
    }

    public void clearCurrentStatistic() {

        currentStatistic = null;
    }

    public boolean isCurrentStatisticReady() {
        boolean status = false;
        if (currentStatistic != null) {

            Player player = RealmManager.getInstance().getPlayer(currentStatistic.getPlayerID());

            if (player != null
                    && currentStatistic.getStatisticType() != null
                    && currentStatistic.getStatisticType().isNeedFieldLocation()
                    && currentStatistic.getLocation() != null) {
                status = true;
            } else if (player != null
                    && currentStatistic.getStatisticType() != null
                    && !currentStatistic.getStatisticType().isNeedFieldLocation()) {
                status = true;
            } else if (currentStatistic.getStatisticType() != null
                    && !currentStatistic.getStatisticType().isNeedFieldLocation()
                    && player != null) {
                status = true;
            } else if (currentStatistic.getStatisticType() != null
                    && currentStatistic.getStatisticType().isNeedFieldLocation()
                    && currentStatistic.getLocation() != null
                    && player != null) {
                status = true;
            } else if (player == null
                    || currentStatistic.getLocation() == null
                    || currentStatistic.getStatisticType() == null) {
                status = false;
            }
        } else status = false;
        return status;
    }

    public ArrayList<Statistic> getCurrentStatisticsList() {
        if (gameManager.getCurrentGame() != null)
            this.currentStatisticsList = realmManager.getStatisticsByGameId(gameManager.getCurrentGame().getId());

        return currentStatisticsList;
    }

    private boolean isStatisticInList(String id) {
        boolean status = false;
        for (Statistic stat : this.currentStatisticsList) {
            if (stat.getId().equals(id))
                status = true;
        }
        return status;
    }

    public int getCurrentStatisticsListSize() {
        return getCurrentStatisticsList().size();
    }

    public Statistic getStatisticsByIndex(int index) {
        return getCurrentStatisticsList().get(index);
    }

    public StatisticType getStatisticTypeByAbbreviation(String abbreviation) {
        StatisticType statisticType = null;
        for (StatisticType type : getStatisticTypeList()) {
            if (type.getAbbreviation().equals(abbreviation)) {
                statisticType = type;
            }
        }
        return statisticType;
    }

    public long calculateScore(String teamId) {
        long score = 0;
        for (Statistic stat : getCurrentStatisticsList()) {
            if (stat.getTeamId().equals(teamId)) {
                if (stat.getCategory().equals(StatisticCategory.Score))
                    score += stat.getStatisticType().getScoreValue();
            }
        }
        return score;
    }

    public long calculateScoreByTimeSegmentNumber(String team, String timeSegmentName) {
        long score = 0;
        for (Statistic stat : getCurrentStatisticsList()) {
            if (stat.getTeamId().equals(team)) {
                if (stat.getCategory().equals(StatisticCategory.Score) && stat.getTimeSegmentName().equals(timeSegmentName))
                    score += stat.getStatisticType().getScoreValue();
            }
        }
        return score;
    }

    public void saveCurrentStatisticToList() {
        if (!currentStatistic.getStatisticType().isNeedFieldLocation())
            currentStatistic.setLocation(new Point());
        String gameId = gameManager.getCurrentGame().getId();
        if (!gameId.equals(""))
            currentStatistic.setId((realmManager.getAllStatisticsFromDb().size() + 1) + "_" + gameId);
        currentStatistic.setReporterId(UserManager.getInstance().getCurrentUser().getId());
        realmManager.saveStatisticRealmDb(currentStatistic);
        updateObserversForStatistic(currentStatistic);
        currentStatistic = null;
    }

    public void removeCurrentStatisticFromList(Statistic statistic) {
        String gameId = statistic.getGameId();
        String statId = statistic.getId();
        realmManager.deleteStatisticItemByGameId(gameId, statId);

        LogLigNotification notification = new LogLigNotification();
        notification.setAction(NotificationEnum.SCORE_UPDATE);
        notification.setNotificationMessage("");
        notifyAllObservers(notification);
    }

    public void updateObserversForStatistic(Statistic statistic) {

        NotificationEnum notificationEnum = null;

        switch (statistic.getCategory()) {
            case StatisticCategory.OverTime:
                notificationEnum = NotificationEnum.OVER_TIME;
                break;

            case StatisticCategory.TeamStatistic:
                notificationEnum = NotificationEnum.TEAM_STATISTIC_UPDATE;
                break;

            case StatisticCategory.PlayerStatistic:
                notificationEnum = NotificationEnum.UPDATE_SELECTED_PLAYER;
                break;

            case StatisticCategory.Time:
                notificationEnum = NotificationEnum.TIMER_UPDATE;
                break;

            case StatisticCategory.Score:
            default:
                notificationEnum = NotificationEnum.SCORE_UPDATE;
                break;
        }

        LogLigNotification notification = new LogLigNotification();
        notification.setAction(notificationEnum);
        notifyAllObservers(notification);
    }

    public void register(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers(LogLigNotification notification) {
        for (Observer observer : observers) {
            observer.update(this, notification);
        }
    }

    public void createTeamFoulStatistic(String team) {
        Statistic statistic = new Statistic();
        StatisticType type = getStatisticTypeByAbbreviation(StatisticCategory.TeamFoul.toString());
        statistic.setAbbreviation(type.getAbbreviation());
        statistic.setStatisticTypeId(type.getId());
        statistic.setTeamId(team);
        statistic.setGameId(gameManager.getCurrentGame().getId());
        statistic.setCategory(StatisticCategory.TeamStatistic);
        statistic.setTimestamp(timerManager.getRealTimestamp());
        statistic.setGameTime(timerManager.getGameTimestamp());
        statistic.setTimeSegmentName(timerManager.getTimeSegmentName());
        statistic.setPlayerID("");
        this.currentStatistic = statistic;
        saveCurrentStatisticToList();
        updateObserversForStatistic(statistic);
    }

    public void createGameOverStatistic(String note) {
        Statistic statistic = new Statistic();
        StatisticType type = getStatisticTypeByAbbreviation(StatisticCategory.OverTime); //TODO : add statistic type for game over
        statistic.setNote(note);
        statistic.setStatisticTypeId(type.getId());
        statistic.setGameId(gameManager.getCurrentGame().getId());
        statistic.setCategory(StatisticCategory.GameOver.toString());
        statistic.setGameTime(timerManager.getGameTimestamp());
        statistic.setTeamId(String.valueOf(TeamEnum.NONE));
        this.currentStatistic = statistic;
        saveCurrentStatisticToList();
        updateObserversForStatistic(statistic);
    }

    public int calculateFoulsByTimeSegmentNumber(String team, String timeSegmentName) {
        int fouls = 0;
        for (Statistic stat : getCurrentStatisticsList()) {
            if (stat.getTeamId().equals(team)) {
                if (stat.getTimeSegmentName().equals(timeSegmentName))
                    if ((stat.getAbbreviation().equals(StatisticCategory.TeamFoul) ||
                            stat.getCategory().equals(StatisticCategory.PlayerFoul)))
                        fouls++;
            }
        }
        return fouls;
    }

    public void createTeamTimeOutStatistic(String team) {
        Statistic statistic = new Statistic();
        StatisticType type = getStatisticTypeByAbbreviation(TeamBasketballStatisticTypeEnum.teamTimeOut.toString());
        statistic.setAbbreviation(type.getAbbreviation());
        statistic.setStatisticTypeId(type.getId());
        statistic.setTeamId(team);
        statistic.setGameId(gameManager.getCurrentGame().getId());
        statistic.setCategory(StatisticCategory.TeamStatistic);
        statistic.setTimestamp(timerManager.getRealTimestamp());
        statistic.setGameTime(timerManager.getGameTimestamp());
        statistic.setTimeSegmentName(timerManager.getTimeSegmentName());
        statistic.setPlayerID("");
        this.currentStatistic = statistic;
        saveCurrentStatisticToList();
        updateObserversForStatistic(statistic);
    }

    public int getTemTimeOutCounter(String team) {
        int timeout = 0;
        for (Statistic stat : getCurrentStatisticsList()) {
            if (stat.getTeamId().equals(team)) {
                if (stat.getAbbreviation().equals("TO")) timeout++;
            }
        }
        return timeout;
    }

    public int getMaxNumberOfTimeOuts() {
        return maxNumberOfTimeOuts;
    }

    public void createOverTimeStatistic() {
        Statistic statistic = new Statistic();
        StatisticType type = getStatisticTypeByAbbreviation(StatisticCategory.OverTime);
        statistic.setStatisticTypeId(type.getId());
        statistic.setAbbreviation(type.getAbbreviation());
        statistic.setGameId(gameManager.getCurrentGame().getId());
        statistic.setCategory(StatisticCategory.OverTime.toString());
        statistic.setGameTime(timerManager.getGameTimestamp());
        statistic.setTeamId(String.valueOf(TeamEnum.NONE));
        this.currentStatistic = statistic;
        saveCurrentStatisticToList();
        updateObserversForStatistic(statistic);
    }

    public void clearCurrentStatisticsList() {
        if (currentStatisticsList != null)
            currentStatisticsList.clear();
    }

    public ArrayList<StatisticType> getStatisticTypeList() {

        if (this.statisticTypesList != null
                && this.statisticTypesList.size() > 0
                && this.statisticTypesList.get(0).isValid()) {
            return this.statisticTypesList;
        } else {
            this.statisticTypesList = RealmManager.getInstance().getAllStatisticsTypesFromDb();
            return this.statisticTypesList;
        }
        /*if (this.statisticTypesList == null) {
            this.statisticTypesList = RealmManager.getInstance().getAllStatisticsTypesFromDb();
        }

        if (this.statisticTypesList.size() > 0) {
            if (!this.statisticTypesList.get(0).isValid()) {
                this.statisticTypesList = RealmManager.getInstance().getAllStatisticsTypesFromDb();
            }
        }
        return this.statisticTypesList;*/
    }

    public ArrayList<PersonalStatistic> getPlayersPersonalStatisticsList(Player player) {
        ArrayList<PersonalStatistic> personalStatisticsList = new ArrayList<PersonalStatistic>();
        personalStatisticsList.add(new PersonalStatistic("PTS", calculatePersonalStatisticPTS(player.getId())));
        personalStatisticsList.add(new PersonalStatistic("FOUL", calculatePersonalStatisticFOUL(player.getId())));
        personalStatisticsList.add(new PersonalStatistic("+/-", calculatePersonalStatisticDifferenceInResults(player.getId())));
        personalStatisticsList.add(new PersonalStatistic("REB", calculatePersonalStatisticREB(player.getId())));
        personalStatisticsList.add(new PersonalStatistic("BLK", calculatePersonalStatisticBLK(player.getId())));
        personalStatisticsList.add(new PersonalStatistic("MIN", calculatePersonalStatisticMIN(player.getId())));
        personalStatisticsList.add(new PersonalStatistic("AST", calculatePersonalStatisticAST(player.getId())));
        personalStatisticsList.add(new PersonalStatistic("TO", calculatePersonalStatisticTO(player.getId())));
        personalStatisticsList.add(new PersonalStatistic("FG%", calculatePersonalStatisticFGpercentage(player)));
        personalStatisticsList.add(new PersonalStatistic("EFF", calculatePersonalStatisticEFF(player)));
        personalStatisticsList.add(new PersonalStatistic("STL", calculatePersonalStatisticSTL(player.getId())));
        personalStatisticsList.add(new PersonalStatistic("FT%", calculatePersonalStatisticFTpercentage(player.getId())));
        return personalStatisticsList;
    }

    private int calculatePersonalStatisticPTS(String playerId) {
        //COMMENT: players points calculation (PTS)
        int points = 0;
        for (Statistic statistic : getCurrentStatisticsList()) {
            String abbreviation = statistic.getAbbreviation();
            if (statistic.getPlayerID().equals(playerId)
                    && (abbreviation.equals("+1") || abbreviation.equals("+2") || abbreviation.equals("+3")))
                points += statistic.getStatisticType().getScoreValue();
        }
        return points;
    }

    private double calculatePersonalStatisticFOUL(String playerId) {
        // COMMENT: FOUL - Total fouls for player
        double fouls = 0;
        for (Statistic statistic : getCurrentStatisticsList()) {
            String abbreviation = statistic.getAbbreviation();
            if (statistic.getPlayerID().equals(playerId) &&
                    (abbreviation.equals("Foul")
                            || abbreviation.equals("OFoul")
                            || abbreviation.equals("TFoul")
                            || abbreviation.equals("Tecf")))
                fouls++;
        }
        return fouls;
    }

    private double calculatePersonalStatisticDifferenceInResults(String playerId) {
        // COMMENT: +/- Difference in results that was made by team during player was on field.
        // TODO: see the comment in LL-219
        double difference = 0;
        return difference;
    }

    private double calculatePersonalStatisticREB(String playerId) {
        // COMMENT: REB - Total Rebounds for player
        double rebounds = 0;
        for (Statistic statistic : getCurrentStatisticsList()) {
            String abbreviation = statistic.getAbbreviation();
            if (statistic.getPlayerID().equals(playerId) &&
                    (abbreviation.equals("OReb") || abbreviation.equals("DReb")))
                rebounds++;
        }
        return rebounds;
    }

    private double calculatePersonalStatisticBLK(String playerId) {
        // COMMENT: BLK - Total Blocks for player
        double blocks = 0;
        for (Statistic statistic : getCurrentStatisticsList()) {
            if (statistic.getPlayerID().equals(playerId) && statistic.getAbbreviation().equals("Blk"))
                blocks++;
        }
        return blocks;
    }

    private double calculatePersonalStatisticMIN(String playerId) {
        // COMMENT: MIN - Total amount minutes that player participated in the game.
        double minutes = 0;
        double minutesOnField = 0;
        double minutesOffField = 0;
        ArrayList<Statistic> list = new ArrayList<Statistic>();
        int numberOfTimeSegments = timerManager.getCurrentTimeSegment();

        for (int i = 0; i < numberOfTimeSegments; i++) {
            list.addAll(realmManager.getStatisticsByTimeSegmentName(timerManager.getTimeSegmentName(i)));
        }
        String abbreviation = "";
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPlayerID().equals(playerId)) {
                abbreviation = list.get(i).getAbbreviation();
                if (abbreviation.equals("onField")) {
                    minutesOnField = list.get(i).getGameTime();
                }
                if (abbreviation.equals("offField")) {
                    minutesOffField = list.get(i).getGameTime();
                }
            }
            minutes += Math.abs(minutesOffField - minutesOnField);
        }
        return minutes;
    }

    private double calculatePersonalStatisticAST(String playerId) {
        // COMMENT: AST - Total assists for player
        double assists = 0;
        for (Statistic statistic : getCurrentStatisticsList()) {
            if (statistic.getPlayerID().equals(playerId) && statistic.getAbbreviation().equals("Ast"))
                assists++;
        }
        return assists;
    }

    private double calculatePersonalStatisticTO(String playerId) {
        // COMMENT: To - Total TimeOuts for player
        double to = 0;
        for (Statistic statistic : getCurrentStatisticsList()) {
            if (statistic.getPlayerID().equals(playerId) && statistic.getAbbreviation().equals("TO"))
                to++;
        }
        return to;
    }

    private double calculatePersonalStatisticFGpercentage(Player player) {
        // COMMENT: FG% - TPlayer's Shooting percentage (shoots from 2 and 3 are counted too).
        double fgPercentage = (calculatePersonalStatisticFGM(player.getId())
                / calculatePersonalStatisticFGA(player.getId())) * 100;
        if (Double.isNaN(fgPercentage)) {
            fgPercentage = 0;
        }
        return fgPercentage;
    }

    private double calculatePersonalStatisticEFF(Player player) {
        //COMMENT:  EFF - Efficiency Index: EFF = (PTS + REB + AST + STL + BLK − ((FGA − FGM) + (FTA − FTM) + TO)).
        double efficiencyIndex = 0;
        // TODO: Question is +1 counted in PTS, in score calculation it is not counted.
        String playerId = player.getId();
        efficiencyIndex
                = calculatePersonalStatisticPTS(playerId)
                + calculatePersonalStatisticREB(playerId)
                + calculatePersonalStatisticAST(playerId)
                + calculatePersonalStatisticSTL(playerId)
                + calculatePersonalStatisticBLK(playerId)
                - (calculatePersonalStatisticFGA(playerId) - calculatePersonalStatisticFGM(playerId)
                + calculatePersonalStatisticFTA(playerId) - calculatePersonalStatisticFTM(playerId)
                + calculatePersonalStatisticTO(playerId));
        return efficiencyIndex;
    }

    private double calculatePersonalStatisticSTL(String playerId) {
        // COMMENT: STL - Total steals for player
        double steals = 0;
        for (Statistic statistic : getCurrentStatisticsList()) {
            if (statistic.getPlayerID().equals(playerId) && statistic.getAbbreviation().equals("Stl"))
                steals++;
        }
        return steals;
    }

    private double calculatePersonalStatisticFTpercentage(String playerId) {
        // COMMENT: FT% - Free throw percentage.
        double ftCounter = 0;
        return calculatePersonalStatisticFTM(playerId) / calculatePersonalStatisticFTA(playerId) * 100;
    }

    private double calculatePersonalStatisticFGA(String playerId) {
        // COMMENT: FGA - Field goal attempt
        double fgaCounter = 0;
        for (Statistic statistic : getCurrentStatisticsList()) {
            String abbreviation = statistic.getAbbreviation();
            if (statistic.getPlayerID().equals(playerId) &&
                    (abbreviation.equals("+2") || abbreviation.equals("+3")) ||
                    abbreviation.equals("Miss2") || abbreviation.equals("Miss3")) {
                fgaCounter++;
            }
        }
        return fgaCounter;
    }

    private double calculatePersonalStatisticFGM(String playerId) {
        // COMMENT: FGM - Field goal made
        double fgmCounter = 0;
        for (Statistic statistic : getCurrentStatisticsList()) {
            String abbreviation = statistic.getAbbreviation();
            if (statistic.getPlayerID().equals(playerId) &&
                    (abbreviation.equals("+2") || abbreviation.equals("+3"))) {
                fgmCounter++;
            }
        }
        return fgmCounter;
    }

    private double calculatePersonalStatisticFTA(String playerId) {
        // COMMENT: FTA - Free throw attempt
        double ftaCounter = 0;
        for (Statistic statistic : getCurrentStatisticsList()) {
            String abbreviation = statistic.getAbbreviation();
            if (statistic.getPlayerID().equals(playerId) &&
                    (abbreviation.equals("+1") || abbreviation.equals("Miss1"))) {
                ftaCounter++;
            }
        }
        return ftaCounter;
    }

    private double calculatePersonalStatisticFTM(String playerId) {
        // COMMENT: FTM - Free throw made
        double ftmCounter = 0;
        for (Statistic statistic : getCurrentStatisticsList()) {
            if (statistic.getPlayerID().equals(playerId) && statistic.getAbbreviation().equals("+1")) {
                ftmCounter++;
            }
        }
        return ftmCounter;
    }

    public void createStatisticOnField(Player player) {
        String playerId = player.getId().toLowerCase();
        if (!playerId.contains("empty")) {
            Statistic onFieldStatistic = new Statistic();
            onFieldStatistic.setTimestamp(timerManager.getRealTimestamp());
            onFieldStatistic.setGameTime(timerManager.getGameTimestamp());
            onFieldStatistic.setTimeSegmentName(timerManager.getTimeSegmentName());
            onFieldStatistic.setGameId(gameManager.getCurrentGame().getId());
            onFieldStatistic.setPlayerID(player.getId());
            onFieldStatistic.setTeamId(player.getTeamId());
            onFieldStatistic.setCategory(StatisticCategory.PlayerStatistic);
            onFieldStatistic.setStatisticTypeId(getStatisticTypeByAbbreviation("onField").getId());
            onFieldStatistic.setAbbreviation("onField");
            onFieldStatistic.setLocation(new Point());
            currentStatistic = onFieldStatistic;
            saveCurrentStatisticToList();
            updateObserversForStatistic(onFieldStatistic);
        }
    }

    public void createStatisticOffField(Player player) {
        String playerId = player.getId().toLowerCase();
        if (!playerId.contains("empty")) {
            Log.d("PLAYER", "ID=" + player.getId());
            Statistic offFieldStatistic = new Statistic();
            offFieldStatistic.setTimestamp(timerManager.getRealTimestamp());
            offFieldStatistic.setGameTime(timerManager.getGameTimestamp());
            offFieldStatistic.setTimeSegmentName(timerManager.getTimeSegmentName());
            offFieldStatistic.setGameId(gameManager.getCurrentGame().getId());
            offFieldStatistic.setPlayerID(player.getId());
            offFieldStatistic.setTeamId(player.getTeamId());
            offFieldStatistic.setCategory(StatisticCategory.PlayerStatistic);
            offFieldStatistic.setStatisticTypeId(getStatisticTypeByAbbreviation("offField").getId());
            offFieldStatistic.setAbbreviation("offField");
            currentStatistic = offFieldStatistic;
            saveCurrentStatisticToList();
            updateObserversForStatistic(offFieldStatistic);
        }
    }

    private void createStatisticOnFieldForAllPlayersOnFieldOnNewTimeSegment(TeamEnum teamIdentifier) {
        for (Player player : PlayersManager.getInstance().playersOnFieldForTeam(teamIdentifier)) {
            if (player.isOnField())
                createStatisticOnField(player);
        }
    }

    private void createStatisticOffFieldForAllPlayersOnFieldOnNewTimeSegment(TeamEnum teamIdentifier) {
        for (Player player : PlayersManager.getInstance().playersOnFieldForTeam(teamIdentifier)) {
            if (player.isOnField())
                createStatisticOffField(player);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if ((((LogLigNotification) data).getAction().equals(NotificationEnum.SEGMENT_END))) {
            createStatisticOffFieldForAllPlayersOnFieldOnNewTimeSegment(TeamEnum.TEAM_A);
            createStatisticOffFieldForAllPlayersOnFieldOnNewTimeSegment(TeamEnum.TEAM_B);
        }
        if ((((LogLigNotification) data).getAction().equals(NotificationEnum.SEGMENT_START))) {
            createStatisticOnFieldForAllPlayersOnFieldOnNewTimeSegment(TeamEnum.TEAM_A);
            createStatisticOnFieldForAllPlayersOnFieldOnNewTimeSegment(TeamEnum.TEAM_B);
        }
    }

    private void syncTimer() {
        Timer syncTimer = new Timer();
        initializeSyncTimerTask();
        syncTimer.schedule(syncTimerTask, 0, syncTime);


    }

    private void initializeSyncTimerTask() {
        syncTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        synchronizeStatisticsList();
                    }
                });
            }
        };
    }

    private void synchronizeStatisticsList() {
        ArrayList<Statistic> list = realmManager.getStatisticsBySyncStatus(SyncStatusEnum.NOT_SYNCED.toString());
        for (int index = 0; index < list.size(); index++) {
            final Statistic statistic = list.get(index);
            realmManager.updateStatisticSyncStatus(statistic.getId(), SyncStatusEnum.IN_PROGRESS.toString());
            StatisticServiceHandler handler = new StatisticServiceHandler() {
                @Override
                public void onSuccess(int statusCode, JSONObject response) {
                    realmManager.updateStatisticSyncStatus(statistic.getId(), SyncStatusEnum.SYNCED.toString());
                }

                @Override
                public void onFailure(int statusCode, Throwable throwable) {
                    realmManager.updateStatisticSyncStatus(statistic.getId(), SyncStatusEnum.NOT_SYNCED.toString());
                }
            };
            sendSingleStatisticsToServer(statistic, handler);
        }
    }

    private void sendSingleStatisticsToServer(final Statistic statistic, final StatisticServiceHandler handler) {
        StatisticServiceHandler statisticServiceHandler = new StatisticServiceHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
                handler.onFailure(statusCode, throwable);
            }
        };
        StatisticService.getInstance().sendStatisticsToServer(statistic, statisticServiceHandler);
    }

    public StatisticType getStatisticType(String id) {
        return RealmManager.getInstance().getStatisticType(id);
    }
}