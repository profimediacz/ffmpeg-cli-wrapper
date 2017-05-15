package com.clapix.ffmpeg.probe.helper;

import java.io.IOException;
import java.util.List;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.info.Codec;
import net.bramp.ffmpeg.probe.FFmpegStream;

public class FFmpegStreamHelper {

  public static Codec searchCodec(String codecName, FFprobe ffprobe) throws IOException {

    List<Codec> codecs = ffprobe.codecs();
    Codec codecFound = null;
    for (Codec codec : codecs) {
      if (codec.getName().equalsIgnoreCase(codecName)) {
        codecFound = codec;
      }
    }
    return codecFound;
  }

  public static boolean canEncode(FFmpegStream stream, FFprobe ffprobe) throws IOException {

    Codec codec = searchCodec(stream.codec_name, ffprobe);
    if (codec != null) {
      return codec.getCanEncode();
    } else {
      return false;
    }
  }

  public static boolean canDecode(FFmpegStream stream, FFprobe ffprobe) throws IOException {

    Codec codec = searchCodec(stream.codec_name, ffprobe);
    if (codec != null) {
      return codec.getCanDecode();
    } else {
      return false;
    }
  }

  public static String searchTag(String tagName, FFmpegStream stream) {
    if (stream.tags == null) {
      return null;
    } else {
      return stream.tags.get(tagName);
    }
  }

}
