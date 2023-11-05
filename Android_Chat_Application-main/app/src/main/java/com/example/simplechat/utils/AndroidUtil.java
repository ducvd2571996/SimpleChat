package com.example.simplechat.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.simplechat.model.FeedModel;
import com.example.simplechat.model.UserModel;

public class AndroidUtil {

   public static  void showToast(Context context,String message){
       Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel model){
       intent.putExtra("username",model.getUsername());
       intent.putExtra("phone",model.getPhone());
       intent.putExtra("userId",model.getUserId());
        intent.putExtra("fcmToken",model.getFcmToken());

    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        userModel.setFcmToken(intent.getStringExtra("fcmToken"));
        return userModel;
    }

    public static void passFeedModelAsIntent(Intent intent, FeedModel model){
        intent.putExtra("title",model.getTitle());
        intent.putExtra("description",model.getDescription());
        intent.putExtra("short_description",model.getShort_description());
        intent.putExtra("image",model.getImage());
        intent.putExtra("date",model.getDate());
        intent.putExtra("author",model.getAuthor());
        intent.putExtra("extra_image", model.getExtra_image());

    }

    public static FeedModel getFeedModelDetail(Intent intent){
        FeedModel feedModel = new FeedModel();
        feedModel.setTitle(intent.getStringExtra("title"));
        feedModel.setDescription(intent.getStringExtra("description"));
        feedModel.setAuthor(intent.getStringExtra("author"));
        feedModel.setDate(intent.getStringExtra("date"));
        feedModel.setShort_description(intent.getStringExtra("short_description"));
        feedModel.setImage(intent.getStringExtra("image"));
        feedModel.setExtra_image(intent.getStringExtra("extra_image"));
        return feedModel;
    }

    public static void setProfilePic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }

    public static void setNewsPic(Context context, Uri imageUri, ImageView imageView){
        Glide.with(context).load(imageUri).into(imageView);
    }

}
