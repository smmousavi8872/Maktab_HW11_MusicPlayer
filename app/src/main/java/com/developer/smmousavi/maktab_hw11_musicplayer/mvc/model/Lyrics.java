package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model;

import java.util.ArrayList;
import java.util.List;

public class Lyrics {

  private long songId;
  private String syncedLyrics;
  private String unsyncedLyrics;
  private List<String> lyricParts;
  private long lineCounter;
  private long startTimeMillis;
  private long endTimeMillis;
  private String lyric;

  public Lyrics(long songId) {
    this.songId = songId;
    lyricParts = new ArrayList<>();
    lineCounter = 1;
  }


  public List<String> getLyricParts() {
    return lyricParts;
  }


  public long getSongId() {
    return songId;
  }

  public String getSyncedLyrics() {
    return syncedLyrics;
  }

  public void setSyncedLyrics(String syncedLyrics) {
    this.syncedLyrics = syncedLyrics;
  }

  public String getUnsyncedLyrics() {
    return unsyncedLyrics;
  }

  public void setUnsyncedLyrics(String unsyncedLyrics) {
    this.unsyncedLyrics = unsyncedLyrics;
  }

  public long getLineCounter() {
    return lineCounter;
  }


  public void setLineCounter(long lineCounter) {
    this.lineCounter = lineCounter;
  }


  public long getStartTimeMillis() {
    return startTimeMillis;
  }


  public void setStartTimeMillis(long startTimeMillis) {
    this.startTimeMillis = startTimeMillis;
  }


  public long getEndTimeMillis() {
    return endTimeMillis;
  }


  public void setEndTimeMillis(long endTimeMillis) {
    this.endTimeMillis = endTimeMillis;
  }


  public String getLyric() {
    return lyric;
  }


  public void setLyric(String lyric) {
    this.lyric = lyric;
  }


  public void parseLyric() {
    double startSeconds = getStartTimeMillis() / 1000.0;
    double endSeconds = getEndTimeMillis() / 1000.0;
    String lyricText = getLyric();

    int startHour = (int) startSeconds / 3600;
    int startMin = (int) startSeconds % 3600 / 60;
    int startSec = (int) startSeconds % 3600 % 60;
    int startMilis = (int) (startSeconds - Math.floor(startSeconds)) * 1000;

    int endHour = (int) endSeconds / 3600;
    int endMin = (int) endSeconds % 3600 / 60;
    int endSec = (int) endSeconds % 3600 % 60;
    int endMilis = (int) (endSeconds - Math.floor(endSeconds)) * 1000;

    String lyric = String.format("%d%n%02d:%02d:%02d,%3d -->%02d:%02d:%02d,%3d%n%s%n%n",
      lineCounter, startHour, startMin, startSec, startMilis, endHour, endMin, endSec, endMilis, lyricText);

    lyricParts.add(lyric);

    lineCounter++;
  }


  public String generateSyncedLyricsText() {
    String syncedLyricsText = "";
    for (String lyric : lyricParts) {
      syncedLyricsText += lyric;
    }
    setSyncedLyrics(syncedLyricsText);

    return syncedLyricsText;
  }

}