package com.example.simplechat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simplechat.R;
import com.example.simplechat.model.FeedModel;
import com.example.simplechat.utils.AndroidUtil;

public class FeedDetailActivity extends AppCompatActivity {
    FeedModel feedModel;
    TextView detail_title, detail_description, detail_author, detail_date;
    ImageView detail_image, extra_image;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        feedModel = AndroidUtil.getFeedModelDetail(getIntent());
        detail_title = findViewById(R.id.news_detail_title);
        detail_description = findViewById(R.id.news_detail_description);
        detail_date = findViewById(R.id.news_detail_date);
        detail_author = findViewById(R.id.news_detail_author);
        detail_image = findViewById(R.id.news_img_detail);
        extra_image = findViewById(R.id.news_extra_img);
        backButton = findViewById(R.id.back_btn_detail);
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        setupView();
    }

    private void setupView() {
        detail_title.setText(feedModel.getTitle());
        detail_description.setText(feedModel.getDescription());
        detail_date.setText(feedModel.getDate());
        detail_author.setText("Tác giả: " + feedModel.getAuthor());

        Uri imgUri = Uri.parse(feedModel.getImage());
        AndroidUtil.setNewsPic(FeedDetailActivity.this, imgUri, detail_image);

        Uri extraImgUri = Uri.parse(feedModel.getExtra_image());
        AndroidUtil.setNewsPic(FeedDetailActivity.this, extraImgUri, extra_image);
    }
}