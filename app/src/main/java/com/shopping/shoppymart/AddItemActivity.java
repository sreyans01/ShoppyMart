package com.shopping.shoppymart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shopping.shoppymart.ModelClasses.ProductDetails;
import com.soundcloud.android.crop.Crop;
import com.soundcloud.android.crop.CropImageActivity;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class AddItemActivity extends AppCompatActivity {

    public static final int REQUEST_READSTORAGE = 9998;
    public static final int REQUEST_WRITESTORAGE = 9997;
    private Context context = AddItemActivity.this;
    private Button addItemBtn;
    private ImageButton addImageBtn;
    private RadioGroup rg_season,rg_basicTaste,rg_productClassification,rg_productCategory,rg_region;
    private ProductDetails productDetails;
    private FirebaseUser user;
    private String username;
    private FirebaseFirestore db;
    private GoogleSignInAccount account;
    private Uri uriImage;
    private int imageCount=0;
    private Uri[] uris;
    private int mainImageIndex=0;

    private ImageView mainImage,img[];
    private EditText e_dateOfManufacture;
    private String sellerProductUIDs="";
    private long sellerProductsCount=0;

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
        uris = new Uri[3];


        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITESTORAGE);
        
        loadData();

    }

    private void allfindviewbyids() {

        rg_season= findViewById(R.id.rg_season);
        rg_basicTaste= findViewById(R.id.rg_basicTaste);
        rg_productClassification= findViewById(R.id.rg_productClassification);
        rg_productCategory= findViewById(R.id.rg_productCategory);
        rg_region = findViewById(R.id.rg_region);
        addItemBtn = findViewById(R.id.addItemBtn);
        addImageBtn = findViewById(R.id.addImageBtn);
        mainImage = findViewById(R.id.mainImage);
        img = new ImageView[3];
        img[0] = findViewById(R.id.img1);
        img[1] = findViewById(R.id.img2);
        img[2] = findViewById(R.id.img3);
        e_dateOfManufacture = findViewById(R.id.e_dateOfManufacture);
    }
    private void HandleOnClicks(){

        for(int i=0;i<3;i++){
            int finalI = i;
            img[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainImage.setImageURI(null);
                    mainImage.setImageURI(uris[finalI]);

                }
            });
        }
        e_dateOfManufacture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(e_dateOfManufacture);
            }
        });

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

                productDetails.setSellerId(username);
                productDetails.setProductName(getTextFromEditText(R.id.e_productname));
                productDetails.setProductPrice(Integer.parseInt(getTextFromEditText(R.id.e_price)));
                productDetails.setProductDateOfManufacture(getTextFromEditText(R.id.e_dateOfManufacture));
                productDetails.setProductDistrictOfOrigin(getTextFromEditText(R.id.e_districtOfOrigin));
                productDetails.setProductBasicIngredients(getTextFromEditText(R.id.e_basicIngredients));
                productDetails.setProductDescription(getTextFromEditText(R.id.e_productDescription));
                productDetails.setProductStorageTips(getTextFromEditText(R.id.e_storageTips));
                productDetails.setProductImageCount(imageCount);

                try {
                    productDetails.setProductShelfLife(Integer.parseInt(getTextFromEditText(R.id.e_shelfLife)));
                }catch (Exception e){
                    productDetails.setProductShelfLife(-1);
                }


                String collectionName = "Products";
                String uid = db.collection(collectionName).document().getId();
                final String[] path = {"Products/" + uid};
                productDetails.setProductUID(uid);

                if(imageCount!=0){
                    for(int i=0;i<imageCount;i++){

                        uploadImage(uris[i], uid + "_" + String.valueOf(i), new ImageUploadCallback() {
                            @Override
                            public void onImageUploaded(int imagesUploaded) {
                                if(imagesUploaded==imageCount){

                                    pushToFirebase(productDetails, path[0],true);
                                    path[0] = "Sellers/"+username;
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("myProducts",sellerProductUIDs+"%="+uid);
                                    map.put("productsCount",sellerProductsCount+1);
                                    pushToFirebase(map, path[0],false);

                                    finish();
                                }
                            }
                        });
                    }
                }



            }
        });

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageCount==3){
                    Toast.makeText(context,"You have already added the maximum number of images",Toast.LENGTH_SHORT).show();
                    return;
                }
                pickImage(context,Crop.REQUEST_PICK);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "IMAGE_"+String.valueOf(imageCount)));
        Uri imageUri;
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            try {

                Uri uri = data.getData();
                Crop.of(uri, destinationUri).start((Activity) context);

            } catch (Exception e) {

                //Because if the Crop library has some issues, then my default picking works.
                try {
                    mainImage.setImageURI(null);
                    mainImage.setImageURI(imageUri);
                    uriImage = imageUri;
                    uris[imageCount] = imageUri;

                    imageCount++;
                    setImage();

                } catch (Exception e1) {
                    Toast.makeText(context, "Please choose a valid image file", Toast.LENGTH_LONG).show();
                }
            }

        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                //uploadImage(Crop.getOutput(data));
                imageUri = Crop.getOutput(data);
                mainImage.setImageURI(null);
                mainImage.setImageURI(imageUri);
                uriImage = imageUri;
                uris[imageCount] = imageUri;
                imageCount++;
                setImage();

            } catch (Exception e) {
                Toast.makeText(context, "Unexpected error occured, please contact the developer.", Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void setImage(){


        for(int i=0;i<imageCount;i++){
            if(uris[i]!=null){
                Log.i("KLKLKL",String.valueOf(i)+"       "+uris[i].toString());
                img[i].setImageURI(null);
                img[i].setImageURI(uris[i]);
            }
        }
    }
    public void chooseImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select picture "), Crop.REQUEST_PICK);
    }

    public void pickImage(Context context,int request_code){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Toast.makeText(getApplicationContext(),"Storage permission is required to access the files.\nPlease grant the permission first",Toast.LENGTH_LONG).show();

                Toast.makeText(context,"Please grant storage permission and try again.",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request_code);

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                //Toast.makeText(context, "Storage permission required to choose photo", Toast.LENGTH_LONG).show();

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request_code);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            // Permission is not granted
        } else {
            try {
                chooseImage();
            } catch (Exception e) {
                Toast.makeText(context, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private int imagesUploadedCurrently=0;

    public interface ImageUploadCallback{
        void onImageUploaded(int imagesUploaded);
    }
    private void uploadImage(Uri uri,String uidfilename,ImageUploadCallback callback){

        StorageReference imagesRef = FirebaseStorage.getInstance().getReference();
        imagesRef = imagesRef.child("images/" + "Product"+"/"+uidfilename);

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading images "+"...");
        progressDialog.show();
        UploadTask uploadTask = imagesRef.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progressDialog.dismiss();
                Toast.makeText(context, "Upload Failed " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                progressDialog.dismiss();
                imagesUploadedCurrently++;
                callback.onImageUploaded(imagesUploadedCurrently);
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                //dp_ownerteacher.setImageURI(downloadUrl);
                //  imageView.setImageURI(downloadUrl);

                //  pushToFirebase(uploadImagedetails);

                // Do what you want
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded images"+"..." + (int) progress + "%");
            }
        });

    }

    public void setDate(EditText editText){



        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                StringBuilder builder=new StringBuilder();;

                builder.append(dayOfMonth+"/");
                builder.append((month + 1)+"/");//month is 0 based
                builder.append(year);

                editText.setText(builder.toString());

            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.show();
    }
    
    private void loadData(){
        
        db.collection("Sellers").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
           
                if(documentSnapshot!=null){
                    try {
                        Map<String, Object> map = documentSnapshot.getData();
                        sellerProductsCount = Long.parseLong(map.get("productsCount").toString());
                        sellerProductUIDs = map.get("myProducts").toString();

                    }catch (Exception e){}
                    
                }
            }
        });
        
    }

}