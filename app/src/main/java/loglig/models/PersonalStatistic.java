package loglig.models;

/**
 * Created by is_uptown4 on 28/08/16.
 */
public class PersonalStatistic {
    private String personalStatisticAbbreviation;
    private double personalStatisticPoints;

    public PersonalStatistic() {
        this.personalStatisticAbbreviation = "";
        this.personalStatisticPoints = 0.0;
    }

    public PersonalStatistic(String personalStatisticAbbreviation) {
        this();
        this.personalStatisticAbbreviation = personalStatisticAbbreviation;
    }

    public PersonalStatistic(double personalStatisticPoints) {
        this();
        this.personalStatisticPoints = personalStatisticPoints;
    }

    public PersonalStatistic(String personalStatisticAbbreviation, double personalStatisticPoints) {
        this();
        this.personalStatisticAbbreviation = personalStatisticAbbreviation;
        this.personalStatisticPoints = personalStatisticPoints;
    }

    public String getPersonalStatisticAbbreviation() {
        return personalStatisticAbbreviation;
    }

    public void setPersonalStatisticAbbreviation(String personalStatisticAbbreviation) {
        this.personalStatisticAbbreviation = personalStatisticAbbreviation;
    }

    public double getPersonalStatisticPoints() {
        return personalStatisticPoints;
    }

    public void setPersonalStatisticPoints(double personalStatisticPoints) {
        this.personalStatisticPoints = personalStatisticPoints;
    }
}
