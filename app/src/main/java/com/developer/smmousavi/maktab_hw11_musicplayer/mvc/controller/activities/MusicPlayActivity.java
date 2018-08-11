package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities;

import android.support.v4.app.Fragment;

import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment;

public class MusicPlayActivity extends _SingleFragmentActivity {

  @Override
  public Fragment createFragment() {
    return MusicPlayFragment.newInstance();
  }

  @Override
  public String getTag() {
    return MusicPlayFragment.FRAGMENT_TAG;
  }
}
