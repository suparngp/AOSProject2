package server.services;

import common.utils.Logger;
import server.network.Node;

import java.util.Scanner;

/**
 * Created by suparngupta on 4/22/14.
 */
public class InputProcessor {

    private final Scanner sc;
    private final Node node;

    public InputProcessor(Node node) {
        this.node = node;
        sc = new Scanner(System.in);
        displayWelcomeScreen();
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

    void displayMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("1. Show node details").append("\n")
                .append("2. Reset all configuration and data").append("\n")
                .append("3. Show reachable servers").append("\n")
                .append("4. Add serverId to blocking list").append("\n")
                .append("5. Remove server from blocking list\n")
                .append("6. Redisplay Menu").append("\n")
                .append("7. Exit the system").append("\n\n");
        System.out.println(sb);
    }

    public void processInput() {
        boolean proceed = true;
        while (proceed) {
            displayMenu();
            try {
                int option = sc.nextInt();

                switch (option) {
                    case 1: {
                        System.out.println(node);
                        break;
                    }
                    case 2: {
                        node.reset();
                        break;
                    }
                    case 3: {
                        System.out.println("Reachable servers " + node.getAliveServers());
                        break;
                    }
                    case 4: {
                        processBlocking();
                        break;
                    }
                    case 5: {
                        processRemoveBlockingList();
                        break;
                    }
                    case 6: {
                        displayMenu();
                        break;
                    }
                    case 7: {
                        proceed = false;
                        System.exit(0);
                        break;
                    }
                    default: {
                        System.out.println("Please enter a valid input.");
                    }
                }
            } catch (Exception e) {
                Logger.debug(e);
            }
        }


    }

    void processBlocking() {
        System.out.println("Please enter the server's id to block channel");
        String input = sc.nextLine();
        while (true) {
            try {
                int serverId = Integer.parseInt(input.trim());
                if (node.getBlockingList().contains(serverId)) {
                    System.out.println("Server already present in the blocking list");
                } else {
                    boolean add = node.getBlockingList().add(serverId);
                    if (!add) {
                        System.err.println("Unable to add server to blocking list");
                    } else {
                        System.out.println("Server " + serverId + " added to blocking list successfully");
                        System.out.println("Testing reachability.....");
                        System.out.println("Reachable: " + node.isPeerReachable(serverId));
                    }
                }

                break;
            } catch (Exception e) {
                System.out.println("Please enter a valid server id");
                input = sc.nextLine();
            }
        }
    }

    void processRemoveBlockingList() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the server's id to unblock channel");
        String input = sc.nextLine();
        while (true) {
            try {
                int serverId = Integer.parseInt(input.trim());
                if (!node.getBlockingList().contains(serverId)) {
                    System.out.println("Server not present in the blocking list");
                } else {
                    boolean add = node.getBlockingList().remove(serverId);
                    if (!add) {
                        System.err.println("Unable to remove server from blocking list");
                    } else {
                        System.out.println("Server " + serverId + " from blocking list successfully");
                        System.out.println("Testing reachability.....");
                        System.out.println("Reachable: " + node.isPeerReachable(serverId));
                    }
                }

                break;
            } catch (Exception e) {
                System.out.println("Please enter a valid server id");
                input = sc.nextLine();
            }
        }
    }
}
