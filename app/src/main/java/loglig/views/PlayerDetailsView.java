package loglig.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import loglig.activities.GameMonitoringActivity;
import loglig.adapters.PlayerPersonalStatisticAdapter;
import loglig.interfaces.OnHidePlayerDetailsViewListener;
import loglig.interfaces.RealmTransaction;
import loglig.is_uptown4.loglig.R;
import loglig.managers.PlayersManager;
import loglig.managers.RealmManager;
import loglig.managers.StatisticsManager;
import loglig.models.Player;
import loglig.models.Point;
import loglig.models.Statistic;
import loglig.utility.KeyboardUtility;

/**
 * Created by is_uptown4 on 21/08/16.
 */
public class PlayerDetailsView extends RelativeLayout implements OnHidePlayerDetailsViewListener {

    private static final int POINT_MARK_SIZE = 25;
    private ImageView editPlayerShirtNumberImageView;
    private LayoutInflater inflater;
    private CircularImageView playerDetailsImage;
    private TextView playerDetailsName, playerDetailsTeam;
    private EditText playerDetailsShirtNumber;
    private GridView playerPersonalStatisticGridView;
    private PlayerPersonalStatisticAdapter playerPersonalStatisticsAdapter;
    private ImageView playerDetailsCourtImage;
    private ViewGroup courtContainer;
    private ImageView courtImage;
    private Player player;
    private Button closePlayerDetailsButton;
    private boolean isPlayerShirtNumberInEditMode;

    public PlayerDetailsView(Context context) {
        super(context);
        init(context);
    }

    public PlayerDetailsView(Context context, Player _player) {
        super(context);
        this.player = _player;
        init(context);
        setPlayerImage(player.getImageUrl(), context);
        setPlayerName(player.getName());
        setTeamName(PlayersManager.getInstance().getTeamById(player.getTeamId()).getTeamName());
        setPlayerShirtNumber(player);
    }

    public PlayerDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerDetailsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.player_details_layout, this, true);
        // --- --- --- --- ---
        this.playerDetailsImage = (CircularImageView) findViewById(R.id.playerDetailsImage);
        this.playerDetailsName = (TextView) findViewById(R.id.playerDetailsName);
        this.playerDetailsTeam = (TextView) findViewById(R.id.playerDetailsTeam);
        this.playerDetailsShirtNumber = (EditText) findViewById(R.id.playerDetailsShirtNumber);

        this.editPlayerShirtNumberImageView = (ImageView) findViewById(R.id.editPlayerShirtNumberImageView);

        this.playerDetailsShirtNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String newNumber = playerDetailsShirtNumber.getText().toString();
                    savePlayerDetailsShirtNumberToDb(newNumber);
                    editPlayerDetailsShirtNumber(isPlayerShirtNumberInEditMode);
                }
                return true;
            }
        });
        this.editPlayerShirtNumberImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlayerShirtNumberInEditMode = playerDetailsShirtNumber.isEnabled();
                editPlayerDetailsShirtNumber(isPlayerShirtNumberInEditMode);

                /*if (!playerDetailsShirtNumber.isEnabled()) {
                    playerDetailsShirtNumber.setEnabled(true);
                    playerDetailsShirtNumber.setTextColor(Color.BLACK);
                    playerDetailsShirtNumber.setBackgroundResource(R.color.colorWhite);
                } else {
                    playerDetailsShirtNumber.setEnabled(false);
                    playerDetailsShirtNumber.setTextColor(Color.WHITE);
                    playerDetailsShirtNumber.setBackgroundResource(R.color.colorBlack);
                    RealmManager.getInstance().transactionExecute(new RealmTransaction() {
                        @Override
                        public void execute() {
                            player.setShirtNumber(playerDetailsShirtNumber.getText().toString());
                        }
                    });
                }*/
            }
        });

        this.playerPersonalStatisticGridView = (GridView) findViewById(R.id.playerDetailsPersonalStatisticGridView);
        // TODO: get from statistic manager players personal statistics.
        // TODO: Set player personal statistics into playerDetailsPersonalStatisticGridView
        this.playerPersonalStatisticsAdapter = new PlayerPersonalStatisticAdapter(context, player);
        this.playerPersonalStatisticGridView = (GridView) findViewById(R.id.playerDetailsPersonalStatisticGridView);
        this.playerPersonalStatisticGridView.setClickable(false);
        this.playerPersonalStatisticGridView.setAdapter(playerPersonalStatisticsAdapter);

        // TODO: Set players score and fouls as marker on field.
        this.playerDetailsCourtImage = (ImageView) findViewById(R.id.playerDetailsCourtImage);
        this.courtContainer = (RelativeLayout) findViewById(R.id.playerDetailsFieldSummary);
        List<Statistic> currentStatisticsList = StatisticsManager.getInstance().getCurrentStatisticsList();
        String playerId = this.player.getId();
        Statistic statistic;
        Point location;
        int locationX;
        int locationY;
        int pointMarkX = (int) this.playerDetailsCourtImage.getX();
        int pointMarkY = (int) this.playerDetailsCourtImage.getY();
        float courtContainerScale = ((GameMonitoringActivity) context).getCourtContainerScale();

        for (int i = 0; i < currentStatisticsList.size(); i++) {
            statistic = currentStatisticsList.get(i);
            location = statistic.getLocation();
            locationX = (int) location.getX();
            locationY = (int) location.getY();

            if (playerId.equals(statistic.getPlayerID()) && locationX > 0 && locationY > 0) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(POINT_MARK_SIZE, POINT_MARK_SIZE);
                ImageView pointMark = new ImageView(getContext());
                params.leftMargin = (int) ((locationX + pointMarkX) / courtContainerScale);
                params.topMargin = (int) ((locationY + pointMarkY) / courtContainerScale);
                pointMark.setImageResource(getStatTypeIcon(statistic.getAbbreviation()));
                this.courtContainer.addView(pointMark, params);
            }
        }

        this.closePlayerDetailsButton = (Button) findViewById(R.id.closePlayerDetailsButton);
    }

    private void setPlayerImage(String imageUrl, final Context context) {
        int placeHolder;

        if (isShirtNumberCorrect(player)) {
            placeHolder = R.drawable.player_icon_holder;
        } else {
            placeHolder = R.drawable.no_icon_holder;
        }

        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .placeholder(placeHolder)
                .into(playerDetailsImage);
    }

    private void setPlayerName(String name) {
        this.playerDetailsName.setText(name);
    }

    private void setTeamName(String teamName) {
        this.playerDetailsTeam.setText(teamName);
    }

    private void setPlayerShirtNumber(Player player) {
        if (isShirtNumberCorrect(player)) {
            this.playerDetailsShirtNumber.setText(player.getShirtNumber());
        } else {
            this.playerDetailsShirtNumber.setText(getContext().getString(R.string.empty_string));
        }
    }

    public Button getClosePlayerDetailsButton() {
        return closePlayerDetailsButton;
    }

    private boolean isShirtNumberCorrect(Player player) {
        int shirtNum = -1;
        try {
            shirtNum = Integer.parseInt(player.getShirtNumber());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return shirtNum >= 0;
    }

    private void editPlayerDetailsShirtNumber(boolean isInEditMode) {
        playerDetailsShirtNumber.setEnabled(!isInEditMode);
        isPlayerShirtNumberInEditMode = !isInEditMode;
        if (isInEditMode) {
            playerDetailsShirtNumber.setTextColor(Color.WHITE);
            playerDetailsShirtNumber.setBackgroundResource(R.color.colorBlack);
            KeyboardUtility.hideSoftKeyboard(playerDetailsShirtNumber);
        } else {
            playerDetailsShirtNumber.setTextColor(Color.BLACK);
            playerDetailsShirtNumber.setBackgroundResource(R.color.colorWhite);
            playerDetailsShirtNumber.selectAll();
            KeyboardUtility.showSoftKeyboard(playerDetailsShirtNumber);
        }
    }

    private void savePlayerDetailsShirtNumberToDb(final String number) {
        RealmManager.getInstance().transactionExecute(new RealmTransaction() {
            @Override
            public void execute() {
                player.setShirtNumber(number);
            }
        });
    }

    @Override
    public void onHide() {
        if (isPlayerShirtNumberInEditMode) {
            String newNumber = playerDetailsShirtNumber.getText().toString();
            savePlayerDetailsShirtNumberToDb(newNumber);
        }
    }

    @DrawableRes
    private int getStatTypeIcon(String abbreviation) {
        @DrawableRes int imageResource = 0;
        switch (abbreviation) {
            case "+1": {
            }

            case "+2": {
            }

            case "+3": {
                imageResource = R.drawable.field_point_circle;
                break;
            }

            case "Miss1": {
            }

            case "Miss2": {
            }

            case "Miss3": {
                imageResource = R.drawable.field_point_x;
                break;
            }
        }
        return imageResource;
    }
}
