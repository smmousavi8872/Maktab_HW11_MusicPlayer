package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.smmousavi.maktab_hw11_musicplayer.R;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class MusicMenuFragment extends Fragment {

  public static final String FRAGMENT_TAG = "music_menu_fragment";

  private RecyclerView recyclerView;
  private List<Song> songList;


  public static MusicMenuFragment newInstance() {

    Bundle args = new Bundle();

    MusicMenuFragment fragment = new MusicMenuFragment();
    fragment.setArguments(args);
    return fragment;
  }


  public MusicMenuFragment() {
    // Required empty public constructor
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    songList = new ArrayList<>();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_music_menu, container, false);
    recyclerView = view.findViewById(R.id.music_menu_recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setAdapter(new MusicAdapter());
    return view;
  }


  private class MusicViewHolder extends RecyclerView.ViewHolder {

    public MusicViewHolder(@NonNull View itemView) {
      super(itemView);
    }
  }


  private class MusicAdapter extends RecyclerView.Adapter<MusicViewHolder> {


    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
      return 0;
    }
  }

}
