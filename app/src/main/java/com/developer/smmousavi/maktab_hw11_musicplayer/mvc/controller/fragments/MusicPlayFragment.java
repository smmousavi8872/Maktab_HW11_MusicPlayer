package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.smmousavi.maktab_hw11_musicplayer.R;
import com.developer.smmousavi.maktab_hw11_musicplayer.database.Repository;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Song;
import com.developer.smmousavi.maktab_hw11_musicplayer.utilities.AndroidImage;
import com.developer.smmousavi.maktab_hw11_musicplayer.utilities.GaussianBlur;

import java.io.IOException;

import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicMenuFragment.REPEATE_STATUS_ALL;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicMenuFragment.REPEATE_STATUS_OFF;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicMenuFragment.REPEATE_STATUS_ONE;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicMenuFragment.isShuffled;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicMenuFragment.mp;
import static com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments.MusicMenuFragment.repeatStatus;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicPlayFragment extends Fragment {

  public static final String FRAGMENT_TAG = "music_play_fragment_tag";
  public static final String ARGS_SONG_ID = "args_song_id";

  private Button favariotSongBtn;
  private ImageView favariotSongIcon;
  private ImageView fragmentLayout;
  public static ImageView musicImageLyricsImg;
  public static TextView musicTitleTxt;
  public static TextView musicAlbumTxt;
  public static AppCompatSeekBar seekBar;
  public static TextView currentSongTimeTxt;
  public static TextView songDurationTxt;
  public static Button songPlayPauseBtn;
  public static ImageView songPlayPauseIcon;
  public static Button songNextBtn;
  public static Button songPrevBtn;
  public static Button songShuffleBtn;
  public static ImageView songShuffleIcon;
  public static Button songRepeatBtn;
  public static ImageView songRepeatIcon;

  private long songId;
  private Song currentSong;
  private SharedPreferences sharedPreferences;


  public static MusicPlayFragment newInstance(long songId) {

    Bundle args = new Bundle();
    args.putLong(ARGS_SONG_ID, songId);

    MusicPlayFragment fragment = new MusicPlayFragment();
    fragment.setArguments(args);
    return fragment;
  }

  public MusicPlayFragment() {
    // Required empty public constructor
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    songId = getArguments().getLong(ARGS_SONG_ID);
    currentSong = Repository.getInstance(getActivity()).getSong(songId);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_music_play, container, false);
    getViews(view);
    fragmentLayout = view.findViewById(R.id.music_play_fragment_layout);
    Uri imageUri = Uri.parse(currentSong.getUri());
    try {
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
      GaussianBlur blur = new GaussianBlur();
      Bitmap bluredImage = blur.process(new AndroidImage(bitmap)).getImage();
      fragmentLayout.setImageBitmap(bluredImage);

    } catch (IOException e) {
      e.printStackTrace();

    }
    favariotButtonActions();
    setPlayBtnIcon();
    setShuffleBtnIcon();
    setRepeatBtnIcon();
    return view;
  }


  private void favariotButtonActions() {
    if (!currentSong.isFavariot())
      favariotSongIcon.setBackground(getResources().getDrawable(R.drawable.ic_favariot_song));

    else
      favariotSongIcon.setBackground(getResources().getDrawable(R.drawable.ic_favariot_song_red));

    favariotSongBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!currentSong.isFavariot()) {
          currentSong.setFavariot(true);
          favariotSongIcon.setBackground(getResources().getDrawable(R.drawable.ic_favariot_song_red));

        } else {
          currentSong.setFavariot(false);
          favariotSongIcon.setBackground(getResources().getDrawable(R.drawable.ic_favariot_song));
        }
      }
    });
  }// end of favariotButtonActions()


  private void setPlayBtnIcon() {
    if (mp == null)
      songPlayPauseIcon.setBackground(getResources().getDrawable(R.drawable.ic_play_button));

    else if (mp.isPlaying())
      songPlayPauseIcon.setBackground(getResources().getDrawable(R.drawable.ic_pause_button));

    else
      songPlayPauseIcon.setBackground(getResources().getDrawable(R.drawable.ic_play_button));

  }// end of setPlayBtnIcon()


  private void setShuffleBtnIcon() {
    if (isShuffled) {
      songShuffleIcon.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_song_purple));
    } else {

      songShuffleIcon.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_song));
    }
  }


  private void setRepeatBtnIcon() {
    switch (repeatStatus) {
      case REPEATE_STATUS_OFF:
        songRepeatIcon.setBackground(getResources().getDrawable(R.drawable.ic_repeat_song));
        break;
      case REPEATE_STATUS_ONE:
        songRepeatIcon.setBackground(getResources().getDrawable(R.drawable.ic_repeat_current_song));
        break;
      case REPEATE_STATUS_ALL:
        songRepeatIcon.setBackground(getResources().getDrawable(R.drawable.ic_repeat_all_songs));
    }
  }


  public void getViews(View view) {
    musicImageLyricsImg = view.findViewById(R.id.song_lyrics_image_background);
    musicTitleTxt = view.findViewById(R.id.song_name_text);
    musicAlbumTxt = view.findViewById(R.id.song_albume_text);
    favariotSongBtn = view.findViewById(R.id.favariot_song_btn);
    favariotSongIcon = view.findViewById(R.id.favariot_song_icn);
    seekBar = view.findViewById(R.id.song_seek_bar);
    currentSongTimeTxt = view.findViewById(R.id.current_music_time);
    songDurationTxt = view.findViewById(R.id.music_duration);
    songPlayPauseBtn = view.findViewById(R.id.song_play_btn);
    songPlayPauseIcon = view.findViewById(R.id.song_play_icn);
    songNextBtn = view.findViewById(R.id.song_next_btn);
    songPrevBtn = view.findViewById(R.id.song_prev_btn);
    songShuffleBtn = view.findViewById(R.id.song_shuffle_btn);
    songShuffleIcon = view.findViewById(R.id.song_shuffle_icn);
    songRepeatBtn = view.findViewById(R.id.song_repeat_btn);
    songRepeatIcon = view.findViewById(R.id.song_repeat_icn);
  }


  private void setSongImage(Song song, ImageView imageView) {
    String albumArtId = Repository.getInstance(getActivity()).getSongImgUri(song.getAlbumId());
    Uri imgUri = Uri.parse(albumArtId);
    imageView.setBackground(getResources().getDrawable(R.drawable.music_background));
    imageView.setImageURI(imgUri);
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


  public void savePreferences(String key, long value) {
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putLong(key, value);
    editor.apply();
  }
}