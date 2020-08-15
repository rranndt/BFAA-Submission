package com.belajar.bfaa_submission1.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.belajar.bfaa_submission1.BuildConfig;
import com.belajar.bfaa_submission1.Model.Users;
import com.belajar.bfaa_submission1.R;
import com.belajar.bfaa_submission1.Ui.Activity.DetailActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<Users> usersList;
    private Users users;
    private Activity context;

    public UsersAdapter(List<Users> usersList, Activity context) {
        this.usersList = usersList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_main, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
        users = usersList.get(position);

        holder.bind(usersList.get(position));
        holder.cvContainer.setOnClickListener(view -> {
            Intent intentDetails = new Intent(view.getContext(), DetailActivity.class);
            intentDetails.putExtra(BuildConfig.PARCEL, users);
            view.getContext().startActivity(intentDetails);
            context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private CardView cvContainer;
        private ImageView ivAvatar;
        private TextView tvUsername, tvHtmlUrl;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            cvContainer = itemView.findViewById(R.id.cvContainer);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvHtmlUrl = itemView.findViewById(R.id.tv_html_url);
        }

        private void bind(Users users) {
            Glide.with(itemView.getContext())
                    .load(users.getAvatarUrl())
                    .apply(new RequestOptions().override(55, 55))
                    .into(ivAvatar);
            tvUsername.setText(users.getLogin());
            tvHtmlUrl.setText(users.getHtmlUrl());
        }
    }
}
