package loglig.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;

import loglig.adapters.StatisticPresetAdapter;
import loglig.adapters.StatisticTypeAdapter;
import loglig.dialogs.LogLigAlertDialog;
import loglig.dialogs.LogLigPromptDialog;
import loglig.handlers.StatisticSetHandler;
import loglig.handlers.StatisticTypeServiceHandler;
import loglig.is_uptown4.loglig.R;
import loglig.managers.StatisticsManager;
import loglig.models.StatisticType;
import loglig.models.StatisticsSet;


/**
 * Created by is_uptown4 on 10/05/16.
 */
public class StatisticSelectionActivity extends Activity {

    private Context context;
    private GridView statisticTypeGridView;
    private ListView presetListView;
    private Button startButton, savePresetButton;
    private CheckBox selectAllStatisticsCheckBox;
    private StatisticTypeAdapter statisticTypeAdapter;
    private StatisticPresetAdapter presetAdapter;
    private Button addPresetButton, deletePresetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_selection);
        this.context = this;
        // --- --- --- --- --- ---
        this.startButton = (Button) findViewById(R.id.startButton);
        this.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });
        // --- --- --- --- --- ---
        this.savePresetButton = (Button) findViewById(R.id.savePresetButton);
        setSavePresetButtonEnabled(false);
        this.savePresetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreset();
            }
        });
        // --- --- --- --- --- ---
        this.selectAllStatisticsCheckBox = (CheckBox) findViewById(R.id.selectAllStatisticsCheckBox);
        this.selectAllStatisticsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    selectAllStatistics(true);
                    ((CheckBox) v).setText("Deselect All");
                } else {
                    selectAllStatistics(false);
                    ((CheckBox) v).setText("Select All");
                }
            }
        });
        // --- --- --- --- --- ---
        // --- --- --- --- --- ---
        this.presetListView = (ListView) findViewById(R.id.presetsListView);
        this.presetAdapter = new StatisticPresetAdapter(this.context);
        final StatisticSetHandler statisticSetHandler = new StatisticSetHandler() {
            @Override
            public void onSuccess(int statusCode, JSONArray response) {
                presetListView.setAdapter(presetAdapter);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
            }
        };
        // --- --- --- --- --- ---
        this.statisticTypeGridView = (GridView) findViewById(R.id.statisticsGridView);
        this.statisticTypeAdapter = new StatisticTypeAdapter(this.context);
        StatisticTypeServiceHandler statisticTypeServiceHandler = new StatisticTypeServiceHandler() {
            @Override
            public void onSuccess(int statusCode, JSONArray response) {
                statisticTypeAdapter.setStatisticTypeList(StatisticsManager.getInstance().getStatisticTypesListForPlayer());
                statisticTypeGridView.setAdapter(statisticTypeAdapter);
                StatisticsManager.getInstance().fetchStatisticsPresets(statisticSetHandler);
            }

            @Override
            public void onFailure(int statusCode, Throwable throwable) {
            }
        };
        StatisticsManager.getInstance().fetchAllStatisticTypes(statisticTypeServiceHandler);
        // --- --- --- --- --- ---
        // TODO: verify if user is admin that set addPresetButton, deletePresetButton and savePresetButton visibility to TRUE else to FALSE
        this.addPresetButton = (Button) findViewById(R.id.addPresetButton);
        this.addPresetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewPreset();
            }
        });
        // --- --- --- --- --- ---
        this.deletePresetButton = (Button) findViewById(R.id.deletePresetButton);
        this.deletePresetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePreset();
            }
        });
    }

    private void startGame() {
        if (countSelectedStatistics() < 1) {
            new LogLigAlertDialog(context, "Statistics Selection Error", "Select at least 1 " +
                    "statistics in order to begin game monitoring.");
        } else {
            Intent intent = new Intent(context, GameMonitoringActivity.class);
            context.startActivity(intent);
        }
    }

    private void deletePreset() {
        StatisticsManager.getInstance().deleteStatisticPreset();
        updateStatisticPresetListView();
        updateStatisticTypeGridView();
    }

    private void savePreset() {
        if (countSelectedStatistics() < 2) {
            new LogLigAlertDialog(context, "Statistics Selection Error", "Select at least 2 " +
                    "statistics in order to save this preset");
        } else {
            StatisticsManager.getInstance().savePresetSelections(getSelectedStatistics());
            new LogLigAlertDialog(context, "Success", "The Preset '" + StatisticsManager.getInstance().getSelectedSet().getSetName() + "' saved successfully.");
            setSavePresetButtonEnabled(false);
        }
    }

    private void addNewPreset() {
        final LogLigPromptDialog promptDialog = new LogLigPromptDialog(context, "Set new preset name", "Type new preset name");
        promptDialog.setOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setName = promptDialog.getInputTextValue();
                if (StatisticsManager.getInstance().isStatisticPresetNameExist(setName)) {
                    new LogLigAlertDialog(context, "Invalid preset name", "Preset '" + setName + "' is already exist.");
                } else if (setName.equals("")) {
                    new LogLigAlertDialog(context, "Invalid preset name", "The preset name can't be an empty string");
                } else {
                    StatisticsManager.getInstance().createNewStatisticPreset(setName);
                    StatisticsManager.getInstance().setSelectedPreset(
                            StatisticsManager.getInstance().findSetByName(setName));
                    updateStatisticPresetListView();
                    presetListView.smoothScrollToPosition(presetAdapter.getCount());
                }
                promptDialog.getDialog().dismiss();
                setSavePresetButtonEnabled(true);
                selectAllStatistics(false);
            }
        });
        promptDialog.setCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog.getDialog().dismiss();
                setSavePresetButtonEnabled(false);
            }
        });
        promptDialog.show();
    }

    private void selectAllStatistics(boolean isChecked) {
        CheckBox view;
        for (int i = 0; i < this.statisticTypeGridView.getChildCount(); i++) {
            view = (CheckBox) this.statisticTypeGridView.getChildAt(i);
            view.setChecked(isChecked);
            if (isChecked) {
                view.setTextColor(ContextCompat.getColor(context, R.color.presetFontSelected));
            } else {
                view.setTextColor(ContextCompat.getColor(context, R.color.presetFontNotSelected));
            }
        }

        for (StatisticType type : StatisticsManager.getInstance().getSelectedSet().getSet()) {
            type.setSelected(isChecked);
        }
    }

    public void updateIsCheckedStatisticsBySelectedStatisticPreset() {
        StatisticsSet set = StatisticsManager.getInstance().getSelectedSet();
        CheckBox view;
        for (int i = 0; i < this.statisticTypeGridView.getChildCount(); i++) {
            view = (CheckBox) this.statisticTypeGridView.getChildAt(i);
            if (view.getText().toString().equals(set.getSetName()))
                view.setChecked(true);
        }
        updateStatisticTypeGridView();
    }

    public void updateStatisticPresetListView() {
        this.presetAdapter.notifyDataSetChanged();
    }

    public void updateStatisticTypeGridView() {
        this.statisticTypeAdapter.notifyDataSetChanged();
    }

    private int countSelectedStatistics() {
        int count = 0;
        CheckBox view;
        for (int i = 0; i < this.statisticTypeGridView.getChildCount(); i++) {
            view = (CheckBox) this.statisticTypeGridView.getChildAt(i);
            if (view.isChecked()) count++;
        }
        return count;
    }

    public void resetSelectAllStatisticsCheckBox() {
        this.selectAllStatisticsCheckBox.setText("Select All");
        this.selectAllStatisticsCheckBox.setChecked(false);
    }

    public void resetDeselectAllStatisticsCheckBox() {
        this.selectAllStatisticsCheckBox.setText("Deselect All");
        this.selectAllStatisticsCheckBox.setChecked(true);
    }

    private ArrayList<String> getSelectedStatistics() {
        ArrayList<String> statisticsList = new ArrayList<String>();
        CheckBox view;
        for (int i = 0; i < this.statisticTypeGridView.getChildCount(); i++) {
            view = (CheckBox) this.statisticTypeGridView.getChildAt(i);
            if (view.isChecked()) statisticsList.add(view.getText().toString());
        }
        return statisticsList;
    }

    private void setSavePresetButtonEnabled(boolean isEnabled) {
        this.savePresetButton.setEnabled(isEnabled);
        if (isEnabled)
            this.savePresetButton.setTextColor(ContextCompat.getColor(context, R.color.presetFontSelected));
        else
            this.savePresetButton.setTextColor(ContextCompat.getColor(context, R.color.colorOrangeDisabled));


    }

}