package net.bramp.ffmpeg.filter;

public class OverlayFilter extends VideoFilter {

  private static final String FILTER_NAME = "overlay";
  private String x;
  private String y;

  public OverlayFilter(String x, String y) {
    super(FILTER_NAME);
    this.x = x;
    this.y = y;
  }

  @Override
  public String getCommandString() {
    StringBuffer sb = new StringBuffer(getFilterName());
    sb.append("=").append("x").append("=").append(x).append(":").append("y").append("=").append(y);
    return sb.toString();
  }
}
