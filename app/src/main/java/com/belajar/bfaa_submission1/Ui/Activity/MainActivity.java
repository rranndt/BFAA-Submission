package com.belajar.bfaa_submission1.Ui.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.belajar.bfaa_submission1.Adapter.UsersAdapter;
import com.belajar.bfaa_submission1.BuildConfig;
import com.belajar.bfaa_submission1.Model.Users;
import com.belajar.bfaa_submission1.Network.Config;
import com.belajar.bfaa_submission1.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private UsersAdapter adapter;
    private List<Users> users = new ArrayList<>();

    private long secondBackPressed;
    private Toast backToast;
    private RecyclerView recyclerView;
    private LottieAnimationView loading;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecycler();
        getUsers();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        loading = findViewById(R.id.loading);
    }

    private void setupRecycler() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UsersAdapter(users, MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void getUsers() {
        Call<List<Users>> call = Config.getApi().getUsers(BuildConfig.API_KEY);
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(@NotNull Call<List<Users>> call, @NotNull Response<List<Users>> response) {
                try {
                    if (response.body() != null) {
                        adapter = new UsersAdapter(response.body(), MainActivity.this);
                        recyclerView.setAdapter(adapter);
                        loading.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onResponse: ");
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Users>> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView mSearchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            mSearchView.setSearchableInfo((searchManager.getSearchableInfo(getComponentName())));
            mSearchView.setQueryHint(getResources().getString(R.string.enter_name));
            mSearchView.setQuery("", false);
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                    searchIntent.putExtra(BuildConfig.PARCEL, query);
                    startActivity(searchIntent);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (secondBackPressed + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), R.string.back_press, Toast.LENGTH_SHORT);
            backToast.show();
        }
        secondBackPressed = System.currentTimeMillis();
    }
}