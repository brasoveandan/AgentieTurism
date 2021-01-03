package agentie.server;

import agentie.model.Angajat;
import agentie.model.Bilet;
import agentie.model.Zbor;
import agentie.persistence.jdbc.AngajatRepository;
import agentie.persistence.jdbc.BiletRepository;
import agentie.persistence.jdbc.ZborRepository;
import agentie.services.AgentieException;
import agentie.services.IAgentieObserver;
import agentie.services.IAgentieServices;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AgentieServicesImplementation implements IAgentieServices {
    private AngajatRepository angajatRepository;
    private ZborRepository zborRepository;
    private BiletRepository biletRepository;
    public Map<String, IAgentieObserver> angajatiLogati;

    public AgentieServicesImplementation(AngajatRepository angajatRepository, ZborRepository zborRepository, BiletRepository biletRepository) {
        this.angajatRepository = angajatRepository;
        this.zborRepository = zborRepository;
        this.biletRepository = biletRepository;
        this.angajatiLogati = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(String username, String password, IAgentieObserver client) throws AgentieException {
        Angajat angajat = new Angajat("", "");
        angajatRepository.findAll().forEach(angajat1 -> {
            if (angajat1.getUsername().equals(username) && angajat1.getPassword().equals(password)) {
                angajat.setId(angajat1.getId());
                angajat.setUsername(angajat1.getUsername());
                angajat.setPassword(angajat1.getPassword());
            }
        });
        if(angajat.getUsername().equals(username))
        {
            if(angajatiLogati.get(angajat.getUsername()) != null)
                throw new AgentieException("Angajatul este deja logat!");
            if(angajat.getPassword().compareTo(angajat.getPassword()) == 0)
                angajatiLogati.put(angajat.getUsername(), client);
        }
        else
        {
            throw new AgentieException("Wrong username or password");
        }
    }

    @Override
    public void logout(Angajat angajat, IAgentieObserver client) throws AgentieException {
        IAgentieObserver localClient=angajatiLogati.remove(angajat.getUsername());
        if (localClient == null)
            throw new AgentieException("Angajatul "+angajat.getId()+" nu este logat.");
    }

    @Override
    public synchronized Zbor[] findAllZbor() {
        Iterable<Zbor> zboruri = zborRepository.findAll();
        Zbor[] zbors = new Zbor[zborRepository.size()];
        List<Zbor> listaZboruri = StreamSupport.stream(zboruri.spliterator(), false)
                .collect(Collectors.toList());
        for (int elem =0; elem < listaZboruri.size(); elem++)
            zbors[elem] =  listaZboruri.get(elem);
        return zbors;
    }

    @Override
    public synchronized Zbor[] saveBilet(String id, String idZbor, String numeClient, String numeTuristi, String adresaClient, int numarLocuri) throws AgentieException {
        Bilet bilet = new Bilet(idZbor, numeClient, adresaClient, numeTuristi, numarLocuri);
        bilet.setId(id);
        Bilet biletExistent = biletRepository.findOne(bilet.getId());
        if (biletExistent!=null) {
            throw new AgentieException("Exista deja bilet cu id-ul" + biletExistent.getId());
        }
        Zbor zbor = zborRepository.findOne(idZbor);
        if (numarLocuri < zbor.getNrLocuri() && numarLocuri > 0) {
            zbor.setNrLocuri(zbor.getNrLocuri() - numarLocuri);
            zborRepository.update(idZbor, zbor);
            biletRepository.save(bilet);
            for (IAgentieObserver client : angajatiLogati.values()) {
                try {
                    client.cumparareBilet(findAllZbor());
                } catch (AgentieException | RemoteException e) {
                    System.out.println("Error: " + e);
                    e.printStackTrace();
                }
            }
        }
        else throw new AgentieException("Mai sunt doar " + zbor.getNrLocuri() + " locuri disponibile!");
        return findAllZbor();
    }
}
