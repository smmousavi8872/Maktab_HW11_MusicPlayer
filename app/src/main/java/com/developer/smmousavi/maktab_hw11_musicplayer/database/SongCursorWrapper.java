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
    String title = getString(getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE));
    String artist = getString(getColumnIndex(MediaStore.Audio.Media.ARTIST));
    String album = getString(getColumnIndex(MediaStore.Audio.Media.ALBUM));

    Song song = new Song(id);
    song.setTitle(title);
    song.setArtist(artist);
    song.setArtist(album);

    return song;
  }
}
