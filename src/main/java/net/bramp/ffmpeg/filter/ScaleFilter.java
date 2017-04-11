package net.bramp.ffmpeg.filter;

public class ScaleFilter extends VideoFilter {

  private static final String FILTER_NAME = "scale";
  private static final String SIZE_CALCULATED = "-1";
  //private static final String SIZE_NO_CHANGE = "0";

  private String width;
  private String height;

  public ScaleFilter(String width, String height) {
    this.width = width;
    this.height = height;
  }

  public ScaleFilter(Integer width, Integer height) {
    this.width = width.toString();
    this.height = height.toString();
  }

  public ScaleFilter(String width) {
    this.width = width;
    this.height = SIZE_CALCULATED;
  }

  public ScaleFilter(Integer width) {
    this.width = width.toString();
    this.height = "-1";
  }

  @Override
  protected String getFilterName() {
    return FILTER_NAME;
  }

  @Override
  public String getCommandString() {
    StringBuffer sb = new StringBuffer(getFilterName());
    sb.append("=")
        .append("w")
        .append("=")
        .append(width)
        .append(":")
        .append("h")
        .append("=")
        .append(height);
    return sb.toString();
  }
}
