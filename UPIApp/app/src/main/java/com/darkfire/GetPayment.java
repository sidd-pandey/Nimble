package com.darkfire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.R;

import java.util.List;

public class GetPayment extends Activity {

    Payment paymentObj;
    TextView payerName, idNumber, amount;
    EditText password;
    ListView listView;

    protected static final String DEEP_LINK_URL_BASE = "upi://pay";
    public static final int PSP_APP = 1;
    boolean testing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_payment);

        paymentObj=(Payment)getIntent().getSerializableExtra(VerifyPaymentTask.PAYMENT_OBJ);
        if(paymentObj==null)
            return;
        if(!paymentObj.isVerified())
            return;
        intializeView();
        setUpListeners();

    }

    private void intializeView(){
        listView = (ListView) findViewById(R.id.billView);
        payerName = (TextView) findViewById(R.id.payer_name);
        payerName.setText(paymentObj.getPayerVirtualAdd());
        idNumber = (TextView) findViewById(R.id.id_number);
        idNumber.setText(paymentObj.getTransactionId());
        amount = (TextView) findViewById(R.id.amount);
        amount.setText(""+paymentObj.getAmount());
        password = (EditText)findViewById(R.id.password);
    }

    private void setUpListeners(){
        Button verifyPayBtn = (Button) findViewById(R.id.getPayBtn);
        verifyPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testing == true){
                    Log.i("tag","Reached inside testing...");
                    if(password.getText().toString().equals(CurrentUser.getInstance().getPassword())) {
                        onActivityResult(PSP_APP, RESULT_OK, new Intent());
                        return;
                    }
                }
                if(password.getText().toString().equals(CurrentUser.getInstance().getPassword())) {
                    //new GetPaymentTask(GetPayment.this).execute(paymentObj);

                    StringBuilder urlBuilder = new StringBuilder();
                            urlBuilder.append(DEEP_LINK_URL_BASE).append("?")
                            .append("pa").append("=").append(paymentObj.getPayeeVirtualAdd())
                            .append("&") .append("pn").append("=").append(paymentObj.getPayeeName())
                            .append("&") .append("mc").append("=").append(paymentObj.getPayeeIdNumber())
                            .append("&") .append("ti").append("=").append(paymentObj.getPayeeIdNumber())
                            .append("&") .append("tr").append("=").append(paymentObj.getTransactionId())
                            .append("&") .append("tn").append("=").append(paymentObj.getTransactionDesc())
                            .append("&") .append("am").append("=").append(paymentObj.getAmount())
                            .append("&") .append("cu").append("=").append("INR");
                            //.append("&")

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urlBuilder.toString()));

                    startActivityForResult(intent, PSP_APP);

                }
                else{
                    Toast.makeText(getBaseContext(),"Wrong Password!",Toast.LENGTH_LONG).show();
                }
            }
        });
        listView.setAdapter(new BillAdapter(paymentObj.getBill()));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == PSP_APP){
            if(data.getStringExtra("status").equalsIgnoreCase("success")) {
                Toast.makeText(getBaseContext(), "Success!", Toast.LENGTH_SHORT).show();
                new SavePaymentTask(getApplicationContext()).execute(paymentObj);
                finish();
            }else{
                Toast.makeText(getBaseContext(), "Transaction Failed!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getBaseContext(), "Transaction Failed!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private class BillAdapter extends BaseAdapter{

        List<BillElement> elements;

        BillAdapter(List<BillElement> elements){
            this.elements = elements;
        }

        @Override
        public int getCount() {
            return elements.size();
        }

        @Override
        public Object getItem(int i) {
            return elements.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view != null){
                return view;
            }
            view = getLayoutInflater().inflate(R.layout.bill_element, null);
            ((TextView)view.findViewById(R.id.element_serial)).setText("" + (i+1));
            ((TextView)view.findViewById(R.id.element_name)).setText(elements.get(i).name);
            ((TextView)view.findViewById(R.id.noOfUnits)).setText("" + elements.get(i).noOfUnits);
            ((TextView)view.findViewById(R.id.costPerUnit)).setText(""+elements.get(i).costPerUnit);
            ((TextView)view.findViewById(R.id.amount)).setText(""+elements.get(i).calculateAmount());
            return view;
        }
    }
}
