package soc.agents;

import soc.assignment.IAgent;
import soc.assignment.RequestExtension;
import uk.ac.lancaster.scc.turtles.stellar.core.agent.AgentIdentifier;
import uk.ac.lancaster.scc.turtles.stellar.core.history.relational.mysql.MySQLHistory;

public class IAgentImp extends IAgent implements Runnable {

    private final AgentIdentifier selfIdentifier;
    private final AgentIdentifier sIdentifier;
    
    public IAgentImp(AgentIdentifier selfIdentifier, AgentIdentifier sIdentifier, MySQLHistory history) {
        super(selfIdentifier, history);
        this.selfIdentifier = selfIdentifier;
        this.sIdentifier = sIdentifier;
    }
    
    @Override
    protected void handleRequestExtension(RequestExtension receivedMessage) {
        
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
