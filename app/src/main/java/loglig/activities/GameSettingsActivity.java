package loglig.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import loglig.is_uptown4.loglig.R;

/**
 * Created by is_uptown4 on 10/05/16.
 */
public class GameSettingsActivity extends Activity {

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);
        this.context = this;
    }

}