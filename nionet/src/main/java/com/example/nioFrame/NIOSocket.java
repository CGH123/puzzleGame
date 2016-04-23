package com.example.nioFrame;

import java.net.Socket;


public interface NIOSocket extends NIOAbstractSocket {

    /**
     * Write a packet of bytes asynchronously on this socket.
     * <p>
     * The bytes will be sent to the PacketWriter belonging to this
     * socket for dispatch. However, if the queue is full (i.e. the new
     * queue size would exceed <code>getMaxQueueSize()</code>),
     * the packet is discarded and the method returns false.
     * <p>
     * <em>This method is thread-safe.</em>
     *
     * @param packet the packet to send.
     * @return true if the packet was queued, false if the queue limit
     * was reached and the packet was thrown away.
     */
    boolean write(byte[] packet);

    /**
     * Write a packet of bytes asynchronously on this socket.
     */
    boolean write(byte[] packet, Object tag);


    /**
     * Opens the socket for reads.
     * <p>
     * The socket observer will receive connects, disconnects and packets.
     * If the socket was opened or disconnected before the observer was attached,
     * the socket observer will still receive those callbacks.
     * <p>
     * <em>This method is thread-safe, but may only be called once.</em>
     *
     * @param socketObserver the observer to receive packets and be notified of connects/disconnects.
     * @throws IllegalStateException if the method already has been called.
     */
    void listen(SocketObserver socketObserver);


    /**
     * Allows access to the underlying socket.
     * <p>
     * <em>Note that accessing streams or closing the socket will
     * put this NIOSocket in an undefined state</em>
     *
     * @return return the underlying socket.
     */
    Socket socket();
}
