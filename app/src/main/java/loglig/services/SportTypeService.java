package loglig.services;

import java.util.ArrayList;

import loglig.models.SportType;
import loglig.is_uptown4.loglig.R;

/**
 * Created by is_uptown4 on 02/06/16.
 */
public class SportTypeService {

    private static SportTypeService instance;

    private SportTypeService() {
    }

    public static SportTypeService getSportTypeServiceInstance() {
        if (instance == null) instance = new SportTypeService();
        return instance;
    }

    public static ArrayList<SportType> fetchSportTypes() {
        ArrayList<SportType> sport_type_list = new ArrayList<SportType>();
        sport_type_list.add(new SportType("v11", "Volleyball"));
        sport_type_list.add(new SportType("b22", "Basketball"));
        return sport_type_list;
    }

}