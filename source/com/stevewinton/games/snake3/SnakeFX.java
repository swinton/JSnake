package com.stevewinton.games.snake3;

import java.io.IOException;

import java.net.URL;

import java.util.Hashtable;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.stevewinton.games.snake3.event.*;

public class SnakeFX implements FoodEatenEventListener, Runnable {
  private static final int  EXTERNAL_BUFFER_SIZE = 128000;
  private Hashtable<String, URL> resources = new Hashtable<String, URL>();
  private String[] effects = {"belch.wav", "crunch.wav"};
  private int counter = 0;

  void test() {
    for (String effect : effects) {
      play(effect);
    }
  }

  void init() {
    for (String effect : effects) {
      URL url = getClass().getClassLoader().getResource("audio/" + effect);
      if (url != null)
        resources.put(effect, url);
    }
  }

  public void foodEaten(FoodEatenEvent e) {
    Thread t = new Thread(this);
    t.start();
  }

  public void run() {
    if (++counter % 10 == 0)
      play("belch.wav");
    else
      play("crunch.wav");
  }

  void play(String effect) {
    if (resources.containsKey(effect)) {
      AudioInputStream input;
      try {
        input = AudioSystem.getAudioInputStream(resources.get(effect));
      }
      catch (Exception e) {
        return;
      }
      AudioFormat format = input.getFormat();
      play(input, format, new DataLine.Info(SourceDataLine.class, format));
    }
  }

  void play(AudioInputStream input, AudioFormat format, DataLine.Info info) {
    SourceDataLine line;
    try {
      line = (SourceDataLine) AudioSystem.getLine(info);
      line.open(format);
    } 
    catch (LineUnavailableException e) {
      e.printStackTrace();
      return;
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }

    line.start();
    int bytesRead = 0;
    byte[] data = new byte[EXTERNAL_BUFFER_SIZE];
    while (bytesRead != -1) {
      try {
        bytesRead = input.read(data, 0, data.length);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      if (bytesRead >= 0) {
        int bytesWritten = line.write(data, 0, bytesRead);
      }
    }
    line.drain();
    line.close();
  }

  public static void main(String[] args) {
    SnakeFX fx = new SnakeFX();
    fx.init();
    fx.test();
  }

}

