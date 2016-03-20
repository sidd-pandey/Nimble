package com.darkfire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.R;

import java.util.HashMap;

public class Dashboard extends Activity {

    private final int GET_PAID_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setUpListeners();
    }

    private void setUpListeners(){
        //collect call to psp mobile app
        Button getPaidBtn = (Button) findViewById(R.id.getpaidbtn);
        getPaidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //write implementation for getting paid
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, GET_PAID_CODE);
            }
        });

        //get call to psp mobile app
        Button makePaymentBtn = (Button) findViewById(R.id.makepaybtn);
        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MakePayment.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GET_PAID_CODE && resultCode ==RESULT_OK){
            String result = data.getStringExtra("SCAN_RESULT");
            Payment payment;
            if(result != null) {
                payment = buildPayment(result);
                new VerifyPaymentTask(this).execute(payment);
            }


            //build payment object
            //confirm if payment is of valid credentials through upi server
            //if yes direct to make payment activity
            //else cancel the transaction
        }
    }

    private Payment buildPayment(String valsList){
        String vals[] = valsList.split(Utility.lineSeparator());
        HashMap<String, String> map = new HashMap<>();
        map.put(Payment.PAYER_VIRTUAL_ADD, vals[0]);
        map.put(Payment.PAYER_ID_NUMBER, vals[1]);
        map.put(Payment.TRANSACTION_ID, vals[2]);
        map.put(Payment.TRANSACTION_DESC, vals[3]);
        map.put(Payment.AMOUNT, vals[4]);
        if(CurrentUser.getInstance().isActive()){
            map.put(Payment.PAYEE_NAME, CurrentUser.getInstance().getMobile());
            map.put(Payment.PAYEE_VIRTUAL_ADD, CurrentUser.getInstance().getVpa());
        }
        //TODO: put current user credentials from user singleton
        Payment p = new Payment();
        p.makePaymentObj(map);
        return p;
    }

}
