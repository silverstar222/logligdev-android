package loglig.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import loglig.activities.StatisticSelectionActivity;
import loglig.is_uptown4.loglig.R;
import loglig.managers.StatisticsManager;
import loglig.models.StatisticType;
import loglig.models.StatisticsSet;


/**
 * Created by is_uptown4 on 10/05/16.
 */
public class StatisticTypeAdapter extends BaseAdapter {

    private Context context;
    private List<StatisticType> statisticTypeList = Collections.emptyList();

    public StatisticTypeAdapter(Context context) {
        this.context = context;
    }


    public void setStatisticTypeList(List<StatisticType> statisticTypeList) {
        List<StatisticType> internal = new ArrayList<>(statisticTypeList);
        Iterator<StatisticType> iterator = internal.iterator();
        while (iterator.hasNext()) {
            StatisticType type = iterator.next();
            if (type.getAbbreviation().equalsIgnoreCase("onField") || type.getAbbreviation().equalsIgnoreCase("offField")) {
                iterator.remove();
            }
        }
        this.statisticTypeList = internal;
    }

    @Override
    public int getCount() {
        int size = this.statisticTypeList.size();
        return (size > 0) ? size : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String abbreviation = "";
        StatisticType statisticType = this.statisticTypeList.get(position);
        LayoutInflater inflater;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.statistic_type_list_item, parent, false);
        }

        if (statisticType.isValid()) {
            abbreviation = statisticType.getAbbreviation();
        }

        if (abbreviation.equals("TTO")) {
            ((CheckBox) convertView).setText("T");
        } else {
            ((CheckBox) convertView).setText(abbreviation);
        }

        if (isStatisticInCurrentSet(abbreviation)) {
            setViewChecked((CheckBox) convertView, true);
        } else {
            setViewChecked((CheckBox) convertView, false);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    setViewChecked((CheckBox) view, true);
                } else {
                    setViewChecked((CheckBox) view, false);
                }

                if (isAllStatisticsSelected()){
                    ((StatisticSelectionActivity)context).resetDeselectAllStatisticsCheckBox();
                }else {
                    ((StatisticSelectionActivity)context).resetSelectAllStatisticsCheckBox();
                }

                setStatisticTypeSelected(position, ((CheckBox) view).isChecked());
            }
        });
        return convertView;
    }

    private boolean isStatisticInCurrentSet(String abbreviation) {
        boolean status = false;
        StatisticsSet set = StatisticsManager.getInstance().getSelectedSet();
        for (StatisticType item : set.getSet())
            if (item.getAbbreviation().equals(abbreviation) && item.isSelected())
                status = true;
        return status;
    }

    private boolean isAllStatisticsSelected(){
        for (StatisticType statistic : statisticTypeList) {
            if (!statistic.isSelected()) {
                return false;
            }
        }
        return true;
    }

    private void setViewChecked(CheckBox view, boolean isChecked) {
        view.setChecked(isChecked);
        if (isChecked)
            view.setTextColor(ContextCompat.getColor(context, R.color.presetFontSelected));
        else view.setTextColor(ContextCompat.getColor(context, R.color.presetFontNotSelected));
    }

    private void setStatisticTypeSelected(int position, boolean setSelected) {
        StatisticsSet set = StatisticsManager.getInstance().getSelectedSet();
        set.getSet().get(position).setSelected(setSelected);
    }
}