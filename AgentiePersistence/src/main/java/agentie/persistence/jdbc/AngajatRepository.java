package agentie.persistence.jdbc;

import agentie.model.Angajat;
import agentie.persistence.IAngajatRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class AngajatRepository implements IAngajatRepository {
    private static final Logger logger = LogManager.getLogger();
    private JDBCUtils dbUtils;

    public AngajatRepository(Properties props) {
        logger.info("Initializing AngajatRepository with properties: {} ", props);
        dbUtils = new JDBCUtils(props);
    }

    @Override
    public int size() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from Angajat")) {
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
    public void save(Angajat entity) {
        logger.traceEntry("saving task {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Angajat values (?,?,?)")) {
            preStmt.setString(1, entity.getId());
            preStmt.setString(2, entity.getUsername());
            preStmt.setString(3, entity.getPassword());
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
        try (PreparedStatement preStmt = con.prepareStatement("delete from Angajat where id=?")) {
            preStmt.setString(1, string);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(String string, Angajat entity) {
        logger.traceEntry("updating task {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Angajat set username=?, password=? where id=?")) {
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());
            preStmt.setString(3, string);
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public Angajat findOne(String string) {
        logger.traceEntry("finding task with id {} ", string);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Angajat where id=?")) {
            preStmt.setString(1, string);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String id = result.getString("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    Angajat angajat = new Angajat(username, password);
                    angajat.setId(id);
                    logger.traceExit(angajat);
                    return angajat;
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
    public Iterable<Angajat> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Angajat> tasks = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Angajat")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String id = result.getString("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    Angajat angajat = new Angajat(username, password);
                    angajat.setId(id);
                    tasks.add(angajat);
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
