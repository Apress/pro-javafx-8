/*
 * Copyright (c) 2011, Pro JavaFX Authors
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JFXtras nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package projavafx.audioplayer4;

import javafx.beans.property.*;
import javafx.collections.MapChangeListener;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author dean
 */
public final class MediaModel {
  private static final String DEFAULT_IMG_URL = 
          MediaModel.class.getResource("resources/defaultAlbum.png").toString();
  
  private static final Image DEFAULT_ALBUM_COVER = 
          new Image(DEFAULT_IMG_URL.toString());

  private final StringProperty album = new SimpleStringProperty(this, "album");
  private final StringProperty artist = new SimpleStringProperty(this,"artist");
  private final StringProperty title = new SimpleStringProperty(this, "title");
  private final StringProperty year = new SimpleStringProperty(this, "year");
  
  private final ObjectProperty<Image> albumCover = 
          new SimpleObjectProperty<Image>(this, "albumCover");
  
  private final ReadOnlyObjectWrapper<MediaPlayer> mediaPlayer = 
          new ReadOnlyObjectWrapper<MediaPlayer>(this, "mediaPlayer");

  public MediaModel() {
    resetProperties();
  }

  public void setURL(String url) {
    if (mediaPlayer.get() != null) {
      mediaPlayer.get().stop();
    }
    
    initializeMedia(url);
  }

  public String getAlbum() { return album.get(); }
  public void setAlbum(String value) { album.set(value); }
  public StringProperty albumProperty() { return album; }

  public String getArtist() { return artist.get(); }
  public void setArtist(String value) { artist.set(value); }
  public StringProperty artistProperty() { return artist; }

  public String getTitle() { return title.get(); }
  public void setTitle(String value) { title.set(value); }
  public StringProperty titleProperty() { return title; }

  public String getYear() { return year.get(); }
  public void setYear(String value) { year.set(value); }
  public StringProperty yearProperty() { return year; }

  public Image getAlbumCover() { return albumCover.get(); }
  public void setAlbumCover(Image value) { albumCover.set(value); }
  public ObjectProperty<Image> albumCoverProperty() { return albumCover; }

  public MediaPlayer getMediaPlayer() { return mediaPlayer.get(); }
  public ReadOnlyObjectProperty<MediaPlayer> mediaPlayerProperty() { 
    return mediaPlayer.getReadOnlyProperty();
  }

  private void resetProperties() {
    setArtist("");
    setAlbum("");
    setTitle("");
    setYear("");
    
    setAlbumCover(DEFAULT_ALBUM_COVER);
  }

  private void initializeMedia(String url) {
    resetProperties();
    
    try {
      final Media media = new Media(url);
      media.getMetadata().addListener(new MapChangeListener<String, Object>() {
        @Override
        public void onChanged(Change<? extends String, ? extends Object> ch) {
          if (ch.wasAdded()) {
            handleMetadata(ch.getKey(), ch.getValueAdded());
          }
        }
      });

      mediaPlayer.setValue(new MediaPlayer(media));
      mediaPlayer.get().setOnError(new Runnable() {
        @Override
        public void run() {
          String errorMessage = mediaPlayer.get().getError().getMessage();
          // Handle errors during playback
          System.out.println("MediaPlayer Error: " + errorMessage);
        }
      });
    } catch (RuntimeException re) {
      // Handle construction errors
      System.out.println("Caught Exception: " + re.getMessage());
    }
  }

  private void handleMetadata(String key, Object value) {
    if (key.equals("album")) {
      setAlbum(value.toString());
    } else if (key.equals("artist")) {
      setArtist(value.toString());
    } if (key.equals("title")) {
      setTitle(value.toString());
    } if (key.equals("year")) {
      setYear(value.toString());
    } if (key.equals("image")) {
      setAlbumCover((Image)value);
    }
  }
}
