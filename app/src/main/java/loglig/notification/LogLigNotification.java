package loglig.notification;

import loglig.enums.NotificationEnum;

/**
 * Created by is_uptown4 on 11/07/16.
 */
public class LogLigNotification {
    NotificationEnum action;
    String notificationMessage;

    public LogLigNotification() {
    }

    public LogLigNotification(NotificationEnum action, String notificationMessage) {
        this.action = action;
        this.notificationMessage = notificationMessage;
    }

    public LogLigNotification(NotificationEnum action, String notificationMessage, String team) {
        this.action = action;
        this.notificationMessage = notificationMessage;
    }

    public NotificationEnum getAction() {
        return action;
    }

    public void setAction(NotificationEnum action) {
        this.action = action;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String toString() {
        String str = "";
        str += "\naction: " + action;
        str += "\nnotificationMessage: " + notificationMessage;
        return str;
    }


}
