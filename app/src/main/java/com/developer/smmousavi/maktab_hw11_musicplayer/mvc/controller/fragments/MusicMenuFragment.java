package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.controller.fragments;


import android.Manifest;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.smmousavi.maktab_hw11_musicplayer.R;
import com.developer.smmousavi.maktab_hw11_musicplayer.database.Repository;
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
  private List<Song> songList;
  private MediaPlayer mp;
  public static String tabName;


  public static MusicMenuFragment newInstance(String tabName) {
    MusicMenuFragment.tabName = tabName;

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
    sortSongList();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_music_menu, container, false);
    recyclerView = view.findViewById(R.id.music_menu_recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    setupAdapter();

    return view;
  }


  private void setupAdapter() {
    recyclerView.setAdapter(new MusicAdapter(songList));
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

  private class MusicViewHolder extends RecyclerView.ViewHolder {
    private Song selectedSong;
    private Song lastPlayingSong;
    private String songTitle;
    private String artist;
    private ImageView songImageCard;
    private TextView titleView;
    private TextView artistView;
    private RelativeLayout songTrackLayout;
    private Button songTrackBtn;

    public MusicViewHolder(@NonNull final View itemView) {
      super(itemView);
      titleView = itemView.findViewById(R.id.music_track_name);
      artistView = itemView.findViewById(R.id.music_track_artist);
      songImageCard = itemView.findViewById(R.id.music_track_image);
      songTrackLayout = itemView.findViewById(R.id.music_track_layout);
      songTrackBtn = itemView.findViewById(R.id.song_track_btn);


      songTrackBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          lastPlayingSong = selectedSong;

          playInList();

        }
      });
    }


    private void playInList() {
      if (mp == null) {
        playAudio(selectedSong.getUri());
        selectedSong.setPlaying(true);

      } else if (lastPlayingSong.getId() == selectedSong.getId()) {
        restAudio(mp, selectedSong.getUri());

      } else if (lastPlayingSong.getId() != selectedSong.getId()) {
        stopAudio(mp);
        lastPlayingSong.setPlaying(false);
        lastPlayingSong = selectedSong.cloneSong();
        playAudio(selectedSong.getUri());
        selectedSong.setPlaying(true);
      }
    }

    public void onBinde(Song song) {
      selectedSong = song;
      songTitle = song.getDisplayingName();
      artist = song.getArtist();
      titleView.setText(songTitle);
      artistView.setText(artist);

      String albumArtId = Repository.getInstance(getActivity()).getSongImgUri(song.getAlbumId());
      Log.i("TAG9", albumArtId + "");
      if (albumArtId == null) {
        albumArtId = "";
      }

      Uri imgUri = Uri.parse(albumArtId);
      setSongCover(imgUri);

    }

    private void setSongCover(Uri img_url) {
      if (!img_url.equals("")) {
        Picasso.get()
          .load(img_url)
          .placeholder(R.drawable.music_background)// Place holder image from drawable folder
          .error(R.drawable.music_background)
          .resize(52, 55)
          .into(songImageCard);
      }
    }
  }


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
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
      holder.onBinde(songs.get(position));

    }

    @Override
    public int getItemCount() {
      return songs.size();
    }
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
  }


  public void pauseAudio(MediaPlayer mp) {
    try {
      mp.pause();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void restAudio(MediaPlayer mp, String path) {
    try {
      mp.reset();
      playAudio(path);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  public void stopAudio(MediaPlayer mp) {
    try {
      mp.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
