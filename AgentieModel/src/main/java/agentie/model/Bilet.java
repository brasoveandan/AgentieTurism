package agentie.model;

import java.io.Serializable;
import java.util.Objects;

public class Bilet extends Entity<String> implements Serializable {
    private String idZbor;
    private String numeClient;
    private String adresaClient;
    private String numeTurisiti;
    private int numarLocuri;

    public Bilet(String idZbor, String numeClient, String adresaClient, String numeTurisiti, int numarLocuri) {
        this.idZbor = idZbor;
        this.numeClient = numeClient;
        this.adresaClient = adresaClient;
        this.numeTurisiti = numeTurisiti;
        this.numarLocuri = numarLocuri;
    }

    public String getZbor() {
        return idZbor;
    }

    public void setZbor(String idZbor) {
        this.idZbor = idZbor;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    public String getAdresaClient() {
        return adresaClient;
    }

    public void setAdresaClient(String adresaClient) {
        this.adresaClient = adresaClient;
    }

    public String getNumeTurisiti() {
        return numeTurisiti;
    }

    public void setNumeTurisiti(String numeTurisiti) {
        this.numeTurisiti = numeTurisiti;
    }

    public int getNumarLocuri() {
        return numarLocuri;
    }

    public void setNumarLocuri(int numarLocuri) {
        this.numarLocuri = numarLocuri;
    }

    @Override
    public String toString() {
        return "Bilet{" +
                "zbor=" + idZbor +
                ", numeClient='" + numeClient + '\'' +
                ", adresaClient='" + adresaClient + '\'' +
                ", numeTurisit=" + numeTurisiti +
                ", numarLocuri=" + numarLocuri +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bilet bilet = (Bilet) o;
        return numarLocuri == bilet.numarLocuri &&
                Objects.equals(idZbor, bilet.idZbor) &&
                Objects.equals(numeClient, bilet.numeClient) &&
                Objects.equals(adresaClient, bilet.adresaClient) &&
                Objects.equals(numeTurisiti, bilet.numeTurisiti);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idZbor, numeClient, adresaClient, numeTurisiti, numarLocuri);
    }
}
