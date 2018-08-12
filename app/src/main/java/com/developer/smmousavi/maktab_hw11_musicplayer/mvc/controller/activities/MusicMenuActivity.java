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

  public static final String FATARIOT_TAB_NAME = "FAVARIOT";
  public static final String TRACK_TAB_NAME = "TRACK";
  public static final String ALBUM_TAB_NAME = "ALBUM";
  public static final String ARTIST_TAB_NAME = "ARTIST";

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

  private Fragment favariots;
  private Fragment tracks;
  private Fragment albums;
  private Fragment artists;


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

    favariots = MusicMenuFragment.newInstance(FATARIOT_TAB_NAME);
    tracks = MusicMenuFragment.newInstance(TRACK_TAB_NAME);
    albums = MusicMenuFragment.newInstance(ALBUM_TAB_NAME);
    artists = MusicMenuFragment.newInstance(ARTIST_TAB_NAME);

    addFragment(favariots, FATARIOT_TAB_NAME);
    addFragment(tracks, TRACK_TAB_NAME);
    addFragment(albums, ALBUM_TAB_NAME);
    addFragment(artists, ARTIST_TAB_NAME);

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
