package com.example.nioFrame;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;


public class NIOUtils {

    NIOUtils() {
    }

    /**
     * Silently close both a key and a channel.
     *
     * @param key     the key to cancel, may be null.
     * @param channel the channel to close, may be null.
     */
    public static void closeKeyAndChannelSilently(SelectionKey key, Channel channel) {
        closeChannelSilently(channel);
        cancelKeySilently(key);
    }

    /**
     * Encodes a length into byte buffer using
     * either big or little endian encoding (i.e. biggest or smallest byte first).
     *
     * @param byteBuffer    the ByteBuffer to use.
     * @param headerSize    the header size in bytes. 1-4.
     * @param valueToEncode the value to encode, 0 <= value < 2^(headerSize * 8)
     * @param bigEndian     if the encoding is big endian or not.
     * @throws IllegalArgumentException if the value is out of range for the given header size.
     */
    public static void setPacketSizeInByteBuffer(ByteBuffer byteBuffer, int headerSize, int valueToEncode, boolean bigEndian) {
        if (valueToEncode < 0) throw new IllegalArgumentException("Payload size is less than 0.");
        // If header size is 4, we get valueToEncode >> 32, which is defined as valueToEncode >> 0 for int.
        // Therefore, we handle the that case separately, as any int will fit in 4 bytes.
        if (headerSize != 4 && valueToEncode >> (headerSize * 8) > 0) {
            throw new IllegalArgumentException("Payload size cannot be encoded into " + headerSize + " byte(s).");
        }
        for (int i = 0; i < headerSize; i++) {
            int index = bigEndian ? (headerSize - 1 - i) : i;
            // We do not need to extend valueToEncode here, since the maximum is valueToEncode >> 24
            byteBuffer.put((byte) (valueToEncode >> (8 * index) & 0xFF));
        }
    }


    /**
     * Converts a value in a header buffer encoded in either big or little endian
     * encoding.
     * <p/>
     * <em>Note that trying to decode a value larger than 2^31 - 2 is not supported.</em>
     *
     * @param header    the header to encode from.
     * @param size      the header size, 1-4.
     * @param bigEndian if the encoding is big endian or not.
     * @return the decoded number.
     */
    public static int getPacketSizeFromByteBuffer(ByteBuffer header, int size, boolean bigEndian) {
        long packetSize = 0;
        if (bigEndian) {
            for (int i = 0; i < size; i++) {
                packetSize <<= 8;
                packetSize += header.get() & 0xFF;
            }
        } else {
            int shift = 0;
            for (int i = 0; i < size; i++) {
                // We do not need to extend valueToEncode here, since the maximum is valueToEncode >> 24
                packetSize += (header.get() & 0xFF) << shift;
                shift += 8;
            }
        }
        return (int) packetSize;
    }


    /**
     * Silently close a channel.
     *
     * @param channel the channel to close, may be null.
     */
    public static void closeChannelSilently(Channel channel) {
        try {
            if (channel != null) {
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Silently cancel a key.
     *
     * @param key the key to cancel, may be null.
     */
    public static void cancelKeySilently(SelectionKey key) {
        try {
            if (key != null) key.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ByteBuffer[] concat(ByteBuffer buffer, ByteBuffer[] buffers2) {
        return concat(new ByteBuffer[]{buffer}, buffers2);
    }

    public static ByteBuffer[] concat(ByteBuffer[] buffers1, ByteBuffer[] buffers2) {
        if (buffers1 == null || buffers1.length == 0) return buffers2;
        if (buffers2 == null || buffers2.length == 0) return buffers1;
        ByteBuffer[] newBuffers = new ByteBuffer[buffers1.length + buffers2.length];
        System.arraycopy(buffers1, 0, newBuffers, 0, buffers1.length);
        System.arraycopy(buffers2, 0, newBuffers, buffers1.length, buffers2.length);
        return newBuffers;
    }

    public static ByteBuffer copy(ByteBuffer buffer) {
        if (buffer == null) return null;
        ByteBuffer copy = ByteBuffer.allocate(buffer.remaining());
        copy.put(buffer);
        copy.flip();
        return copy;
    }

    public static long remaining(ByteBuffer[] byteBuffers) {
        long length = 0;
        for (ByteBuffer buffer : byteBuffers) length += buffer.remaining();
        return length;
    }

}