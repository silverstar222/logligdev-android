package loglig.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import loglig.is_uptown4.loglig.R;
import loglig.managers.StatisticsManager;
import loglig.models.PersonalStatistic;
import loglig.models.Player;

/**
 * Created by is_uptown4 on 21/08/16.
 */
public class PlayerPersonalStatisticAdapter extends BaseAdapter {

    private Context context;
    private Player player;
    private ArrayList<PersonalStatistic> personalStatisticsList;

    private static class ViewHolder {
        TextView playerPersonalStatistic;
        TextView playerPersonalStatisticPoints;
    }

    public PlayerPersonalStatisticAdapter(Context context, Player player) {
        this.context = context;
        this.player = player;
        this.personalStatisticsList = StatisticsManager.getInstance().getPlayersPersonalStatisticsList(player);
    }

    @Override
    public int getCount() {
        return personalStatisticsList.size();
    }

    @Override
    public Object getItem(int position) {
        return personalStatisticsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.player_personal_statistic_item_layout, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.playerPersonalStatistic = (TextView) convertView.findViewById(R.id.playerPersonalStatistic);
            viewHolder.playerPersonalStatisticPoints = (TextView) convertView.findViewById(R.id.playerPersonalStatisticPoints);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.playerPersonalStatistic.setText(personalStatisticsList.get(position).getPersonalStatisticAbbreviation());
        viewHolder.playerPersonalStatisticPoints.setText(String.valueOf(((int) personalStatisticsList.get(position).getPersonalStatisticPoints())));

        return convertView;
    }
}
