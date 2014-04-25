package clientNetwork;


import client.clientNetwork.ClientNode;
import common.messages.Account;
import common.messages.MutationType;
import utils.Commons;

/**
 * Created by suparngupta on 4/24/14.
 */
public class TestClients extends Thread {

    int clientId;
    int caseId;
    public TestClients(int clientID, int caseId) {
        this.clientId=clientID;
        this.caseId=caseId;
    }

    public void run() {
        switch (caseId){
            case 1:
                testCreate();
                break;
            case 2:
                testUpdate();
                break;
            case 3:{
                testRead();
                break;
            }
        }
    }

    private void testUpdate() {
        Account acc = Commons.createRandomAccount();
        int repeat = 100;
        long startTime = System.currentTimeMillis();
        for (int i=0;i<repeat;i++) {
            ClientNode node = new ClientNode(clientId);
            acc.setId("1398393570940-10-3");
            try {
                node.sendMutationRequest(acc, MutationType.UPDATE, acc.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("Client ID= "+clientId+"Time for 100 update requests" + (end - startTime));
    }

    private void testRead() {
        Account acc = Commons.createRandomAccount();
        acc.setId("1398393570940-10-3");
        int repeat = 100;
        long startTime = System.currentTimeMillis();
        for (int i=0;i<repeat;i++) {
            ClientNode node = new ClientNode(clientId);
            try {
                node.sendReadRequest(acc.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();

        System.out.println("Client ID= "+clientId+"Time for 100 read requests" + (end - startTime));
    }

    public void testCreate(){
        try {
            long startTime = System.currentTimeMillis();
            int repeat = 100;
            ClientNode node = new ClientNode(clientId);

            for (int i = 0; i < repeat; i++) {
                Account acc = Commons.createRandomAccount();
                acc.setId(node.generateObjectId());
                acc.setOwnerName(clientId + "-" +acc.getOwnerName());
                node.sendMutationRequest(acc, MutationType.CREATE, acc.getId());
            }
            long end = System.currentTimeMillis();

            System.out.println("Client ID= "+clientId+"Time for 100 create requests" + (end - startTime));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
