package com.example.simplechat;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.simplechat.adapter.SuggestionsUserAdapter;
import com.example.simplechat.databinding.ActivityChatBinding;
import com.example.simplechat.databinding.ActivityUserInfoBinding;
import com.example.simplechat.model.UserModel;
import com.example.simplechat.utils.AndroidUtil;
import com.example.simplechat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class UserInfoActivity extends AppCompatActivity {
    private String userId;
    private UserModel currentUserModel;
    private ActivityUserInfoBinding vb;
    private boolean isFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());
        userId = AndroidUtil.getUserIdAsIntent(getIntent());
        vb.profileProgressBar.setVisibility(View.GONE);
        vb.backBtnDetail.setOnClickListener(v -> {
            onBackPressed();
        });
        Log.d("hihi", "onCreate: " + userId);
        vb.profleChat.setOnClickListener((v) -> {
            UserModel otherUserModel = new UserModel();
            otherUserModel.setUserId(userId);
            otherUserModel.setUsername(currentUserModel.getUsername());
            //navigate to chat activity
            Intent intent = new Intent(this, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent, otherUserModel);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        });
        getUserInfo();
        checkFriendIsExist();

    }

    private void setUpAddFriendBtn() {
        vb.profleAddFriend.setText("Kết bạn");
        vb.profleAddFriend.setBackgroundColor(getResources().getColor(R.color.my_primary));
        vb.profleAddFriend.setOnClickListener((v) -> {
            setInProgress(true);
            addFriend();
        });
    }

    private void setupRemoveFriend() {
        vb.profleAddFriend.setText("Huỷ kết bạn");
        vb.profleAddFriend.setBackgroundColor(getResources().getColor(R.color.light_gray));
        vb.profleAddFriend.setOnClickListener((v) -> {
            setInProgress(true);
            setUpAddFriendBtn();
            removeFriend();
        });
    }

    private void checkFriendIsExist() {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            try {
                if (task.isSuccessful()) {
                    UserModel mainUser = task.getResult().toObject(UserModel.class);
                    if (mainUser.getFriendIds() == null) {
                        setUpAddFriendBtn();
                    } else if (mainUser.getFriendIds().contains(userId)) {
                        setupRemoveFriend();
                    } else {
                        setUpAddFriendBtn();
                    }
                } else {
                    AndroidUtil.showToast(this, "load bạn thất bại!");
                }
            } catch (Exception e) {
                AndroidUtil.showToast(this, "load bạn thất bại!");
            }

        });
    }

    private void removeFriend() {
        try {
            FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserModel mainUser = task.getResult().toObject(UserModel.class);
                    List<String> friends = mainUser.getFriendIds();
//                    if(mainUser.getFriendIds() == null){
//                        friends = new ArrayList<>();
//                    } else {
//                        friends = mainUser.getFriendIds();
//                    }
                    friends.remove(currentUserModel.getUserId());
                    mainUser.setFriendIds(friends);
                    FirebaseUtil.currentUserDetails().set(mainUser).addOnCompleteListener(nextTask -> {
                        setInProgress(false);
                        if (nextTask.isSuccessful()) {
//                            isFriend = false;
                            setUpAddFriendBtn();
                            AndroidUtil.showToast(this, "Xoá bạn thành công!");
                        } else {
                            AndroidUtil.showToast(this, "Xoá bạn thất bại!");
                        }
                    });
                    setInProgress(false);
                } else {
                    setInProgress(false);
                    AndroidUtil.showToast(this, "Xoá bạn thất bại!");
                }
            });
        } catch (Exception e) {
            setInProgress(false);
            AndroidUtil.showToast(this, "Xoá bạn thất bại!" + e.getMessage());
        }
        setInProgress(false);

    }

    private void getUserInfo() {
        try {
            FirebaseUtil.getUserDetails(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    currentUserModel = task.getResult().toObject(UserModel.class);
                    vb.profileName.setText(currentUserModel.getUsername());
                    vb.profileIntro.setText(currentUserModel.getIntro());
                    vb.profilePhone.setText(currentUserModel.getPhone());
                    FirebaseUtil.getOtherProfilePicStorageRef(userId).getDownloadUrl().addOnCompleteListener(t -> {
                        if (t.isSuccessful()) {
                            Uri uri = t.getResult();
                            AndroidUtil.setProfilePic(this, uri, vb.profileImv);
                        }
                    });
                } else {
                    AndroidUtil.showToast(this, "Load user thất bại!");
                }
            });
        } catch (Exception e) {
            AndroidUtil.showToast(this, "Load user thất bại!");
        }
    }

    private void addFriend() {
        try {
            FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserModel mainUser = task.getResult().toObject(UserModel.class);
                    List<String> friends;
                    if (mainUser.getFriendIds() == null) {
                        friends = new ArrayList<>();
                    } else {
                        friends = mainUser.getFriendIds();
                    }
                    friends.add(currentUserModel.getUserId());
                    mainUser.setFriendIds(friends);
                    FirebaseUtil.currentUserDetails().set(mainUser).addOnCompleteListener(nextTask -> {
                        setInProgress(false);
                        if (nextTask.isSuccessful()) {
//                            isFriend = true;
                            setupRemoveFriend();
                            AndroidUtil.showToast(this, "Thêm bạn thành công!");
                        } else {
                            AndroidUtil.showToast(this, "Thêm bạn thất bại!");
                        }
                    });
                    setInProgress(false);
                } else {
                    setInProgress(false);
                    AndroidUtil.showToast(this, "Thêm bạn thất bại!");
                }
            });
        } catch (Exception e) {
            setInProgress(false);
            AndroidUtil.showToast(this, "Thêm bạn thất bại!" + e.getMessage());
        }

    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            vb.profileProgressBar.setVisibility(View.VISIBLE);
            vb.profleAddFriend.setVisibility(View.GONE);
        } else {
            vb.profileProgressBar.setVisibility(View.GONE);
            vb.profleAddFriend.setVisibility(View.VISIBLE);
        }
    }
}