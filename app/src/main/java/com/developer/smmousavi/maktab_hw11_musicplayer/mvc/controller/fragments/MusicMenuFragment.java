package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.*;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.currentSongTimeTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.musicAlbumTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.musicImageLyricsImg;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.musicTitleTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.seekBar;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songDurationTxt;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songPlayPauseBtn;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicPlayFragment.songPlayPauseIcon;

/**
 * A simple {@link Fragment} subclass.
 */

public class MusicMenuFragment extends Fragment {

  public static final String FRAGMENT_TAG = "music_menu_fragment";
  private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;

  public static MediaPlayer mp;
  public static List<Song> songList;
  public RecyclerView recyclerView;
  public MusicAdapter musicAdapter;
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
     * just in the MusicHolder clase
    userPanelPlayBtn.setOnClickListener(new View.OnClickListener() {
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
  }


  public class MusicViewHolder extends RecyclerView.ViewHolder {
    private Song selectedSong;
    private Song lastPlayingSong;
    private String songTitle;
    private String artist;
    private ImageView songImageCard;
    private TextView titleView;
    private TextView artistView;
    private Button songTrackBtn;
    private int lastNextPosition;
    private int lastPrevPosition;
    private boolean positionByNext;
    private boolean positionByPrev;
    private Song nowPlayingSong;

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

          userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

          playInList(selectedSong);
          setCurrentSongText(selectedSong);
          setupUserMusicPanel();

          currentSongUserPanelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = MusicPlayActivity.newIntent(parent.context, nowPlayingSong.getId(), getAdapterPosition());
              startActivity(intent);
              Handler handler = new Handler();
              handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  setOnNextListener(songNextBtn);
                  setOnPrevListener(songPrevBtn);
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
      try {
        setSongImage(song, musicImageLyricsImg);
        musicTitleTxt.setText(song.getDisplayingName());
        musicAlbumTxt.setText(song.getArtist());
        int durationMin = (int) song.getDuration() / 1000 / 60;
        int durationSec = (int) song.getDuration() / 1000 % 60;
        songDurationTxt.setText(getString(R.string.music_duration, durationMin, durationSec));
        currentSongTimeTxt.setText(getString(R.string.music_duration, durationMin, durationSec));
      } catch (Exception e) {
        e.getStackTrace();
      }
    }


    private void setSeekBarActions(Song song) {
      try {
        seekBar.setMax((int) song.getDuration());
        final Handler mHandler = new Handler();

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
      } catch (Exception e) {
        e.getStackTrace();
      }
    }// end of setSeekBarActions()


    public void setupUserMusicPanel() {
      playOrPauseBtnListener(userPanelPlayBtn, userPanelPlayIcon);

      setOnNextListener(userPanelNextBtn);

      setOnPrevListener(userPanelPrevBtn);
    }// end of setupUserMusicPanel()


    private void playOrPauseBtnListener(Button button, final ImageView BtnIcon) {
      button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          playOrPause(BtnIcon);
        }
      });
    }// end of playOrPauseBtnListener()


    private void setOnPrevListener(Button button) {
      button.setOnClickListener(new View.OnClickListener() {
        int prevPos = getAdapterPosForPrev(getAdapterPosition());

        @Override
        public void onClick(View view) {
          positionByPrev = true;
          positionByNext = false;

          if (prevPos > 1)
            prevPos--;

          else
            prevPos = songList.size() - 1;

          lastPrevPosition = prevPos;
          nowPlayingSong = songList.get(prevPos);
          stopAudio(mp, selectedSong);
          playInList(nowPlayingSong);

          userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

          setCurrentSongText(nowPlayingSong);

          setPlayingSongInfo(nowPlayingSong);
          setSeekBarActions(nowPlayingSong);
          playOrPauseBtnListener(userPanelPlayBtn, userPanelPlayIcon);
          if (songPlayPauseBtn != null)
            playOrPauseBtnListener(songPlayPauseBtn, songPlayPauseIcon);
        }
      });
    }

    private void setOnNextListener(Button button) {
      button.setOnClickListener(new View.OnClickListener() {
        int nextPos = getAdapterPosForNext(getAdapterPosition());

        @Override
        public void onClick(View view) {
          positionByNext = true;
          positionByPrev = false;

          if (nextPos < songList.size() - 1)
            nextPos++;

          else
            nextPos = 0;

          lastNextPosition = nextPos;
          nowPlayingSong = songList.get(nextPos);
          stopAudio(mp, selectedSong);
          playInList(nowPlayingSong);

          userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

          setCurrentSongText(nowPlayingSong);

          setPlayingSongInfo(nowPlayingSong);
          setSeekBarActions(nowPlayingSong);
          playOrPauseBtnListener(userPanelPlayBtn, userPanelPlayIcon);
          if (songPlayPauseBtn != null)
            playOrPauseBtnListener(songPlayPauseBtn, songPlayPauseIcon);
        }
      });

    }


    public int getAdapterPosForNext(int adapterPos) {
      int nextPos;
      if (!positionByPrev)
        nextPos = adapterPos;

      else
        nextPos = lastPrevPosition;
      return nextPos;
    }


    public int getAdapterPosForPrev(int adapterPos) {
      int prevPos;
      if (!positionByNext)
        prevPos = adapterPos;
      else
        prevPos = lastNextPosition;

      return prevPos;
    }


    public void setCurrentSongText(Song song) {
      currentPlayingMusicName.setText(song.getDisplayingName());
      currentPlayingMusicArtist.setText(song.getArtist());
      setSongImage(song, currentPlayingMusicImageView);
    }// end of setCurrentSongText()


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

      setUserPanelPlayIcon(icon);
    }// end of playOrPause()


    private void setUserPanelPlayIcon(ImageView icon) {
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
      return new MusicViewHolder(view);
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
