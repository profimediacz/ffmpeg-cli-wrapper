package net.bramp.ffmpeg.filter;

public class RotateFilter extends VideoFilter {

  private static final String FILTER_NAME = "rotate";

  private String angle;

  public RotateFilter(String angle) {
    super(FILTER_NAME);
    this.angle = angle;
  }

  @Override
  public String getCommandString() {
    StringBuffer sb = new StringBuffer(getFilterName());
    sb.append("=").append(angle);
    return sb.toString();
  }
}
