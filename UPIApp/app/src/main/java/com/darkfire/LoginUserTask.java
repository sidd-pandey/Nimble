package com.darkfire;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.zxing.client.android.R;

/**
 * Created by Siddharth on 2/24/2016.
 */
public class LoginUserTask extends AsyncTask<CurrentUser, Void, Boolean> {

    private Activity activity;
    ProgressDialog dialog;

    LoginUserTask(Activity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(activity, "Signing In","Please wait...", true);
    }

    @Override
    protected Boolean doInBackground(CurrentUser... currentUsers) {
        return currentUsers[0].verify();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        dialog.dismiss();
        if(result){
            Intent intent = new Intent(activity.getBaseContext(), HomeScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
        else{
            Toast.makeText(activity, "Unable to login...",Toast.LENGTH_LONG).show();
        }

    }
}
