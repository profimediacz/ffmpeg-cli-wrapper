/** */
package net.bramp.ffmpeg.filter;

public abstract class AbstractFilter {

  protected abstract String getFilterName();

  public abstract String getCommandString();

}
