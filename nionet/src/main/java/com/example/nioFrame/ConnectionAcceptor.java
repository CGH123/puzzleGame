package com.example.nioFrame;

import java.net.InetSocketAddress;


public interface ConnectionAcceptor {
    /**
     * A connection acceptor that refuses all connections.
     */
    ConnectionAcceptor DENY = new ConnectionAcceptor() {
        public boolean acceptConnection(InetSocketAddress address) {
            return false;
        }
    };

    /**
     * A connection acceptor that accepts all connections.
     */
    ConnectionAcceptor ALLOW = new ConnectionAcceptor() {
        public boolean acceptConnection(InetSocketAddress address) {
            return true;
        }
    };

    /**
     * Return true if the connection should be accepted, false otherwise.
     *
     * @param inetSocketAddress the adress the connection came from.
     * @return true to accept, false to refuse.
     */
    boolean acceptConnection(InetSocketAddress inetSocketAddress);
}
