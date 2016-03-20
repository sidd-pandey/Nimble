package com.darkfire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Siddharth on 2/24/2016.
 */
public class GetPaymentTask extends AsyncTask<Payment, Void, Payment> {

    Activity activity;
    ProgressDialog dialog;

    GetPaymentTask(Activity activity){
        this.activity = activity;

    }

    @Override
    protected void onPreExecute() {
        //TODO: create a progress dialog
        dialog = ProgressDialog.show(activity,"Making Payment","Please wait...",true);
    }

    @Override
    protected Payment doInBackground(Payment... payments) {
        //TODO: call pay on payment object
        //proceed accordingly
        payments[0].pay();
        return payments[0];
    }

    @Override
    protected void onPostExecute(Payment payment) {
        //TODO: disable progress dialog
        //redirect accordingly to dashboard
        dialog.dismiss();
        if(payment.isPaid()){
            Toast.makeText(activity.getApplicationContext(),"Success!",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(activity.getApplicationContext(),"Failed!",Toast.LENGTH_LONG).show();
        }
        activity.startActivity(new Intent(activity, Dashboard.class));
    }
}
