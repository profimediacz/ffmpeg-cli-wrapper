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
    
    ScaleFilter filter2 = new ScaleFilter();
    filter2.setWidth("iw/2");
    filter2.setHeight("ih/2");
    assertEquals(filter2.getCommandString(), "scale=w=iw/2:h=ih/2");
    
    filter2 = new ScaleFilter();
    filter2.setWidth("2*iw");
    filter2.setHeight("2*ih");
    filter2.setInterl(1);
    assertEquals(filter2.getCommandString(), "scale=w=2*iw:h=2*ih:interl=1");
  }
}
