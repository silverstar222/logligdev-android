package loglig.translators;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import loglig.enums.StatisticCategory;
import loglig.models.StatisticAssociation;
import loglig.models.StatisticType;
import loglig.models.StatisticsSet;

/**
 * Created by is_uptown4 on 08/09/16.
 */
public class StatisticTranslator {
    private String TAG_EXCEPTION = "TRANSLATOR";


    public static ArrayList<StatisticType> translateStatisticTypeData(JSONArray array) {
        ArrayList<StatisticType> statisticTypeList = new ArrayList<StatisticType>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = null;
                object = array.getJSONObject(i);
                StatisticType statisticType = new StatisticType();
                statisticType.setId(object.getString("id"));
                String abbreviation = object.getString("abbreviation");
                if (abbreviation.equals("+1")) {
                    statisticType.setScoreValue(1);
                    statisticType.setCategory(StatisticCategory.Score);
                    statisticType.setNeedFieldLocation(false);
                }
                if (abbreviation.equals("+2")) {
                    statisticType.setScoreValue(2);
                    statisticType.setCategory(StatisticCategory.Score);
                    statisticType.setNeedFieldLocation(true);
                }
                if (abbreviation.equals("+3")) {
                    statisticType.setScoreValue(3);
                    statisticType.setCategory(StatisticCategory.Score);
                    statisticType.setNeedFieldLocation(true);
                }
                if (abbreviation.equals("Miss1")) {
                    statisticType.setScoreValue(-1);
                    statisticType.setNeedFieldLocation(true);
                }
                if (abbreviation.equals("Miss2")) {
                    statisticType.setScoreValue(-2);
                    statisticType.setNeedFieldLocation(true);
                }
                if (abbreviation.equals("Miss3")) {
                    statisticType.setScoreValue(-3);
                    statisticType.setNeedFieldLocation(true);
                }
                if (abbreviation.equals("Foul") || abbreviation.equals("Tecf") || abbreviation.equals("OFoul")) {
                    statisticType.setCategory(StatisticCategory.PlayerFoul);
                }
                if (abbreviation.equals("TFoul")) {
                    statisticType.setCategory(StatisticCategory.TeamFoul);
                }
                if (abbreviation.equals("TFoul") || abbreviation.equals("T") || abbreviation.equals("TOT") || abbreviation.equals("OT")) {
                    statisticType.setAssociation(StatisticAssociation.Team);
                }
                if (abbreviation.equals("OT")) {
                    statisticType.setCategory(StatisticCategory.OverTime);
                }
                statisticType.setAbbreviation(abbreviation);
                statisticType.setName(object.getString("name"));
                statisticType.setNeedFieldLocation(object.getBoolean("needsFieldLocation"));
                statisticType.setSportBranch(object.getString("sportsType"));
                // TODO: statisticType.setAssociation(object.getInt("association"));
                // TODO: statisticType.setCategory(object.getInt("category"));
                statisticTypeList.add(statisticType);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG_EXCEPTION", e.getMessage());
            }
        }
        return statisticTypeList;
    }

    public static ArrayList<StatisticsSet> translateStatisticPresetData(JSONArray array) {
        // TODO: the function will be functional after statistic set API will be created.
        ArrayList<StatisticsSet> statisticSetList = new ArrayList<StatisticsSet>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = null;
                object = array.getJSONObject(i);
                StatisticsSet statisticsSet = new StatisticsSet();
                statisticsSet.setSetName(object.getString("name"));
                statisticsSet.setSet(translateStatisticTypeData(object.getJSONArray("set")));
                statisticSetList.add(statisticsSet);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("TAG_EXCEPTION", e.getMessage());
            }
        }
        return statisticSetList;
    }

}
