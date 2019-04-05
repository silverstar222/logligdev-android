package loglig.managers;

import android.util.Log;

import org.json.JSONObject;

import loglig.handlers.GenericHandler;
import loglig.handlers.LoginHandler;
import loglig.models.User;
import loglig.services.UserService;
import loglig.translators.UserTranslator;

/**
 * Created by is_uptown4 on 11/05/16.
 */
public class UserManager {

    private static UserManager instance;
    private UserService userService;
    private String userToken;
    private User currentUser;

    private UserManager() {
        userService = UserService.getInstance();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void performUserLogin(String userName, String password, final GenericHandler handler) {
        LoginHandler loginHandler = new LoginHandler() {
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                UserManager.getInstance().setUserToken(UserTranslator.translateToken(response));
                UserManager.getInstance().setCurrentUser(UserTranslator.translateUserData(response));
                handler.onSuccess(statusCode, response);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
                handler.onFailure(statusCode, throwable);
            }

        };

        userService.loginUser(loginHandler, userName, password);
    }

    public User getCurrentUser() {
        if (currentUser == null) currentUser = new User();
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}