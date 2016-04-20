package com.example.nionet_test;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;

/**
 * 管理Channel的管理者类
 * Created by HUI on 2016-04-19.
 */
public class ChannelManger {

    /**
     * 用来把packet都弄进去
     */
    


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
            // Do nothing
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
            // Do nothing
        }
    }

}
