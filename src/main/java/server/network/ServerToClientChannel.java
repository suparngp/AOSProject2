package server.network;

import common.utils.Logger;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Communication Channel for server comm.
 * Created by suparngupta on 4/5/14.
 */
public class ServerToClientChannel extends Thread {

    private ServerSocket serverToClientSocket;
    private Node node;
    private int portNum;
    
    public ServerToClientChannel(Node node) {
        super("ServerToClientChannelThread");
        this.node = node;
        this.portNum = node.getClientPortNum();
    }


    /**
     * Initializes the Server to Client Channel serverToClientSocket to listen to client's incoming channels.
     *
     * @throws Exception if listening serverToClientSocket cannot be established.
     */
    public ServerToClientChannel init() throws Exception {
        try {
            this.serverToClientSocket = new ServerSocket(this.portNum);
            Logger.log("Server to client channel is now open");
        } catch (Exception e) {
            Logger.error("Unable to start the Server to Client Channel", e);
            throw e;
        }
        return this;
    }

    @Override
    public void run() {

        try {
            /**
             * Clean up routine to close the serverToClientSocket if the server serverToClientSocket shuts down.
             */
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    destroyClientChannel();
                }
            });

            Logger.log("This server is ready to serve the clients");

            while (true) {
                Socket socket = serverToClientSocket.accept();
                Logger.debug("Connection from client was successfully established.");
                new ServerToClientHandler(node, socket).start();
            }
        } catch (SocketException se) {
            Logger.log("Server to Client Channel has been successfully closed");
        } catch (Exception e) {
            Logger.error("Server to Client Channel thread interrupted while accepting connections", e);

        }
    }

    /**
     * Destroys the Server to Client Channel
     */
    public void destroyClientChannel() {
        try {
            if (serverToClientSocket != null && !serverToClientSocket.isClosed()) {
                serverToClientSocket.close();
            }
        } catch (Exception e) {
            Logger.error("Unable to close the Server to Client Channel serverToClientSocket");
        }
    }

    /**
     * Gets serverToClientSocket.
     *
     * @return Value of serverToClientSocket.
     */
    public ServerSocket getServerToClientSocket() {
        return serverToClientSocket;
    }

    /**
     * Sets new serverToClientSocket.
     *
     * @param serverToClientSocket New value of serverToClientSocket.
     */
    public void setServerToClientSocket(ServerSocket serverToClientSocket) {
        this.serverToClientSocket = serverToClientSocket;
    }
}
