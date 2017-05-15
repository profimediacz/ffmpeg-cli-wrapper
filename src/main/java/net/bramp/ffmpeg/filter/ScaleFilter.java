package net.bramp.ffmpeg.filter;

/**
 Scale (resize) the input video, using the libswscale library.
 https://www.ffmpeg.org/ffmpeg-filters.html#scale
 The scale filter forces the output display aspect ratio to be the same of the input, by changing the output sample aspect ratio.
 If the input image format is different from the format requested by the next filter, the scale filter will convert the input to the requested format.
 
 Options:
 The filter accepts the following options, or any of the options supported by the libswscale scaler.
 See https://www.ffmpeg.org/ffmpeg-scaler.html#scaler_005foptions for the complete list of scaler options.
 width, w
 height, h
   Set the output video dimension expression. Default value is the input dimension.
   If the value is 0, the input width is used for the output.
   If one of the values is -1, the scale filter will use a value that maintains the aspect ratio of the input image, 
   calculated from the other specified dimension. If both of them are -1, the input size is used
   If one of the values is -n with n > 1, the scale filter will also use a value that maintains the aspect ratio of the 
   input image, calculated from the other specified dimension. After that it will, however, make sure that the calculated 
   dimension is divisible by n and adjust the value if necessary.
   See below for the list of accepted constants for use in the dimension expression.
 eval
   Specify when to evaluate width and height expression. It accepts the following values:
     ‘init’ - Only evaluate expressions once during the filter initialization or when a command is processed.
     ‘frame’ - Evaluate expressions for each incoming frame.
   Default value is ‘init’.
 interl
   Set the interlacing mode. It accepts the following values:
     ‘1’ - Force interlaced aware scaling.
     ‘0’ - Do not apply interlaced scaling.
     ‘-1’ - Select interlaced aware scaling depending on whether the source frames are flagged as interlaced or not.
   Default value is ‘0’.
 flags
   Set libswscale scaling flags. See https://www.ffmpeg.org/ffmpeg-scaler.html#sws_005fflags for the complete list of values.
   If not explicitly specified the filter applies the default flags.
 param0, param1
   Set libswscale input parameters for scaling algorithms that need them. See https://www.ffmpeg.org/ffmpeg-scaler.html#sws_005fparams 
   for the complete documentation. If not explicitly specified the filter applies empty parameters.
 size, s
   Set the video size. For the syntax of this option, check the "Video size" section in the ffmpeg-utils manual.
 in_color_matrix
 out_color_matrix
   Set in/output YCbCr color space type.
   This allows the autodetected value to be overridden as well as allows forcing a specific value used for the output and encoder.
   If not specified, the color space type depends on the pixel format.
   Possible values:
     ‘auto’ - Choose automatically.
     ‘bt709’ - Format conforming to International Telecommunication Union (ITU) Recommendation BT.709.
     ‘fcc’ - Set color space conforming to the United States Federal Communications Commission (FCC) 
             Code of Federal Regulations (CFR) Title 47 (2003) 73.682 (a).
     ‘bt601’ - Set color space conforming to:
                 - ITU Radiocommunication Sector (ITU-R) Recommendation BT.601
                 - ITU-R Rec. BT.470-6 (1998) Systems B, B1, and G
                 - Society of Motion Picture and Television Engineers (SMPTE) ST 170:2004
     ‘smpte240m’ - Set color space conforming to SMPTE ST 240:1999.
 in_range
 out_range
   Set in/output YCbCr sample range.
   This allows the autodetected value to be overridden as well as allows forcing a specific value used for the output 
   and encoder. If not specified, the range depends on the pixel format. Possible values:
     ‘auto’ - Choose automatically.
     ‘jpeg/full/pc’ - Set full range (0-255 in case of 8-bit luma).
     ‘mpeg/tv’ - Set "MPEG" range (16-235 in case of 8-bit luma).
 force_original_aspect_ratio
   Enable decreasing or increasing output video width or height if necessary to keep the original aspect ratio. Possible values:
     ‘disable’ - Scale the video as specified and disable this feature.
     ‘decrease’ - The output video dimensions will automatically be decreased if needed.
     ‘increase’ - The output video dimensions will automatically be increased if needed.
   One useful instance of this option is that when you know a specific device’s maximum allowed resolution, you can use this 
   to limit the output video to that, while retaining the aspect ratio. For example, device A allows 1280x720 playback, 
   and your video is 1920x800. Using this option (set it to decrease) and specifying 1280x720 to the command line makes 
   the output 1280x533.
   Please note that this is a different thing than specifying -1 for w or h, you still need to specify the output resolution 
   for this option to work.
   
 The values of the w and h options are expressions containing the following constants:
 in_w
 in_h
   The input width and height
 iw
 ih
   These are the same as in_w and in_h.
 out_w
 out_h
  The output (scaled) width and height
 ow
 oh
   These are the same as out_w and out_h
 a
   The same as iw / ih
 sar
   input sample aspect ratio
 dar
   The input display aspect ratio. Calculated from (iw / ih) * sar.
 hsub
 vsub
   horizontal and vertical input chroma subsample values. For example for the pixel format "yuv422p" hsub is 2 and vsub is 1.
 ohsub
 ovsub
   horizontal and vertical output chroma subsample values. For example for the pixel format "yuv422p" hsub is 2 and vsub is 1.

 Examples:
 * Scale the input video to a size of 200x100
     scale=w=200:h=100
 * This is equivalent to:
     scale=200:100
   or:
     scale=200x100
 * Specify a size abbreviation for the output size:
     scale=qcif
   which can also be written as:
     scale=size=qcif
 * Scale the input to 2x:
     scale=w=2*iw:h=2*ih
 * The above is the same as:
     scale=2*in_w:2*in_h
 * Scale the input to 2x with forced interlaced scaling:
     scale=2*iw:2*ih:interl=1
 * Scale the input to half size:
     scale=w=iw/2:h=ih/2
 * Increase the width, and set the height to the same size:
     scale=3/2*iw:ow
 * Seek Greek harmony:
     scale=iw:1/PHI*iw
     scale=ih*PHI:ih
 * Increase the height, and set the width to 3/2 of the height:
     scale=w=3/2*oh:h=3/5*ih
 * Increase the size, making the size a multiple of the chroma subsample values:
     scale="trunc(3/2*iw/hsub)*hsub:trunc(3/2*ih/vsub)*vsub"
 * Increase the width to a maximum of 500 pixels, keeping the same aspect ratio as the input:
     scale=w='min(500\, iw*3/2):h=-1'   
 
 Commands: 
 width, w
 height, h
   Set the output video dimension expression. The command accepts the same syntax of the corresponding option.
   If the specified expression is not valid, it is kept at its current value.   
 */
public class ScaleFilter extends VideoFilter {

  private static final String FILTER_NAME = "scale";
  public static final String SIZE_CALCULATED = "-1";
  public static final String SIZE_NO_CHANGE = "0";

  private String width;
  private String height;
  private String eval;
  private String interl;
  private String flags;
  private String param0;
  private String size;
  private String inColorMatrix;
  private String outColorMatrix;
  private String inRange;
  private String outRange;
  private String forceOriginalAspectRatio;
  

  public ScaleFilter(String width, String height) {
    this.width = width;
    this.height = height;
    this.eval = "";
    this.interl = "";
    this.flags = "";
    this.param0 = "";
    this.size = "";
    this.inColorMatrix = "";
    this.outColorMatrix = "";
    this.inRange = "";
    this.outRange = "";
    this.forceOriginalAspectRatio = "";    
  }

  public ScaleFilter() {
    this("","");
  }
  
  public ScaleFilter(Integer width, Integer height) {
    this(width.toString(), height.toString());
  }

  public ScaleFilter(String width) {
    this(width, SIZE_CALCULATED);
  }

  public ScaleFilter(Integer width) {
    this(width.toString(), SIZE_CALCULATED);
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public void setWidth(Integer width) {
    this.width = width.toString();
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public void setHeight(Integer height) {
    this.height = height.toString();
  }

  public void setEval(String eval) {
    this.eval = eval;
  }

  public void setInterl(String interl) {
    this.interl = interl;
  }

  public void setInterl(Integer interl) {
    this.interl = interl.toString();
  }

  public void setFlags(String flags) {
    this.flags = flags;
  }

  public void setParam0(String param0) {
    this.param0 = param0;
  }

  public void setSize(String size) {
    this.size = size;
  }


  public void setInColorMatrix(String inColorMatrix) {
    this.inColorMatrix = inColorMatrix;
  }

  public void setOutColorMatrix(String outColorMatrix) {
    this.outColorMatrix = outColorMatrix;
  }

  public void setInRange(String inRange) {
    this.inRange = inRange;
  }

  public void setOutRange(String outRange) {
    this.outRange = outRange;
  }

  public void setForceOriginalAspectRatio(String forceOriginalAspectRatio) {
    this.forceOriginalAspectRatio = forceOriginalAspectRatio;
  }

  @Override
  protected String getFilterName() {
    return FILTER_NAME;
  }

  @Override
  public String getCommandString() {
    StringBuffer sb = new StringBuffer(getFilterName());
    sb.append("=w=").append(width);
    sb.append(":h=").append(height);
    if (!eval.isEmpty())
      sb.append(":eval=").append(eval);
    if (!interl.isEmpty())
      sb.append(":interl=").append(interl);
    if (!flags.isEmpty())
      sb.append(":flags=").append(flags);
    if (!param0.isEmpty())
      sb.append(":param0=").append(param0);
    if (!size.isEmpty())
      sb.append(":size=").append(size);
    if (!inColorMatrix.isEmpty())
      sb.append(":in_color_matrix=").append(inColorMatrix);
    if (!outColorMatrix.isEmpty())
      sb.append(":out_color_matrix=").append(outColorMatrix);
    if (!inRange.isEmpty())
      sb.append(":in_range=").append(inRange);
    if (!outRange.isEmpty())
      sb.append(":out_range=").append(outRange);
    if (!forceOriginalAspectRatio.isEmpty())
      sb.append(":force_original_aspect_ratio=").append(forceOriginalAspectRatio);    
    return sb.toString();
  }
}
