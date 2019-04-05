package loglig.managers;

import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import loglig.is_uptown4.loglig.R;

/**
 * Created by is_uptown4 on 16/05/16.
 */
public class GlobalManager {

    public static ArrayList<CheckBox> clickedStatistics = new ArrayList<CheckBox>();
    public static ArrayList<View> presetList = new ArrayList<View>();

    public static void clearStatisticSelections() {
        for (CheckBox ch : clickedStatistics) {
            ch.setChecked(false);
        }
    }

}