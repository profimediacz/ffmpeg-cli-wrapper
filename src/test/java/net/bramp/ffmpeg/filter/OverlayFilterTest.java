package net.bramp.ffmpeg.filter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class OverlayFilterTest {

  @Test
  public void testCreateOverlayFilter() {

    VideoFilter filter = new OverlayFilter("200", "100");
    assertEquals(filter.getCommandString(), "overlay=x=200:y=100");
  }
}
