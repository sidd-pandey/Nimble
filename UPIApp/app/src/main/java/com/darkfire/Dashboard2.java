package com.darkfire;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Dashboard2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int GET_PAID_CODE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard2.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard2 newInstance(String param1, String param2) {
        Dashboard2 fragment = new Dashboard2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Dashboard2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.activity_dashboard, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setUpListeners();
    }

    private void setUpListeners(){
        //collect call to psp mobile app
        Button getPaidBtn = (Button) getView().findViewById(R.id.getpaidbtn);
        getPaidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //write implementation for getting paid
                Intent intent = new Intent(getActivity().getBaseContext(), MakePayment.class);
                startActivity(intent);

            }
        });

        //get call to psp mobile app
        Button makePaymentBtn = (Button) getView().findViewById(R.id.makepaybtn);
        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), CaptureActivity.class);
                intent.setAction("com.google.zxing.client.android.SCAN");
                intent.putExtra("SAVE_HISTORY", false);
                startActivityForResult(intent, GET_PAID_CODE);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == GET_PAID_CODE && resultCode ==Activity.RESULT_OK){
            String result = data.getStringExtra("SCAN_RESULT");
            Payment payment;
            if(result != null) {
                payment = buildPayment(result);
                if(payment == null){
                    Toast.makeText(getActivity(), "Bill not for you!", Toast.LENGTH_SHORT).show();
                    return;
                }
                new VerifyPaymentTask(getActivity()).execute(payment);
            }
            //build payment object
            //confirm if payment is of valid credentials through upi server
            //if yes direct to make payment activity
            //else cancel the transaction
        }
    }

    private Payment buildPayment(String valsList){
        return MakePayment.decodeData(valsList);
    }



}
