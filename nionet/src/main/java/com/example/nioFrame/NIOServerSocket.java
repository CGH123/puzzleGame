package com.example.nioFrame;

import java.net.ServerSocket;


public interface NIOServerSocket extends NIOAbstractSocket {

    /**
     * Associates a server socket observer with this server socket and
     * starts accepting connections.
     * <p/>
     * <em>This method is thread-safe, but may only be called once.</em>
     *
     * @param observer the observer to receive callbacks from this socket.
     * @throws NullPointerException  if the observer given is null.
     * @throws IllegalStateException if an observer has already been set.
     */
    void listen(ServerSocketObserver observer);

    /**
     * Sets the connection acceptor for this server socket.
     * <p/>
     * A connection acceptor determines if a connection should be
     * disconnected or not <em>after</em> the initial accept is done.
     * <p/>
     * For more information, see the documentation for <code>naga.ConnectionAcceptor</code>.
     * <p/>
     * <em>This method is thread-safe.</em>
     *
     * @param acceptor the acceptor to use, or null to default to
     *                 ConnectorAcceptor.DENY.
     */
    void setConnectionAcceptor(ConnectionAcceptor acceptor);

    /**
     * Allows access to the underlying server socket.
     * <p/>
     * <em>Note calling close and similar functions on this socket
     * will put the NIOServerSocket in an undefined state</em>
     *
     * @return return the underlying server socket.
     */
    ServerSocket socket();
}
