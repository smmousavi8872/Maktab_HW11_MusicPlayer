package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.smmousavi.maktab_hw11_musicplayer.R;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicMenuFragment;

import java.util.ArrayList;
import java.util.List;

public class MusicMenuActivity extends AppCompatActivity {


  public static Intent newIntent(Context orgin) {
    Intent intent = new Intent(orgin, MusicMenuActivity.class);
    return intent;
  }


  private TabLayout tabLayout;
  private ViewPager viewPager;
  private RelativeLayout currentPlayingMusicLayout;
  private ImageView currentPlayingMusicImageView;
  private TextView currentPlayingMusicName;
  private TextView currentPlayingMusicArtist;
  private Button currentPlayingMusicPlayBtn;
  private Button currentPlayingMusicNextBtn;
  private Button currentPlayingMusicPrevBtn;
  List<Fragment> musicMenuFragments;
  List<String> musicMenuTitles;


  @Override

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_music_menu);

    musicMenuFragments = new ArrayList<>();
    musicMenuTitles = new ArrayList<>();

    viewPager = findViewById(R.id.music_menu_view_pager);
    tabLayout = findViewById(R.id.music_menu_tablayout);
    currentPlayingMusicLayout = findViewById(R.id.current_playing_music_layout);
    currentPlayingMusicImageView = findViewById(R.id.current_playing_music_Image);
    currentPlayingMusicName = findViewById(R.id.current_playing_music_name);
    currentPlayingMusicArtist = findViewById(R.id.current_playing_music_artist);
    currentPlayingMusicPlayBtn = findViewById(R.id.current_playing_music_play);
    currentPlayingMusicNextBtn = findViewById(R.id.current_playing_music_next);
    currentPlayingMusicPrevBtn = findViewById(R.id.current_playing_music_prev);

    addFragment(MusicMenuFragment.newInstance(), "FAVARIOT");
    addFragment(MusicMenuFragment.newInstance(), "TRACKS");
    addFragment(MusicMenuFragment.newInstance(), "ALBUMS");
    addFragment(MusicMenuFragment.newInstance(), "ARTISTS");

    viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        return musicMenuFragments.get(position);
      }

      @Override
      public int getCount() {
        return musicMenuFragments.size();
      }

      @Nullable
      @Override
      public CharSequence getPageTitle(int position) {
        return musicMenuTitles.get(position);
      }
    });

    tabLayout.setupWithViewPager(viewPager);

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        //this method might be useful
      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

  }


  private void addFragment(Fragment musicFragmetn, String title) {
    musicMenuFragments.add(musicFragmetn);
    musicMenuTitles.add(title);

  }

}
