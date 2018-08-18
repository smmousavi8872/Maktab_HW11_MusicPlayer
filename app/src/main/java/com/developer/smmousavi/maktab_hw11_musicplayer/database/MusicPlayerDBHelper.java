package com.developer.smmousavi.maktab_hw11_musicplayer.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.developer.smmousavi.maktab_hw11_musicplayer.database.DBSchema.LyricsTable;
import com.developer.smmousavi.maktab_hw11_musicplayer.database.DBSchema.SongTable;


public class MusicPlayerDBHelper extends SQLiteOpenHelper {

  public static final int VERSION = 1;
  public static final String DATABASE_NAME = "MusicPlayerDB.db";

  public MusicPlayerDBHelper(Context context) {
    super(context, DATABASE_NAME, null, VERSION);
  }

  public static final String CREATE_SONG_TABLE =
    "create table if not exists " + SongTable.NAME + "(" +
      "_id integer primary key autoincrement, " +
      SongTable.Cols.ID + "," +
      SongTable.Cols.URI + "," +
      SongTable.Cols.TITLE + "," +
      SongTable.Cols.ARTIST + "," +
      SongTable.Cols.ALBUM + "," +
      SongTable.Cols.DISPLAYING_NAME + "," +
      SongTable.Cols.LYRICS + "," +
      SongTable.Cols.ALBUM_ART_ID + "," +
      SongTable.Cols.DURATION + "," +
      SongTable.Cols.IS_PLAYING + "," +
      SongTable.Cols.IS_FAVARIOT +
      ")";// end of CREATE_SONG_TABLE


  public static final String CREATE_LYRICS_TABLE =
    "create table if not exists " + LyricsTable.NAME + "(" +
      "_id integer primary key autoincrement, " +
      LyricsTable.Cols.SONG_ID + "," +
      LyricsTable.Cols.SYNCED_LYRICS_TEXT + "," +
      LyricsTable.Cols.UNSYNCED_LYRICS_TEXT +
      ")";// end of CREATE_SONG_TABLE


  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(CREATE_SONG_TABLE);
    sqLiteDatabase.execSQL(CREATE_LYRICS_TABLE);
  }


  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

  }

}
