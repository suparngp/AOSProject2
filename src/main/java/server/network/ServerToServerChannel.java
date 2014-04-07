package server.network;

import common.utils.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Communication Channel for server comm.
 * Created by suparngupta on 4/5/14.
 */
public class ServerToServerChannel extends Thread {

    private ServerSocket serverToServerSocket;
    private Node node;
    private int portNum;
    public ServerToServerChannel(Node node) {
        super("ServerToServerChannelThread");
        this.node = node;
        this.portNum = node.getServerPortNum();
    }


    /**
     * Initializes the Server to Server Channel serverToServerSocket to listen to other server's incoming channels.
     *
     * @throws Exception if listening serverToServerSocket cannot be established.
     */
    public ServerToServerChannel init() throws Exception {
        try {
            this.serverToServerSocket = new ServerSocket(this.portNum);
            Logger.log("Server to Server channel is now open");
        } catch (Exception e) {
            Logger.error("Unable to start the Server to Server Channel", e);
            throw e;
        }
        return this;
    }

    @Override
    public void run() {

        try {
            /**
             * Clean up routine to close the serverToServerSocket if the server serverToServerSocket shuts down.
             */
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    destroyServerChannel();
                }
            });


            while (true) {
                Socket socket = serverToServerSocket.accept();
                Logger.debug("Connection was successfully established.");
                new ServerToServerHandler(node, socket).start();
            }
        } catch (SocketException se) {
            Logger.log("Server to Server Channel has been successfully closed");
        } catch (Exception e) {
            Logger.error("Server to Server Channel thread interrupted while accepting connections", e);

        }
    }

    /**
     * Destroys the Server to Server Channel
     */
    public void destroyServerChannel() {
        try {
            if (serverToServerSocket != null && !serverToServerSocket.isClosed()) {
                serverToServerSocket.close();
            }
        } catch (Exception e) {
            Logger.error("Unable to close the Server to Server Channel serverToServerSocket");
        }
    }

    /**
     * Gets serverToServerSocket.
     *
     * @return Value of serverToServerSocket.
     */
    public ServerSocket getServerToServerSocket() {
        return serverToServerSocket;
    }

    /**
     * Sets new serverToServerSocket.
     *
     * @param serverToServerSocket New value of serverToServerSocket.
     */
    public void setServerToServerSocket(ServerSocket serverToServerSocket) {
        this.serverToServerSocket = serverToServerSocket;
    }
}
