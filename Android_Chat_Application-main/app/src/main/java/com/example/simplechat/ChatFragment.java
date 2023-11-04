package com.example.simplechat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simplechat.R;
import com.example.simplechat.adapter.RecentChatRecyclerAdapter;
import com.example.simplechat.model.ChatroomModel;
import com.example.simplechat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    RecentChatRecyclerAdapter adapter;


    public ChatFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyler_view);
        setupRecyclerView();

        return view;
    }

    void setupRecyclerView(){


        try{
            Query query = FirebaseUtil.allChatroomCollectionReference()
                    .whereArrayContains("userIds",FirebaseUtil.currentUserId())
                    .orderBy("lastMessageTimestamp",Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<ChatroomModel> options = new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                    .setQuery(query,ChatroomModel.class).build();

            adapter = new RecentChatRecyclerAdapter(options,getContext());
            recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }catch (Exception e) {
            Log.d("erooooooor", "setupRecyclerView: "+ e);
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }
}