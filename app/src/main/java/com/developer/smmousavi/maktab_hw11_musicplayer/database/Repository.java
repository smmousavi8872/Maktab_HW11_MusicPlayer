package com.developer.smmousavi.maktab_hw11_musicplayer.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Song;

import java.util.ArrayList;
import java.util.List;

public class Repository {

  private static Repository instance;
  private Context context;

  private Repository(Context context) {
    this.context = context;

  }


  public static Repository getInstance(Context context) {
    if (instance != null)
      return instance;

    return new Repository(context);
  }


  public Song getSong(long songId) {
    List<Song> songs = getSongList();
    for (Song song : songs) {
      if (song.getId() == songId)
        return song;

    }
    return null;
  }


  public List<Song> getSongList() {
    List<Song> songs = new ArrayList<>();
    Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    SongCursorWrapper cursor = getSongQuery(musicUri, null, null);
    if (cursor.getCount() > 0) {
      try {
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
          songs.add(cursor.getSong());
          cursor.moveToNext();
        }

      } finally {
        cursor.close();
      }
    }
    return songs;
  }// end of getSongList()


  public String getSongImgUri(long albumId) {
    String path = "";
    Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
      new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
      MediaStore.Audio.Albums._ID + "=?",
      new String[]{String.valueOf(albumId)},
      null);

    if (cursor.moveToFirst()) {
      path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
      // do whatever you need to do
    }
    return path;
  } // end of getSongImgUri()


  public SongCursorWrapper getSongQuery(Uri musicUri, String whereClause, String[] whereArgs) {
    ContentResolver musicResolver = context.getContentResolver();
    musicResolver.getPersistedUriPermissions();
    Cursor musicCursor = musicResolver.query(
      musicUri,
      null,
      whereClause,
      whereArgs,
      null,
      null);

    return new SongCursorWrapper(musicCursor);

  }// end of getSongQuery()


}