package com.example.agarw.mondinermanager_android;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Adddetails extends Fragment {


    public Adddetails() {
        // Required empty public constructor
    }

    private DatabaseReference databaseInfoRef;
    private EditText nametext, addresstext, lattext, lontext, openhrstext, closedaystext, descriptiontext, cuisinetext, contacttext, telephonetext;
    private Button editBtn;
    private String codevalue;
    private ImageButton logoImageButton;
    private Uri imageuri = null;
    private final int GALLERY_REQUEST = 2;
    private StorageReference storageLogo;
    private ProgressDialog mprogress;
    private SharedPreferences restaurantIDpref;
    private DatabaseReference databaseOwnerRef;
    private FirebaseAuth mAuth;
    private Button saveBtn;
    private ScrollView mScrollView;
    private IntentHandle intentHandle = new IntentHandle();
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_adddetails, container, false);
        nametext = (EditText)(rootview.findViewById(R.id.nametext));
        addresstext = (EditText)(rootview.findViewById(R.id.addresstext));
        lattext = (EditText)(rootview.findViewById(R.id.lattitudetext));
        lontext = (EditText)(rootview.findViewById(R.id.longitudetext));
        openhrstext = (EditText)(rootview.findViewById(R.id.openhourstext));
        closedaystext = (EditText)(rootview.findViewById(R.id.closedaystext));
        descriptiontext = (EditText)(rootview.findViewById(R.id.descriptiontext));
        cuisinetext = (EditText)(rootview.findViewById(R.id.servestext));
        contacttext = (EditText)(rootview.findViewById(R.id.contacttext));
        telephonetext = (EditText)(rootview.findViewById(R.id.telephonetext));
        editBtn = (Button)(rootview.findViewById(R.id.editBtn));
        logoImageButton = (ImageButton)(rootview.findViewById(R.id.logoimagebutton));
        mAuth = FirebaseAuth.getInstance();
        mprogress = new ProgressDialog(getActivity());
        saveBtn = (Button)(rootview.findViewById(R.id.saveBtn));
        mScrollView = (ScrollView)(rootview.findViewById(R.id.mScrollView)) ;
        logoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        getActivity(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            12345
                    );

                }
                Intent galleryIntent = intentHandle.getPickImageIntent(getActivity());
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }

        });

        restaurantIDpref = getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
        databaseOwnerRef = FirebaseDatabase.getInstance().getReference().child("owners");
        databaseInfoRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue);
        storageLogo = FirebaseStorage.getInstance().getReference();

            saveBtn.setVisibility(View.INVISIBLE);
            nametext.setEnabled(false);
            addresstext.setEnabled(false);
            lattext.setEnabled(false);
            lontext.setEnabled(false);
            openhrstext.setEnabled(false);
            closedaystext.setEnabled(false);
            descriptiontext.setEnabled(false);
            cuisinetext.setEnabled(false);
            contacttext.setEnabled(false);
            telephonetext.setEnabled(false);
            logoImageButton.setEnabled(false);
            mprogress.setMessage("Loading");
            mprogress.show();
            databaseInfoRef.child("info").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    nametext.setText(dataSnapshot.child("nametxt").getValue().toString());
                    addresstext.setText(dataSnapshot.child("address").getValue().toString());
                    lattext.setText(dataSnapshot.child("lat").getValue().toString());
                    lontext.setText(dataSnapshot.child("lon").getValue().toString());
                    openhrstext.setText(dataSnapshot.child("openhrs").getValue().toString());
                    closedaystext.setText(dataSnapshot.child("closedays").getValue().toString());
                    descriptiontext.setText(dataSnapshot.child("description").getValue().toString());
                    cuisinetext.setText(dataSnapshot.child("servesCuisine").getValue().toString());
                    contacttext.setText(dataSnapshot.child("contact").getValue().toString());
                    telephonetext.setText(dataSnapshot.child("telephone").getValue().toString());
                    String logouri = dataSnapshot.child("imageURL").getValue().toString();
                    Glide.with(getActivity()).load(logouri).into(logoImageButton);
                    mprogress.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//

                    nametext.setEnabled(true);
                    addresstext.setEnabled(true);
                    lattext.setEnabled(true);
                    lontext.setEnabled(true);
                    openhrstext.setEnabled(true);
                    closedaystext.setEnabled(true);
                    descriptiontext.setEnabled(true);
                    cuisinetext.setEnabled(true);
                    contacttext.setEnabled(true);
                    telephonetext.setEnabled(true);
                    logoImageButton.setEnabled(true);
                    mScrollView.fullScroll(ScrollView.FOCUS_UP);
                    saveBtn.setVisibility(View.VISIBLE);
                    editBtn.setVisibility(View.INVISIBLE);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nametext.getText().toString().trim();
                String address = addresstext.getText().toString().trim();
                String lat = lattext.getText().toString().trim();
                String lon = lontext.getText().toString().trim();
                String openhrs = openhrstext.getText().toString().trim();
                String closedays = closedaystext.getText().toString().trim();
                String description = descriptiontext.getText().toString().trim();
                String cuisine = cuisinetext.getText().toString().trim();
                String contact = contacttext.getText().toString().trim();
                String telephone = telephonetext.getText().toString().trim();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)
                        && !TextUtils.isEmpty(openhrs) && !TextUtils.isEmpty(closedays) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(cuisine)
                        && !TextUtils.isEmpty(contact) && !TextUtils.isEmpty(telephone) && imageuri!=null){

                    mprogress.setMessage("Uploading Please Wait");
                    mprogress.show();
                    databaseInfoRef.child("info").child("nametxt").setValue(name);
                    databaseInfoRef.child("info").child("address").setValue(address);
                    databaseInfoRef.child("info").child("lat").setValue(lat);
                    databaseInfoRef.child("info").child("lon").setValue(lon);
                    databaseInfoRef.child("info").child("openhrs").setValue(openhrs);
                    databaseInfoRef.child("info").child("closedays").setValue(closedays);
                    databaseInfoRef.child("info").child("description").setValue(description);
                    databaseInfoRef.child("info").child("servesCuisine").setValue(cuisine);
                    databaseInfoRef.child("info").child("contact").setValue(contact);
                    databaseInfoRef.child("info").child("telephone").setValue(telephone);


                    StorageReference filepath = storageLogo.child("Restaurant_logo").child(imageuri.getLastPathSegment());

                    filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            Uri downloadurl = taskSnapshot.getDownloadUrl();
                            String uri = downloadurl.toString();
                            Toast.makeText(getActivity(),"Your profile has been updated",Toast.LENGTH_SHORT).show();
                            databaseInfoRef.child("info").child("imageURL").setValue(uri);
                            mprogress.dismiss();
                            saveBtn.setVisibility(View.INVISIBLE);
                            editBtn.setVisibility(View.VISIBLE);
                            saveBtn.setVisibility(View.INVISIBLE);
                            nametext.setEnabled(false);
                            addresstext.setEnabled(false);
                            lattext.setEnabled(false);
                            lontext.setEnabled(false);
                            openhrstext.setEnabled(false);
                            closedaystext.setEnabled(false);
                            descriptiontext.setEnabled(false);
                            cuisinetext.setEnabled(false);
                            contacttext.setEnabled(false);
                            telephonetext.setEnabled(false);
                            logoImageButton.setEnabled(false);
                        }
                    });

                }else{
                    Toast.makeText(getActivity(),"Please enter all the fields",Toast.LENGTH_SHORT).show();
                }



            }
        });

        return rootview;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_REQUEST && resultCode == RESULT_OK) {

            imageuri = intentHandle.getPickImageResultUri(data);

            CropImage.activity(imageuri)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(getActivity());
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(getActivity(), "Crop opening", Toast.LENGTH_SHORT).show();
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    Toast.makeText(getActivity(), "Result Ok", Toast.LENGTH_SHORT).show();
                    try{

                        Toast.makeText(getActivity(), "Try fine", Toast.LENGTH_SHORT).show();
                        imageuri = result.getUri();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageuri);
                        Double ratio = ((double) bitmap.getWidth()) / bitmap.getHeight();

                        if (bitmap.getByteCount() > 350000) {

                            bitmap = Bitmap.createScaledBitmap(bitmap, 960, (int) (960 / ratio), false);
                        }
                        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, imageuri.getLastPathSegment(), null);
                        imageuri = Uri.parse(path);
                        logoImageButton.setImageURI(imageuri);
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }

        }
}
