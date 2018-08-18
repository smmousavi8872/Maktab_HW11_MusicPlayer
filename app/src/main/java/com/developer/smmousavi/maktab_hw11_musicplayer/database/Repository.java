package com.developer.smmousavi.maktab_hw11_musicplayer.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;

import com.developer.smmousavi.maktab_hw11_musicplayer.database.DBSchema.LyricsTable;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Lyrics;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Song;

import java.util.ArrayList;
import java.util.List;

public class Repository {

  private static Repository instance;
  private Context context;
  private SQLiteDatabase mDatabase;

  private Repository(Context context) {
    this.context = context;
    mDatabase = new MusicPlayerDBHelper(context).getWritableDatabase();
  }


  public static Repository getInstance(Context context) {
    if (instance != null)
      return instance;

    instance = new Repository(context);
    return instance;
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

    if (cursor.moveToFirst())
      path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

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


  public void addSongLyrics(Lyrics lyrics) {
    ContentValues values = getLyricsContentValue(lyrics);
    mDatabase.insert(LyricsTable.NAME, null, values);
  }// end of addSongLyrics()


  public Lyrics getSongLyric(long songId) {
    String whereClause = LyricsTable.Cols.SONG_ID + " = " + songId;

    try (LyricsCursorWrapper cursor = lyricsQuery(LyricsTable.NAME, whereClause, null)) {
      if (cursor.getCount() == 0)
        return null;

      cursor.moveToFirst();
      return cursor.getLyrics();
    }
  }// end of getSongLyrics()


  private LyricsCursorWrapper lyricsQuery(String tableName, String whereClause, String[] whereArgs) {
    Cursor cursor = mDatabase.query(
      tableName,
      null, /* cols == null returns all cols */
      whereClause,
      whereArgs,
      null,
      null,
      null);

    return new LyricsCursorWrapper(cursor);
  } // end of queryAllTasks()


  public ContentValues getLyricsContentValue(Lyrics lyrics) {
    ContentValues values = new ContentValues();
    values.put(LyricsTable.Cols.SONG_ID, lyrics.getSongId());
    values.put(LyricsTable.Cols.SYNCED_LYRICS_TEXT, lyrics.getSyncedLyrics());
    values.put(LyricsTable.Cols.UNSYNCED_LYRICS_TEXT, lyrics.getUnsyncedLyrics());
    return values;
  }

}