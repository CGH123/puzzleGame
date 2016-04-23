package com.example.nioFrame;

public interface NIOAbstractSocket {
    /**
     * Closes this socket (the actual disconnect will occur on the NIOService thread)
     * <p>
     * <em>This method is thread-safe.</em>
     */
    void close();

    /**
     * Returns the current state of this socket.
     * <p>
     * <em>This method is thread-safe.</em>
     *
     * @return true if the connection is socket is open, false if closed.
     */
    boolean isOpen();


    /**
     * Returns the tag for this socket.
     *
     * @return the tag or null if no tag has been set.
     */
    Object getTag();

    /**
     * Returns the tag for this socket. A tag is an object
     * that you can associate with the socket and retrieve later.
     *
     * @param tag the new tag for this socket.
     */
    void setTag(Object tag);
}
