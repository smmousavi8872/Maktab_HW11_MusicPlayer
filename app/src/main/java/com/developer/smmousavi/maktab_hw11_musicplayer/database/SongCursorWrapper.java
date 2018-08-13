package com.developer.smmousavi.maktab_hw11_musicplayer.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.provider.MediaStore;

import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Song;

public class SongCursorWrapper extends CursorWrapper {
  public SongCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Song getSong() {
    long id = getLong(getColumnIndex(MediaStore.Audio.Media._ID));
    String title = getString(getColumnIndex(MediaStore.Audio.Media.TITLE));
    String displayingName = getString(getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
    String Uri = getString(getColumnIndex(MediaStore.Audio.Media.DATA));
    String artist = getString(getColumnIndex(MediaStore.Audio.Media.ARTIST));
    String album = getString(getColumnIndex(MediaStore.Audio.Media.ALBUM));
    long albumId = getLong(getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
    long duration = getLong(getColumnIndex(MediaStore.Audio.Media.DURATION));

    Song song = new Song(id);
    song.setUri(Uri);
    song.setTitle(title);
    song.setDisplayingName(displayingName);
    song.setArtist(artist);
    song.setArtist(album);
    song.setAlbumId(albumId);
    song.setDuration(duration);

    return song;
  }
}
