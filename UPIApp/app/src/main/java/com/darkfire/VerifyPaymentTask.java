package com.darkfire;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Siddharth on 2/24/2016.
 */
public class VerifyPaymentTask extends AsyncTask<Payment, Void, Payment> {

    private Activity activity;
    private ProgressDialog dialog;
    public static final String PAYMENT_OBJ = "PAYMENT_OBJ";

    VerifyPaymentTask(Activity c){
        this.activity = c;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(activity, "Verifying Payment", "Please wait...", true);
    }

    @Override
    protected Payment doInBackground(Payment... payments) {
        payments[0].verify();
        return  payments[0];
    }

    @Override
    protected void onPostExecute(Payment payment) {

        dialog.dismiss();

        if(payment.isVerified()){
            //redirect to make payment activity;
            Intent i = new Intent(activity.getBaseContext(), GetPayment.class);
            i.putExtra(PAYMENT_OBJ, payment);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.getBaseContext().startActivity(i);
        }
        else{
            //display error message
            Toast.makeText(activity.getBaseContext(), "Unable to verify payment!",Toast.LENGTH_LONG).show();
        }
    }
}
