package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.developer.smmousavi.maktab_hw11_musicplayer.R;
import com.developer.smmousavi.maktab_hw11_musicplayer.database.Repository;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities.MusicMenuActivity;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities.MusicPlayActivity;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.currentSongTimeTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.musicAlbumTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.musicImageLyricsImg;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.musicTitleTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.seekBar;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songDurationTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songNextBtn;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songPlayPauseBtn;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songPlayPauseIcon;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songPrevBtn;

/**
 * A simple {@link Fragment} subclass.
 */

public class MusicMenuFragment extends Fragment {

  public static final String FRAGMENT_TAG = "music_menu_fragment";
  private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;
  public static final String SHARED_NOW_PLAYING = "shared_now_playing";
  public static final int PREV_BTN_ID = 0;
  public static final int NEXT_BTN_ID = 1;

  public static MediaPlayer mp;
  public static Song nowPlayingSong;
  private List<Song> songList;
  private MusicAdapter musicAdapter;
  private Handler mHandler;

  public RecyclerView recyclerView;
  public RelativeLayout currentPlayingMusicLayout;
  public Button currentSongUserPanelBtn;
  public ImageView currentPlayingMusicImageView;
  public TextView currentPlayingMusicName;
  public TextView currentPlayingMusicArtist;
  public Button userPanelPlayBtn;
  public Button userPanelNextBtn;
  public Button userPanelPrevBtn;
  public ImageView userPanelPlayIcon;
  public ImageView userPanelNextIcon;
  public ImageView userPanelPrevIcon;
  private SharedPreferences sharedPreferences;
  private MusicViewHolder musicHolder;


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
  public void onResume() {
    super.onResume();
    userPanelPlayButtonInitialize();
    if (loadPreferences(SHARED_NOW_PLAYING) != 0) {
      nowPlayingSong = Repository.getInstance(getActivity()).getSong(loadPreferences(SHARED_NOW_PLAYING));
      setCurrentSongText(nowPlayingSong);
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    songList = new ArrayList<>();
    songList = getSongListPermission();
    setRetainInstance(true);
    sortSongList();
    findParentViews();
    currentPlayingMusicLayout.bringToFront();

    userPanelPlayButtonInitialize();
  }


  private void userPanelPlayButtonInitialize() {
    if (mp == null)
      userPanelPlayIcon.setBackground(getResources().getDrawable(R.drawable.ic_play_button));

    else if (!mp.isPlaying())
      userPanelPlayIcon.setBackground(getResources().getDrawable(R.drawable.ic_play_button));

    else if (mp.isPlaying())
      userPanelPlayIcon.setBackground(getResources().getDrawable(R.drawable.ic_pause_button));


    /* for having the userPanelPlayBtn works as soon as the first time is's cliked
     * it's listener must be redeclerd here. but playOrPause() method is available
     * just in the MusicHolder clase */

    /*userPanelPlayBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        MusicMenuFragment.playOrPause();
      }
    });*/
  }


  @Override
  public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_music_menu, container, false);
    recyclerView = view.findViewById(R.id.music_menu_recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    setupAdapter();

    return view;
  }


  private void setupAdapter() {
    musicAdapter = new MusicAdapter(songList);
    recyclerView.setAdapter(musicAdapter);
  }// end of setupAdapter()


  public List<Song> getSongListPermission() {
    int readPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    if (readPermission != PackageManager.PERMISSION_GRANTED)
      ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);

    else
      songList = Repository.getInstance(getActivity()).getSongList();

    return songList;
  }

  public void sortSongList() {
    Collections.sort(songList, new Comparator<Song>() {
      public int compare(Song song1, Song song2) {
        return song1.getTitle().compareTo(song2.getTitle());
      }
    });
  }


  public void findParentViews() {
    currentPlayingMusicLayout = getActivity().findViewById(R.id.current_playing_music_layout);
    currentSongUserPanelBtn = getActivity().findViewById(R.id.song_user_panel_btn);
    currentPlayingMusicImageView = getActivity().findViewById(R.id.current_playing_music_Image);
    currentPlayingMusicName = getActivity().findViewById(R.id.current_playing_music_name);
    currentPlayingMusicArtist = getActivity().findViewById(R.id.current_playing_music_artist);
    userPanelPlayBtn = getActivity().findViewById(R.id.user_panel_play_btn);
    userPanelNextBtn = getActivity().findViewById(R.id.user_panel_next_btn);
    userPanelPrevBtn = getActivity().findViewById(R.id.user_panel_prev_btn);
    userPanelPlayIcon = getActivity().findViewById(R.id.user_panel_play_icon);
    userPanelNextIcon = getActivity().findViewById(R.id.user_panel_next_icon);
    userPanelPrevIcon = getActivity().findViewById(R.id.user_panel_prev_icon);
  }// end of findParentViews()


  public class MusicViewHolder extends RecyclerView.ViewHolder {
    private Song selectedSong;
    private Song lastPlayingSong;
    private String songTitle;
    private String artist;
    private ImageView songImageCard;
    private TextView titleView;
    private TextView artistView;
    private Button songTrackBtn;

    public MusicViewHolder(@NonNull final View itemView) {
      super(itemView);
      titleView = itemView.findViewById(R.id.music_track_name);
      artistView = itemView.findViewById(R.id.music_track_artist);
      songImageCard = itemView.findViewById(R.id.music_track_image);
      songTrackBtn = itemView.findViewById(R.id.song_track_btn);


    }// end of MusicViewHolder()


    public void onBinde(Song song) {
      selectedSong = song;
      songTitle = song.getDisplayingName();
      artist = song.getArtist();
      titleView.setText(songTitle);
      artistView.setText(artist);
      setSongImage(song, songImageCard);

      songTrackOnClickActions();
    }// end of onBinde()


    private void songTrackOnClickActions() {
      final MusicMenuActivity parent = (MusicMenuActivity) getActivity();
      songTrackBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (lastPlayingSong != null)
            lastPlayingSong.setPlaying(false);

          lastPlayingSong = selectedSong;
          lastPlayingSong.setPlaying(true);
          nowPlayingSong = selectedSong;
          savePreferences(SHARED_NOW_PLAYING, nowPlayingSong.getId());

          userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

          playInList(selectedSong);
          setCurrentSongText(selectedSong);
          setupUserMusicPanel();

          currentSongUserPanelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = MusicPlayActivity.newIntent(parent.context, nowPlayingSong.getId());
              startActivity(intent);
              Handler handler = new Handler();
              handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  setOnPrevOrNextListener(songNextBtn, NEXT_BTN_ID);
                  setOnPrevOrNextListener(songPrevBtn, PREV_BTN_ID);
                  setPlayingSongInfo(nowPlayingSong);
                  setSeekBarActions(nowPlayingSong);
                  playOrPauseBtnListener(songPlayPauseBtn, songPlayPauseIcon);
                }
              }, 100);
            }
          });
        }
      });
    }


    private void setPlayingSongInfo(Song song) {
      setSongImage(song, musicImageLyricsImg);
      musicTitleTxt.setText(song.getDisplayingName());
      musicAlbumTxt.setText(song.getArtist());
      int durationMin = (int) song.getDuration() / 1000 / 60;
      int durationSec = (int) song.getDuration() / 1000 % 60;
      songDurationTxt.setText(getString(R.string.music_duration, durationMin, durationSec));
      currentSongTimeTxt.setText(getString(R.string.music_duration, durationMin, durationSec));
    }


    private void setSeekBarActions(Song song) {
      seekBar.setMax((int) song.getDuration());
      mHandler = new Handler();

      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          if (mp != null) {
            int mCurrentPosition = mp.getCurrentPosition();
            seekBar.setProgress(mCurrentPosition);
            int min = mCurrentPosition / 1000 / 60;
            int sec = mCurrentPosition / 1000 % 60;
            String currentTimeStr = String.format("%02d:%02d", min, sec);
            currentSongTimeTxt.setText(currentTimeStr);
          }
          mHandler.postDelayed(this, 1000);
        }
      });

      seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
          if (mp != null && fromUser) {
            mp.seekTo(progress);
          }
        }
      });
    }// end of setSeekBarActions()


    public void setupUserMusicPanel() {
      playOrPauseBtnListener(userPanelPlayBtn, userPanelPlayIcon);

      setOnPrevOrNextListener(userPanelNextBtn, NEXT_BTN_ID);

      setOnPrevOrNextListener(userPanelPrevBtn, PREV_BTN_ID);
    }// end of setupUserMusicPanel()


    private void playOrPauseBtnListener(Button button, final ImageView BtnIcon) {
      button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          playOrPause(BtnIcon);
        }
      });
    }// end of playOrPauseBtnListener()


    private void setOnPrevOrNextListener(Button button, final int btnId) {
      button.setOnClickListener(new View.OnClickListener() {
        int newPos = songList.indexOf(nowPlayingSong);

        @Override
        public void onClick(View view) {
          switch (btnId) {
            case 0:
              if (newPos > 1)
                newPos--;
              else
                newPos = songList.size() - 1;
              break;
            case 1:
              if (newPos < songList.size() - 1)
                newPos++;
              else
                newPos = 0;
          }
          nowPlayingSong = songList.get(newPos);
          selectedSong = nowPlayingSong;
          savePreferences(SHARED_NOW_PLAYING, nowPlayingSong.getId());
          stopAudio(mp, selectedSong);
          playInList(nowPlayingSong);

          userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

          setCurrentSongText(nowPlayingSong);

          playOrPauseBtnListener(userPanelPlayBtn, userPanelPlayIcon);

          if (songPlayPauseBtn != null) {
            playOrPauseBtnListener(songPlayPauseBtn, songPlayPauseIcon);
            songPlayPauseIcon.setBackgroundResource(R.drawable.ic_pause_button);
            setPlayingSongInfo(nowPlayingSong);
            setSeekBarActions(nowPlayingSong);
          }
        }
      });
    }// end of setOnPrevOrNextListener()


    private void playInList(Song selectedSong) {
      if (mp == null) {
        playAudio(selectedSong.getUri());
        selectedSong.setPlaying(true);

      } else if (lastPlayingSong.getId() == selectedSong.getId()) {
        resetAudio(mp, selectedSong.getUri());

      } else if (lastPlayingSong.getId() != selectedSong.getId()) {
        stopAudio(mp, selectedSong);
        lastPlayingSong.setPlaying(false);
        lastPlayingSong = selectedSong;
        playAudio(selectedSong.getUri());
        selectedSong.setPlaying(true);
      }
    }// end of playInList()


    public void playOrPause(ImageView icon) {
      if (selectedSong.isPlaying())
        pauseAudio(mp, selectedSong);

      else
        startAudio(mp, selectedSong);

      setUserPanelPlayIcon(icon, selectedSong);
    }// end of playOrPause()


  }// end of MusicViewHolder{}


  public class MusicAdapter extends RecyclerView.Adapter<MusicViewHolder> {
    private List<Song> songs;

    public MusicAdapter(List<Song> songs) {
      this.songs = songs;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(getActivity());
      View view = inflater.inflate(R.layout.song_track_view, parent, false);
      musicHolder = new MusicViewHolder(view);
      return musicHolder;
    }// end of onCreateViewHolder()

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
      holder.onBinde(songs.get(position));
      holder.setIsRecyclable(false);
    }// end of onBindeViewHolder()

    @Override
    public int getItemCount() {
      return songs.size();
    }// end of getItemCount()


  }// end of MusicAdapter{}


  private void setUserPanelPlayIcon(ImageView icon, Song selectedSong) {
    if (selectedSong.isPlaying()) {
      icon.setBackgroundResource(R.drawable.ic_pause_button);
      userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

    } else {
      icon.setBackgroundResource(R.drawable.ic_play_button);
      userPanelPlayIcon.setBackgroundResource(R.drawable.ic_play_button);
    }
  }// end of setUserPanelPlayIcon()


  private void setSongImage(Song song, ImageView imageView) {
    String albumArtId = Repository.getInstance(getActivity()).getSongImgUri(song.getAlbumId());
    Uri imgUri = Uri.parse(albumArtId);
    imageView.setBackground(getResources().getDrawable(R.drawable.music_background));
    imageView.setImageURI(imgUri);
  }


  public void setCurrentSongText(Song song) {
    currentPlayingMusicName.setText(song.getDisplayingName());
    currentPlayingMusicArtist.setText(song.getArtist());
    setSongImage(song, currentPlayingMusicImageView);
  }// end of setCurrentSongText()


  public void savePreferences(String key, long value) {
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putLong(key, value);
    editor.apply();
  }


  public long loadPreferences(String key) {
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    return sharedPreferences.getLong(key, 0);
  }


  public void playAudio(String path) {
    try {
      mp = new MediaPlayer();
      mp.setDataSource(path);
      mp.prepare();
      mp.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }// end of playAudio()


  public void pauseAudio(MediaPlayer mp, Song song) {
    try {
      song.setPlaying(false);
      mp.pause();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }// end of pauseAudio()


  public void startAudio(MediaPlayer mp, Song song) {
    try {
      song.setPlaying(true);
      mp.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }// end of startAudio()


  public void resetAudio(MediaPlayer mp, String path) {
    try {
      mp.reset();
      playAudio(path);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }// end of resetAudio()


  public void stopAudio(MediaPlayer mp, Song song) {
    try {
      song.setPlaying(false);
      mp.stop();
      mp.release();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }// end of stopAudio()

}
