package com.example.simplechat.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simplechat.R;
import com.example.simplechat.WrapContentLinearLayoutManager;
import com.example.simplechat.adapter.FeedRecyclerAdapter;
import com.example.simplechat.adapter.RecentChatRecyclerAdapter;
import com.example.simplechat.model.ChatroomModel;
import com.example.simplechat.model.FeedModel;
import com.example.simplechat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class FeedFragment extends Fragment {

    RecyclerView recyclerView;
    FeedRecyclerAdapter adapter;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = view.findViewById(R.id.recyler_view_feed);
        setupRecyclerView();
        return view;
    }

    void setupRecyclerView(){


        try{
            Query query = FirebaseUtil.allNewsReference();
            FirestoreRecyclerOptions<FeedModel> options = new FirestoreRecyclerOptions.Builder<FeedModel>()
                    .setQuery(query,FeedModel.class).build();

            adapter = new FeedRecyclerAdapter(options,getContext());
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