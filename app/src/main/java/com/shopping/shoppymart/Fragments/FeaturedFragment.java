package com.shopping.shoppymart.Fragments;

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

public class FeaturedFragment extends Fragment {

    View fragmentView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_featured,container,false);

        allfindviewbyids();
        HandleOnClicks();

        return fragmentView;
    }

    private void allfindviewbyids(){


    }
    private void HandleOnClicks(){



    }

}
