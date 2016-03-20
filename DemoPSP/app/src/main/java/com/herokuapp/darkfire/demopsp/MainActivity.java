package com.herokuapp.darkfire.demopsp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {

    EditText payeeVPA, payeeName, tid, amount;
    Button payBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimulatePaymentTask().execute();
            }
        });

    }

    private void initializeViews(){
        payeeVPA = (EditText) findViewById(R.id.payeeVPA);
        payeeName = (EditText) findViewById(R.id.payeeNameEditText);
        tid = (EditText) findViewById(R.id.tidEditText);
        amount = (EditText) findViewById(R.id.amountEditText);
        payBtn = (Button) findViewById(R.id.payBtn);
        Uri uri = getIntent().getData();
        if(uri!=null){
            payeeVPA.setText(uri.getQueryParameter("pa").toString());
            if(!uri.getQueryParameter("pn").equals("null"))
                payeeName.setText(uri.getQueryParameter("pn"));
            else if(!uri.getQueryParameter("mc").equals("null"))
                payeeName.setText(uri.getQueryParameter("mc"));
            tid.setText(uri.getQueryParameter("tr"));
            amount.setText(uri.getQueryParameter("am"));
        }
    }

    private class SimulatePaymentTask extends AsyncTask<Void, Void, Void>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(MainActivity.this, "Making Payment","Please wait", true, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra("txnId", tid.getText().toString());
            intent.putExtra("responseCode", "RC500");
            intent.putExtra("approvalRefNo", "APP2013");
            intent.putExtra("status", "Success");
            intent.putExtra("txnRef", tid.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }


}
