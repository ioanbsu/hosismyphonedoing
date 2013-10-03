package com.artigile.checkmyphone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
        displayNextMessage(MessageToDeviceService.getMessageList().get(0));
    }

    public void addMessageToQueue(String message) {
        MessageToDeviceService.getMessageList().add(message);
    }

    private void displayNextMessage(String message) {
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                checkNextMessage();
            }
        }).show();
    }

    private void checkNextMessage() {
        MessageToDeviceService.getMessageList().remove(0);
        if (MessageToDeviceService.getMessageList().isEmpty()) {
            this.finish();
        } else {
            displayNextMessage(MessageToDeviceService.getMessageList().get(0));
        }
    }
}
