package com.example.nioFrame;

import java.io.EOFException;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


class SocketReader {
    public static byte[] SKIP_PACKET = new byte[0];
    private final NIOService m_nioService;
    private ByteBuffer m_previousBytes;
    // Send SKIP_PACKET to cause the returning byte to be discarded, while not stopping the read loop.
    private final boolean m_bigEndian;
    private final int m_headerSize;

    SocketReader(NIOService nioService, int headerSize, boolean bigEndian) {
        m_nioService = nioService;
        if (headerSize < 1 || headerSize > 4)
            throw new IllegalArgumentException("Header must be between 1 and 4 bytes long.");
        m_bigEndian = bigEndian;
        m_headerSize = headerSize;

    }

    public int read(SocketChannel channel) throws IOException {
        // Retrieve the shared buffer.
        ByteBuffer buffer = getBuffer();

        // Clear the buffer.
        buffer.clear();

        // Create an offset if there are unconsumed bytes.
        if (m_previousBytes != null) {
            buffer.position(m_previousBytes.remaining());
        }

        // Read data
        int read = channel.read(buffer);

        // We might encounter the end of the socket stream here.
        if (read < 0) throw new EOFException("Buffer read -1");

        // If we have no space left in the buffer, we need to throw an exception.
        if (!buffer.hasRemaining()) throw new BufferOverflowException();

        // If nothing was read, simply return 0.
        if (read == 0) return 0;

        // If we read data, we need to insert the previous bytes.
        // We could avoid this at the cost of making the "read" method more complex in PacketReader.
        if (m_previousBytes != null) {
            // Remember the old position.
            int position = buffer.position();

            // Shift to position 0
            buffer.position(0);

            // Add the bytes.
            buffer.put(m_previousBytes);

            // Restore the position.
            buffer.position(position);

            // Clear the bytes we kept.
            m_previousBytes = null;
        }

        // Flip the buffer to prepare for reading.
        buffer.flip();

        return read;
    }

    /**
     * Moves any unread bytes to a buffer to be available later.
     */
    public void compact() {
        // Retrieve our shared buffer.
        ByteBuffer buffer = getBuffer();

        // If there is data remaining, copy that data.
        if (buffer.remaining() > 0) {
            m_previousBytes = NIOUtils.copy(buffer);
        }
    }


    /**
     * Returns the shared buffer (associated with the NIOService) for read/write.
     *
     * @return the shared buffer-
     */
    public ByteBuffer getBuffer() {
        return m_nioService.getSharedBuffer();
    }

    /**
     * Create a new packet using the ByteBuffer given.
     */
    byte[] nextPacket(ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() < m_headerSize) return null;
        byteBuffer.mark();
        int length = NIOUtils.getPacketSizeFromByteBuffer(byteBuffer, m_headerSize, m_bigEndian);
        if (byteBuffer.remaining() >= length) {
            byte[] packet = new byte[length];
            byteBuffer.get(packet);
            return packet;
        } else {
            byteBuffer.reset();
            return null;
        }
    }
}
