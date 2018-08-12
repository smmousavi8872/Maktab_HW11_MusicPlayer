package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;

import com.developer.smmousavi.maktab_hw11_musicplayer.R;
import com.developer.smmousavi.maktab_hw11_musicplayer.database.Repository;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.activities.MusicPlayActivity;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Song;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class MusicMenuFragment extends Fragment {

  public static final String FRAGMENT_TAG = "music_menu_fragment";
  private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;

  private RecyclerView recyclerView;
  private MusicAdapter musicAdapter;
  private List<Song> songList;
  private MediaPlayer mp;
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
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    songList = new ArrayList<>();
    songList = getSongListPermission();
    setRetainInstance(true);
    sortSongList();
    findParentViews();
    currentPlayingMusicLayout.bringToFront();

    currentSongUserPanelBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = MusicPlayActivity.newIntent(getContext());
        startActivity(intent);
      }
    });
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
    musicAdapter.notifyDataSetChanged();
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


  private class MusicViewHolder extends RecyclerView.ViewHolder {
    private Song selectedSong;
    private Song lastPlayingSong;
    private String songTitle;
    private String artist;
    private ImageView songImageCard;
    private TextView titleView;
    private TextView artistView;
    private Button songTrackBtn;
    private int currentPositoin = getAdapterPosition();

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

      songTrackBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (lastPlayingSong != null)
            lastPlayingSong.setPlaying(false);

          lastPlayingSong = selectedSong;
          lastPlayingSong.setPlaying(true);

          userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

          playInList();

          setCurrentSongText();

          setUserMusicPanel();
        }
      });
    }// end of onBinde()


    public void setUserMusicPanel() {

      userPanelPlayBtn.setFocusableInTouchMode(false);

      userPanelPlayBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          playOrPause();
        }
      });

      userPanelNextBtn.setOnClickListener(new View.OnClickListener() {
        int nextPos = getAdapterPosition();

        @Override
        public void onClick(View view) {
          if (nextPos < songList.size() - 1)
            nextPos++;

          else
            nextPos = 0;

          stopAudio(mp, selectedSong);
          selectedSong = songList.get(nextPos);
          playAudio(selectedSong.getUri());

          userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

          setCurrentSongText();

          setupAdapter();

          recyclerView.scrollToPosition(nextPos);
        }
      });

      userPanelPrevBtn.setOnClickListener(new View.OnClickListener()

      {
        int prevPos = getAdapterPosition();

        @Override
        public void onClick(View view) {
          if (prevPos > 1)
            prevPos--;

          else
            prevPos = songList.size() - 1;

          stopAudio(mp, selectedSong);
          selectedSong = songList.get(prevPos);
          playAudio(selectedSong.getUri());

          userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

          setCurrentSongText();

          setupAdapter();

          recyclerView.scrollToPosition(prevPos);
        }
      });
    }// end of setUserMusicPanel()


    public void setCurrentSongText() {
      currentPlayingMusicName.setText(selectedSong.getDisplayingName());
      currentPlayingMusicArtist.setText(selectedSong.getArtist());
      setSongImage(selectedSong, currentPlayingMusicImageView);
    }// end of setCurrentSongText()


    private void playInList() {
      if (mp == null) {
        playAudio(selectedSong.getUri());
        selectedSong.setPlaying(true);

      } else if (lastPlayingSong.getId() == selectedSong.getId()) {
        resetAudio(mp, selectedSong.getUri());

      } else if (lastPlayingSong.getId() != selectedSong.getId()) {
        stopAudio(mp, selectedSong);
        lastPlayingSong.setPlaying(false);
        lastPlayingSong = selectedSong.cloneSong();
        playAudio(selectedSong.getUri());
        selectedSong.setPlaying(true);
      }
    }// end of playInList()


    private void playOrPause() {
      if (selectedSong.isPlaying())
        pauseAudio(mp, selectedSong);

      else
        startAudio(mp, selectedSong);

      setUserPanelPlayIcon();
    }// end of playOrPause()


    private void setUserPanelPlayIcon() {
      if (selectedSong.isPlaying())
        userPanelPlayIcon.setBackgroundResource(R.drawable.ic_pause_button);

      else
        userPanelPlayIcon.setBackgroundResource(R.drawable.ic_play_button);
    }// end of setUserPanelPlayIcon()


    private void setSongImage(Song song, ImageView imageView) {
      String albumArtId = Repository.getInstance(getActivity()).getSongImgUri(song.getAlbumId());
      if (albumArtId == null)
        albumArtId = "";

      Uri imgUri = Uri.parse(albumArtId);

      if (!imgUri.equals("")) {
        Picasso.get()
          .load(imgUri)
          .placeholder(R.drawable.music_background)// Place holder image from drawable folder
          .error(R.drawable.music_background)
          .resize(52, 55)
          .into(imageView);
      }
    }
  }// end of MusicViewHolder{}


  private class MusicAdapter extends RecyclerView.Adapter<MusicViewHolder> {
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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }// end of stopAudio()

}
