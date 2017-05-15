package net.bramp.ffmpeg.filter;

import static org.junit.Assert.*;

import org.junit.Test;

import net.bramp.ffmpeg.filter.TransposeFilter.Dir;
import net.bramp.ffmpeg.filter.TransposeFilter.Passthrough;

public class TransposeFilterTest {

  @Test
  public void testCreateTransposeFilter() {

    VideoFilter filter = new TransposeFilter(Dir.CCLOCK_FLIP);
    assertEquals(filter.getCommandString(), "transpose=dir=0:passthrough=none");
    filter = new TransposeFilter(Dir.CLOCK);
    assertEquals(filter.getCommandString(), "transpose=dir=1:passthrough=none");
    filter = new TransposeFilter(Dir.CCLOCK);
    assertEquals(filter.getCommandString(), "transpose=dir=2:passthrough=none");
    filter = new TransposeFilter(Dir.CLOCK_FLIP);
    assertEquals(filter.getCommandString(), "transpose=dir=3:passthrough=none");
    filter = new TransposeFilter(Dir.CLOCK, Passthrough.PORTRAIT);
    assertEquals(filter.getCommandString(), "transpose=dir=1:passthrough=portrait");
    
    TransposeFilter filter2 = new TransposeFilter();
    filter2.setRotateRight();
    filter2.setPortrait();
    assertEquals(filter2.getCommandString(), "transpose=dir=1:passthrough=portrait");
  }
}
