package com.shopping.shoppymart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.shopping.shoppymart.Fragments.AccountFragment;
import com.shopping.shoppymart.Fragments.FeaturedFragment;
import com.shopping.shoppymart.Fragments.SellFragment;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context context = MainActivity.this;
    private BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            Fragment fragment = new FeaturedFragment();

            /*
            Use if we want to pass data to fragments from this activity
            //passing serialized object
            Bundle args = new Bundle();
            args.putSerializable(CURR_DETAILS, (Serializable) currentDetails);
            fragment.setArguments(args);

            //passing single data
            Bundle bundle = new Bundle();
            bundle.putString("AliasName",alias_name);
            bundle.putStringArrayList("BatchesList", (ArrayList<String>) allBatcheslist);

             */

            switch (id){

                case R.id.menu_featured:

                    fragment = new FeaturedFragment();

                    break;
                case R.id.menu_sell:
                    fragment = new SellFragment();

                    break;
                case R.id.menu_account:

                    fragment = new AccountFragment();


                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(context);
        allfindviewbyids();
        HandleOnClicks();


        try {
            onNavigationItemSelectedListener.onNavigationItemSelected(bottomNavigationView.getMenu().getItem(0));
        }catch (Exception e){}
    }

    private void allfindviewbyids(){


        bottomNavigationView = findViewById(R.id.bottomNavView);

    }
    private void HandleOnClicks(){

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


    }

}