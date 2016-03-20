package com.darkfire;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.zxing.client.android.R;

public class BiilsFragment extends Fragment {

    ImageButton allBillButton;

    public static BiilsFragment newInstance() {
        BiilsFragment fragment = new BiilsFragment();
        return fragment;
    }

    public BiilsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_biils, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        allBillButton = (ImageButton) view.findViewById(R.id.allBills);
        allBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), YourBill.class);
                startActivity(intent);
            }
        });
    }
}
