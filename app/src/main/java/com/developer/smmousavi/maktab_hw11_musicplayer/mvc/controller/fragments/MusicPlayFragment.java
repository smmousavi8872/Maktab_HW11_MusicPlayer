package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.smmousavi.maktab_hw11_musicplayer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicPlayFragment extends Fragment {


  public static final String FRAGMENT_TAG = "music_play_fragment_tag";


  public static MusicPlayFragment newInstance() {

    Bundle args = new Bundle();

    MusicPlayFragment fragment = new MusicPlayFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public MusicPlayFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_music_play, container, false);
  }

}
