package com.shopping.shoppymart.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shopping.shoppymart.AddItemActivity;
import com.shopping.shoppymart.R;

public class SellFragment extends Fragment {

    View fragmentView;
    Button addItemBtn;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_sell,container,false);
        context = getContext();
        allfindviewbyids();
        HandleOnClicks();

        return fragmentView;
    }

    private void allfindviewbyids(){

        addItemBtn = fragmentView.findViewById(R.id.addItemBtn);

    }
    private void HandleOnClicks(){

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AddItemActivity.class);
                startActivity(i);
            }
        });

    }

}
