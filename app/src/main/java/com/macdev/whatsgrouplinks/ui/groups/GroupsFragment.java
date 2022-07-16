package com.macdev.whatsgrouplinks.ui.groups;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.macdev.whatsgrouplinks.BottomNavigationActivity;
import com.macdev.whatsgrouplinks.R;
import com.macdev.whatsgrouplinks.adapters.AdapterGroups;

import com.macdev.whatsgrouplinks.databinding.FragmentGroupsBinding;
import com.macdev.whatsgrouplinks.model.ModelGroups;

import java.util.ArrayList;
import java.util.List;

public class GroupsFragment extends Fragment {

    private FragmentGroupsBinding binding;
    private List<ModelGroups> listGroups;
    private RecyclerView recycleGroups;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    AdView mAdView;
    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GroupsViewModel groupsViewModel =
                new ViewModelProvider(this).get(GroupsViewModel.class);

        binding = FragmentGroupsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        swipeRefreshLayout = root.findViewById(R.id.swipeToRefresh);


        mAdView = root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);






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



        recycleGroups = root.findViewById(R.id.recycler_view_groups);
        recycleGroups.setHasFixedSize(true);
        recycleGroups.setLayoutManager(new LinearLayoutManager(getContext()));

        AdapterGroups adapter_unNotify = new AdapterGroups(listGroups, getActivity().getApplicationContext());


        listGroups = new ArrayList<>();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                    listGroups.clear();
                    adapter_unNotify.notifyDataSetChanged();

                    fetchGroups();




            }
        });



            fetchGroups();



        return root;
    }

    private void fetchGroups() {

//        ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setTitle("Loading...");
//        progressDialog.setMessage("Loading groups for you");
//        progressDialog.show();

        swipeRefreshLayout.setRefreshing(true);

        db.collection("Groups").orderBy("CreatedDate", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

//                                Toast.makeText(getContext(), document.getString("profile"), Toast.LENGTH_SHORT).show();

                                ModelGroups modelGroups = new ModelGroups(

                                        document.getString("GroupImage"),
                                        document.getString("GroupName"),
                                        document.getString("GroupCategory"),
                                        document.getString("GroupLink"));

                                listGroups.add(modelGroups);


                            }

                            AdapterGroups adapter_unNotify = new AdapterGroups(listGroups, getActivity().getApplicationContext());
                            recycleGroups.setAdapter(adapter_unNotify);
                            swipeRefreshLayout.setRefreshing(false);
//                            progressDialog.dismiss();


                        } else {
                            swipeRefreshLayout.setRefreshing(false);
//                            progressDialog.dismiss();

                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();


                        }
                    }
                });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}