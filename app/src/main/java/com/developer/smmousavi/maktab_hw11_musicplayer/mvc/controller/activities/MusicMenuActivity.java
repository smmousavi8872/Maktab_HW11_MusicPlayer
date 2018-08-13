package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.developer.smmousavi.maktab_hw11_musicplayer.R;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicMenuFragment;

import java.util.ArrayList;
import java.util.List;

public class MusicMenuActivity extends AppCompatActivity {

  public static final String FATARIOT_TAB_NAME = "FAVARIOT";
  public static final String TRACK_TAB_NAME = "TRACK";
  public static final String ALBUM_TAB_NAME = "ALBUM";
  public static final String ARTIST_TAB_NAME = "ARTIST";

  private TabLayout tabLayout;
  private ViewPager viewPager;
  private Button currentSongUserPanelBtn;

  private List<Fragment> musicMenuFragments;
  private List<String> musicMenuTitles;
  public MusicMenuActivity context;
  FragmentStatePagerAdapter adapter;

  private Fragment favariots;
  private Fragment tracks;
  private Fragment albums;
  private Fragment artists;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_music_menu);
    context = this;

    musicMenuFragments = new ArrayList<>();
    musicMenuTitles = new ArrayList<>();
    viewPager = findViewById(R.id.music_menu_view_pager);
    tabLayout = findViewById(R.id.music_menu_tablayout);
    currentSongUserPanelBtn = findViewById(R.id.song_user_panel_btn);

    favariots = MusicMenuFragment.newInstance();
    tracks = MusicMenuFragment.newInstance();
    albums = MusicMenuFragment.newInstance();
    artists = MusicMenuFragment.newInstance();

    addFragment(favariots, FATARIOT_TAB_NAME);
    addFragment(tracks, TRACK_TAB_NAME);
    addFragment(albums, ALBUM_TAB_NAME);
    addFragment(artists, ARTIST_TAB_NAME);


    /*currentSongUserPanelBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = MusicPlayActivity.newIntent(context, MusicMenuFragment.songForParent.getId());
        startActivity(intent);
      }
    });*/ // listener doesn't work correclty here

    adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
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
    };

    viewPager.setAdapter(adapter);

    viewPager.setOffscreenPageLimit(4);

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
