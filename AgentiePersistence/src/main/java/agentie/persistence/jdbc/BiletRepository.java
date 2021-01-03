package agentie.persistence.jdbc;

import agentie.model.Bilet;
import agentie.persistence.IBiletRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BiletRepository implements IBiletRepository {
    private static final Logger logger = LogManager.getLogger();
    private JDBCUtils dbUtils;

    public BiletRepository(Properties props) {
        logger.info("Initializing BiletRepository with properties: {} ", props);
        dbUtils = new JDBCUtils(props);
    }

    @Override
    public int size() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from Bilet")) {
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        return 0;
    }

    @Override
    public void save(Bilet entity) {
        logger.traceEntry("saving task {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Bilet values (?,?,?,?,?,?)")) {
            preStmt.setString(1, entity.getId());
            preStmt.setString(2, entity.getZbor());
            preStmt.setString(3, entity.getNumeClient());
            preStmt.setString(4, entity.getAdresaClient());
            preStmt.setString(5, entity.getNumeTurisiti());
            preStmt.setInt(6, entity.getNumarLocuri());
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(String string) {
        logger.traceEntry("deleting task with {}", string);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("delete from Bilet where id=?")) {
            preStmt.setString(1, string);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(String string, Bilet entity) {
        logger.traceEntry("updating task {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Bilet set idZbor=?, numeClient=?, adresaClient=?, numeTuristi=?, numarLocuri=? where id=?")) {
            preStmt.setString(1, entity.getZbor());
            preStmt.setString(2, entity.getNumeClient());
            preStmt.setString(3, entity.getAdresaClient());
            preStmt.setString(4, entity.getNumeTurisiti());
            preStmt.setInt(5, entity.getNumarLocuri());
            preStmt.setString(6, string);
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public Bilet findOne(String string) {
        logger.traceEntry("finding task with id {} ", string);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Bilet where id=?")) {
            preStmt.setString(1, string);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String id = result.getString("id");
                    String idZbor = result.getString("idZbor");
                    String numeClient = result.getString("numeClient");
                    String adresaClient = result.getString("adresaClient");
                    String numeTuristi = result.getString("numeTuristi");
                    String numarLocuri = result.getString("numarLocuri");
                    Bilet bilet = new Bilet(idZbor, numeClient, adresaClient, numeTuristi, Integer.parseInt(numarLocuri));
                    bilet.setId(id);
                    logger.traceExit(idZbor);
                    return bilet;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit("No task found with id {}", string);
        return null;
    }

    @Override
    public Iterable<Bilet> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Bilet> tasks = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Bilet")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String id = result.getString("id");
                    String idZbor = result.getString("idZbor");
                    String numeClient = result.getString("numeClient");
                    String adresaClient = result.getString("adresaClient");
                    String numeTuristi = result.getString("numeTuristi");
                    String numarLocuri = result.getString("numarLocuri");
                    Bilet bilet = new Bilet(idZbor, numeClient, adresaClient, numeTuristi, Integer.parseInt(numarLocuri));
                    bilet.setId(id);
                    tasks.add(bilet);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(tasks);
        return tasks;
    }
}
