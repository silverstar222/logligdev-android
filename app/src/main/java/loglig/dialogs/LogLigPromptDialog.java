package loglig.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import loglig.is_uptown4.loglig.R;
import loglig.activities.StatisticSelectionActivity;
import loglig.managers.StatisticsManager;

/**
 * Created by is_uptown4 on 16/05/16.
 */
public class LogLigPromptDialog extends Dialog {
    private Context context;
    private String promptMessage;
    private String promptTitle;
    private Dialog dialog;
    View.OnClickListener okClickListener;
    View.OnClickListener cancelClickListener;
    EditText inputText;
    private String okButtonTitle;

    protected LogLigPromptDialog(Context context) {
        super(context);
        this.context = context;
        this.promptMessage = "";
        this.promptTitle = "";
        this.okButtonTitle = null;
    }

    public LogLigPromptDialog(Context context, String title, String message) {
        super(context);
        this.context = context;
        this.promptTitle = title;
        this.promptMessage = message;
        this.okButtonTitle = null;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setOkClickListener(View.OnClickListener listener) {
        this.okClickListener = listener;
    }

    public void setCancelClickListener(View.OnClickListener listener) {
        this.cancelClickListener = listener;
    }

    public String getInputTextValue() {
        return this.inputText.getText().toString();
    }

    public void setOkButtonTitle(String okButtonTitle) {
        this.okButtonTitle = okButtonTitle;
    }

    public void show() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.prompt_dialog_layout);
        dialog.setTitle(this.promptTitle);
        this.inputText = (EditText) dialog.findViewById(R.id.promptInput);
        this.inputText.setHint(promptMessage);

        ImageView image = (ImageView) dialog.findViewById(R.id.promptImage);
        image.setImageResource(R.drawable.dialog_icon);

        this.okClickListener = this.okClickListener == null ? new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } : this.okClickListener;

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
        if (this.okButtonTitle != null)
            dialogButtonOk.setText(this.okButtonTitle);
        dialogButtonOk.setOnClickListener(this.okClickListener);

        this.cancelClickListener = this.cancelClickListener == null ? new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        } : this.cancelClickListener;

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        dialogButtonCancel.setOnClickListener(this.cancelClickListener);

        dialog.show();
    }

}