package loglig.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import loglig.is_uptown4.loglig.R;

/**
 * Created by is_uptown4 on 16/05/16.
 */
public class LogLigAlertDialog extends AlertDialog {
    private Context context;
    private String alertMessage;
    private String alertTitle;


    protected LogLigAlertDialog(Context context) {
        super(context);
        this.context = context;
        popupAlert();
    }

    public LogLigAlertDialog(Context context, String title, String message) {
        super(context);
        this.context = context;
        this.alertTitle = title;
        this.alertMessage = message;
        popupAlert();
    }

    private void popupAlert() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_dialog_layout);
        dialog.setTitle(this.alertTitle);
        TextView messageTxt = (TextView) dialog.findViewById(R.id.alertMessage);
        messageTxt.setText(this.alertMessage);
        ImageView image = (ImageView) dialog.findViewById(R.id.alertImage);
        image.setImageResource(R.drawable.alert_icon);

        Button dialogButton = (Button) dialog.findViewById(R.id.alertButtonOK);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}