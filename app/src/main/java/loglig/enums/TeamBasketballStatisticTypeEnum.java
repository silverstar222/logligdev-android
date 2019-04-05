package loglig.enums;

/**
 * Created by is_uptown4 on 27/07/16.
 */
public enum TeamBasketballStatisticTypeEnum {
    teamTimeOut("TO"),
    teamOverTime("TOT"),
    teamFoul("TFoul");

    private final String teamStatisticType;

    private TeamBasketballStatisticTypeEnum(String _teamStatisticType) {
        teamStatisticType = _teamStatisticType;
    }

    public String toString() {
        return this.teamStatisticType;
    }
}
