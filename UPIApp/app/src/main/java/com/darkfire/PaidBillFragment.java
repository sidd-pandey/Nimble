package com.darkfire;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.google.zxing.client.android.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaidBillFragment extends Fragment {

    GridView gridView;
    ArrayList<Payment> payments;
    String type;

    public PaidBillFragment(){
        payments = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_paid_bill, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.type = getArguments().getString("type");
        gridView = (GridView) view.findViewById(R.id.paidBillGrid);
        new LoadDataTask().execute();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Payment p = (Payment) gridView.getAdapter().getItem(i);
                Intent intent = new Intent(getActivity(), DisplaySavedBillActivity.class);
                intent.putExtra(Payment.BILL, p);
                startActivity(intent);
            }
        });

        ((TextView)view.findViewById(R.id.savedBillsFragHead)).setText(type + " Bills");
    }

    private class PaidBillAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return payments.size();
        }

        @Override
        public Object getItem(int i) {
            return payments.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getActivity().getLayoutInflater().inflate(R.layout.paidbill_grid_element, null);
            if(type.equalsIgnoreCase("paid"))
                ((TextView)view.findViewById(R.id.gridElePayeeVPA)).setText(payments.get(i).getPayeeVirtualAdd());
            else
                ((TextView)view.findViewById(R.id.gridElePayeeVPA)).setText(payments.get(i).getPayerVirtualAdd());
            ((TextView)view.findViewById(R.id.gridEleAmountPaid)).setText(payments.get(i).getAmount()+"/-");
            return view;
        }
    }

    public class LoadDataTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(getActivity().openFileInput(CurrentUser.getInstance().getMobile()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(ois == null){
                return false;
            }
            Object obj = null;
            try {
                while((obj = ois.readObject())!=null){
                    Payment p = (Payment)obj;
                    Log.i("PayObj",p.toString());
                    Log.i("tag",""+p.getAmount());
                    if(type.equalsIgnoreCase("paid") && p.getPayerVirtualAdd().equalsIgnoreCase(CurrentUser.getInstance().getVpa())){
                        Log.i("tag","Added to list"+p.getAmount());
                        payments.add(p);
                    }
                    if (type.equalsIgnoreCase("collected") && !p.getPayerVirtualAdd().equalsIgnoreCase(CurrentUser.getInstance().getVpa())){
                        Log.i("tag","Added to list"+p.getAmount());
                        payments.add(p);
                    }

                }
                ois.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            gridView.setAdapter(new PaidBillAdapter());
        }
    }
}