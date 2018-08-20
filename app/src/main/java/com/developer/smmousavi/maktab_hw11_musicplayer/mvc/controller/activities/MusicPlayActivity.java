package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment;

public class MusicPlayActivity extends _SingleFragmentActivity {

  public static final String EXTRAS_SONG_ID = "com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities.extras_song_id";
  public static final String EXTRAS_ADAPTER_POS = "com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities.extras_adapter_pos";

  public static Intent newIntent(Context orgin, long songId) {
    Intent intent = new Intent(orgin, MusicPlayActivity.class);
    intent.putExtra(EXTRAS_SONG_ID, songId);
    return intent;
  }

  @Override
  public Fragment createFragment() {
    long songId = getIntent().getExtras().getLong(EXTRAS_SONG_ID);
    return MusicPlayFragment.newInstance();
  }

  @Override
  public String getTag() {
    return MusicPlayFragment.FRAGMENT_TAG;
  }
}
