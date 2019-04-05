package loglig.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import loglig.activities.StatisticSelectionActivity;
import loglig.is_uptown4.loglig.R;
import loglig.managers.StatisticsManager;
import loglig.models.StatisticsSet;

/**
 * Created by is_uptown4 on 10/05/16.
 */
public class StatisticPresetAdapter extends BaseAdapter {

    private Context context;

    public StatisticPresetAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return StatisticsManager.getInstance().getStatisticsPresetList().size();
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final StatisticsSet set = StatisticsManager.getInstance().getStatisticsPresetList().get(position);
        LayoutInflater inflater;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.statistic_preset_list_item, parent, false);
        }
        final TextView presetView = (TextView) convertView.findViewById(R.id.presetName);
        presetView.setText(set.getSetName());

        ((StatisticSelectionActivity) context).resetSelectAllStatisticsCheckBox();

        convertView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                deselectAllPresets(parent);
                setPresetBackground(view, true);
                StatisticsManager.getInstance().setSelectedPreset(set);
                ((StatisticSelectionActivity) context).updateIsCheckedStatisticsBySelectedStatisticPreset();
            }
        });

        if (set.isSelected()) {
            setPresetBackground(convertView, true);
        } else setPresetBackground(convertView, false);

        return convertView;
    }

    private void setPresetBackground(View view, boolean selected) {
        if (selected) {
            view.setBackgroundResource(R.drawable.statistic_preset_backgroud_selected);
            ((TextView) ((RelativeLayout) view).getChildAt(1)).setTextColor(ContextCompat.getColor(context, R.color.presetFontSelected));
        } else {
            view.setBackgroundResource(R.drawable.statistic_preset_backgroud_not_selected);
            ((TextView) ((RelativeLayout) view).getChildAt(1)).setTextColor(ContextCompat.getColor(context, R.color.presetFontNotSelected));
        }
    }

    private void deselectAllPresets(View parentView) {
        for (int i = 0; i < ((ListView) parentView).getChildCount(); i++) {
            setPresetBackground(((ListView) parentView).getChildAt(i), false);
        }
    }

}