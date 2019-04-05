package loglig.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import loglig.enums.StatisticCategory;
import loglig.interfaces.INodeObject;

public class StatisticType extends RealmObject implements Comparable<StatisticType>, INodeObject {
    @PrimaryKey
    private String id;
    private String name;
    private String abbreviation;
    private boolean selected;
    private String sportBranch;
    private boolean needsFieldLocation;
    private String category;  // This can be TIME, SCORE
    private int association;
    private int scoreValue;
    private String context;


    public StatisticType() {
        this.id = "";
        this.name = "";
        this.abbreviation = "";
        this.selected = false;
        this.sportBranch = "";
        this.needsFieldLocation = false;
        this.scoreValue = 0;
        this.association = StatisticAssociation.Player;
        this.category = StatisticCategory.PlayerStatistic;
    }

    public StatisticType(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public StatisticType(String id, String name, String abbreviation) {
        this(id, name);
        this.abbreviation = abbreviation;
    }

    public StatisticType(String id, String name, String abbreviation, boolean needsFieldLocation) {
        this(id, name, abbreviation);
        this.needsFieldLocation = needsFieldLocation;
    }

    public StatisticType(String id, String name, String abbreviation, boolean needsFieldLocation, int scoreValue) {
        this(id, name, abbreviation, needsFieldLocation);
        this.scoreValue = scoreValue;
    }

    public StatisticType(String id, String name, String abbreviation, boolean needsFieldLocation, String category) {
        this(id, name, abbreviation, needsFieldLocation);
        this.category = category;
    }

    public StatisticType(String id, String name, String abbreviation, boolean needsFieldLocation, String category, int scoreValue) {
        this(id, name, abbreviation, needsFieldLocation, category);
        this.scoreValue = scoreValue;
    }

    public StatisticType(String id, String name, String abbreviation, int association) {
        this(id, name, abbreviation);
        this.association = association;
    }

    public StatisticType(String id, String name, String abbreviation, int association, String category) {
        this(id, name, abbreviation, association);
        this.category = category;
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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSportBranch() {
        return sportBranch;
    }

    public void setSportBranch(String sportBranch) {
        this.sportBranch = sportBranch;
    }

    public boolean isNeedFieldLocation() {
        return needsFieldLocation;
    }

    public void setNeedFieldLocation(boolean needsFieldLocation) {
        this.needsFieldLocation = needsFieldLocation;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String toString() {
        String str = "id: " + this.id + ", name; " + this.name + ", abbreviation: " + this.abbreviation + ", " +
                "selected: " + this.selected + ", sport branch " + sportBranch + ", " +
                "need field location " + this.needsFieldLocation + ", category: " + this.category + ", scoreValue" + this.scoreValue;
        return str;
    }

    @Override
    public int compareTo(StatisticType statistic) {
        int compare = this.name.compareTo(statistic.name);
        return compare;
    }

    public int getAssociation() {
        return association;
    }

    public void setAssociation(int association) {
        this.association = association;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String endpointPath() {
        return "statisticTypes";
    }

    @Override
    public JSONObject jsonRepresentation() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("sportsType", getSportBranch());
            obj.put("abbreviation", abbreviation);
            obj.put("name", getName());
            obj.put("needsFieldLocation", needsFieldLocation);
            obj.put("context", getContext());
            obj.put("id", getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static StatisticType clone(StatisticType statisticType) {
        StatisticType cloneStatisticType = new StatisticType();
        cloneStatisticType.setId(statisticType.getId());
        cloneStatisticType.setName(statisticType.getName());
        cloneStatisticType.setAbbreviation(statisticType.getAbbreviation());
        cloneStatisticType.setSelected(statisticType.isSelected());
        cloneStatisticType.setSportBranch(statisticType.getSportBranch());
        cloneStatisticType.setNeedFieldLocation(statisticType.isNeedFieldLocation());
        cloneStatisticType.setCategory(statisticType.getCategory());
        cloneStatisticType.setAssociation(statisticType.getAssociation());
        cloneStatisticType.setScoreValue(statisticType.getScoreValue());
        cloneStatisticType.setContext(statisticType.getContext());
        return cloneStatisticType;
    }
}