package com.example.simplechat.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.simplechat.App;
import com.example.simplechat.Constant;
import com.example.simplechat.activity.SplashActivity;
import com.example.simplechat.databinding.FragmentProfileBinding;
import com.example.simplechat.model.UserModel;
import com.example.simplechat.utils.AndroidUtil;
import com.example.simplechat.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileFragment extends Fragment {

    UserModel currentUserModel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    private FragmentProfileBinding vb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vb = FragmentProfileBinding.inflate(inflater, container, false);
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.getData() != null) {
                    selectedImageUri = data.getData();
                    AndroidUtil.setProfilePic(App.instant, selectedImageUri, vb.profileImageView);
                }
            }
        });

        getUserData();

        vb.profleUpdateBtn.setOnClickListener((v -> {
            updateBtnClick();
        }));

        vb.logoutBtn.setOnClickListener((v) -> {
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseUtil.logout();
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });


        });

        vb.profileImageView.setOnClickListener((v) -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512).createIntent(new Function1<Intent, Unit>() {
                @Override
                public Unit invoke(Intent intent) {
                    imagePickLauncher.launch(intent);
                    return null;
                }
            });
        });
    }

    void updateBtnClick() {
        String newUsername = vb.profileUsername.getText().toString();
        String newIntro = vb.profileIntro.getText().toString();
        if (newUsername.isEmpty() || newUsername.length() < 3) {
            vb.profileUsername.setError(Constant.MAX_CHARS);
            return;
        }

        if (newIntro.isEmpty() || newIntro.length() < 3) {
            vb.profileIntro.setError(Constant.MAX_CHARS);
            return;
        }

        currentUserModel.setUsername(newUsername);
        currentUserModel.setIntro(newIntro);
        setInProgress(true);


        if (selectedImageUri != null) {
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri).addOnCompleteListener(task -> {
                updateToFirestore();
            });
        } else {
            updateToFirestore();
        }


    }

    void updateToFirestore() {
        FirebaseUtil.currentUserDetails().set(currentUserModel).addOnCompleteListener(task -> {
            setInProgress(false);
            if (task.isSuccessful()) {
                AndroidUtil.showToast(getContext(), "Cập nhật thành công!");
            } else {
                AndroidUtil.showToast(getContext(), "Cập nhật thất bại!");
            }
        });
    }


    void getUserData() {
        setInProgress(true);
        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri uri = task.getResult();
                AndroidUtil.setProfilePic(App.instant, uri, vb.profileImageView);
            }
        });

        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            currentUserModel = task.getResult().toObject(UserModel.class);
            vb.profileUsername.setText(currentUserModel.getUsername());
            vb.profilePhone.setText(currentUserModel.getPhone());
        });
    }


    void setInProgress(boolean inProgress) {
        if (inProgress) {
            vb.profileProgressBar.setVisibility(View.VISIBLE);
            vb.profleUpdateBtn.setVisibility(View.GONE);
        } else {
            vb.profileProgressBar.setVisibility(View.GONE);
            vb.profleUpdateBtn.setVisibility(View.VISIBLE);
        }
    }
}













