package com.example.simplechat.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplechat.ChatActivity;
import com.example.simplechat.FeedDetailActivity;
import com.example.simplechat.R;
import com.example.simplechat.model.ChatroomModel;
import com.example.simplechat.model.FeedModel;
import com.example.simplechat.model.UserModel;
import com.example.simplechat.utils.AndroidUtil;
import com.example.simplechat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FeedRecyclerAdapter extends FirestoreRecyclerAdapter<FeedModel, FeedRecyclerAdapter.FeedModelViewHolder> {

    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FeedRecyclerAdapter(@NonNull FirestoreRecyclerOptions<FeedModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FeedModelViewHolder holder, int position, @NonNull FeedModel model) {
        holder.titleTxt.setText(model.getTitle());
        holder.descriptionTxt.setText(model.getShort_description());
        holder.author.setText("Tác giả: "+model.getAuthor());
        holder.date.setText(model.getDate());
        Uri imgUri = Uri.parse(model.getImage());
        AndroidUtil.setNewsPic(context,imgUri,holder.newImage);

        holder.itemView.setOnClickListener(v -> {
            //navigate to chat activity
            Intent intent = new Intent(context, FeedDetailActivity.class);
            AndroidUtil.passFeedModelAsIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public FeedModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item,parent,false);
        return new FeedRecyclerAdapter.FeedModelViewHolder(view);
    }


    class FeedModelViewHolder extends RecyclerView.ViewHolder{
        TextView titleTxt;
        TextView descriptionTxt;
        TextView date;
        TextView author;
        ImageView newImage;

        public FeedModelViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.news_title);
            descriptionTxt = itemView.findViewById(R.id.news_description);
            date = itemView.findViewById(R.id.news_date);
            author = itemView.findViewById(R.id.news_author);
            newImage = itemView.findViewById(R.id.news_img);
        }
    }
}
