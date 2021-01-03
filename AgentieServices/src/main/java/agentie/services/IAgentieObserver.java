package agentie.services;

import agentie.model.Zbor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAgentieObserver extends Remote {
    void cumparareBilet(Zbor[] zbor) throws AgentieException, RemoteException;
}
