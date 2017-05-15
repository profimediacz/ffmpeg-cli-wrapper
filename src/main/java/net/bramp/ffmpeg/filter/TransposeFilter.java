package net.bramp.ffmpeg.filter;

/**
 Transpose rows with columns in the input video and optionally flip it.
 https://www.ffmpeg.org/ffmpeg-filters.html#transpos
 It accepts the following parameters:
 dir
   Specify the transposition direction.
   Can assume the following values:
     ‘0, 4, cclock_flip’ - Rotate by 90 degrees counterclockwise and vertically flip (default), that is:
       L.R     L.l
       . . ->  . .
       l.r     R.r
     ‘1, 5, clock’ - Rotate by 90 degrees clockwise, that is:
       L.R     l.L
       . . ->  . .
       l.r     r.R
     ‘2, 6, cclock’ - Rotate by 90 degrees counterclockwise, that is:
       L.R     R.r
       . . ->  . .
       l.r     L.l
     ‘3, 7, clock_flip’ - Rotate by 90 degrees clockwise and vertically flip, that is:
       L.R     r.R
       . . ->  . .
       l.r     l.L
     For values between 4-7, the transposition is only done if the input video geometry is portrait and not landscape. 
     These values are deprecated, the passthrough option should be used instead.
     Numerical values are deprecated, and should be dropped in favor of symbolic constants.
 passthrough
   Do not apply the transposition if the input geometry matches the one specified by the specified value.
   It accepts the following values:
     ‘none’ - Always apply transposition.
     ‘portrait’ - Preserve portrait geometry (when height >= width).
     ‘landscape’ - Preserve landscape geometry (when width >= height).
   Default value is none.

 For example to rotate by 90 degrees clockwise and preserve portrait layout:
   transpose=dir=1:passthrough=portrait
 The command above can also be specified as:
   transpose=1:portrait
 */
public class TransposeFilter extends VideoFilter {

  public enum Dir {
    CCLOCK_FLIP(0),
    CLOCK(1),
    CCLOCK(2),
    CLOCK_FLIP(3);

    private final int value;

    private Dir(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }
  }

  public enum Passthrough {
    NONE("none"),
    PORTRAIT("portrait"),
    LANDSCAPE("landscape");

    private final String name;

    private Passthrough(String name) {
      this.name = name;
    }

    public String getName() {
      return this.name;
    }
  }

  private static final String FILTER_NAME = "transpose";
  private int dir;
  private String passthrough;

  public TransposeFilter() {
    this.dir = Dir.CCLOCK_FLIP.value;
    this.passthrough = Passthrough.NONE.getName();
  }

  public TransposeFilter(Dir dir) {
    this.dir = dir.getValue();
    this.passthrough = Passthrough.NONE.getName();
  }

  public TransposeFilter(Dir dir, Passthrough passthrough) {
    this.dir = dir.getValue();
    this.passthrough = passthrough.getName();
  }
  
  public void setDir(Dir dir) {
    this.dir = dir.getValue();
  }
  
  public void setPassthrough(Passthrough passthrough) {
    this.passthrough = passthrough.getName();
  }
  
  public void setRotateLeft() {
    setDir(Dir.CCLOCK);
  }
  
  public void setRotateLeftFlip() {
    setDir(Dir.CCLOCK_FLIP);
  }
  
  public void setRotateRight() {
    setDir(Dir.CLOCK);
  }
  
  public void setRotateRighFlip() {
    setDir(Dir.CLOCK_FLIP);
  }
  
  public void setPortrait() {
    setPassthrough(Passthrough.PORTRAIT);
  }
  
  public void setLandscape() {
    setPassthrough(Passthrough.LANDSCAPE);
  }

  @Override
  protected String getFilterName() {
    return FILTER_NAME;
  }

  @Override
  public String getCommandString() {
    StringBuffer sb = new StringBuffer(getFilterName());
    sb.append("=")
        .append("dir")
        .append("=")
        .append(dir)
        .append(":")
        .append("passthrough")
        .append("=")
        .append(passthrough);
    return sb.toString();
  }
}
