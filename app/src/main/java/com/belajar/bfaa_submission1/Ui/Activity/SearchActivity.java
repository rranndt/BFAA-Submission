package com.belajar.bfaa_submission1.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.belajar.bfaa_submission1.Adapter.UsersAdapter;
import com.belajar.bfaa_submission1.BuildConfig;
import com.belajar.bfaa_submission1.Model.Users;
import com.belajar.bfaa_submission1.Model.UsersResponse;
import com.belajar.bfaa_submission1.Network.Config;
import com.belajar.bfaa_submission1.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LottieAnimationView loading;
    private LottieAnimationView itemNotFound;
    private ConstraintLayout contraint;

    private List<Users> users = new ArrayList<>();
    private UsersAdapter usersAdapter;
    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getUsersSearch(getIntent().getStringExtra(BuildConfig.PARCEL));

        initViews();
        setupRecycler();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        loading = findViewById(R.id.loading);
        itemNotFound = findViewById(R.id.search_not_found);
        contraint = findViewById(R.id.contraint);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(users, SearchActivity.this);
        recyclerView.setAdapter(usersAdapter);
    }

    private void getUsersSearch(String username) {
        Call<UsersResponse> call = Config.getApi().getUsersResponse(username);
        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(@NotNull Call<UsersResponse> call, @NotNull Response<UsersResponse> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().getTotalCount() > 0) {
                            usersAdapter = new UsersAdapter(response.body().getItems(), SearchActivity.this);
                            recyclerView.setAdapter(usersAdapter);
                            loading.setVisibility(View.GONE);
                        } else {
                            itemNotFound.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            CoordinatorLayout.LayoutParams params =
                                    (CoordinatorLayout.LayoutParams) contraint.getLayoutParams();
                            params.setBehavior(null);
                        }
                    } else {
                        Log.e(TAG, "onResponseGetUsersSearch");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<UsersResponse> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailureGetUsersSearch: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}