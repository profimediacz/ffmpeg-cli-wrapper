/** */
package net.bramp.ffmpeg.filter;

public abstract class AbstractFilter {

  protected AbstractFilter(String filterName) {
    super();
    this.filterName = filterName;
  }

  protected String filterName = "UNDEFINED";

  protected String getFilterName() {
    return filterName;
  }

  public abstract String getCommandString();
}
