package agentie.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Zbor extends Entity<String> implements Serializable {
    private String destinatia;
    private LocalDate data;
    private LocalTime ora;
    private String aeroport;
    private int nrLocuri;

    public Zbor(String destinatia, LocalDate data, LocalTime ora, String aeroport, int nrLocuri) {
        this.destinatia = destinatia;
        this.data = data;
        this.ora = ora;
        this.aeroport = aeroport;
        this.nrLocuri = nrLocuri;
    }

    public String getDestinatia() {
        return destinatia;
    }

    public void setDestinatia(String destinatia) {
        this.destinatia = destinatia;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public String getAeroport() {
        return aeroport;
    }

    public void setAeroport(String aeroport) {
        this.aeroport = aeroport;
    }

    public int getNrLocuri() {
        return nrLocuri;
    }

    public void setNrLocuri(int nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    @Override
    public String toString() {
        return "Zbor{" +
                "destinatia='" + destinatia + '\'' +
                ", data=" + data +
                ", ora=" + ora +
                ", aeroport='" + aeroport + '\'' +
                ", nrLocuri=" + nrLocuri +
                '}';
    }

    public String getDataString() {
        if (data.getDayOfMonth() >= 1 && data.getDayOfMonth() <= 9)
            return "0" + data.getDayOfMonth() + "/" + data.getMonthValue() + "/" + data.getYear();
        return data.getDayOfMonth() + "/" + data.getMonthValue() + "/" + data.getYear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zbor zbor = (Zbor) o;
        return nrLocuri == zbor.nrLocuri &&
                Objects.equals(destinatia, zbor.destinatia) &&
                Objects.equals(data, zbor.data) &&
                Objects.equals(ora, zbor.ora) &&
                Objects.equals(aeroport, zbor.aeroport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinatia, data, ora, aeroport, nrLocuri);
    }
}
