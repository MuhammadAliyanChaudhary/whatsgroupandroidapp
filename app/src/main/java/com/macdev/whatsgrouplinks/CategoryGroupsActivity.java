package com.macdev.whatsgrouplinks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.macdev.whatsgrouplinks.adapters.AdapterGroups;
import com.macdev.whatsgrouplinks.model.ModelGroups;

import java.util.ArrayList;
import java.util.List;

public class CategoryGroupsActivity extends AppCompatActivity {

    private List<ModelGroups> listGroups;
    private RecyclerView recycleGroups;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_groups);


        db= FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        toolbar = findViewById(R.id.toolbarCategory);


        swipeRefreshLayout = findViewById(R.id.swipeToRefreshCategory);

        String categoryNameNow = getIntent().getStringExtra("CategoryOfGroup");



        setSupportActionBar(toolbar); //NO PROBLEM !!!!
        toolbar.setTitle(categoryNameNow+" Groups");
       toolbar.getMenu();
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        recycleGroups= findViewById(R.id.recycler_view_groups_category);
        recycleGroups.setHasFixedSize(true);
        recycleGroups.setLayoutManager(new LinearLayoutManager(this));

        listGroups = new ArrayList<>();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchGroups(categoryNameNow);

            }
        });




       fetchGroups(categoryNameNow);
    }

    private void fetchGroups(String categoryName) {

//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Loading...");
//        progressDialog.setMessage("Loading groups for you");
//        progressDialog.show();

        swipeRefreshLayout.setRefreshing(true);


        db.collection("Groups").whereEqualTo("GroupCategory", categoryName).orderBy("CreatedDate", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot document : task.getResult() ){

//                                Toast.makeText(getContext(), document.getString("profile"), Toast.LENGTH_SHORT).show();

                                ModelGroups modelGroups =  new ModelGroups(

                                        document.getString("GroupImage"),
                                        document.getString("GroupName"),
                                        document.getString("GroupCategory"),
                                        document.getString("GroupLink"));

                                listGroups.add(modelGroups);






                            }

                            AdapterGroups adapter_unNotify = new AdapterGroups(listGroups,CategoryGroupsActivity.this);
                            recycleGroups.setAdapter(adapter_unNotify);
                            swipeRefreshLayout.setRefreshing(false);
//                            progressDialog.dismiss();



                        }
                        else {

//                            progressDialog.dismiss();
                                swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(CategoryGroupsActivity.this, "Error", Toast.LENGTH_SHORT).show();


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(CategoryGroupsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}