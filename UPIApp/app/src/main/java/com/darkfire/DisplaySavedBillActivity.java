package com.darkfire;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.client.android.R;

import java.util.List;

public class DisplaySavedBillActivity extends Activity {

    Payment payment;
    TextView payerName, idNumber, amount;
    EditText password;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_saved_bill);

        if(getIntent().getSerializableExtra(Payment.BILL)==null){
            return;
        }

        payment = (Payment) getIntent().getSerializableExtra(Payment.BILL);
        initializeView();

    }

    private void initializeView(){
        listView = (ListView) findViewById(R.id.billView);
        listView.setAdapter(new BillAdapter(payment.getBill()));
        payerName = (TextView) findViewById(R.id.payer_name);
        payerName.setText(payment.getPayerVirtualAdd());
        idNumber = (TextView) findViewById(R.id.id_number);
        idNumber.setText(payment.getTransactionId());
        amount = (TextView) findViewById(R.id.amount);
        amount.setText(""+payment.getAmount());
        password = (EditText)findViewById(R.id.password);
    }

    private class BillAdapter extends BaseAdapter {

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
