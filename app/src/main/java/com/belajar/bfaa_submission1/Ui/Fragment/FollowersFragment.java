package com.belajar.bfaa_submission1.Ui.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.belajar.bfaa_submission1.Adapter.UsersAdapter;
import com.belajar.bfaa_submission1.BuildConfig;
import com.belajar.bfaa_submission1.Model.Users;
import com.belajar.bfaa_submission1.Network.Config;
import com.belajar.bfaa_submission1.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersFragment extends Fragment {

    private UsersAdapter adapter;
    private List<Users> usersList = new ArrayList<>();
    private Users users;

    private View view;
    private RecyclerView recyclerView;
    private LottieAnimationView itemNotFound;
    private static final String TAG = "FollowersFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_followers, container, false);

        users = Objects.requireNonNull(getActivity()).getIntent().getParcelableExtra(BuildConfig.PARCEL);
        itemNotFound = view.findViewById(R.id.followers_not_found);
        setupRecycler();
        getUserFollower();

        return view;
    }

    private void setupRecycler() {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UsersAdapter(usersList, getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void getUserFollower() {
        Call<List<Users>> call = Config.getApi().getUserFollowers(users.getLogin());
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(@NotNull Call<List<Users>> call, @NotNull Response<List<Users>> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().isEmpty()) {
                            itemNotFound.setVisibility(View.VISIBLE);
                        } else {
                            adapter = new UsersAdapter(response.body(), getActivity());
                            recyclerView.setAdapter(adapter);
                        }
                    } else {
                        Log.e(TAG, "onResponseGetUserFollowers");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Users>> call, @NotNull Throwable t) {
                Log.e(TAG, "onFailureGetUserFollowers: " + t.getMessage());
            }
        });
    }
}