package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model;

import java.util.ArrayList;
import java.util.List;

public class Lyrics {

  private long songId;
  private String syncedLyrics;
  private String unsyncedLyrics;
  private List<String> lyricParts;
  private long lineCounter;

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


  public List<String> splitLyricsString() {
    String[] roughSplits = unsyncedLyrics.split("\\r?\\n");
    List<String> lyricsSplits = new ArrayList<>();
    for (String split : roughSplits) {
      if (!split.trim().equals(""))
        lyricsSplits.add(split);
    }

    return lyricsSplits;
  }


  public void parseLyric(double startTime, double endTime, String lyricText) {


    int startMilis = (int) (startTime / 1000.0 - Math.floor(startTime / 1000.0)) * 1000;
    int startSec = (int) (startTime / 1000) % 60;
    int startMin = (int) ((startTime / (1000 * 60)) % 60);
    int startHour = (int) ((startTime / (1000 * 60 * 60)) % 24);

    int endMilis = (int) (endTime / 1000.0 - Math.floor(endTime / 1000.0)) * 1000;
    int endSec = (int) (endTime / 1000) % 60;
    int endMin = (int) ((endTime / (1000 * 60)) % 60);
    int endHour = (int) ((endTime / (1000 * 60 * 60)) % 24);

    String lyric = String.format("%d%n%02d:%02d:%02d,%03d -->%02d:%02d:%02d,%03d%n%s%n%n",
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