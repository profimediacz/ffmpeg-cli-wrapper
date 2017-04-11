package net.bramp.ffmpeg.filter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RotateFilterTest {

  @Test
  public void testCreateScaleFilter() {

    VideoFilter filter = new RotateFilter("PI/2");
    assertEquals(filter.getCommandString(), "rotate=PI/2");
  }
}
