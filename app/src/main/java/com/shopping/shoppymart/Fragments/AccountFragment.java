package com.shopping.shoppymart.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shopping.shoppymart.R;

public class AccountFragment extends Fragment {

    View fragmentView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_account,container,false);

        allfindviewbyids();
        HandleOnClicks();

        return fragmentView;
    }

    private void allfindviewbyids(){



    }
    private void HandleOnClicks(){


    }

}
