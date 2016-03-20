package com.darkfire;

import android.app.Activity;
import android.content.Intent;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.R;
import com.google.zxing.client.android.encode.EncodeActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MakePayment extends Activity {

    TextView payerName, payerId, amount;
    EditText optionalPayerVPA;
    ListView bill;
    Button genQrBtn;
    final String tid = Utility.getTransactionId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment2);
        initializeView();
        setUpListeners();
        setUpListView();
        Log.i("Log: ", "is log working");
    }


    private void setUpListeners(){

        genQrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CurrentUser.getInstance().isActive()) {
                    //Intent intent = new Intent("com.google.zxing.client.android.ENCODE");
                    Intent intent = new Intent(getBaseContext(), EncodeActivity.class);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setAction(Intents.Encode.ACTION);
                    intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);

                    String data = encodeData();
                    /*
                    //encode Virtual Private Address
                    data = data + CurrentUser.getInstance().getVpa() + Utility.lineSeparator();
                    //encode Merchant code
                    data = data + CurrentUser.getInstance().getMca() + Utility.lineSeparator();
                    //endocde the Transaction reference ID
                    data = data + Utility.getTransactionId() + Utility.lineSeparator();
                    //encode transaction short description
                    data = data + "A sample transaction." + Utility.lineSeparator();
                    //encode the amount;
                    data = data + amount.getText().toString();
                    */
                    intent.putExtra(Intents.Encode.DATA, data);
                    intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.toString());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Wrong password!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void initializeView(){

        bill = (ListView) findViewById(R.id.billListView);

        payerName = (TextView) findViewById(R.id.payer_name);
        payerName.setText(CurrentUser.getInstance().getMobile());

        payerId = (TextView) findViewById(R.id.id_number);
        payerId.setText(tid);

        amount = (TextView) findViewById(R.id.amount);

        genQrBtn = (Button) findViewById(R.id.getPayBtn);

        optionalPayerVPA = (EditText) findViewById(R.id.optionalPayerVPA);

    }

    private void setUpListView(){
        bill.setAdapter(new BillAdapter());
    }

    private class BillAdapter extends BaseAdapter{

        ArrayList<BillElement> elements = new ArrayList<>();

        @Override
        public int getCount() {
            return elements.size() + 1;
        }

        @Override
        public Object getItem(int i) {
            if(i==elements.size()){
                return null;
            }
            return elements.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Log.i("Bill","Inside getView method start");

            Log.i("Bill","Inside getView method");
            Log.i("Value of i: ", ""+i);
            if(i == elements.size()){
                //this is the last element
                Log.i("Bill","creating last element");
                LayoutInflater inflater = getLayoutInflater();
                final View billElement = inflater.inflate(R.layout.bill_element_edit, null);
                Button addElement = (Button) billElement.findViewById(R.id.addElement);
                addElement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View btn) {
                        String name = ((EditText)billElement.findViewById(R.id.element_name_edit)).getText().toString();
                        String costPerUnit = ((EditText)billElement.findViewById(R.id.costPerUnit_edit)).getText().toString();
                        String noOfUnits = ((EditText)billElement.findViewById(R.id.noOfUnits_edit)).getText().toString();
                        if(name.equals("") || costPerUnit.equals("") || noOfUnits.equals("")){
                            return;
                        }
                        BillElement element = new BillElement();
                        element.name = name;
                        element.costPerUnit = Double.parseDouble(costPerUnit);
                        element.noOfUnits = Integer.parseInt(noOfUnits);
                        elements.add(element);
                        amount.setText("" + totalAmount());
                        notifyDataSetChanged();
                    }
                });
                return billElement;
            }
            view = getLayoutInflater().inflate(R.layout.bill_element, null);
            ((TextView)view.findViewById(R.id.element_serial)).setText("" + (i+1));
            ((TextView)view.findViewById(R.id.element_name)).setText(elements.get(i).name);
            ((TextView)view.findViewById(R.id.noOfUnits)).setText(""+elements.get(i).noOfUnits);
            ((TextView)view.findViewById(R.id.costPerUnit)).setText(""+elements.get(i).costPerUnit);
            ((TextView)view.findViewById(R.id.amount)).setText(""+elements.get(i).calculateAmount());
            return view;
        }

        public double totalAmount(){
            double amt = 0;
            for(BillElement e : elements){
                amt += e.calculateAmount();
            }
            return amt;
        }
    }

    public String encodeData(){
        Payment paymentObj = new Payment();
        String data = "";
        StringBuilder encoded = new StringBuilder("");
        //encode type of qr whether a or b
        String payerVPA = optionalPayerVPA.getText().toString();
        if(payerVPA == null || payerVPA.equals(""))
            payerVPA = "unknownVPA";
        paymentObj.setPayerVirtualAdd(payerVPA);
        encoded.append(payerVPA+Utility.elementSeparator);
        //encode all the BillElements
        ArrayList<BillElement> arrayList = new ArrayList<>();
        for(int i = 0; i < bill.getAdapter().getCount(); i++){
            BillElement element = (BillElement) bill.getAdapter().getItem(i);
            if(element == null)
                continue;
            arrayList.add(element);
            encoded.append(element.name + Utility.billValueSeparator);
            encoded.append(element.noOfUnits+Utility.billValueSeparator);
            encoded.append(element.costPerUnit+Utility.billElementSeparator);

        }
        paymentObj.setBill(arrayList);
        encoded.append(Utility.elementSeparator);
        //encode Virtual Private Address
        paymentObj.setPayeeVirtualAdd(CurrentUser.getInstance().getVpa());
        encoded.append(CurrentUser.getInstance().getVpa() + Utility.elementSeparator);
        //encode Merchant code
        paymentObj.setPayerIdNumber(CurrentUser.getInstance().getMca());
        encoded.append(CurrentUser.getInstance().getMca() + Utility.elementSeparator);
        //endocde the Transaction reference ID
        paymentObj.transactionId = tid;
        encoded.append(tid + Utility.elementSeparator);
        //encode transaction short description
        paymentObj.transactionDesc = "A simple transaction.";
        encoded.append("A sample transaction." + Utility.elementSeparator);
        //encode the amount;
        paymentObj.setAmount(Double.parseDouble(amount.getText().toString()));
        encoded.append(amount.getText().toString());
        new SavePaymentTask(getApplicationContext()).execute(paymentObj);
        return encoded.toString();
    }

    public static Payment decodeData(String valsList){
        Log.i("Log", valsList.toString());
        String vals[] = valsList.split(Utility.elementSeparator);
        HashMap<String, String> map = new HashMap<>();

        if(!CurrentUser.getInstance().getVpa().equals(vals[0])){
            //does not match
            //it is not the same vpa intended to
            return null;
        }
        map.put(Payment.PAYEE_VIRTUAL_ADD, vals[2]);
        map.put(Payment.PAYEE_ID_NUMBER, vals[3]);
        map.put(Payment.TRANSACTION_ID, vals[4]);
        map.put(Payment.TRANSACTION_DESC, vals[5]);
        map.put(Payment.AMOUNT, vals[6]);
        if(CurrentUser.getInstance().isActive()){
            //TODO: put current user credentials from user singleton
            map.put(Payment.PAYER_NAME, CurrentUser.getInstance().getMobile());
            map.put(Payment.PAYER_VIRTUAL_ADD, CurrentUser.getInstance().getVpa());
        }
        Payment p = new Payment();
        p.makePaymentObj(map);
        p.setBill(decodeBill(vals[1]));
        return p;

    }
    public static ArrayList<BillElement> decodeBill(String data){
        ArrayList<BillElement> bill = new ArrayList<>();
        if(data == null){
            return bill;
        }
        String[] billElements = data.split(Utility.billElementSeparator);
        for(String billElement : billElements){
            if(billElement.equals("") || billElement == null)
                continue;
            String[] billVals = billElement.split(Utility.billValueSeparator);
                BillElement element = new BillElement();
                element.name = billVals[0];
                element.noOfUnits = Integer.parseInt(billVals[1]);
                element.costPerUnit = Double.parseDouble(billVals[2]);
                bill.add(element);
        }
        return bill;
    }

}
