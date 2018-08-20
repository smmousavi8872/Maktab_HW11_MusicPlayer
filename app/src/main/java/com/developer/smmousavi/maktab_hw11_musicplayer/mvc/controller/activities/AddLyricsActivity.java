package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.AddLyricsFragment;

public class AddLyricsActivity extends _SingleFragmentActivity {

  public static final String EXTRAS_SONG_ID = "extras_song_id";

  public static Intent newIntent(Context orgin, long songId) {
    Intent intent = new Intent(orgin, AddLyricsActivity.class);
    intent.putExtra(EXTRAS_SONG_ID, songId);
    return intent;
  }


  @Override
  public Fragment createFragment() {
    long songId = getIntent().getExtras().getLong(EXTRAS_SONG_ID);
    return AddLyricsFragment.newInstance(songId);
  }


  @Override
  public String getTag() {
    return AddLyricsFragment.FRAGMENT_TAG;
  }

}
