package com.artigile.checkmyphone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.artigile.checkmyphone.service.ActivityAndBroadcastUtils;
import com.artigile.howismyphonedoing.api.model.MessageToDeviceModel;
import roboguice.activity.RoboActivity;

import javax.inject.Singleton;

/**
 * User: ioanbsu
 * Date: 10/3/13
 * Time: 9:59 AM
 */
@Singleton
public class DialogActivity extends RoboActivity {
    private AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity);
        builder = new AlertDialog.Builder(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MessageToDeviceModel messageToDeviceModel = (MessageToDeviceModel) this.getIntent().getExtras().get(ActivityAndBroadcastUtils.MESSAGE_OBJECT);
        // Use the Builder class for convenient dialog construction
        builder.setMessage(messageToDeviceModel.getMessage())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogActivity.this.finish();
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                DialogActivity.this.finish();
            }
        }).show();
    }
}
