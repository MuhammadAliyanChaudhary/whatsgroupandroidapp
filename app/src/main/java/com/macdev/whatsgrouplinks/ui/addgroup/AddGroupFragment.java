package com.macdev.whatsgrouplinks.ui.addgroup;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.macdev.whatsgrouplinks.BottomNavigationActivity;
import com.macdev.whatsgrouplinks.R;
import com.macdev.whatsgrouplinks.databinding.FragmentAddgroupBinding;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddGroupFragment extends Fragment {


    // Declaring Views and their variables

    EditText groupName, groupLink;
    Button addGroup;
    LinearLayout uploadImageBox;
    String matchingWhatsappLink;
    String spinnerValue = null;

    AdView mAdView;

    // Firebase Variables

    FirebaseFirestore db;
    FirebaseStorage storage;
    Uri path;

    // progress Dialogs
    ProgressDialog progressDialog;


    // Fragment view creation started....

    private FragmentAddgroupBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddGroupViewModel addGroupViewModel =
                new ViewModelProvider(this).get(AddGroupViewModel.class);

        binding = FragmentAddgroupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Finding views by ID's

        groupLink = root.findViewById(R.id.group_link_editText);
        groupName = root.findViewById(R.id.group_name_editText);
        addGroup = root.findViewById(R.id.btn_addGroup);
        uploadImageBox = root.findViewById(R.id.Upload_photo);



        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        // Initialize Firebase variables and get instance

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();


        // Progress dialog object creation
        progressDialog = new ProgressDialog(getActivity());






        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();

            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                super.onAdClosed();
            }
        });


        // Spinner items List declaring in Array

        String[] dropDownItems = new String[]{
                "Friendship","Islamic" ,"Business", "Gaming" ,"Education" , "Actress", "Entertainment",
                "Funny" , "Tiktok",  "Food", "Pet", "Traveling","Online Earning" , "Love & Dating"
        };


        // Adapter making for spinner items array

        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.drop_down_item,
                dropDownItems
        );

        AutoCompleteTextView autoCompleteTextView = root.findViewById(R.id.filed_exposed);

        autoCompleteTextView.setAdapter(itemAdapter);

        //Setting on click listeners

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                spinnerValue = autoCompleteTextView.getText().toString();
            }
        });


        uploadImageBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // open image from gallery and select image
                launcher.launch("image/*");


            }
        });


        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // validate the empty fields
                validate();

            }
        });


        // returning root to complete view creation
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    // Function for validating empty fields and correct link
    private void validate() {


        matchingWhatsappLink = "https://chat.whatsapp.com/";

        if(!isNetworkConnected()){

            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();

        }




        else if (groupName.getText().toString().isEmpty()) {

            Toast.makeText(getActivity(), "Enter Group Name", Toast.LENGTH_SHORT).show();
            groupName.requestFocus();
        } else if (groupLink.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Enter Group Link", Toast.LENGTH_SHORT).show();
            groupLink.requestFocus();


        } else if (groupLink.getText().toString().trim().length() < 26) {
            Toast.makeText(getActivity(), "Invalid Group Link", Toast.LENGTH_SHORT).show();
            groupLink.requestFocus();
        } else if (!groupLink.getText().toString().trim().substring(0, 26).equals(matchingWhatsappLink.substring(0, 26))) {

            Toast.makeText(getActivity(), "Invalid Link", Toast.LENGTH_SHORT).show();
            groupLink.requestFocus();
        } else if (spinnerValue == null) {
            Toast.makeText(getActivity(), "Please Select category", Toast.LENGTH_SHORT).show();
        } else {

            uploadImage();

        }


    }


    // Function for uploading Image to storage and setting link to database
    private void uploadImage() {


        // Setting dialog

        progressDialog.setTitle("Adding Group...");
        progressDialog.setMessage("Uploading your group data...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();


        if (path != null) {

            final StorageReference reference = storage.getReference().child("images/" + UUID.randomUUID().toString());


            reference.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            addGroupToDb(groupLink.getText().toString().trim(), groupName.getText().toString(), spinnerValue, uri.toString());


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();

                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        } else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please Upload Image", Toast.LENGTH_SHORT).show();
        }


    }


    // Function for uploading group data in database document

    private void addGroupToDb(String groupLink, String groupName, String category, String downloadLink) {


        Date date = new Date();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("GroupLink", groupLink);
        userMap.put("GroupName", groupName);
        userMap.put("GroupCategory", category);
        userMap.put("GroupImage", downloadLink);
        userMap.put("CreatedDate", date);

        db.collection("Groups")
                .document().set(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            reloadFragment();
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Group Added successfully", Toast.LENGTH_LONG).show();

                        } else {

                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }
        });


    }


    // Function for setting image to imageview from gallery

    ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {


                    if (uri != null) {
                        binding.imageGroup.setImageURI(uri);
                        path = uri;
                    }


                }
            });


    // Function for reloading fragment
    public void reloadFragment() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(AddGroupFragment.this.getId(), new AddGroupFragment()).commit();
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



}


