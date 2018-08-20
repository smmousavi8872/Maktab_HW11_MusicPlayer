package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.developer.smmousavi.maktab_hw11_musicplayer.R;
import com.developer.smmousavi.maktab_hw11_musicplayer.database.Repository;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Lyrics;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Song;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
enum SyncTimeStatus {
  NONE,
  START,
  END,
  DONE
}

public class AddLyricsFragment extends Fragment {

  public static final String FRAGMENT_TAG = "add_lyrics_fargment_tag";
  public static final String ARGS_SONG_ID = "args_song_id";

  private TextInputEditText songLyricsEdt;
  private MaterialButton syncLyricsBtn;
  private MaterialButton addLyricsBtn;
  private AppCompatTextView showSyncingLyricTxt;
  private AppCompatSeekBar syncSeekBar;
  private TextView syncingMusicCurrentTimeTxt;
  private TextView syncingMusicDurationTxt;
  private ImageView syncRevertImg;
  private ImageView syncRemoveImg;
  private MaterialButton startSyncBtn;

  private Handler mHandler;
  private Lyrics currentSongLyrics;
  private MediaPlayer mediaPlayer;
  private Song currentSong;

  private SyncTimeStatus timeStatus = SyncTimeStatus.NONE;
  private int splitConuter = 0;
  private double startTime = 0;
  private double endTime = 0;


  public AddLyricsFragment() {
    // Required empty public constructor
  }


  public static AddLyricsFragment newInstance(long songId) {

    Bundle args = new Bundle();
    args.putLong(ARGS_SONG_ID, songId);

    AddLyricsFragment fragment = new AddLyricsFragment();
    fragment.setArguments(args);
    return fragment;
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mediaPlayer = new MediaPlayer();
    currentSong = MusicMenuFragment.classSelectedSong;
  }


  @Override
  public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_add_lyrics, container, false);
    findViews(view);

    syncRemoveImg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Repository.getInstance(getActivity()).removeSongLyrics(currentSongLyrics);
        songLyricsEdt.setText("");
        getActivity().finish();
      }
    });
    final Song currentSong = MusicMenuFragment.classSelectedSong;
    currentSongLyrics = Repository.getInstance(getActivity()).getSongLyric(currentSong.getId());
    if (currentSongLyrics != null) {
      songLyricsEdt.setText(currentSongLyrics.getUnsyncedLyrics());
      addLyricsBtn.setText("Update Lyrics");
      addLyricsBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          String lyrics = songLyricsEdt.getText().toString();
          if (!songLyricsEdt.getText().toString().equals("")) {
            currentSongLyrics.setUnsyncedLyrics(lyrics);
            Repository.getInstance(getActivity()).updateSongLyrics(currentSongLyrics);
            getActivity().finish();

          } else
            Snackbar.make(getView(), "Lyrics must have at least one sentance", Snackbar.LENGTH_SHORT).show();
        }
      });
    } else {
      addLyricsBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          String lyricsText = songLyricsEdt.getText().toString();
          if (!songLyricsEdt.getText().toString().equals("")) {
            currentSongLyrics = new Lyrics(currentSong.getId());
            currentSongLyrics.setUnsyncedLyrics(lyricsText);
            Repository.getInstance(getActivity()).addSongLyrics(currentSongLyrics);
            getActivity().finish();

          } else
            Snackbar.make(getView(), "Lyrics must have at least one sentance", Snackbar.LENGTH_SHORT).show();
        }
      });
    }

    syncLyricsBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!songLyricsEdt.getText().toString().equals("")) {
          final List<String> splitLyrics;
          setSeekBarActions(currentSong);

          if (currentSongLyrics == null) {
            currentSongLyrics = new Lyrics(currentSong.getId());
            currentSongLyrics.setUnsyncedLyrics(songLyricsEdt.getText().toString());
            splitLyrics = currentSongLyrics.splitLyricsString();

          } else {
            splitLyrics = currentSongLyrics.splitLyricsString();

          }
          showSyncingLyricTxt.setEnabled(true);
          startSyncBtn.setEnabled(true);
          songLyricsEdt.setEnabled(false);
          addLyricsBtn.setEnabled(false);
          syncLyricsBtn.setEnabled(false);
          syncRevertImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_resync));
          syncRemoveImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_off));

          syncRemoveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
          });

          startSyncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if (mediaPlayer == null || !mediaPlayer.isPlaying())
                playAudio(currentSong.getUri());

              switch (timeStatus) {
                case NONE:
                  timeStatus = SyncTimeStatus.START;
                  startSyncBtn.setText("Start Time");
                  break;
                case START:
                  startTime = mediaPlayer.getCurrentPosition();
                  timeStatus = SyncTimeStatus.END;
                  startSyncBtn.setText("End Time");
                  break;
                case END:
                  endTime = mediaPlayer.getCurrentPosition();
                  timeStatus = SyncTimeStatus.START;
                  currentSongLyrics.parseLyric(startTime, endTime, splitLyrics.get(splitConuter));
                  splitConuter++;
                  startSyncBtn.setText("Start Time");
                  if (splitConuter >= splitLyrics.size())
                    startSyncBtn.setText("Finish");

                  break;
                case DONE:
                  showSyncingLyricTxt.setText("All Done!");
                  startSyncBtn.setText("Sync Lyrics");
                  if (mediaPlayer.isPlaying())
                    pauseAudio(mediaPlayer, currentSong);

                  doneSyncing();
              }
              if (splitConuter < splitLyrics.size())
                showSyncingLyricTxt.setText(splitLyrics.get(splitConuter));

              else
                timeStatus = SyncTimeStatus.DONE;
            }
          });
        } else
          Snackbar.make(getView(), "Lyrics must have at least one sentance", Snackbar.LENGTH_SHORT).show();
      }
    });
    return view;
  }


  public void doneSyncing() {
    startSyncBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String completeLyrics = currentSongLyrics.generateSyncedLyricsText();
        currentSongLyrics.setSyncedLyrics(completeLyrics);
        Repository.getInstance(getActivity()).updateSongLyrics(currentSongLyrics);
        getActivity().finish();
      }
    });
  }


  private void findViews(View view) {
    songLyricsEdt = view.findViewById(R.id.edt_song_lyrics);
    syncLyricsBtn = view.findViewById(R.id.btn_sync_lyrics);
    addLyricsBtn = view.findViewById(R.id.btn_add_lyrics);
    showSyncingLyricTxt = view.findViewById(R.id.txt_synicing_lyric);
    syncSeekBar = view.findViewById(R.id.sync_song_seek_bar);
    syncingMusicCurrentTimeTxt = view.findViewById(R.id.syncing_music_current_time);
    syncingMusicDurationTxt = view.findViewById(R.id.syncing_music_duration);
    syncRevertImg = view.findViewById(R.id.sync_revert);
    syncRemoveImg = view.findViewById(R.id.sync_remove);
    startSyncBtn = view.findViewById(R.id.btn_start_sync);
  }


  private void setSeekBarActions(Song song) {
    int duration = (int) song.getDuration();
    syncSeekBar.setMax(duration);
    mHandler = new Handler();
    int min = duration / 1000 / 60;
    int sec = duration / 1000 % 60;
    syncingMusicDurationTxt.setText(getResources().getString(R.string.music_duration, min, sec));

    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (mediaPlayer != null) {
          int mCurrentPosition = mediaPlayer.getCurrentPosition();
          syncSeekBar.setProgress(mCurrentPosition);
          int min = mCurrentPosition / 1000 / 60;
          int sec = mCurrentPosition / 1000 % 60;
          String currentTimeStr = String.format("%02d:%02d", min, sec);
          syncingMusicCurrentTimeTxt.setText(currentTimeStr);
        }
        mHandler.postDelayed(this, 1000);
      }
    });
    syncSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mediaPlayer != null && fromUser) {
          mediaPlayer.seekTo(progress);
        }
      }
    });
  }// end of setSeekBarActions()


  public void playAudio(String path) {
    try {
      mediaPlayer = new MediaPlayer();
      mediaPlayer.setDataSource(path);
      mediaPlayer.prepare();
      mediaPlayer.start();
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


  @Override
  public void onDetach() {
    super.onDetach();
    pauseAudio(mediaPlayer, currentSong);
  }
}
