package com.secureline.secureline.crypto;

import java.util.ArrayList;
import java.util.List;

public class MessageFragmentation {

    private static final int MAX_FRAGMENT_SIZE = 1024;

    public static List<byte[]> fragment(byte[] message) {
        List<byte[]> fragments = new ArrayList<>();
        int totalFragments = (int) Math.ceil((double) message.length / MAX_FRAGMENT_SIZE);

        for (int i = 0; i < totalFragments; i++) {
            int start = i * MAX_FRAGMENT_SIZE;
            int end = Math.min(start + MAX_FRAGMENT_SIZE, message.length);
            int length = end - start;

            byte[] fragment = new byte[length + 4];
            fragment[0] = (byte) ((i >> 8) & 0xFF);
            fragment[1] = (byte) (i & 0xFF);
            fragment[2] = (byte) ((totalFragments >> 8) & 0xFF);
            fragment[3] = (byte) (totalFragments & 0xFF);
            System.arraycopy(message, start, fragment, 4, length);
            fragments.add(fragment);
        }
        return fragments;
    }

    public static byte[] defragment(List<byte[]> fragments) {
        if (fragments == null || fragments.isEmpty()) return new byte[0];

        int totalFragments = ((fragments.get(0)[2] & 0xFF) << 8) | (fragments.get(0)[3] & 0xFF);
        byte[][] orderedFragments = new byte[totalFragments][];

        for (byte[] fragment : fragments) {
            int index = ((fragment[0] & 0xFF) << 8) | (fragment[1] & 0xFF);
            if (index >= 0 && index < totalFragments) {
                orderedFragments[index] = fragment;
            }
        }

        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        for (byte[] fragment : orderedFragments) {
            if (fragment != null && fragment.length > 4) {
                output.write(fragment, 4, fragment.length - 4);
            }
        }
        return output.toByteArray();
    }
}