package net.bramp.ffmpeg.filter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ScaleFilterTest {

  @Test
  public void testCreateScaleFilter() {

    VideoFilter filter = new ScaleFilter("200", "100");
    assertEquals(filter.getCommandString(), "scale=w=200:h=100");
    filter = new ScaleFilter(200, 100);
    assertEquals(filter.getCommandString(), "scale=w=200:h=100");
    filter = new ScaleFilter("300");
    assertEquals(filter.getCommandString(), "scale=w=300:h=-1");
    filter = new ScaleFilter(300);
    assertEquals(filter.getCommandString(), "scale=w=300:h=-1");
  }
}
