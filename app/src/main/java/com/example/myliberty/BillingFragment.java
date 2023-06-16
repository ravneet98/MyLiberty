package com.example.myliberty;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillingFragment#} factory method to
 * create an instance of this fragment.
 */
public class BillingFragment extends Fragment {


    public BillingFragment() {
        // Required empty public constructor
        //Ajay Paul
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_billing, container, false);
    }
}