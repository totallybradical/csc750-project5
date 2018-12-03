package soc.agents;

import soc.assignment.EnabledExtension;
import soc.assignment.EnabledPostAssignment;
import soc.assignment.IAgent;
import soc.assignment.RequestExtension;
import uk.ac.lancaster.scc.turtles.stellar.core.agent.AgentIdentifier;
import uk.ac.lancaster.scc.turtles.stellar.core.history.relational.mysql.MySQLHistory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class IAgentImp extends IAgent implements Runnable {

    private final AgentIdentifier selfIdentifier;
    private final AgentIdentifier sIdentifier;
    private int assignmentCount = 5;

    public IAgentImp(AgentIdentifier selfIdentifier, AgentIdentifier sIdentifier, MySQLHistory history) {
        super(selfIdentifier, history);
        this.selfIdentifier = selfIdentifier;
        this.sIdentifier = sIdentifier;
        createLogs();
    }

    public void createLogs() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./logs/I.log"));
            writer.write("BEGIN IAGENT LOG:");
            writer.newLine();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendToLog(String logEntry) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./logs/I.log", true));
            writer.append(logEntry);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void handleRequestExtension(RequestExtension receivedMessage) {
        appendToLog(selfIdentifier.getName() + " received " + receivedMessage.getName() + "[" +
                receivedMessage.bindingOfAID() + ", " +
                receivedMessage.bindingOfExtensionReason() + "]");
        if (receivedMessage.bindingOfExtensionReason().equals("medical")) {
            EnabledExtension enabledExtension = retrieveEnabledExtension().get(0);
            sendEnabledExtension(enabledExtension, "approved", sIdentifier);
            appendToLog(selfIdentifier.getName() + " sent " + enabledExtension.getName() + " with " +
                    "extension = approved]");
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
        // This handler is called by Stellar only once when the agent starts.
        // Agent can send messages here.
        // Agent does not receive any message until this method returns.
        for (int i = 0; i < assignmentCount; i++) {
            EnabledPostAssignment enabledPostAssignment = retrieveEnabledPostAssignment();
            sendEnabledPostAssignment(enabledPostAssignment, sIdentifier);
            appendToLog(selfIdentifier.getName() + " sent " + enabledPostAssignment.getName() + " " + (i+1));
            try {
                // This agent has nothing to do and sleeps for half a second.
                Thread.sleep(500);
            } catch (InterruptedException ex) {

            }
        }
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
