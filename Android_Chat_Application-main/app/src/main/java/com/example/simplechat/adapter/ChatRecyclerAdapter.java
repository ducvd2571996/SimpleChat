package com.example.simplechat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.simplechat.databinding.ItemImageLeftBinding;
import com.example.simplechat.databinding.ItemImageRightBinding;
import com.example.simplechat.databinding.ItemTextLeftBinding;
import com.example.simplechat.databinding.ItemTextRightBinding;
import com.example.simplechat.model.ChatMessageModel;
import com.example.simplechat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatMessageModel, ChatRecyclerAdapter.ChatModelViewHolder> {

    Context context;

    public ChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatMessageModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatModelViewHolder holder, int position, @NonNull ChatMessageModel model) {
        if (holder.vb instanceof ItemTextRightBinding) {
            ((ItemTextRightBinding) holder.vb).labelRight.setText(model.getMessage());
        } else if (holder.vb instanceof ItemTextLeftBinding) {
            ((ItemTextLeftBinding) holder.vb).labelLeft.setText(model.getMessage());
        } else if (holder.vb instanceof ItemImageRightBinding) {
            byte[] decodedBytes = Base64.decode(model.getImageBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            ((ItemImageRightBinding) holder.vb).imageRight.setImageBitmap(bitmap);
        } else if (holder.vb instanceof ItemImageLeftBinding) {
            byte[] decodedBytes = Base64.decode(model.getImageBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            ((ItemImageLeftBinding) holder.vb).imageLeft.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isImage() && getItem(position).getSenderId().equals(FirebaseUtil.currentUserId())) {
            return 0;
        } else if (getItem(position).isImage() && !getItem(position).getSenderId().equals(FirebaseUtil.currentUserId())) {
            return 1;
        } else if (!getItem(position).isImage() && getItem(position).getSenderId().equals(FirebaseUtil.currentUserId())) {
            return 2;
        } else if (!getItem(position).isImage() && !getItem(position).getSenderId().equals(FirebaseUtil.currentUserId())) {
            return 3;
        } else {
            return 3;
        }
    }

    @NonNull
    @Override
    public ChatModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            ItemImageRightBinding vb = ItemImageRightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ChatModelViewHolder(vb);
        } else if (viewType == 1) {
            ItemImageLeftBinding vb = ItemImageLeftBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ChatModelViewHolder(vb);
        } else if (viewType == 2) {
            ItemTextRightBinding vb = ItemTextRightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ChatModelViewHolder(vb);
        } else if (viewType == 3) {
            ItemTextLeftBinding vb = ItemTextLeftBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ChatModelViewHolder(vb);
        } else {
            ItemTextLeftBinding vb = ItemTextLeftBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ChatModelViewHolder(vb);
        }
    }

    class ChatModelViewHolder extends RecyclerView.ViewHolder {
        public final ViewBinding vb;

        public ChatModelViewHolder(ViewBinding binding) {
            super(binding.getRoot());
            this.vb = binding;
        }
    }

}
