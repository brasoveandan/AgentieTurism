package agentie.model;


import java.io.Serializable;
import java.util.Objects;

public class Angajat extends Entity<String> implements Serializable {
    private String username;
    private String password;

    public Angajat(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Angajat{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Angajat angajat = (Angajat) o;
        return Objects.equals(username, angajat.username) &&
                Objects.equals(password, angajat.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
