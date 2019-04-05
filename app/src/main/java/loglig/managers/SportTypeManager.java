package loglig.managers;

import java.util.ArrayList;

import loglig.models.SportType;
import loglig.services.SportTypeService;

/**
 * Created by is_uptown4 on 02/06/16.
 */
public class SportTypeManager {
    private static SportTypeManager instance;
    private static SportTypeService sportTypeService;
    private static ArrayList<SportType> sportTypeList;
    private static SportType currentSportType;

    private SportTypeManager() {
    }

    public static SportTypeManager getInstance() {
        if (instance == null) {
            instance = new SportTypeManager();
            sportTypeService = SportTypeService.getSportTypeServiceInstance();
        }
        return instance;
    }

    public static void fetchSportTypeList() {
        sportTypeList = sportTypeService.fetchSportTypes();
    }

    public static ArrayList<SportType> getSportTypeList() {
        return sportTypeList;
    }

    public static SportType getSportTypeAtIndex(int index) {
        return sportTypeList.get(index);
    }

    public static SportType getCurrentSportType() {
        return currentSportType;
    }

    public static void setCurrentSportType(SportType sport_type) {
        currentSportType = sport_type;
    }

    public static String toStringSportTypeList(String team_id) {
        String str = "";
        for (SportType st : sportTypeList) {
            str = st.toString() + "\n";
        }
        return str;
    }
}