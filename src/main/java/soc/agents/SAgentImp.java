package soc.agents;

import soc.assignment.Extension;
import soc.assignment.PostAssignment;
import soc.assignment.PostGrade;
import soc.assignment.Regrade;
import soc.assignment.SAgent;
import uk.ac.lancaster.scc.turtles.stellar.core.agent.AgentIdentifier;
import uk.ac.lancaster.scc.turtles.stellar.core.history.relational.mysql.MySQLHistory;

public class SAgentImp extends SAgent implements Runnable {

    private final AgentIdentifier selfIdentifier;
    private final AgentIdentifier iIdentifier;
    private final AgentIdentifier tIdentifier;
    
    public SAgentImp(AgentIdentifier selfIdentifier, AgentIdentifier iIdentifier, AgentIdentifier tIdentifier, MySQLHistory history) {
        super(selfIdentifier, history);
        this.selfIdentifier = selfIdentifier;
        this.iIdentifier = iIdentifier;
        this.tIdentifier = tIdentifier;
    }
    
    @Override
    protected void handlePostAssignment(PostAssignment receivedMessage) {

    }

    @Override
    protected void handleExtension(Extension receivedMessage) {

    }

    @Override
    protected void handlePostGrade(PostGrade receivedMessage) {

    }

    @Override
    protected void handleRegrade(Regrade receivedMessage) {

    }

    @Override
    protected void beforeStart() {
        
    }

    @Override
    protected void afterStart() {
        
    }

    @Override
    protected void act() {
    }

    @Override
    protected void beforeStop() {
        
    }

    @Override
    protected void afterStop() {
        
    }

    @Override
    public void run() {
        execute();
    }
    
}
