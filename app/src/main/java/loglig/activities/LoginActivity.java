package loglig.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import loglig.dialogs.LogLigAlertDialog;
import loglig.handlers.LoginHandler;
import loglig.is_uptown4.loglig.R;
import loglig.managers.RealmManager;
import loglig.managers.UserManager;
import loglig.services.AppService;
import loglig.services.InternetStatusService;

/**
 * Created by is_uptown4 on 10/05/16.
 */
public class LoginActivity extends Activity {

    Context context;
    private EditText emailInput, passwordInput;
    private Button loginButton;
    private CheckBox rememberMeCheckbox;

    private static String EMAIL_KEY = "user_email";
    private static String SECRET_KEY = "user_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.context = this;

        this.emailInput = (EditText) findViewById(R.id.emailEditText);
        this.passwordInput = (EditText) findViewById(R.id.passwordEditText);
        this.loginButton = (Button) findViewById(R.id.loginButton);
        this.rememberMeCheckbox = (CheckBox) findViewById(R.id.rememberMeCheckBox);
        // --- --- --- --- ---
        Fresco.initialize(this.context);
        // --- --- --- --- ---
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this.context)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        //RealmManager.getInstance().deleteAllPlayers();
        // --- --- --- --- ---
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
        AppService.destroyManagers();
    }

    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void saveUsersData() {
        if (rememberMeCheckbox.isChecked()) {
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(EMAIL_KEY, emailInput.getText().toString());
            editor.putString(SECRET_KEY, passwordInput.getText().toString());
            editor.commit();
        }
    }

    private void loadUserData() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String savedEmail = sharedPref.getString(EMAIL_KEY, null);
        String savedPassword = sharedPref.getString(SECRET_KEY, null);

        if (savedEmail != null) {
            emailInput.setText(savedEmail);
            emailInput.setSelection(emailInput.getText().length());
        }

        if (savedPassword != null) {
            passwordInput.setText(savedPassword);
        }
    }

    public void login(View view) {
        if (!InternetStatusService.isOnline(this.context)) {
            new LogLigAlertDialog(context, "Internet connection error", "No internet connection.");
        } else {

            LoginHandler loginHandler = new LoginHandler() {
                @Override
                public void onSuccess(int statusCode, JSONObject response) {
                    saveUsersData();
                    Intent intent = new Intent(context, GameSelectionActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFailure(int statusCode, Throwable throwable) {
                    new LogLigAlertDialog(context, "Server Error", "LogIn failed.\nServer error: " + statusCode);
                }
            };

            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();
            if (!isEmailValid(email)) {
                new LogLigAlertDialog(context, "Invalid email", "invalid email.");
                return;
            }

            if (!isPasswordValid(password)) {
                new LogLigAlertDialog(context, "Illegal password", "Password too short.");
                return;
            }

            UserManager.getInstance().performUserLogin(email, password, loginHandler);
            //UserManager.getInstance().performUserLogin("info@loglig.com", "loglig%123", loginHandler);
        }
    }

    public void forgotPassword(View view) {
        // TODO: coll to service restore password
    }
}