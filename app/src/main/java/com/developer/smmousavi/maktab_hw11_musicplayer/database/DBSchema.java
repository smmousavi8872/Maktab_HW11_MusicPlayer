package com.developer.smmousavi.maktab_hw11_musicplayer.database;

public class DBSchema {
  public static final String VERSION = "version";

  public static final class SongTable {
    public static final String NAME = "song_table";

    public static final class Cols {
      public static final String ID = "song_id";
      public static final String URI = "song_uri";
      public static final String TITLE = "title";
      public static final String ARTIST = "artist";
      public static final String ALBUM = "album";
      public static final String DISPLAYING_NAME = "displaying_name";
      public static final String LYRICS = "lyrics";
      public static final String ALBUM_ART_ID = "album_art_id";
      public static final String DURATION = "duration";
      public static final String IS_PLAYING = "is_playing";
      public static final String IS_FAVARIOT = "is_favariot";
    }
  }// end of SongTable{}


  public static final class LyricsTable {
    public static final String NAME = "lyrics_table";

    public static final class Cols {
      public static final String SONG_ID = "song_id";
      public static final String SYNCED_LYRICS_TEXT = "synced_lyrics_text";
      public static final String UNSYNCED_LYRICS_TEXT = "unsynced_lyrics_text";
    }
  }// end of LyricsTable{}

}
