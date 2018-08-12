package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment;

public class MusicPlayActivity extends _SingleFragmentActivity {

  public static Intent newIntent(Context orgin) {
    Intent intent = new Intent(orgin, MusicPlayActivity.class);
    return intent;

  }

  @Override
  public Fragment createFragment() {
    return MusicPlayFragment.newInstance();
  }

  @Override
  public String getTag() {
    return MusicPlayFragment.FRAGMENT_TAG;
  }
}
