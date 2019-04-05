package loglig.adapters;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import loglig.enums.StatisticCategory;
import loglig.enums.TeamEnum;
import loglig.is_uptown4.loglig.R;
import loglig.managers.PlayersManager;
import loglig.managers.RealmManager;
import loglig.managers.StatisticsManager;
import loglig.managers.TimerManager;
import loglig.models.Statistic;

/**
 * Created by is_uptown4 on 10/05/16.
 */
public class PlayByPlayListAdapter extends BaseAdapter {

    private StatisticsManager statisticManager;
    private PlayersManager playersManager;
    private Context context;
    private ArrayList<Statistic> statistics;

    private static class ViewHolder {
        TextView playerNumber;
        TextView statisticType;
        ImageView statisticTypeIcon;
        TextView playByPlaystatTimeSegment;
        TextView playByPlayStatTimeStamp;
        ImageView deleteStatItem;
    }

    public PlayByPlayListAdapter(Context context) {
        super();
        this.context = context;
        statisticManager = StatisticsManager.getInstance();
        playersManager = PlayersManager.getInstance();
        statistics = statisticManager.getCurrentStatisticsList();
        Collections.reverse(statistics);
    }

    @Override
    public int getCount() {
        return statistics.size();
    }

    @Override
    public Object getItem(int position) {
        return statistics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater inflater;
        final Statistic currentStatistic = statistics.get(position);
        String abbreviation = currentStatistic.getAbbreviation();

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.play_by_play_list_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.playerNumber = (TextView) convertView.findViewById(R.id.currGameStatisticPlayerNumber);
            viewHolder.statisticType = (TextView) convertView.findViewById(R.id.currGameStatType);
            viewHolder.statisticTypeIcon = (ImageView) convertView.findViewById(R.id.currentGameStatTypeIcon);
            viewHolder.playByPlaystatTimeSegment = (TextView) convertView.findViewById(R.id.playByPlayCurrentStatTimeSegmentText);
            viewHolder.playByPlayStatTimeStamp = (TextView) convertView.findViewById(R.id.playByPlayCurrentStatTimeStampText);
            viewHolder.deleteStatItem = (ImageView) convertView.findViewById(R.id.deletePlayByPlayStatItem);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (currentStatistic.getCategory().equals(StatisticCategory.OverTime)) {
            viewHolder.playerNumber.setText("Over-Time");
        } else if (currentStatistic.getCategory().equals(StatisticCategory.GameOver)) {
            viewHolder.playerNumber.setText("Game-Over");
        } else if (currentStatistic.getCategory().equals(StatisticCategory.TeamStatistic)) {
            viewHolder.playerNumber.setText(playersManager.getTeamById(currentStatistic.getTeamId()).getTeamName());
        } else {
            viewHolder.playerNumber.setText(RealmManager.getInstance().getPlayer(currentStatistic.getPlayerID()).getShirtNumber());
        }

        String category = currentStatistic.getCategory();
        String teamId = currentStatistic.getTeamId();
        if (category.equals(StatisticCategory.PlayerStatistic)
                || category.equals(StatisticCategory.Score)
                || category.equals(StatisticCategory.TeamStatistic)
                || category.equals(StatisticCategory.PlayerFoul)
                || category.equals(StatisticCategory.TeamFoul)) {
            if (TeamEnum.TEAM_A.equals(playersManager.getIdentifierForTeamId(teamId))) {
                viewHolder.playerNumber.setTextColor(ContextCompat.getColor(context, R.color.teamACenterColor));
            } else if (TeamEnum.TEAM_B.equals(playersManager.getIdentifierForTeamId(teamId))) {
                viewHolder.playerNumber.setTextColor(ContextCompat.getColor(context, R.color.teamBEndColor));
            } else {
                viewHolder.playerNumber.setTextColor(ContextCompat.getColor(context, R.color.colorNeutralStatistic));
            }
        } else if (category.equals(StatisticCategory.OverTime)) {
            viewHolder.playerNumber.setTextColor(ContextCompat.getColor(context, R.color.colorNeutralStatistic));
        }

        viewHolder.statisticType.setText(abbreviation);
        viewHolder.statisticTypeIcon.setImageResource(getStatTypeIcon(abbreviation));
        viewHolder.playByPlaystatTimeSegment.setText(currentStatistic.getTimeSegmentName());
        viewHolder.playByPlayStatTimeStamp.setText(TimerManager.getInstance().getStringRepresentationOfTimeStamp(currentStatistic.getGameTime()));

        viewHolder.deleteStatItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticsManager.getInstance().removeCurrentStatisticFromList(currentStatistic);
                updateStatisticList();
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void updateStatisticList() {
        statistics = statisticManager.getCurrentStatisticsList();
        Collections.reverse(statistics);
    }

    @DrawableRes
    private int getStatTypeIcon(String abbreviation) {
        switch (abbreviation) {
            case "+1": {
            }

            case "+2": {
            }

            case "+3": {
                return R.drawable.field_point_circle;
            }

            case "Miss1": {
            }

            case "Miss2": {
            }

            case "Miss3": {
                return R.drawable.field_point_x;
            }
        }
        return 0;
    }
}
