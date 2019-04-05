package loglig.views;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import loglig.enums.TeamEnum;
import loglig.is_uptown4.loglig.R;
import loglig.managers.GameManager;
import loglig.managers.PlayersManager;

/**
 * Created by is_uptown4 on 07/06/16.
 */
public class TeamLogoLayout extends RelativeLayout {

    private SimpleDraweeView teamLogo;
    private TextView teamName;
    private LayoutInflater inflater;
    private GameManager gameManager;
    private PlayersManager playersManager;

    public TeamLogoLayout(Context context) {
        super(context);
        init(context);
    }

    public TeamLogoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TeamLogoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater.inflate(R.layout.team_logo_layout, this, true);
        // --- --- --- --- ---
        gameManager = GameManager.getInstance();
        playersManager = PlayersManager.getInstance();
        // --- --- --- --- ---
        this.teamLogo = (SimpleDraweeView) findViewById(R.id.teamLogo);
        setTeamLogo(playersManager.getTeamById(playersManager.getCurrentTeamId(getCurrentTeamIdentifier())).getTeamLogoUri());
        // --- --- --- --- ---
        this.teamName = (TextView) findViewById(R.id.teamNameLogo);
        setTeamName(playersManager.getTeamById(playersManager.getCurrentTeamId(getCurrentTeamIdentifier())).getTeamName());
    }

    public void setTeamLogo(String imageUri) {
        Uri uri = Uri.parse(imageUri);
        teamLogo.setImageURI(uri);
    }

    private void setTeamName(String name) {
        this.teamName.setText(name);
    }

    private TeamEnum getCurrentTeamIdentifier() {
        TeamEnum team = TeamEnum.NONE;
        if (this.getId() == R.id.teamALogo) {
            team = TeamEnum.TEAM_A;
        }
        if (this.getId() == R.id.teamBLogo) {
            team = TeamEnum.TEAM_B;
        }
        return team;
    }


}