package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model;

public class Song {

  private long id;
  private String Uri;
  private String title;
  private String artist;
  private String album;
  private String displayingName;
  private long albumArtId;
  private long duration;
  private boolean playing;


  public Song(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }


  public String getTitle() {
    return title;
  }


  public void setTitle(String title) {
    this.title = title;
  }


  public String getArtist() {
    return artist;
  }


  public void setArtist(String artist) {
    this.artist = artist;
  }


  public String getAlbum() {
    return album;
  }


  public void setAlbum(String album) {
    this.album = album;
  }


  public String getUri() {
    return Uri;
  }

  public void setUri(String uri) {
    Uri = uri;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public boolean isPlaying() {
    return playing;
  }

  public void setPlaying(boolean playing) {
    this.playing = playing;
  }


  public long getAlbumId() {
    return albumArtId;
  }

  public void setAlbumId(long albumArtId) {
    this.albumArtId = albumArtId;
  }

  public String getDisplayingName() {
    return displayingName;
  }

  public void setDisplayingName(String displayingName) {
    this.displayingName = displayingName;
  }

  public Song cloneSong() {
    Song newSong = new Song(this.getId());
    newSong.setTitle(this.getTitle());
    newSong.setArtist(this.getArtist());
    newSong.setAlbum(this.getAlbum());
    newSong.setUri(this.getUri());
    newSong.setDuration(this.getDuration());
    newSong.setPlaying(this.isPlaying());
    newSong.setAlbumId(this.getAlbumId());

    return newSong;

  }
}
