package loglig.enums;

/**
 * Created by is_uptown4 on 11/07/16.
 */
public enum SyncStatusEnum {
    SYNCED("SYNCED"),
    NOT_SYNCED("NOT_SYNCED"),
    IN_PROGRESS("IN_PROGRESS");

    private final String str;

    private SyncStatusEnum(String newValue) {
        str = newValue;
    }

    public String toString() {
        return str;
    }
}