package com.shopping.shoppymart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.shopping.shoppymart.ModelClasses.ProductDetails;

import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    private Context context = AddItemActivity.this;
    private Button addItemBtn;
    private RadioGroup rg_season,rg_basicTaste,rg_productClassification,rg_productCategory,rg_region;
    private ProductDetails productDetails;
    private FirebaseUser user;
    private String username;
    private FirebaseFirestore db;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        setUpToolbar();
        allfindviewbyids();
        HandleOnClicks();

        try {
            account = GoogleSignIn.getLastSignedInAccount((Activity)context);
            username = account.getEmail();
        }catch (Exception e){
            username = "someone@example.com";
        }
        db = FirebaseFirestore.getInstance();

        productDetails = new ProductDetails();

    }

    private void allfindviewbyids() {

        rg_season= findViewById(R.id.rg_season);
        rg_basicTaste= findViewById(R.id.rg_basicTaste);
        rg_productClassification= findViewById(R.id.rg_productClassification);
        rg_productCategory= findViewById(R.id.rg_productCategory);
        rg_region = findViewById(R.id.rg_region);
        addItemBtn = findViewById(R.id.addItemBtn);
    }
    private void HandleOnClicks(){

        rg_season.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                productDetails.setProductSeason(radioButton.getText().toString());
            }
        });
        rg_basicTaste.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                productDetails.setProductBasicTaste(radioButton.getText().toString());
            }
        });
        rg_productClassification.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                productDetails.setProductClassification(radioButton.getText().toString());
            }
        });
        rg_productCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                productDetails.setProductCategory(radioButton.getText().toString());
            }
        });
        rg_region.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                productDetails.setProductRegion(radioButton.getText().toString());
            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getTextFromEditText(R.id.e_productname).compareTo("")==0){
                    Toast.makeText(context,"Product Name cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(getTextFromEditText(R.id.e_dateOfManufacture).compareTo("")==0){
                    Toast.makeText(context,"Date of Manufacture must be specified",Toast.LENGTH_SHORT).show();
                    return;
                    //Hello
                }
                if(getTextFromEditText(R.id.e_price).compareTo("")==0){
                    Toast.makeText(context,"Product Price cannot be empty",Toast.LENGTH_SHORT).show();
                    return;

                }

                productDetails.setProductName(getTextFromEditText(R.id.e_productname));
                productDetails.setProductPrice(Integer.parseInt(getTextFromEditText(R.id.e_price)));
                productDetails.setProductDateOfManufacture(getTextFromEditText(R.id.e_dateOfManufacture));
                productDetails.setProductDistrictOfOrigin(getTextFromEditText(R.id.e_districtOfOrigin));
                productDetails.setProductBasicIngredients(getTextFromEditText(R.id.e_basicIngredients));
                productDetails.setProductDescription(getTextFromEditText(R.id.e_productDescription));
                productDetails.setProductStorageTips(getTextFromEditText(R.id.e_storageTips));
                try {
                    productDetails.setProductShelfLife(Integer.parseInt(getTextFromEditText(R.id.e_shelfLife)));
                }catch (Exception e){
                    productDetails.setProductShelfLife(-1);
                }


                String collectionName = "Products";
                String id = db.collection(collectionName).document().getId();
                String path = "Products/"+id;

                pushToFirebase(productDetails,path,true);
                path = "Sellers/"+username;
                Map<String,Object> map = new HashMap<>();
                map.put("MyProducts",id);
                pushToFirebase(map,path,false);

                finish();
            }
        });

    }




    private void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public String getTextFromEditText(int id){

        EditText editText = findViewById(id);

        if(TextUtils.isEmpty(editText.getText())){
            return "";
        }else
            return editText.getText().toString();

    }


    private <T> void pushToFirebase(T object,String path,Boolean showFeedback){

        db.document(path).set(object, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(showFeedback)
                    Toast.makeText(context,"Item added successfully",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(showFeedback)
                    Toast.makeText(context,"Failed to add item: "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}