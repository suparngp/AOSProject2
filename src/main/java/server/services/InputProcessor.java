package server.services;

import server.network.Node;

/**
 * Created by suparngupta on 4/22/14.
 */
public class InputProcessor {

    private Node node;

    public InputProcessor(Node node){
        this.node = node;
    }

    /**
     * Displays the welcome page
     */
    public void displayWelcomeScreen() {
        StringBuilder sb = new StringBuilder();
        sb.append("****************************************************************\n")
                .append("                        AOS PROJECT 2                           \n")
                .append("****************************************************************\n")
                .append("\n\n");


        System.out.println(sb);

    }

    public void displayMenu(){
        StringBuilder sb = new StringBuilder();
        sb.append("1. Show node details").append("\n")
                .append("2. Reset all configuration and data").append("\n")
                .append("3. Add serverId to blocking list").append("\n")
                .append("4. Show reachable servers").append("\n")
                .append("1. Show node details").append("\n")
                .append("1. Show node details").append("\n");
    }
}
