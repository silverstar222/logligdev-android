package loglig.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import loglig.is_uptown4.loglig.R;
import loglig.managers.StatisticsManager;
import loglig.models.StatisticType;
import loglig.models.StatisticsSet;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class StatisticTypePanel extends LinearLayout {

    private LayoutInflater inflater;
    private List<StatisticTypeItem> statisticTypeItems;
    private ViewGroup layout;

    public StatisticTypePanel(Context context) {
        super(context);
        init(context);
    }

    public StatisticTypePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StatisticTypePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public StatisticTypePanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.statistic_type_panel_layout, this, true);
        this.statisticTypeItems = new ArrayList<StatisticTypeItem>();
        getStatisticTypeViews(context);
    }

    private void getStatisticTypeViews(final Context context) {
        StatisticsSet statisticsSet = StatisticsManager.getInstance().getSelectedSet();
        List<StatisticType> statisticTypeList = statisticsSet.getSet();

        layout = (ViewGroup) findViewById(R.id.statisticTypeContainer);
        View childView;
        String str = "";
        String strSubSequence = "stat";
        StatisticType type;
        boolean isSelected = false;

        for (int i = 0; i < layout.getChildCount(); i++) {
            ViewGroup view = (ViewGroup) layout.getChildAt(i);
            for (int j = 0; j < view.getChildCount(); j++) {
                childView = view.getChildAt(j);

                if (childView instanceof StatisticTypeItem) {
                    str = getResources().getResourceName(childView.getId());
                    str = str.substring(str.lastIndexOf(strSubSequence) + strSubSequence.length(), str.length());

                    if (str.contains("Plus")) {
                        str = str.replace("Plus", "+");
                    }
                    ((StatisticTypeItem) childView).setText(str);
                }

                type = getStatisticTypeByAbbreviation(statisticTypeList, str);

                if (type != null) {
                    isSelected = type.isSelected();
                    setStatisticTypeButtonColors(childView, isSelected, type.getAbbreviation());
                }

                if (childView instanceof TeamTimeOutPanel) {
                    str = "TTO";
                    type = getStatisticTypeByAbbreviation(statisticTypeList, str);
                    isSelected = type.isSelected();
                    setStatisticTypeButtonColors(childView, isSelected, type.getAbbreviation());
                    Log.d("ENABLED", "TYPE=" + type.getAbbreviation() + " Enabled=" + isSelected + " Instance=" + childView.getClass().getSimpleName());
                }
            }
        }
    }

    private StatisticType getStatisticTypeByAbbreviation(List<StatisticType> statisticTypeList, String abbreviation) {
        StatisticType statisticType = null;
        for (StatisticType type : statisticTypeList) {
            if (type.getAbbreviation().equals(abbreviation)) {
                statisticType = type;
            }
        }
        return statisticType;
    }

    private void setStatisticTypeButtonColors(View childView, boolean isEnabled, String statisticType) {
        if (childView instanceof StatisticTypeItem) {
            if (isEnabled) {
                if (isPositiveButton(statisticType)) {
                    setPositiveButtonColors(childView);
                } else {
                    setNegativeButtonColors(childView);
                }
            } else {
                setDisabledButtonColors(childView);
            }
            ((StatisticTypeItem) childView).setEnabled(isEnabled);
        }
        if (childView instanceof TeamTimeOutPanel) {
            if (!isEnabled) {
                setDisabledButtonColors(childView);
            }
            ((TeamTimeOutPanel) childView).setEnabled(isEnabled);
        }
    }

    private void setPositiveButtonColors(View childView) {
        ((StatisticTypeItem) childView).setBackground(R.drawable.statistic_type_positive_button);
    }

    private void setNegativeButtonColors(View childView) {
        ((StatisticTypeItem) childView).setBackground(R.drawable.statistic_type_negative_button);
    }

    private void setDisabledButtonColors(View childView) {
        if (childView instanceof TeamTimeOutPanel) {
            ((TeamTimeOutPanel) childView).setBackground(R.drawable.statistic_type_disabled_button);
        }
        if (childView instanceof StatisticTypeItem) {
            ((StatisticTypeItem) childView).setBackground(R.drawable.statistic_type_disabled_button);
        }
    }

    private boolean isPositiveButton(String statisticType) {
        boolean isPositiveButton = false;
        Log.d("statisticType", statisticType);
        switch (statisticType) {
            case "Stl": {
            }
            case "Ast": {
            }
            case "+1": {
            }
            case "+2": {
            }
            case "+3": {
            }
            case "Blk": {
            }
            case "OReb": {
            }
            case "DReb": {
                isPositiveButton = true;
                break;
            }
            case "Foul": {
            }
            case "TecF": {
            }
            case "Miss1": {
            }
            case "Miss2": {
            }
            case "Miss3": {
            }
            case "OFoul": {
            }
            case "TO": {
                isPositiveButton = false;
                break;
            }
        }

        return isPositiveButton;
    }

    private void unhighlightPressedButton() {
        View childView;
        for (int i = 0; i < layout.getChildCount(); i++) {
            ViewGroup viewGroup = (ViewGroup) layout.getChildAt(i);
            for (int j = 0; j < viewGroup.getChildCount(); j++) {
                childView = viewGroup.getChildAt(j);
                if (childView instanceof StatisticTypeItem) {
                    ((StatisticTypeItem) childView).setSelected(false);
                }
            }
        }
    }

    public void highlightPressedButton(final View view) {
        unhighlightPressedButton();
        ((Button) view).setSelected(true);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((Button) view).setSelected(false);
            }
        }, 2000L);
    }
}