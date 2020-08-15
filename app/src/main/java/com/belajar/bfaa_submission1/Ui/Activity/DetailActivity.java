package com.belajar.bfaa_submission1.Ui.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.belajar.bfaa_submission1.Adapter.ViewPagerAdapter;
import com.belajar.bfaa_submission1.BuildConfig;
import com.belajar.bfaa_submission1.Model.Users;
import com.belajar.bfaa_submission1.Model.UsersDetail;
import com.belajar.bfaa_submission1.Network.Config;
import com.belajar.bfaa_submission1.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private ImageView ivAvatar;
    private TextView tvUsername,
            tvType,
            tvLocation,
            tvCompany,
            tvHtmlUrl,
            tvTotalFollowers,
            tvTotalFollowing,
            tvTotalRepository;
    private ShimmerFrameLayout shimmerContainer;

    private Users users;
    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        users = getIntent().getParcelableExtra(BuildConfig.PARCEL);

        initViews();
        getDetailUsers();
    }

    private void initViews() {
        ivAvatar = findViewById(R.id.iv_avatar);
        tvUsername = findViewById(R.id.tv_username);
        tvType = findViewById(R.id.tv_type);
        tvLocation = findViewById(R.id.tv_location);
        tvCompany = findViewById(R.id.tv_company);
        tvHtmlUrl = findViewById(R.id.tv_html_url);
        tvTotalFollowers = findViewById(R.id.total_followers);
        tvTotalFollowing = findViewById(R.id.total_following);
        tvTotalRepository = findViewById(R.id.total_repository);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager_recycler);
        shimmerContainer = findViewById(R.id.shimmer_container);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(users.getLogin());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getDetailUsers() {
        Call<UsersDetail> call = Config.getApi().getUsersDetail(users.getLogin());
        call.enqueue(new Callback<UsersDetail>() {
            @Override
            public void onResponse(@NotNull Call<UsersDetail> call, @NotNull Response<UsersDetail> response) {
                try {
                    shimmerContainer.setVisibility(View.GONE);
                    if (response.body() != null) {
                        Glide.with(getApplicationContext())
                                .load(response.body().getAvatarUrl())
                                .apply(new RequestOptions().override(85, 85))
                                .into(ivAvatar);
                        tvUsername.setText(response.body().getLogin());
                        tvType.setText(response.body().getType());

                        if (response.body().getLocation() == null && response.body().getCompany() == null) {
                            tvLocation.setVisibility(View.GONE);
                            tvCompany.setVisibility(View.GONE);
                        } else if (response.body().getLocation() == null || response.body().getCompany() == null) {
                            if (response.body().getLocation() == null) {
                                tvLocation.setVisibility(View.GONE);
                                tvCompany.setText(response.body().getCompany());
                            } else if (response.body().getCompany() == null) {
                                tvCompany.setVisibility(View.GONE);
                                tvLocation.setText(response.body().getLocation());
                            }
                        } else {
                            tvLocation.setText(response.body().getLocation());
                            tvCompany.setText(response.body().getCompany());
                        }

                        tvHtmlUrl.setText(response.body().getHtmlUrl());
                        tvHtmlUrl.setOnClickListener(view -> {
                            Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                            intentUrl.setData(Uri.parse(users.getHtmlUrl()));
                            startActivity(intentUrl);
                        });

                        tvTotalFollowers.setText(String.valueOf(response.body().getFollowers()));
                        tvTotalFollowing.setText(String.valueOf(response.body().getFollowing()));
                        tvTotalRepository.setText(String.valueOf(response.body().getPublicRepos()));
                    } else {
                        Log.e(TAG, "onResponseFailedGetDetailUsers");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<UsersDetail> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailureGetDetailUsers: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}