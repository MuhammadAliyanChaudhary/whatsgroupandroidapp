package com.macdev.whatsgrouplinks.ui.categories;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.macdev.whatsgrouplinks.CategoriesModel;
import com.macdev.whatsgrouplinks.CategoryGroupsActivity;
import com.macdev.whatsgrouplinks.GridAdapter;
import com.macdev.whatsgrouplinks.R;
import com.macdev.whatsgrouplinks.adapters.AdapterGroups;

import com.macdev.whatsgrouplinks.databinding.FragmentCategoriesBinding;
import com.macdev.whatsgrouplinks.model.ModelGroups;


public class CategoriesFragment extends Fragment {

    GridView category_grid;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    AdView mAdView;


    private FragmentCategoriesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CategoriesViewModel categoriesViewModel =
                new ViewModelProvider(this).get(CategoriesViewModel.class);

        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        db= FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();


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
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        category_grid = root.findViewById(R.id.category_grid);

        String[] mCategoryNames = { "Friendship","Islamic" ,"Business", "Gaming" ,"Education" , "Actress", "Entertainment",
                "Funny" , "Tiktok",  "Food", "Pet", "Traveling","Online Earning" , "Love & Dating" };

        int[] mCategoryImages = {
                R.drawable.friendship,
                R.drawable.islamic,
                R.drawable.ic_business,
                R.drawable.gaming,
                R.drawable.ic_education,
                R.drawable.actress,
                R.drawable.ic_entertainment,
                R.drawable.ic_funny,
                R.drawable.tiktok,
                R.drawable.ic_food,
                R.drawable.ic_cat,
                R.drawable.ic_traveling,
                R.drawable.money,
                R.drawable.ic_dating
        };

        GridAdapter gridAdapter = new GridAdapter(getActivity(), mCategoryNames, mCategoryImages);

        category_grid.setAdapter(gridAdapter);




        category_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), "" + mCategoryNames[i], Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), CategoryGroupsActivity.class);
                intent.putExtra("CategoryOfGroup", mCategoryNames[i]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);

            }
        });




        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}