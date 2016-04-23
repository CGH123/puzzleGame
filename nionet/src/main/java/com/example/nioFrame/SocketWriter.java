package com.example.nioFrame;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


class SocketWriter {
    private ByteBuffer[] m_writeBuffers;
    private Object m_tag;
    private int m_currentBuffer;

    private final boolean m_bigEndian;
    private final ByteBuffer m_header;

    SocketWriter(int headerSize, boolean bigEndian) {
        m_writeBuffers = null;
        if (headerSize < 1 || headerSize > 4)
            throw new IllegalArgumentException("Header must be between 1 and 4 bytes long.");
        m_bigEndian = bigEndian;
        m_header = ByteBuffer.allocate(headerSize);
    }

    public boolean isEmpty() {
        return m_writeBuffers == null;
    }

    public void setPacket(byte[] data, Object tag) {
        if (!isEmpty())
            throw new IllegalStateException("This method should only called when m_writeBuffers == null");

        // Set the current packet
        m_writeBuffers = write(new ByteBuffer[]{ByteBuffer.wrap(data)});
        m_currentBuffer = 0;
        m_tag = tag;
    }

    public boolean write(SocketChannel channel) throws IOException {
        // If the packet is empty, just clear data and return true
        if (m_writeBuffers == null
                || (m_currentBuffer == m_writeBuffers.length - 1
                && !m_writeBuffers[m_currentBuffer].hasRemaining())) {
            m_writeBuffers = null;
            return true;
        }

        // Write as much as possible to the channel.
        long written = channel.write(m_writeBuffers, m_currentBuffer, m_writeBuffers.length - m_currentBuffer);

        // If nothing is written, then the buffer is full and writing should end temporarily.
        if (written == 0) return false;


        // Delete written buffers, update currentBuffer
        for (int i = m_currentBuffer; i < m_writeBuffers.length; i++) {
            if (m_writeBuffers[i].hasRemaining()) {
                m_currentBuffer = i;
                break;
            }
            m_writeBuffers[i] = null;
        }

        // If the current buffer is empty, clear all.
        if (m_writeBuffers[m_currentBuffer] == null) {
            m_writeBuffers = null;
        }
        return true;
    }


    public Object getTag() {
        return m_tag;
    }


    public ByteBuffer[] write(ByteBuffer[] byteBuffers) {
        m_header.clear();
        NIOUtils.setPacketSizeInByteBuffer(m_header, m_header.capacity(),
                (int) NIOUtils.remaining(byteBuffers), m_bigEndian);
        m_header.flip();
        return NIOUtils.concat(m_header, byteBuffers);
    }
}
