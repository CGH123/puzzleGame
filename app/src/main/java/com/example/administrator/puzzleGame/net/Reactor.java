package com.example.administrator.puzzleGame.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

//import java.nio.channels.AsynchronousSocketChannel;


/**
 * 用于NIO操作的reactor仲裁
 * <p/>
 * 几个重要的概念
 * channels 用于连接文档，socket，支持Non-blocking读写
 * buffers:可以直接被读或者写的array
 * selectors:告诉哪一组channel有IO时间的发生
 * selectionKeys： 维持IO事件的状态和绑定
 * <p/>
 * Created by HUI on 2016-04-16.
 */
public class Reactor implements Runnable {

    final Selector selector;
    final ServerSocketChannel serverSocket;

    Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }

    /*
    可供选择的功能
    Alternatiely, use explicit SPI provider:
     SelectorProvider p = SelectorProvider.provider();
     selector = p.openSelector();
     serverSocket = p.openServerSocketChannel();
     */

    /**
     * 用于dispatch loop
     */
    @Override
    public void run() { //normally in a new Thread
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext())
                    dispatch((SelectionKey) (it.next()));
                selected.clear();
            }
        } catch (IOException ex) {
            //待写
        }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        if (r != null)
            r.run();
    }

    /**
     * Reactor内部的Acceptor，用来处理client对应的请求
     */
    class Acceptor implements Runnable { //inner
        @Override
        public void run() {
            try {
                SocketChannel c = serverSocket.accept();
                if (c != null)
                    new NioHandler(selector, c);
            } catch (IOException ex) {
                //处理Acceptor中的IO异常
            }
        }
    }

    /**
     * NIO为事件驱动的AWT模型
     * NioHandler 用于处理相应事务的活动
     * Reactor中用于执行non-blocking活动，相当出AWT模型中的Listener
     */
    final class NioHandler implements Runnable {

        final SocketChannel socket;
        final SelectionKey sk;
        ByteBuffer input = ByteBuffer.allocate(NetConstant.MAXIN);
        ByteBuffer output = ByteBuffer.allocate(NetConstant.MAXOUT);
        static final int READING = 0, SENDING = 1;
        int state = READING;

        NioHandler(Selector sel, SocketChannel c) throws IOException {
            socket = c;
            c.configureBlocking(false);
            //Optionally try first read now
            sk = socket.register(sel, 0);
            sk.attach(this);
            sk.interestOps(SelectionKey.OP_READ);
            sel.wakeup();
        }

        boolean inputIsComplete() {
            //判断input是否完整
            return true;
        }

        boolean outputIsComplete() {
            //判断output是否完整
            return true;
        }

        void process() {
            //进程还是啥，不懂
        }

        /**
         * Request handling
         */
        @Override
        public void run() {
            try {
                if (state == READING)
                    read();
                else if (state == SENDING)
                    send();
            } catch (IOException ex) {
                //处理state的异常
            }
        }

        void read() throws IOException {
            socket.read(input);
            if (inputIsComplete()) {
                process();
                state = SENDING;
                //Normally also do first write now
                sk.interestOps(SelectionKey.OP_WRITE);
            }
        }

        void send() throws IOException {
            socket.write(output);
            if (outputIsComplete())
                sk.cancel();
        }

    }


    /**
     * 线程池调度，当测试通过单线程NIO后，进入多线程实现
     */
    /*
    final class NioHandlerPool implements Runnable{
        //uses util.concurrent thread pool
        static PooledExcutor pool = new PooledExecutor();
        static final int PROCESSING = 3;
        //...

        synchronized  void read(){
            socket.read(input);
            if(inputIsComplete()){
                state = PROCESSING;
                pool.execute(new Processer());
            }
        }

        synchronized  void processAndHandOff(){
            process();
            state = SENDING;
            sk.interest(SelectionKey.OP_WRITE);
        }

        class Process implements Runnable{

            @Override
            public void run() {
                processAndHandOff();
            }
        }
    }
    */
}
