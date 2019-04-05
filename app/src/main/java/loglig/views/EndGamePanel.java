package loglig.views;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import loglig.activities.GameMonitoringActivity;
import loglig.dialogs.LogLigPromptDialog;
import loglig.is_uptown4.loglig.R;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class EndGamePanel extends RelativeLayout {

    private Context context = null;
    private LayoutInflater inflater;
    private Button stopClockButton;

    public EndGamePanel(Context context) {
        super(context);
        init(context);
    }

    public EndGamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EndGamePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context _context) {
        context = _context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.end_game_layout, this, true);

        this.stopClockButton = (Button) findViewById(R.id.stopClockButton);
        this.stopClockButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final LogLigPromptDialog promptDialog = new LogLigPromptDialog(context, "End Game?", "");
                promptDialog.setOkButtonTitle("END GAME NOW");
                promptDialog.setOkClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((GameMonitoringActivity)_context).endGame(promptDialog.getInputTextValue());
                        promptDialog.getDialog().dismiss();
                    }
                });
                promptDialog.setCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        promptDialog.getDialog().dismiss();
                    }
                });
                promptDialog.show();
            }
        });
    }
}
