package com.example.simplechat.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplechat.R;
import com.example.simplechat.activity.UserInfoActivity;
import com.example.simplechat.model.UserModel;
import com.example.simplechat.utils.AndroidUtil;
import com.example.simplechat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SuggestionsUserAdapter extends FirestoreRecyclerAdapter<UserModel, SuggestionsUserAdapter.UserModelViewHolder> {

    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SuggestionsUserAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {

        FirebaseUtil.getOtherProfilePicStorageRef(model.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if(t.isSuccessful()){
                        Uri uri  = t.getResult();
                        AndroidUtil.setProfilePic(context,uri,holder.userImv);
                    }
                });
        holder.itemView.setOnClickListener(v -> {
            //navigate to chat activity
            Intent intent = new Intent(context, UserInfoActivity.class);
            AndroidUtil.passUserIdAsIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
        holder.userNameTxt.setText(model.getUsername());

    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.suggestions_item,parent,false);
        return new SuggestionsUserAdapter.UserModelViewHolder(view);
    }


    class UserModelViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTxt;
        ImageView userImv;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTxt = itemView.findViewById(R.id.user_name_txt);
            userImv = itemView.findViewById(R.id.user_imv);
        }
    }
}
