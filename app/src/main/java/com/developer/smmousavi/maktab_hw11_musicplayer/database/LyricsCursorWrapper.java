package com.developer.smmousavi.maktab_hw11_musicplayer.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.developer.smmousavi.maktab_hw11_musicplayer.database.DBSchema.LyricsTable;
import com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model.Lyrics;

public class LyricsCursorWrapper extends CursorWrapper {
  public LyricsCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Lyrics getLyrics() {
    long songId = getLong(getColumnIndex(LyricsTable.Cols.SONG_ID));
    String syncedLyricsText = getString(getColumnIndex(LyricsTable.Cols.SYNCED_LYRICS_TEXT));
    String unsyncedLyricsText = getString(getColumnIndex(LyricsTable.Cols.UNSYNCED_LYRICS_TEXT));

    Lyrics lyrics = new Lyrics(songId);
    lyrics.setSyncedLyrics(syncedLyricsText);
    lyrics.setUnsyncedLyrics(unsyncedLyricsText);

    return lyrics;
  }

}
