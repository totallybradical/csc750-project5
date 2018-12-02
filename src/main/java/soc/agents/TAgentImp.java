package soc.agents;

import soc.assignment.AcceptGrade;
import soc.assignment.PostSubmission;
import soc.assignment.RequestRegrade;
import soc.assignment.TAgent;
import uk.ac.lancaster.scc.turtles.stellar.core.agent.AgentIdentifier;
import uk.ac.lancaster.scc.turtles.stellar.core.history.relational.mysql.MySQLHistory;

public class TAgentImp extends TAgent implements Runnable {

    private final AgentIdentifier selfIdentifier;
    private final AgentIdentifier sIdentifier;

   public TAgentImp(AgentIdentifier selfIdentifier, AgentIdentifier sIdentifier, MySQLHistory history) {
        super(selfIdentifier, history);
        this.selfIdentifier = selfIdentifier;
        this.sIdentifier = sIdentifier;
   }
    
    @Override
    protected void handlePostSubmission(PostSubmission receivedMessage) {

    }

    @Override
    protected void handleAcceptGrade(AcceptGrade receivedMessage) {

    }

    @Override
    protected void handleRequestRegrade(RequestRegrade receivedMessage) {

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
