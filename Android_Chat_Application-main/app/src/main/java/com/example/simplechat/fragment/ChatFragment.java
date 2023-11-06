package com.example.simplechat.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simplechat.WrapContentLinearLayoutManager;
import com.example.simplechat.adapter.RecentChatRecyclerAdapter;
import com.example.simplechat.adapter.SuggestionsUserAdapter;
import com.example.simplechat.databinding.FragmentChatBinding;
import com.example.simplechat.model.ChatroomModel;
import com.example.simplechat.model.UserModel;
import com.example.simplechat.utils.AndroidUtil;
import com.example.simplechat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ChatFragment extends Fragment {

    RecyclerView recyclerView, suggestionRecyclerView;
    RecentChatRecyclerAdapter adapter;
    SuggestionsUserAdapter suggestionsUserAdapter;
    private FragmentChatBinding vb;


    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vb = FragmentChatBinding.inflate(inflater, container, false);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        vb.recylerViewSuggestions.setLayoutManager(layoutManager);
        return vb.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupSuggestionRecyclerView();
    }

    private void setupSuggestionRecyclerView() {
        try {
            FirebaseUtil.currentUserDetails().addSnapshotListener((value, error) -> {
                try{
                    UserModel mainUser = value.toObject(UserModel.class);
                    if (mainUser.getFriendIds() != null) {
                        Query query = FirebaseUtil.allUserCollectionReference().whereIn("userId", mainUser.getFriendIds());
                        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel.class).build();
                        suggestionsUserAdapter = new SuggestionsUserAdapter(options, getContext());
                        vb.recylerViewSuggestions.setAdapter(suggestionsUserAdapter);
                        suggestionsUserAdapter.startListening();
                    } else {
                        AndroidUtil.showToast(getContext(), "Không có bạn");
                    }
                }catch (Exception e){
                    Query query = FirebaseUtil.allUserCollectionReference().whereEqualTo("userId", "");
                    FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query, UserModel.class).build();
                    suggestionsUserAdapter = new SuggestionsUserAdapter(options, getContext());
                    vb.recylerViewSuggestions.setAdapter(suggestionsUserAdapter);
                    suggestionsUserAdapter.startListening();
                }

            });
        } catch (Exception e) {
            Log.d("erooooooor", "setupRecyclerView: " + e);
        }
    }

    void setupRecyclerView() {
        try {
            Query query = FirebaseUtil.allChatroomCollectionReference()
                    .whereArrayContains("userIds", FirebaseUtil.currentUserId())
                    .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<ChatroomModel> options = new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                    .setQuery(query, ChatroomModel.class).build();

            adapter = new RecentChatRecyclerAdapter(options, getContext());
            vb.recylerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
            vb.recylerView.setAdapter(adapter);
            adapter.startListening();
        } catch (Exception e) {
            Log.d("erooooooor", "setupRecyclerView: " + e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
        if (suggestionsUserAdapter != null)
            suggestionsUserAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null)
            adapter.stopListening();
        if (suggestionsUserAdapter != null)
            suggestionsUserAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
        if (suggestionsUserAdapter != null)
            suggestionsUserAdapter.notifyDataSetChanged();
    }
}