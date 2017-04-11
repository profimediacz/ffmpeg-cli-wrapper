package net.bramp.ffmpeg.filter;

public class RotateFilter extends VideoFilter {

  private static final String FILTER_NAME = "rotate";

  private String angle;

  public RotateFilter(String angle) {
    this.angle = angle;
  }

  @Override
  protected String getFilterName() {
    return FILTER_NAME;
  }

  @Override
  public String getCommandString() {
    StringBuffer sb = new StringBuffer(getFilterName());
    sb.append("=").append(angle);
    return sb.toString();
  }
}
