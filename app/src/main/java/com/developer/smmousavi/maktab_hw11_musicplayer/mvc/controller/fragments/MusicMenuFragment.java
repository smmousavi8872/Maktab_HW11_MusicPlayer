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
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Lyrics;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.currentSongTimeTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.lyricsScrollView;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.lyricsSubView;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.musicAlbumTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.musicImageLyricsImg;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.musicTitleTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.seekBar;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.showLyricsBtn;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.showLyricsIcon;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songDurationTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songNextBtn;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songPlayPauseBtn;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songPlayPauseIcon;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songPrevBtn;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songRepeatBtn;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songRepeatIcon;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songShuffleBtn;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songShuffleIcon;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.visibleLyrics;

/**
 * A simple {@link Fragment} subclass.
 */


public class MusicMenuFragment extends Fragment {

  public static final String FRAGMENT_TAG = "music_menu_fragment";
  private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;
  public static final String SHARED_NOW_PLAYING = "shared_now_playing";
  public static final String SHARED_LAST_PLAYING = "shared_last_playing";
  public static final String SHARED_LAST_PLAYED_IN_PLAY_LIST = "shared_last_played_in_play_list";
  public static final int PREV_BTN_ID = 0;
  public static final int NEXT_BTN_ID = 1;
  private static final String SHARED_IS_SHUFFLED = "sharded_is_shuffled";
  private static final String SHARED_REPEAT_STATUS = "sharded_repeat_status";
  public static final int REPEATE_STATUS_OFF = 0;
  public static final int REPEATE_STATUS_ONE = 1;
  public static final int REPEATE_STATUS_ALL = 2;

  public static MediaPlayer mp;
  public static Song classSelectedSong;
  public static Song classLastPlayingSong;
  public static boolean isShuffled;
  public static int repeatStatus;

  private List<Song> songPlayList;
  private MusicAdapter musicAdapter;
  private Handler mHandler;
  private Song lastPlayedInPlayList;


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

  private String songTitle;
  private String artist;
  private ImageView songImageCard;
  private TextView titleView;
  private TextView artistView;
  private Button songTrackBtn;

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
    InitializeList();
    findParentViews();
    currentPlayingMusicLayout.bringToFront();

    if (loadLongPreferences(SHARED_NOW_PLAYING) != 0) {
      classSelectedSong = Repository.getInstance(getActivity()).getSong(loadLongPreferences(SHARED_NOW_PLAYING));
      classLastPlayingSong = Repository.getInstance(getActivity()).getSong(loadLongPreferences(SHARED_LAST_PLAYING));
      setCurrentSongText(classSelectedSong);
      setupUserMusicPanel();
      currentSongUserPlayAction();
    }
  }

  private void initializeMediPlayer() {
    if (mp == null) {
      long lastPlayedId = loadLongPreferences(SHARED_NOW_PLAYING);
      Song lastPlayedSong = getSongFromPlayList(lastPlayedId);
      playAudio(lastPlayedSong.getUri());
      pauseAudio(mp, lastPlayedSong);
    }
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    songPlayList = new ArrayList<>();
    songPlayList = getSongListByPermission();
    setRetainInstance(true);
    sortSongList(songPlayList);
    findParentViews();
    currentPlayingMusicLayout.bringToFront();
    InitializeList();
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


  private void InitializeList() {
    initializePlayPauseBtn();
    initializeShuffleBtn();
    initializeMediPlayer();
    initializeRepeatBtn();
  }


  private void initializePlayPauseBtn() {
    if (mp == null)
      userPanelPlayIcon.setBackground(getResources().getDrawable(R.drawable.ic_play_button));

    else if (!mp.isPlaying())
      userPanelPlayIcon.setBackground(getResources().getDrawable(R.drawable.ic_play_button));

    else if (mp.isPlaying())
      userPanelPlayIcon.setBackground(getResources().getDrawable(R.drawable.ic_pause_button));
  }


  private void initializeShuffleBtn() {
    isShuffled = loadBooleanPrefrences(SHARED_IS_SHUFFLED);
    if (isShuffled)
      Collections.shuffle(songPlayList);

    else
      sortSongList(songPlayList);
  }


  private void initializeRepeatBtn() {
    repeatStatus = (int) loadLongPreferences(SHARED_REPEAT_STATUS);
    switch (repeatStatus) {
      case REPEATE_STATUS_OFF:
        doNotRepeat();
        break;
      case REPEATE_STATUS_ONE:
        repeatCurrentSong();
        break;
      case REPEATE_STATUS_ALL:
        repeatAllSongs();
    }
  }


  private void setupAdapter() {
    List<Song> songShowList = Repository.getInstance(getActivity()).getSongList();
    musicAdapter = new MusicAdapter(songShowList);
    recyclerView.setAdapter(musicAdapter);
  }// end of setupAdapter()


  public List<Song> getSongListByPermission() {
    int readPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    if (readPermission != PackageManager.PERMISSION_GRANTED)
      ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);

    else
      songPlayList = Repository.getInstance(getActivity()).getSongList();

    return songPlayList;
  }

  public void sortSongList(List<Song> list) {
    Collections.sort(list, new Comparator<Song>() {
      public int compare(Song song1, Song song2) {
        return song1.getDisplayingName().compareTo(song2.getDisplayingName());
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
      @Override
      public void onClick(View view) {
        goToNextOrPrevSong(btnId);
      }
    });
  }// end of setOnPrevOrNextListener()


  private void goToNextOrPrevSong(int nextOrPrevId) {
    long lastPlayedId = loadLongPreferences(SHARED_LAST_PLAYED_IN_PLAY_LIST);
    Song lastPlayedSong = getSongFromPlayList(lastPlayedId);
    int newPos = songPlayList.indexOf(lastPlayedSong);
    Song prevSong = songPlayList.get(newPos);
    invalidateLyrics();

    switch (nextOrPrevId) {
      case PREV_BTN_ID:
        if (newPos > 1)
          newPos--;
        else
          newPos = songPlayList.size() - 1;
        break;
      case NEXT_BTN_ID:
        if (newPos < songPlayList.size() - 1)
          newPos++;
        else
          newPos = 0;
    }
    lastPlayedInPlayList = songPlayList.get(newPos);
    classSelectedSong = lastPlayedInPlayList;
    saveLongPreferences(SHARED_LAST_PLAYED_IN_PLAY_LIST, lastPlayedInPlayList.getId());
    saveLongPreferences(SHARED_NOW_PLAYING, classSelectedSong.getId());

    stopAudio(mp, prevSong);
    playAudio(lastPlayedInPlayList.getUri());

    userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

    setCurrentSongText(classSelectedSong);

    playOrPauseBtnListener(userPanelPlayBtn, userPanelPlayIcon);

    if (songPlayPauseBtn != null) {
      playOrPauseBtnListener(songPlayPauseBtn, songPlayPauseIcon);
      songPlayPauseIcon.setBackgroundResource(R.drawable.ic_pause_button);
      setPlayingSongInfo(classSelectedSong);
      setSeekBarActions(classSelectedSong);
    }
  }// end of goToNextOrPrevSong()


  private Song getSongFromPlayList(long songId) {
    for (Song song : songPlayList) {
      if (song.getId() == songId)
        return song;
    }
    return null;
  }


  public void playOrPause(ImageView icon) {
    if (mp == null) {
      playAudio(classSelectedSong.getUri());

    } else if (mp.isPlaying())
      pauseAudio(mp, classSelectedSong);

    else if (!mp.isPlaying())
      startAudio(mp, classSelectedSong);

    setUserPanelPlayIcon(icon, classSelectedSong);
  }// end of playOrPause()


  private void playInList(Song selectedSong) {
    if (mp == null) {
      playAudio(selectedSong.getUri());
      selectedSong.setPlaying(true);

    } else if (classLastPlayingSong.getId() == selectedSong.getId()) {
      resetAudio(mp, selectedSong.getUri());

    } else if (classLastPlayingSong.getId() != selectedSong.getId()) {
      stopAudio(mp, selectedSong);
      classLastPlayingSong.setPlaying(false);
      classLastPlayingSong = selectedSong;
      playAudio(selectedSong.getUri());
      selectedSong.setPlaying(true);
    }
  }// end of playInList()


  private void setPlayingSongInfo(Song song) {
    setSongImage(song, musicImageLyricsImg);
    musicTitleTxt.setText(song.getDisplayingName());
    musicAlbumTxt.setText(song.getArtist());
    int durationMin = (int) song.getDuration() / 1000 / 60;
    int durationSec = (int) song.getDuration() / 1000 % 60;
    songDurationTxt.setText(getString(R.string.music_duration, durationMin, durationSec));
    currentSongTimeTxt.setText(getString(R.string.music_duration, durationMin, durationSec));
  }// end of setPlayingSongInfo()


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


  private void currentSongUserPlayAction() {
    final MusicMenuActivity parent = (MusicMenuActivity) getActivity();
    currentSongUserPanelBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = MusicPlayActivity.newIntent(parent.context, classSelectedSong.getId());
        startActivity(intent);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
          @Override
          public void run() {
            setMusicPlayFragmentListeners();

          }
        }, 100);
      }
    });
  }


  private void setMusicPlayFragmentListeners() {
    setOnPrevOrNextListener(songNextBtn, NEXT_BTN_ID);
    setOnPrevOrNextListener(songPrevBtn, PREV_BTN_ID);
    setPlayingSongInfo(classSelectedSong);
    setSeekBarActions(classSelectedSong);
    playOrPauseBtnListener(songPlayPauseBtn, songPlayPauseIcon);
    shuffleBtnListener();
    repeatBtnListener();
    setLyricsListenr();
  }// end of setMusicPlayFragmentListeners()


  private void repeatBtnListener() {
    //for first time going to the MusicPlayActivity
    initializeRepeatBtn();
    repeatStatus = (int) loadLongPreferences(SHARED_REPEAT_STATUS);
    songRepeatBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setRepeatBtnActions();
        //for first time after repeatBtn has changed
        initializeRepeatBtn();
      }
    });
  }


  private void setRepeatBtnActions() {
    switch (repeatStatus) {
      case REPEATE_STATUS_OFF:
        repeatStatus = REPEATE_STATUS_ONE;
        saveLongPreferences(SHARED_REPEAT_STATUS, repeatStatus);
        songRepeatIcon.setBackground(getResources().getDrawable(R.drawable.ic_repeat_current_song));
        repeatAllSongs();
        break;

      case REPEATE_STATUS_ONE:
        repeatStatus = REPEATE_STATUS_ALL;
        saveLongPreferences(SHARED_REPEAT_STATUS, repeatStatus);
        songRepeatIcon.setBackground(getResources().getDrawable(R.drawable.ic_repeat_all_songs));
        repeatCurrentSong();
        break;

      case REPEATE_STATUS_ALL:
        repeatStatus = REPEATE_STATUS_OFF;
        saveLongPreferences(SHARED_REPEAT_STATUS, repeatStatus);
        songRepeatIcon.setBackground(getResources().getDrawable(R.drawable.ic_repeat_song));
        doNotRepeat();
        break;
    }
  }


  private void repeatAllSongs() {
    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        goToNextOrPrevSong(NEXT_BTN_ID);
      }
    });
  }


  private void repeatCurrentSong() {
    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        invalidateLyrics();
        resetAudio(mp, classSelectedSong.getUri());
      }
    });
  }


  private void doNotRepeat() {
    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer) {
        songPlayPauseIcon.setBackgroundResource(R.drawable.ic_play_button);
      }
    });
  }


  private void shuffleBtnListener() {
    if (isShuffled) {
      songShuffleIcon.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_song_purple));
      Collections.shuffle(songPlayList);

    } else {
      sortSongList(songPlayList);
      songShuffleIcon.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_song));
    }

    songShuffleBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!isShuffled) {
          isShuffled = true;
          songShuffleIcon.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_song_purple));
          Collections.shuffle(songPlayList);
          saveBooleanPreferences(SHARED_IS_SHUFFLED, isShuffled);
        } else {
          isShuffled = false;
          songShuffleIcon.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_song));
          sortSongList(songPlayList);
          saveBooleanPreferences(SHARED_IS_SHUFFLED, isShuffled);
        }
      }
    });
  }


  private void setLyricsListenr() {
    showLyricsBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!visibleLyrics) {
          Lyrics lyrics = Repository.getInstance(getActivity()).getSongLyric(classSelectedSong.getId());
          String lyricsText = "No lyrics added for this song!Tap to add one";
          if (lyrics != null)
            lyricsText = lyrics.getSyncedLyrics();

          validateLyrics(lyricsText);
        } else
          invalidateLyrics();
      }
    });
  }


  private void invalidateLyrics() {
    visibleLyrics = false;
    lyricsSubView.setPlayer(null);
    showLyricsIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_song_lyrics));
    lyricsScrollView.animate().alpha(0.0f);
  }


  private void validateLyrics(String lyricsText) {
    visibleLyrics = true;
    showLyricsIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_song_lyrics_violet));
    lyricsScrollView.setVisibility(View.VISIBLE);
    lyricsScrollView.animate().alpha(1.0f);
    lyricsSubView.setPlayer(mp);
    lyricsSubView.setSubSource(lyricsText, MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
  }


  public class MusicViewHolder extends RecyclerView.ViewHolder {
    private Song selectedSong;
    private Song lastPlayingSong;

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
      songTrackBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          updatePlayingSong();
          userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

          if (!isShuffled)
            saveLongPreferences(SHARED_LAST_PLAYED_IN_PLAY_LIST, selectedSong.getId());

          playInList(selectedSong);
          setCurrentSongText(selectedSong);
          setupUserMusicPanel();
          currentSongUserPlayAction();
        }
      });
    }// end of songTrackOnClickActions()


    private void updatePlayingSong() {
      if (lastPlayingSong != null)
        lastPlayingSong.setPlaying(false);

      lastPlayingSong = selectedSong;
      lastPlayingSong.setPlaying(true);

      classSelectedSong = selectedSong;
      classLastPlayingSong = lastPlayingSong;

      saveLongPreferences(SHARED_NOW_PLAYING, classSelectedSong.getId());
      saveLongPreferences(SHARED_LAST_PLAYING, classLastPlayingSong.getId());
    }// end of updatePlayingSong()
  }// end of MusicViewHolder{}


  public class MusicAdapter extends RecyclerView.Adapter<MusicViewHolder> {
    private List<Song> songs;

    public MusicAdapter(List<Song> songs) {
      this.songs = songs;
      sortSongList(songs);
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


  public void saveLongPreferences(String key, long value) {
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putLong(key, value);
    editor.apply();
  }

  public long loadLongPreferences(String key) {
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    return sharedPreferences.getLong(key, 0);
  }


  public void saveBooleanPreferences(String key, boolean value) {
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(key, value);
    editor.apply();
  }


  public boolean loadBooleanPrefrences(String key) {
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    return sharedPreferences.getBoolean(key, false);
  }


  public void playAudio(String path) {
    try {
      mp = new MediaPlayer();
      initializeRepeatBtn();
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
