package soc.agents;

import soc.assignment.*;
import uk.ac.lancaster.scc.turtles.stellar.core.agent.AgentIdentifier;
import uk.ac.lancaster.scc.turtles.stellar.core.history.relational.mysql.MySQLHistory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SAgentImp extends SAgent implements Runnable {

    private final AgentIdentifier selfIdentifier;
    private final AgentIdentifier iIdentifier;
    private final AgentIdentifier tIdentifier;
    private int cnt;
    private final int MAX_CNT = 5;

    public SAgentImp(AgentIdentifier selfIdentifier, AgentIdentifier iIdentifier, AgentIdentifier tIdentifier, MySQLHistory history) {
        super(selfIdentifier, history);
        this.selfIdentifier = selfIdentifier;
        this.iIdentifier = iIdentifier;
        this.tIdentifier = tIdentifier;
        cnt = 1;
        createLogs();
    }

    public void createLogs() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./logs/S.log"));
            writer.write("BEGIN SAGENT LOG:");
            writer.newLine();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendToLog(String logEntry) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./logs/S.log", true));
            writer.append(logEntry);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void handlePostAssignment(PostAssignment receivedMessage) {
        appendToLog(selfIdentifier.getName() + " received " + receivedMessage.getName() + "[" +
                    receivedMessage.bindingOfAID() + "]");
        // The student will request an extension with a probability of 80%.
        // Randomly select the reason between "medical" and "vacation"
        Random r = new Random();
        boolean requestExtension = r.nextInt(100) < 80 ? true : false;
        if (requestExtension) {
            String extensionReason = r.nextInt(2) == 0 ? "medical" : "vacation";
            EnabledRequestExtension enabledRequestExtension = retrieveEnabledRequestExtension().get(0);
            sendEnabledRequestExtension(enabledRequestExtension, extensionReason, iIdentifier);
            appendToLog(selfIdentifier.getName() + " sent new " + enabledRequestExtension.getName() + " with" +
                    " extensionReason = " + extensionReason);
        }

        // The student should wait 0.5s between receiving an assignment
        // and posting their submission.
        try {
            // This agent has nothing to do and sleeps for half a second.
            Thread.sleep(500);
        } catch (InterruptedException ex) {

        }

        // Since this agent sends MsgB immediately after each received MsgA
        // there can only be a single enabled MsgB when the following retrieve
        // method is called. Hence, use get(0) to get the first and only
        // enabled message from the list that is returned by retrieve method.
        EnabledPostSubmission enabledPostSubmission = retrieveEnabledPostSubmission().get(0);
        sendEnabledPostSubmission(enabledPostSubmission, "s_" + receivedMessage.bindingOfAID(), tIdentifier);
        appendToLog(selfIdentifier.getName() + " sent new " + enabledPostSubmission.getName() + " with" +
                    " submission = s_" + receivedMessage.bindingOfAID());
    }

    @Override
    protected void handleExtension(Extension receivedMessage) {
        appendToLog(selfIdentifier.getName() + " received " + receivedMessage.getName() + "[" +
                receivedMessage.bindingOfAID() + ", " +
                receivedMessage.bindingOfExtension() + "]");
    }

    @Override
    protected void handlePostGrade(PostGrade receivedMessage) {
        appendToLog(selfIdentifier.getName() + " received " + receivedMessage.getName() + "[" +
                    receivedMessage.bindingOfAID() + ", " +
                    receivedMessage.bindingOfSubmission() + ", " +
                    receivedMessage.bindingOfTentativeGrade() + "]");

        // The student should request a regrade with a 50% probability, and
        // accept the grade the rest of the time.
        Random r = new Random();
        int requestRegrade = r.nextInt(2);
        if (requestRegrade == 0) {
            // ACCEPT GRADE
            EnabledAcceptGrade enabledAcceptGrade = retrieveEnabledAcceptGrade().get(0);
            sendEnabledAcceptGrade(enabledAcceptGrade, receivedMessage.bindingOfTentativeGrade(), tIdentifier);
            appendToLog(selfIdentifier.getName() + " sent new " + enabledAcceptGrade.getName() + " with " +
                        "accepted grade = " + receivedMessage.bindingOfTentativeGrade());
            cnt++;
            if (MAX_CNT < cnt) {
                stop();
            }
        } else {
            // REQUEST REGRADE
            String reason = r.nextInt(2) == 0 ? "no_reason" : "legit_reason";
            EnabledRequestRegrade enabledRequestRegrade = retrieveEnabledRequestRegrade().get(0);
            String regradeReason = "r_" + receivedMessage.bindingOfAID() + "_" + reason;
            sendEnabledRequestRegrade(enabledRequestRegrade, regradeReason, tIdentifier);
            appendToLog(selfIdentifier.getName() + " sent new " + enabledRequestRegrade.getName() + " with " +
                        "regradeReason = " + regradeReason);
        }
    }

    @Override
    protected void handleRegrade(Regrade receivedMessage) {
        appendToLog(selfIdentifier.getName() + " received " + receivedMessage.getName() + "[" +
                    receivedMessage.bindingOfAID() + ", " +
                    receivedMessage.bindingOfGrade() + "]");
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
