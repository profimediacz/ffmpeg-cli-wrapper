package net.bramp.ffmpeg;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharStreams;

import net.bramp.ffmpeg.info.Codec;
import net.bramp.ffmpeg.info.Format;
import net.bramp.ffmpeg.io.ProcessUtils;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/** Private class to contain common methods for both FFmpeg and FFprobe. */
abstract class FFcommon {

  /** Path to the binary (e.g. /usr/bin/ffmpeg) */
  final String path;

  /** Function to run FFmpeg. We define it like this so we can swap it out (during testing) */
  final ProcessFunction runFunc;

  /** Version string */
  String version = null;

  static final Pattern CODECS_REGEX_OLD =
      Pattern.compile("^ ([ D][ E][VAS][ S][ D][ T]) (\\S+)\\s+(.*)$");
  static final Pattern CODECS_REGEX =
      Pattern.compile("^ ([\\.D][\\.E][VAS][\\.I][\\.L][\\.S]) (\\S+)\\s+(.*)$");
  static final Pattern FORMATS_REGEX = Pattern.compile("^ ([ D][ E]) (\\S+)\\s+(.*)$");

  /** Supported codecs */
  List<Codec> codecs = null;

  /** Supported formats */
  List<Format> formats = null;

  public FFcommon(@Nonnull String path) {
    this(path, new RunProcessFunction());
  }

  protected FFcommon(@Nonnull String path, @Nonnull ProcessFunction runFunction) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(path));
    this.runFunc = checkNotNull(runFunction);
    this.path = path;
  }

  protected BufferedReader wrapInReader(Process p) {
    return new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
  }

  protected void throwOnError(Process p) throws IOException {
    try {
      // TODO In java 8 use waitFor(long timeout, TimeUnit unit)
      if (ProcessUtils.waitForWithTimeout(p, 1, TimeUnit.SECONDS) != 0) {
        // TODO Parse the error
        throw new IOException(path + " returned non-zero exit status. Check stdout.");
      }
    } catch (TimeoutException e) {
      throw new IOException("Timed out waiting for " + path + " to finish.");
    }
  }

  /**
   * Returns the version string for this binary.
   *
   * @return the version string.
   * @throws IOException If there is an error capturing output from the binary.
   */
  public synchronized @Nonnull String version() throws IOException {
    if (this.version == null) {
      Process p = runFunc.run(ImmutableList.of(path, "-version"));
      try {
        BufferedReader r = wrapInReader(p);
        this.version = r.readLine();
        CharStreams.copy(r, CharStreams.nullWriter()); // Throw away rest of the output

        throwOnError(p);
      } finally {
        p.destroy();
      }
    }
    return version;
  }

  public String getPath() {
    return path;
  }

  /**
   * Returns the full path to the binary with arguments appended.
   *
   * @param args The arguments to pass to the binary.
   * @return The full path and arguments to execute the binary.
   * @throws IOException If there is an error capturing output from the binary
   */
  public List<String> path(List<String> args) throws IOException {
    return ImmutableList.<String>builder().add(path).addAll(args).build();
  }

  /**
   * Runs the binary (ffmpeg) with the supplied args. Blocking until finished.
   *
   * @param args The arguments to pass to the binary.
   * @throws IOException If there is a problem executing the binary.
   */
  public void run(List<String> args) throws IOException {
    checkNotNull(args);

    Process p = runFunc.run(path(args));
    try {
      // TODO Move the copy onto a thread, so that FFmpegProgressListener can be on this thread.

      // Now block reading ffmpeg's stdout. We are effectively throwing away the output.
      CharStreams.copy(wrapInReader(p), System.out); // TODO Should I be outputting to stdout?

      throwOnError(p);

    } finally {
      p.destroy();
    }
  }

  public synchronized @Nonnull List<Codec> codecs() throws IOException {

    if (this.codecs == null) {
      codecs = new ArrayList<>();

      Process p = runFunc.run(ImmutableList.of(path, "-codecs"));
      try {
        BufferedReader r = wrapInReader(p);
        String line;
        while ((line = r.readLine()) != null) {
          Matcher m = CODECS_REGEX.matcher(line);
          if (!m.matches()) {
            m = CODECS_REGEX_OLD.matcher(line);
            if (!m.matches()) continue;
          }
          if (!m.matches()) continue;

          if (m.group(2).length() > 1) {
            codecs.add(new Codec(m.group(2), m.group(3), m.group(1)));
          }
        }

        throwOnError(p);
        this.codecs = ImmutableList.copyOf(codecs);
      } finally {
        p.destroy();
      }
    }

    return codecs;
  }

  public synchronized @Nonnull List<Format> formats() throws IOException {

    if (this.formats == null) {
      formats = new ArrayList<>();

      Process p = runFunc.run(ImmutableList.of(path, "-formats"));
      try {
        BufferedReader r = wrapInReader(p);
        String line;
        while ((line = r.readLine()) != null) {
          Matcher m = FORMATS_REGEX.matcher(line);
          if (!m.matches()) continue;

          formats.add(new Format(m.group(2), m.group(3), m.group(1)));
        }

        throwOnError(p);
        this.formats = ImmutableList.copyOf(formats);
      } finally {
        p.destroy();
      }
    }
    return formats;
  }
}
