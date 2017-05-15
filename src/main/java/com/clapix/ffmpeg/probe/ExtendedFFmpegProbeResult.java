package com.clapix.ffmpeg.probe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.clapix.ffmpeg.probe.helper.FFmpegStreamHelper;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import net.bramp.ffmpeg.probe.FFmpegStream.CodecType;

public class ExtendedFFmpegProbeResult extends FFmpegProbeResult {

	public static final String LANGUAGE_TAG = "language";
	
	public int numOfStreams() {
		if (getStreams() == null) {
			return 0;
		} else {
			return getStreams().size();
		}
	}

	public int numOfStreams(CodecType type) {
		int num = 0;
		if (getStreams() != null) {
			for (FFmpegStream stream : getStreams()) {
				if (stream.codec_type.equals(type)) {
					num++;
				}
			}
		}
		return num;
	}

	public int numOfVideoStreams() {
		return numOfStreams(CodecType.VIDEO);
	}

	public int numOfAudioStreams() {
		return numOfStreams(CodecType.AUDIO);
	}

	public int numOfSubtitleStreams() {
		return numOfStreams(CodecType.SUBTITLE);
	}

	public List<FFmpegStream> getStreams(CodecType type) {
		List<FFmpegStream> streams = new ArrayList<>();
		if (getStreams() != null) {
			for (FFmpegStream stream : streams) {
				if (stream.codec_type.equals(type)) {
					streams.add(stream);
				}

			}
		}
		return streams;
	}
	
	public List<FFmpegStream> getVideoStreams() {
		return getStreams(CodecType.VIDEO);
	}
	
	public List<FFmpegStream> getAudioStreams() {
		return getStreams(CodecType.AUDIO);
	}
	
	public List<FFmpegStream> getAudioSubtitle() {
		return getStreams(CodecType.SUBTITLE);
	}
	
	public FFmpegStream getVideoStream() {
		return getStreams(CodecType.VIDEO).get(0);
	}
	
	public FFmpegStream getAudioStream(String language) {
		List<FFmpegStream> audioStreams = getStreams(CodecType.AUDIO);
		if (getStreams() != null) {
			for (FFmpegStream stream : audioStreams) {
				if (stream.tags != null) {
					if (stream.tags.containsKey(LANGUAGE_TAG)) {
						if (stream.tags.get(LANGUAGE_TAG) == language)  {
							return stream;
						}
					}
				}
			}
		}
		return null;
	}
	
	public List<String> getAudioLanguages() {
		List<String> languages = new ArrayList<>();
		List<FFmpegStream> audioStreams = getStreams(CodecType.AUDIO);
		if (getStreams() != null) {
			for (FFmpegStream stream : audioStreams) {
				if (stream.tags != null) {
					if (stream.tags.containsKey(LANGUAGE_TAG)) {
						languages.add(stream.tags.get(LANGUAGE_TAG));
					}
				}
			}
		}
		return languages;
	}

	public boolean canDecode(FFprobe ffprobe) throws IOException {  // vsechny streamy nebo jen vidio a audio ?
		boolean canDecode = false;
		if (getStreams() != null) {
			canDecode = true;
			for (FFmpegStream stream : getStreams()) {
          if (!(FFmpegStreamHelper.canDecode(stream, ffprobe))) {
					canDecode = false;
				}
			}
		}
		return canDecode;
	}
	
}
