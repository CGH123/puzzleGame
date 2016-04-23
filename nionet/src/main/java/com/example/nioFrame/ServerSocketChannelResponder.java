package com.example.nioFrame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


class ServerSocketChannelResponder extends ChannelResponder implements NIOServerSocket {
    private volatile ConnectionAcceptor m_connectionAcceptor;
    private ServerSocketObserver m_observer;

    @SuppressWarnings({"ObjectToString"})
    public ServerSocketChannelResponder(NIOService service,
                                        ServerSocketChannel channel,
                                        InetSocketAddress address) throws IOException {
        super(service, channel, address);
        m_observer = null;
        setConnectionAcceptor(ConnectionAcceptor.ALLOW);
    }

    public void keyInitialized() {
        addInterest(SelectionKey.OP_ACCEPT);
    }

    public ServerSocketChannel getChannel() {
        return (ServerSocketChannel) super.getChannel();
    }

    /**
     * Override point for substituting NIOSocket wrappers.
     *
     * @param channel the channel to register.
     * @param address the address associated with the channel.
     * @return A new NIOSocket
     * @throws IOException if registration failed.
     */
    NIOSocket registerSocket(SocketChannel channel, InetSocketAddress address) throws IOException {
        return getNIOService().registerSocketChannel(channel, address);
    }

    private void notifyNewConnection(NIOSocket socket) {
        try {
            if (m_observer != null) m_observer.newConnection(socket);
        } catch (Exception e) {
            e.getStackTrace();
            socket.close();
        }
    }

    private void notifyAcceptFailed(IOException theException) {
        try {
            if (m_observer != null) m_observer.acceptFailed(theException);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    /**
     * Callback to tell the object that there is at least one accept that can be done on the server socket.
     */
    public void socketReadyForAccept() {
        SocketChannel socketChannel = null;
        try {
            socketChannel = getChannel().accept();
            if (socketChannel == null) {
                // This means there actually wasn't any connection waiting,
                // so tick down the number of actual total connections.
                return;
            }

            InetSocketAddress address = (InetSocketAddress) socketChannel.socket().getRemoteSocketAddress();
            // Is this connection acceptable?
            if (!m_connectionAcceptor.acceptConnection(address)) {
                NIOUtils.closeChannelSilently(socketChannel);
                return;
            }
            notifyNewConnection(registerSocket(socketChannel, address));
        } catch (IOException e) {
            // Close channel in case it opened.
            NIOUtils.closeChannelSilently(socketChannel);
            notifyAcceptFailed(e);
        }
    }


    public void setConnectionAcceptor(ConnectionAcceptor connectionAcceptor) {
        m_connectionAcceptor = connectionAcceptor == null ? ConnectionAcceptor.DENY : connectionAcceptor;
    }

    private void notifyObserverSocketDied(Exception exception) {
        try {
            if (m_observer != null) m_observer.serverSocketDied(exception);
        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    public void listen(ServerSocketObserver observer) {
        if (observer == null) throw new NullPointerException();
        markObserverSet();
        getNIOService().queue(new BeginListenEvent(observer));
    }

    private class BeginListenEvent implements Runnable {
        private final ServerSocketObserver m_newObserver;

        private BeginListenEvent(ServerSocketObserver socketObserver) {
            m_newObserver = socketObserver;
        }

        public void run() {
            m_observer = m_newObserver;
            if (!isOpen()) {
                notifyObserverSocketDied(null);
                return;
            }
            addInterest(SelectionKey.OP_ACCEPT);
        }

        @Override
        public String toString() {
            return "BeginListen[" + m_newObserver + "]";
        }
    }

    protected void shutdown(Exception e) {
        notifyObserverSocketDied(e);
    }

    public ServerSocket socket() {
        return getChannel().socket();
    }
}
