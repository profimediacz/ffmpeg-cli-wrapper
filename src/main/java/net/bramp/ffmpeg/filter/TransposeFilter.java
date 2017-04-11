package net.bramp.ffmpeg.filter;

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

  public TransposeFilter(Dir dir) {
    this.dir = dir.getValue();
    this.passthrough = Passthrough.NONE.getName();
  }

  public TransposeFilter(Dir dir, Passthrough passthrough) {
    this.dir = dir.getValue();
    this.passthrough = passthrough.getName();
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
