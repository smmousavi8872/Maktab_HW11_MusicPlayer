package com.developer.smmousavi.maktab_hw11_musicplayer.mvc.model;

public class Song {

  private long id;
  private String title;
  private String artist;
  private String album;

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
}
