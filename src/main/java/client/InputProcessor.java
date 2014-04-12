package client;

import client.clientNetwork.ClientNode;
import common.messages.Account;
import common.utils.Logger;

import java.util.Scanner;

/**
 * Created by suparngupta on 4/9/14.
 */
public class InputProcessor {

    private ClientNode clientNode;

    public InputProcessor(ClientNode clientNode) {
        this.clientNode = clientNode;
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

    /**
     * Displays the main menu
     */
    private void displayMainMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("Please choose one of the following options\n\n")
                .append("1. Create a new object\n")
                .append("2. Read an existing object\n")
                .append("3. Update an existing object\n")
                .append("4. Remove an existing object\n")
                .append("5. Exit the system\n");
        System.out.println(sb);
    }

    public void getMainMenuInput() {
        boolean proceed = true;
        while(proceed){
            displayMainMenu();
            Scanner sc = new Scanner(System.in);
            int option = sc.nextInt();
            switch (option){
                case 1:{
                    processCreateObj();
                    break;
                }
                case 2:{
                    processReadObject();
                    break;
                }

                case 3:{
                    processUpdateObj();
                    break;
                }
                case 4:{
                    processDelete();
                    break;
                }
                case 5: {
                    proceed = false;
                    break;
                }
            }
        }
    }

    private void processCreateObj() {
        Scanner sc = new Scanner(System.in);
        String objectId = this.clientNode.generateObjectId();

        String name;
        Double openingBalance;
        Double currentBalance;
        System.out.println("Object Id generated: " + objectId);
        System.out.println("Please enter the name: ");
        name = sc.nextLine();

        System.out.println("Please enter the opening balance");
        openingBalance = sc.nextDouble();

        currentBalance = openingBalance;
        try {
            Account acc = this.clientNode.createMultiServer(objectId, name, openingBalance, currentBalance);
            Logger.log("Object created", acc);
        } catch (Exception e) {
            Logger.error("Unable to create object", e);
        }

    }

    private Account processReadObject(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the object id");
        String objectId = sc.nextLine();

        try{
            Account acc = this.clientNode.readMultiServer(objectId);
            if(acc == null){
                throw new Exception("Object was not found");
            }
            Logger.log("Object successfully read from server", acc);
            return acc;
        }
        catch(Exception e){
            Logger.error("Unable to read Object", e);
            return null;
        }
    }

    private void processUpdateObj() {
        Account acc = processReadObject();
        if(acc == null){
            Logger.error("Unable to update the account");
            return;
        }

        Scanner sc = new Scanner(System.in);

        String objectId = acc.getId();

        String name = acc.getOwnerName();
        Double openingBalance = acc.getOpeningBalance();
        Double currentBalance = acc.getCurrentBalance();
        System.out.println("Object Id generate: " + acc.getId());
        System.out.println("Please enter the name (" + acc.getOwnerName() + "): ");
        String input =  sc.nextLine();
        if(input != null && !input.trim().isEmpty()){
            name = input;
        }

        System.out.println("Please enter the opening balance (" + acc.getOpeningBalance() + "): ");
        input = sc.nextLine();
        if(input != null && !input.trim().isEmpty()){
            openingBalance = Double.parseDouble(input.trim());
        }

        System.out.println("Please enter the current balance (" + acc.getCurrentBalance() + "): ");
        input = sc.nextLine();
        if(input != null && !input.trim().isEmpty()){
            currentBalance = Double.parseDouble(input.trim());
        }
        try {
            acc = this.clientNode.updateMultiServer(objectId, name, openingBalance, currentBalance);
            if(acc == null){
                throw new Exception("Unable to update object on server");
            }
            else{
                Logger.log("Object updated successfully", acc);
            }

        } catch (Exception e) {
            Logger.error("Unable to create object", e);
        }
    }

    private Account processDelete(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the object id");
        String objectId = sc.nextLine();

        try{
            Account acc = this.clientNode.deleteMultiServer(objectId);
            if(acc == null){
                throw new Exception("Object was not found");
            }
            Logger.log("Object successfully deleted from server", acc);
            return acc;
        }
        catch(Exception e){
            Logger.error("Unable to delete Object", e);
            return null;
        }
    }
}
