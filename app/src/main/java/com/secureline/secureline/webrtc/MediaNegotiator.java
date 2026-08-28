package com.secureline.secureline.webrtc;

import org.webrtc.MediaConstraints;

public class MediaNegotiator {

    public static MediaConstraints createAudioConstraints() {
        MediaConstraints constraints = new MediaConstraints();

        constraints.mandatory.add(new MediaConstraints.KeyValuePair(
            "OfferToReceiveAudio", "true"
        ));
        constraints.mandatory.add(new MediaConstraints.KeyValuePair(
            "OfferToReceiveVideo", "false"
        ));

        constraints.optional.add(new MediaConstraints.KeyValuePair(
            "DtlsSrtpKeyAgreement", "true"
        ));
        constraints.optional.add(new MediaConstraints.KeyValuePair(
            "googEchoCancellation", "true"
        ));
        constraints.optional.add(new MediaConstraints.KeyValuePair(
            "googAutoGainControl", "true"
        ));
        constraints.optional.add(new MediaConstraints.KeyValuePair(
            "googNoiseSuppression", "true"
        ));
        constraints.optional.add(new MediaConstraints.KeyValuePair(
            "googHighpassFilter", "true"
        ));

        return constraints;
    }

    public static MediaConstraints createDataConstraints() {
        MediaConstraints constraints = new MediaConstraints();
        constraints.mandatory.add(new MediaConstraints.KeyValuePair(
            "OfferToReceiveAudio", "false"
        ));
        constraints.mandatory.add(new MediaConstraints.KeyValuePair(
            "OfferToReceiveVideo", "false"
        ));
        return constraints;
    }
}
