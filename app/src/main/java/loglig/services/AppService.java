package loglig.services;

import loglig.managers.PlayersManager;
import loglig.managers.StatisticsManager;
import loglig.managers.UserManager;

/**
 * Created by is_uptown4 on 08/09/16.
 */
public class AppService {
    protected static String URL = "http://loglig-lb-1060883969.eu-central-1.elb.amazonaws.com/api/";
    protected static String LOGIN_PATH = "LogligUsers/login";
    protected static String GAME_PATH = "games";
    //protected static String GAME_PATH = "Games/Full";
    protected static String TEAM_PATH = "Teams";
    protected static String PLAYER_PATH = "Players";

    protected static String STATISTIC_TYE_PATH = "StatisticTypes";
    protected static String STATISTIC_PRESET_PATH = "StatisticPreset";
    protected static String STATISTIC_PATH = "Statistics";

    public static String concatUrl(String url, String path) {
        return url + path;
    }

    public static String concatUrlWithToken(String url, String path) {
        return url + path + "?access_token=" + UserManager.getInstance().getUserToken();
    }

    public static String concatUrlWithInclude(String url, String path, boolean useToken, String includeValue) {
        if (useToken)
            return url + path + "?access_token=" + UserManager.getInstance().getUserToken() + (includeValue.length() > 0 ? "&include=" + includeValue : "");
        else return concatUrl(url, path) + "?include=" + includeValue;
    }

    public static void destroyManagers(){
        StatisticsManager.destroyInstance();
        PlayersManager.destroyInstance();
    }
}
