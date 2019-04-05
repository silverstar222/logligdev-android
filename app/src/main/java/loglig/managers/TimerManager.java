package loglig.managers;

import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import loglig.enums.NotificationEnum;
import loglig.notification.LogLigNotification;

/**
 * Created by is_uptown4 on 01/06/16.
 */
public class TimerManager extends Observable {
    public static String timeIsUp = "00:00";
    private static TimerManager instance;
    private List<Observer> observers;
    private long gameTimestamp;
    private CountDownTimer countDownTimer;
    private long remainingTimeInSegment = 0;
    private long countDownStartTime = 0;
    private boolean isTimerOn;
    private int currentTimeSegment = 1;
    private double quarterTime = 10;
    private boolean isPaused = false;

    private TimerManager() {
        this.observers = new ArrayList<Observer>();
        this.isTimerOn = false;
        setTimer(getSegmentDuration() * 1000);
        this.gameTimestamp = getSegmentDuration() * 1000;
    }

    public static TimerManager getInstance() {
        if (instance == null) {
            instance = new TimerManager();
        }
        return instance;
    }

    public void register(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers(LogLigNotification notification) {
        for (Observer observer : observers) {
            observer.update(this, notification);
        }
    }

    public void setSegmentDuration(double quarterTime) {
        this.quarterTime = quarterTime;
        setTimer(getSegmentDuration() * 1000);
    }

    public double getQuarterTime() {
        return quarterTime;
    }

    public int getSegmentDuration() {
        if (isOverTime()) {
            return 5 * 60;
        } else if (isTimeSegment()) {
            return (int) (quarterTime * 60);
        }
        return 0;
    }

    public int currentSegmentSecond() {
        long secRemaining = remainingTimeInSegment / 1000;
        return (int) (getSegmentDuration() - secRemaining);
    }

    private void sendNotification(NotificationEnum timerNotification) {
        LogLigNotification notification = new LogLigNotification();
        notification.setAction(timerNotification);
        notification.setNotificationMessage(convertTimeToString(this.remainingTimeInSegment));
        notifyAllObservers(notification);
    }

    public int getGameTimestamp() {
        return (int) gameTimestamp;
    }

    public String getStringRepresentationOfTimeStamp(long time) {
        return convertTimeToString(time);
    }

    public String getUpdatedStringRepresentationOfTimeStamp() {
        return convertTimeToString(remainingTimeInSegment);
    }

    private void setTimer(long startTime) {
        countDownStartTime = startTime;
        gameTimestamp = startTime;
    }

    public void startTimer() {
        if (remainingTimeInSegment != 0) {
            countDownStartTime = remainingTimeInSegment;
        } else {
            sendNotification(NotificationEnum.SEGMENT_START);
        }

        countDownTimer = new CountDownTimer(countDownStartTime, 1000) {
            public void onTick(long milliSecondsUntilFinished) {
                gameTimestamp = milliSecondsUntilFinished;
                remainingTimeInSegment = milliSecondsUntilFinished;
                sendNotification(NotificationEnum.TIMER_INCREMENT);
                setTimerOn(true);
            }

            public void onFinish() {
                remainingTimeInSegment = 0;
                setTimerOn(false);
                sendNotification(NotificationEnum.TIMER_INCREMENT);
                sendNotification(NotificationEnum.TIMER_UPDATE);
                incrementTimeSegment();
                sendNotification(NotificationEnum.SEGMENT_END);
            }
        }.start();
        isPaused = false;
    }

    public void pauseTimer() {
        if (this.isTimerOn) {
            countDownTimer.cancel();
            setTimerOn(false);
            sendNotification(NotificationEnum.TIMER_UPDATE);
            isPaused = true;
        }
    }

    public void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            setTimerOn(false);
            remainingTimeInSegment = 0;
            sendNotification(NotificationEnum.TIMER_INCREMENT);
            isPaused = false;
        }
    }

    public String convertTimeToString(long counter) {
        long timeInSeconds = counter / 1000;
        String format = "%02d";
        int minutes = (int) (timeInSeconds / 60);
        int seconds = (int) (timeInSeconds % 60);
        String timeString = String.format(format, minutes) + ":" + String.format(format, seconds);
        return timeString;
    }

    public boolean isTimerOn() {
        return isTimerOn;
    }

    private void setTimerOn(boolean timerOn) {
        this.isTimerOn = timerOn;
        sendNotification(NotificationEnum.TIMER_UPDATE);
    }

    public int getCurrentTimeSegment() {
        return currentTimeSegment;
    }

    public void setCurrentTimeSegment(int timeSegment) {
        currentTimeSegment = timeSegment;
    }

    public int getMaximumNumberOfTimeSegments() {
        // TODO: get maximum number of time segments from Game Settings Manger
        // TODO: GameSettingsManger.getInstance().getMaximumNumberOfTimeSegments();
        return 4;
    }

    private void incrementTimeSegment() {
        this.currentTimeSegment++;
        setTimer(getSegmentDuration() * 1000);
    }

    public long getRealTimestamp() {
        long timestamp = System.currentTimeMillis() / 1000;
        return timestamp;
    }

    public boolean isTimeSegment() {
        if (this.currentTimeSegment <= getMaximumNumberOfTimeSegments()) return true;
        else return false;
    }

    public boolean isOverTime() {
        if (this.currentTimeSegment > getMaximumNumberOfTimeSegments()) return true;
        else return false;
    }

    public boolean isLastTimeSegment() {
        if (currentTimeSegment == getMaximumNumberOfTimeSegments()) return true;
        else return false;
    }

    public void processAddNewOverTimeEvent() {
        setTimer(getSegmentDuration() * 1000);
        StatisticsManager.getInstance().createOverTimeStatistic();
        sendNotification(NotificationEnum.ADD_NEW_OVER_TIME_SEGMENT);
    }

    public String getTimeSegmentName() {
        String timeSegmentName = "";
        if (isTimeSegment()) timeSegmentName = "Q" + this.currentTimeSegment;
        if (isOverTime())
            timeSegmentName = "OT" + (this.currentTimeSegment - getMaximumNumberOfTimeSegments());
        return timeSegmentName;
    }

    public String getTimeSegmentName(int timeSegmentNumber) {
        String timeSegmentName = "";
        if (timeSegmentNumber <= getMaximumNumberOfTimeSegments())
            timeSegmentName = "Q" + timeSegmentNumber;
        else timeSegmentName = "OT" + timeSegmentNumber;
        return timeSegmentName;
    }

    public int getMaximumNumberOfTimeOuts() {
        // TODO: getmaximum number of time outs from setting manager
        return 3;
    }

    public int getCurrentQuarter() {
        return currentTimeSegment;
    }

    public void updateTimer(int minute, int seconds) {
        remainingTimeInSegment = (minute * 60 + seconds) * 1000;
        sendNotification(NotificationEnum.TIMER_UPDATE);
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public long getCountDownStartTime() {
        return this.remainingTimeInSegment;
    }
}
