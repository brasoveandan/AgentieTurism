package agentie.services;

import agentie.model.Angajat;
import agentie.model.Zbor;

public interface IAgentieServices {
    void login(String username, String password, IAgentieObserver client) throws AgentieException;
    void logout(Angajat angajat, IAgentieObserver client) throws AgentieException;
    Zbor[] findAllZbor() throws AgentieException;
    Zbor[] saveBilet(String id, String idZbor, String numeClient, String numeTuristi, String adresaClient, int numarLocuri) throws AgentieException;
}
