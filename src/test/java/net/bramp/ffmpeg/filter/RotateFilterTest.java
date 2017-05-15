package net.bramp.ffmpeg.filter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RotateFilterTest {

  @Test
  public void testCreateScaleFilter() {

    VideoFilter filter = new RotateFilter("PI/2");
    assertEquals(filter.getCommandString(), "rotate=PI/2");
    
    RotateFilter filter2 = new RotateFilter("");
    filter2.setAngle(45);
    assertEquals(filter2.getCommandString(), "rotate=45*PI/180");

    filter2 = new RotateFilter();
    filter2.setAngle("PI/3+2*PI*t/T");
    assertEquals(filter2.getCommandString(), "rotate=PI/3+2*PI*t/T");

    filter2 = new RotateFilter();
    filter2.setAngle("2*PI*t");
    filter2.setOutW("hypot(iw,ih)");
    filter2.setOutH("ow");
    assertEquals(filter2.getCommandString(), "rotate=2*PI*t:ow=hypot(iw,ih):oh=ow");

    filter2 = new RotateFilter();
    filter2.setAngle("2*PI*t");
    filter2.setOutW("min(iw,ih)/sqrt(2)");
    filter2.setOutH("ow");
    filter2.setFillColor("none");
    assertEquals(filter2.getCommandString(), "rotate=2*PI*t:ow=min(iw,ih)/sqrt(2):oh=ow:c=none");
  }
}
