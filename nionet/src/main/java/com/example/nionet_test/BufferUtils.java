package com.example.nionet_test;

import java.nio.ByteBuffer;

/**
 * �Դ�������е�Buffer������Util��
 * Created by HUI on 2016-04-19.
 */
public class BufferUtils {




    /**
     * ѹ��һ��byte buffers,ֻ�����к������ݵ�Buffers
     */
    public static ByteBuffer[] compact(ByteBuffer[] buffers)
    {
        for (int i = 0; i < buffers.length; i++)
        {
            if (buffers[i].remaining() > 0)
            {
                if (i == 0) return buffers;
                ByteBuffer[] newBuffers = new ByteBuffer[buffers.length - i];
                System.arraycopy(buffers, i, newBuffers, 0, buffers.length - i);
                return newBuffers;
            }
        }
        return null;
    }

    /**
     * �ϲ�buffer
     */
    public static ByteBuffer[] concat(ByteBuffer[] buffers, ByteBuffer buffer)
    {
        return concat(buffers, new ByteBuffer[] { buffer });
    }

    /**
     * �ϲ�buffer
     */
    public static ByteBuffer[] concat(ByteBuffer buffer, ByteBuffer[] buffers2)
    {
        return concat(new ByteBuffer[] { buffer }, buffers2);
    }

    /**
     * �ϲ�buffer
     */
    public static ByteBuffer[] concat(ByteBuffer[] buffers1, ByteBuffer[] buffers2)
    {
        if (buffers1 == null || buffers1.length == 0) return buffers2;
        if (buffers2 == null || buffers2.length == 0) return buffers1;
        ByteBuffer[] newBuffers = new ByteBuffer[buffers1.length + buffers2.length];
        System.arraycopy(buffers1, 0, newBuffers, 0, buffers1.length);
        System.arraycopy(buffers2, 0, newBuffers, buffers1.length, buffers2.length);
        return newBuffers;
    }

    /**
     * ����ByteBuffer
     */
    public static ByteBuffer copy(ByteBuffer buffer)
    {
        if (buffer == null) return null;
        ByteBuffer copy = ByteBuffer.allocate(buffer.remaining());
        copy.put(buffer);
        copy.flip();
        return copy;
    }

    /**
     * ��ȡpack�ĳ���
     * Converts a value in a header byte array encoded in either big or little endian
     * encoding.
     * <p>
     * <em>Note that trying to decode a value larger than 2^31 - 2 is not supported.</em>
     *
     * @param data the data to encode from.
     * @param length the length of the header.
     * @param bigEndian
     * @return the decoded number.
     */
    public static int getPacketSizeFromByteArray(byte[] data, int length, boolean bigEndian)
    {
        long packetSize = 0;
        if (bigEndian)
        {
            for (int i = 0; i < length; i++)
            {
                packetSize <<= 8;
                packetSize += data[i] & 0xFF;
            }
        }
        else
        {
            int shift = 0;
            for (int i = 0; i < length; i++)
            {
                // We do not need to extend valueToEncode here, since the maximum is valueToEncode >> 24
                packetSize += (data[i] & 0xFF) << shift;
                shift += 8;
            }
        }
        return (int) packetSize;
    }

    /**
     * ��ȡbytebuffer�ĳ���
     * Converts a value in a header buffer encoded in either big or little endian
     * encoding.
     * <p>
     * <em>Note that trying to decode a value larger than 2^31 - 2 is not supported.</em>
     *
     * @param header the header to encode from.
     * @param size the header size, 1-4.
     * @param bigEndian if the encoding is big endian or not.
     * @return the decoded number.
     */
    public static int getPacketSizeFromByteBuffer(ByteBuffer header, int size, boolean bigEndian)
    {
        long packetSize = 0;
        if (bigEndian)
        {
            for (int i = 0; i < size; i++)
            {
                packetSize <<= 8;
                packetSize += header.get() & 0xFF;
            }
        }
        else
        {
            int shift = 0;
            for (int i = 0; i < size; i++)
            {
                // We do not need to extend valueToEncode here, since the maximum is valueToEncode >> 24
                packetSize += (header.get() & 0xFF) << shift;
                shift += 8;
            }
        }
        return (int) packetSize;
    }

    /**
     * byteBuffers�����Ƿ�Ϊ��
     */
    public static boolean isEmpty(ByteBuffer[] byteBuffers)
    {
        for (ByteBuffer buffer : byteBuffers)
        {
            if (buffer.remaining() > 0) return false;
        }
        return true;
    }

    /**
     * ��byteBuffer1�ĺ������bytebuffer2
     * @param buffer1
     * @param buffer2
     * @return
     */
    public static ByteBuffer join(ByteBuffer buffer1, ByteBuffer buffer2)
    {
        if (buffer2 == null || buffer2.remaining() == 0) return BufferUtils.copy(buffer1);
        if (buffer1 == null || buffer1.remaining() == 0) return BufferUtils.copy(buffer2);
        ByteBuffer buffer = ByteBuffer.allocate(buffer1.remaining() + buffer2.remaining());
        buffer.put(buffer1);
        buffer.put(buffer2);
        buffer.flip();
        return buffer;
    }

    /**
     *��ȡʣ�µ�bytebuffer�ĳ���
     */
    public static long remaining(ByteBuffer[] byteBuffers)
    {
        long length = 0;
        for (ByteBuffer buffer : byteBuffers) length += buffer.remaining();
        return length;
    }



    /**
     * Inserts a header in the first bytes of a byte array
     * in either big or little endian encoding (i.e. biggest or smallest byte first).
     *
     * @param buffer the byte array to set the header for
     * @param headerSize the header size in bytes. 1-4.
     * @param valueToEncode the value to encode, 0 <= value < 2^(headerSize * 8)
     * @param bigEndian if the encoding is big endian or not.
     * @throws IllegalArgumentException if the value is out of range for the given header size.
     */
    public static void setHeaderForPacketSize(byte[] buffer, int headerSize, int valueToEncode, boolean bigEndian)
    {
        if (valueToEncode < 0) throw new IllegalArgumentException("Payload size is less than 0.");
        // If header size is 4, we get valueToEncode >> 32, which is defined as valueToEncode >> 0 for int.
        // Therefore, we handle the that case separately, as any int will fit in 4 bytes.
        if (headerSize != 4 && valueToEncode >> (headerSize * 8) > 0)
        {
            throw new IllegalArgumentException("Payload size cannot be encoded into " + headerSize + " byte(s).");
        }
        for (int i = 0; i < headerSize; i++)
        {
            int index = bigEndian ? (headerSize - 1 - i) : i;
            // We do not need to extend valueToEncode here, since the maximum is valueToEncode >> 24
            buffer[i] = ((byte) (valueToEncode >> (8 * index) & 0xFF));
        }
    }


    /**
     * Encodes a length into byte buffer using
     * either big or little endian encoding (i.e. biggest or smallest byte first).
     *
     * @param byteBuffer the ByteBuffer to use.
     * @param headerSize the header size in bytes. 1-4.
     * @param valueToEncode the value to encode, 0 <= value < 2^(headerSize * 8)
     * @param bigEndian if the encoding is big endian or not.
     * @throws IllegalArgumentException if the value is out of range for the given header size.
     */
    public static void setPacketSizeInByteBuffer(ByteBuffer byteBuffer, int headerSize, int valueToEncode, boolean bigEndian)
    {
        if (valueToEncode < 0) throw new IllegalArgumentException("Payload size is less than 0.");
        // If header size is 4, we get valueToEncode >> 32, which is defined as valueToEncode >> 0 for int.
        // Therefore, we handle the that case separately, as any int will fit in 4 bytes.
        if (headerSize != 4 && valueToEncode >> (headerSize * 8) > 0)
        {
            throw new IllegalArgumentException("Payload size cannot be encoded into " + headerSize + " byte(s).");
        }
        for (int i = 0; i < headerSize; i++)
        {
            int index = bigEndian ? (headerSize - 1 - i) : i;
            // We do not need to extend valueToEncode here, since the maximum is valueToEncode >> 24
            byteBuffer.put((byte) (valueToEncode >> (8 * index) & 0xFF));
        }
    }

}
