package com.secureline.secureline.webrtc;

import java.util.ArrayList;
import java.util.List;

public class AudioProcessorChain {

    private final List<AudioProcessor> processors;

    public AudioProcessorChain() {
        processors = new ArrayList<>();
    }

    public void addProcessor(AudioProcessor processor) {
        processors.add(processor);
    }

    public short[] process(short[] audioData) {
        short[] result = audioData;
        for (AudioProcessor processor : processors) {
            result = processor.process(result);
        }
        return result;
    }

    public void clearProcessors() {
        processors.clear();
    }

    public interface AudioProcessor {
        short[] process(short[] audioData);
    }
}
