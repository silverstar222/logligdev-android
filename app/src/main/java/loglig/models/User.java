package loglig.models;

/**
 * Created by is_uptown4 on 16/05/16.
 */
public class User implements Comparable<User> {

    private String id;
    private String userName;
    private String userPassword;
    private String userLevel;   //admin,
    private String sportType;

    public User() {
        this.id = "";
        this.userName = "";
        this.userPassword = "";
        this.userLevel = "";
        this.sportType = "";
    }

    public User(String id, String userName, String userPassword, String userLevel) {
        this();
        this.id = id;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userLevel = userLevel;
    }

    public User(String id, String userName, String userPassword, String userLevel, String sportType) {
        this(id, userName, userPassword, userLevel);
        this.sportType = sportType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String toString() {
        String str = "";
        str += this.id + ", " + this.userName + ", " + this.userPassword + ", "
                + this.userLevel + ", " + this.sportType;
        return str;
    }

    @Override
    public int compareTo(User user) {
        int compare = this.userName.compareTo(user.userName);
        return compare;
    }
}