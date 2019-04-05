package loglig.enums;

/**
 * Created by is_uptown4 on 11/07/16.
 */
public enum TeamEnum {
    TEAM_A(0),
    TEAM_B(1),
    NONE(-1);

    private final int value;

    private TeamEnum(int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}