package loglig.models;

import java.util.ArrayList;

/**
 * Created by is_uptown4 on 11/05/16.
 */
public class StatisticsSet implements Comparable<StatisticsSet> {
    private String setName;
    private ArrayList<StatisticType> set;
    private boolean isSelected;


    public StatisticsSet() {
        this.setName = "";
        this.set = new ArrayList<StatisticType>();
        this.isSelected = false;
    }

    public StatisticsSet(String setName) {
        this();
        this.setName = setName;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }


    public ArrayList<StatisticType> getSet() {
        return set;
    }

    public void setSet(ArrayList<StatisticType> set) {
        this.set = set;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int compareTo(StatisticsSet statisticSet) {
        int compare = this.setName.compareTo(statisticSet.setName);
        return compare;
    }
}