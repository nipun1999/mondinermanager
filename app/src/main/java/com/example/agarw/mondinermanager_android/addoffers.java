package com.example.agarw.mondinermanager_android;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class addoffers extends Fragment {


    private EditText titletext, desctext;
    private ImageButton addOffersImageButton;
    private Button addButton, viewButton;
    private String codevalue;
    ProgressDialog mprogress;
    private DatabaseReference databaseOffersRef;
    private final int GALLERY_REQUEST = 2;
    private StorageReference storageOffers;
    private String title;
    private String desc;
    private Uri imageuri;
    private String uri;
    private SharedPreferences restaurantIDpref;

    public addoffers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mview = inflater.inflate(R.layout.fragment_addoffers, container, false);
        restaurantIDpref = getActivity().getSharedPreferences("myPref", Context.
                MODE_WORLD_READABLE);
        codevalue = restaurantIDpref.getString("restaurantID", null);
        titletext = (EditText) (mview.findViewById(R.id.titletxt));
        desctext = (EditText) (mview.findViewById(R.id.descriptiontxt));
        addOffersImageButton = (ImageButton) (mview.findViewById(R.id.offerimage));
        addButton = (Button) (mview.findViewById(R.id.addofferbtn));
        mprogress = new ProgressDialog(getActivity());
        databaseOffersRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(codevalue).child("offers").push();
        storageOffers = FirebaseStorage.getInstance().getReference();
        viewButton = (Button) (mview.findViewById(R.id.viewofferbtn));
        addOffersImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent, GALLERY_REQUEST);
            }

        });
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent offers = new Intent(getActivity(), offers.class);
                offers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                offers.putExtra("codevalue", codevalue);
                startActivity(offers);
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                title = titletext.getText().toString().trim();
                desc = desctext.getText().toString().trim();
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc)) {
                    mprogress.setMessage("Uploading Please Wait");
                    mprogress.show();
                    databaseOffersRef.child("title").setValue(title);
                    databaseOffersRef.child("description").setValue(desc);
                    StorageReference filepath = storageOffers.child("Offer_image").child(imageuri.getLastPathSegment());
                    filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadurl = taskSnapshot.getDownloadUrl();
                            uri = downloadurl.toString();
                            Toast.makeText(getActivity(), "Your offer has been updated", Toast.LENGTH_SHORT).show();
                            databaseOffersRef.child("imageURL").setValue(uri);
                            mprogress.dismiss();

                                sendFCMPush(title,desc,codevalue);


                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
            }


        });
        return mview;
    }

    private void sendFCMPush(String title,String msg,String restid) {

        final String Legacy_SERVER_KEY = "AIzaSyD4kPfNGqZMtAmyFlvueey3bk2GJ94OdVA";
        String FCM_PUSH_URL = "https://fcm.googleapis.com/fcm/send";
//        String token = FCM_RECEIVER_TOKEN;

        JSONObject objData = null;
        JSONObject notifobjData = null;
        JSONObject dataobjData = null;

        try {
            objData = new JSONObject();
            objData.put("to", "/topics/"+restid);
            notifobjData = new JSONObject();
            notifobjData.put("text", msg);
            notifobjData.put("title", title);
            notifobjData.put("img_url", uri);
            objData.put("notification", notifobjData);
//            objData.put("body", msg);
//            objData.put("title", title);//   icon_name image must be there in drawable
//            objData.put("priority", "high");


            //obj.put("priority", "high");
            Log.e("!_@rj@_@@_PASS:>", objData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, FCM_PUSH_URL, objData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            imageuri = data.getData();
            addOffersImageButton.setImageURI(imageuri);
            CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON).start(getActivity());
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == getActivity().RESULT_OK) {
                    Uri resultUri = result.getUri();
                    addOffersImageButton.setImageURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }
        }
    }
}
