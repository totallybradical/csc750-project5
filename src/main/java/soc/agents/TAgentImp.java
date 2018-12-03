package soc.agents;

import soc.assignment.*;
import uk.ac.lancaster.scc.turtles.stellar.core.agent.AgentIdentifier;
import uk.ac.lancaster.scc.turtles.stellar.core.history.relational.mysql.MySQLHistory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TAgentImp extends TAgent implements Runnable {

    private final AgentIdentifier selfIdentifier;
    private final AgentIdentifier sIdentifier;
    private int cnt;
    private final int MAX_CNT = 5;

   public TAgentImp(AgentIdentifier selfIdentifier, AgentIdentifier sIdentifier, MySQLHistory history) {
       super(selfIdentifier, history);
       this.selfIdentifier = selfIdentifier;
       this.sIdentifier = sIdentifier;
       cnt = 1;
       createLogs();
   }

    public void createLogs() {
       try {
           BufferedWriter writer = new BufferedWriter(new FileWriter("./logs/T.log"));
           writer.write("BEGIN TAGENT LOG:");
           writer.newLine();
           writer.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    public void appendToLog(String logEntry) {
       try {
           BufferedWriter writer = new BufferedWriter(new FileWriter("./logs/T.log", true));
           writer.append(logEntry);
           writer.newLine();
           writer.close();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }
    
    @Override
    protected void handlePostSubmission(PostSubmission receivedMessage) {
       appendToLog(selfIdentifier.getName() + " received " + receivedMessage.getName() + "[" +
                receivedMessage.bindingOfAID() + ", " +
                receivedMessage.bindingOfSubmission() + "]");
       Random r = new Random();
       int grade = r.nextInt(101);
        // Since this agent sends MsgB immediately after each received MsgA
        // there can only be a sinle enabled MsgB when the following retrieve
        // method is called. Hence, use get(0) to get the first and only
        // enabled message from the list that is returned by retrieve method.
        EnabledPostGrade enabledPostGrade = retrieveEnabledPostGrade().get(0);
        sendEnabledPostGrade(enabledPostGrade, String.valueOf(grade), sIdentifier);
        appendToLog(selfIdentifier.getName() + " sent new " + enabledPostGrade.getName() + " with" +
                " grade = " + String.valueOf(grade));
    }

    @Override
    protected void handleAcceptGrade(AcceptGrade receivedMessage) {
        appendToLog(selfIdentifier.getName() + " received " + receivedMessage.getName() + "[" +
                receivedMessage.bindingOfAID() + ", " +
                receivedMessage.bindingOfGrade()+ "]");
        cnt++;
        if (MAX_CNT < cnt) {
            stop();
        }
    }

    @Override
    protected void handleRequestRegrade(RequestRegrade receivedMessage) {
        appendToLog(selfIdentifier.getName() + " received " + receivedMessage.getName() + "[" +
                receivedMessage.bindingOfAID() + ", " +
                receivedMessage.bindingOfRegradeReason() + "]");
        if (receivedMessage.bindingOfRegradeReason().endsWith("legit_reason")) {
            Random r = new Random();
            int newGrade = r.nextInt(101);
            EnabledRegrade enabledRegrade = retrieveEnabledRegrade().get(0);
            sendEnabledRegrade(enabledRegrade, String.valueOf(newGrade), sIdentifier);
            appendToLog(selfIdentifier.getName() + " sent new " + enabledRegrade.getName() + " with" +
                    " grade (new) = " + newGrade);
        } else {
            EnabledRegrade enabledRegrade = retrieveEnabledRegrade().get(0);
            sendEnabledRegrade(enabledRegrade, receivedMessage.bindingOfTentativeGrade(), sIdentifier);
            appendToLog(selfIdentifier.getName() + " sent new " + enabledRegrade.getName() + " with" +
                    " grade (old) = " + receivedMessage.bindingOfTentativeGrade());
        }
        cnt++;
        if (MAX_CNT < cnt) {
            stop();
        }
    }

    @Override
    protected void beforeStart() {
        // This handler is called by Stellar only once before the agent starts.
        // No messages can be sent or received here.
        // This method can be used for initializations specific to this agent
        // or can be left empty if no such initialization is needed.
        appendToLog(selfIdentifier.getName() + " is ready to start.");
    }

    @Override
    protected void afterStart() {
        appendToLog(selfIdentifier.getName() + " has started.");
    }

    @Override
    protected void act() {
        // This handler is called at least once by Stellar after the reception of
        // a message. If the agent does not receive any message this handler
        // is repeatedly called until the reception of a message or termination
        // of the agent.
        // This method can be used to implement the agent's business logic in
        // addition to the specific message handlers.
        // Calls to this handler are NOT concurrent with any call to any other handler,
        // which means that there is no need for synchronization.
        try {
            // This agent has nothing to do and sleeps for half a second.
            Thread.sleep(500);
        } catch (InterruptedException ex) {

        }
    }

    @Override
    protected void beforeStop() {
        // This handler is called by Stellar only once before the agent stops.
        // No messages are received once this handler is called, but the
        // agent can still send messages within this handler.
        // This method can be used to send remaining messages before the agent
        // stops or can be left empty if there are no such messages.
        appendToLog(selfIdentifier.getName() + " is going to stop.");
    }

    @Override
    protected void afterStop() {
        // This handler is called by Stellar only once when the agent stops.
        // No messages can be sent or received here.
        // This method can be used to release resource that are specific to this
        // agent or can be left empty if no such clean-up is needed.
        appendToLog(selfIdentifier.getName() + " has stopped.");
    }

    @Override
    public void run() {
        execute();
    }
    
}
