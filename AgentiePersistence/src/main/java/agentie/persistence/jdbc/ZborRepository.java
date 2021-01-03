package agentie.persistence.jdbc;

import agentie.model.Zbor;
import agentie.persistence.IZborRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ZborRepository implements IZborRepository {
    private static final Logger logger = LogManager.getLogger();
    private JDBCUtils dbUtils;

    public ZborRepository(Properties props) {
        logger.info("Initializing ZborRepository with properties: {} ", props);
        dbUtils = new JDBCUtils(props);
    }

    @Override
    public int size() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select count(*) as [SIZE] from Zbor")) {
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
    public void save(Zbor entity) {
        logger.traceEntry("saving task {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Zbor values (?,?,?,?,?,?)")) {
            preStmt.setString(1, entity.getId());
            preStmt.setString(2, entity.getDestinatia());
            preStmt.setString(3, entity.getDataString());
            preStmt.setString(4, entity.getOra().toString());
            preStmt.setString(5, entity.getAeroport());
            preStmt.setInt(6, entity.getNrLocuri());
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
        try (PreparedStatement preStmt = con.prepareStatement("delete from Zbor where id=?")) {
            preStmt.setString(1, string);
            int result = preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
        logger.traceExit();

    }

    @Override
    public void update(String string, Zbor entity) {
        logger.traceEntry("updating task {} ", entity);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Zbor set destinatia=?, dataPlecare=?, ora=?, aeroport=?, nrLocuri=? where id=?")) {
            preStmt.setString(1, entity.getDestinatia());
            preStmt.setString(2, entity.getDataString());
            preStmt.setString(3, entity.getOra().toString());
            preStmt.setString(4, entity.getAeroport());
            preStmt.setInt(5, entity.getNrLocuri());
            preStmt.setString(6, string);
            preStmt.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB " + ex);
        }
    }

    @Override
    public Zbor findOne(String string) {
        logger.traceEntry("finding task with id {} ", string);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Zbor where id=?")) {
            preStmt.setString(1, string);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String id = result.getString("id");
                    String destinatia = result.getString("destinatia");
                    String stringData = result.getString("dataPlecare");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate data = LocalDate.from(formatter.parse(stringData));
                    String oraString = result.getString("ora");
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("H:mm");
                    LocalTime ora = LocalTime.from(formatter2.parse(oraString));
                    String aeroport = result.getString("aeroport");
                    String nrLocuri = result.getString("nrLocuri");
                    Zbor zbor = new Zbor(destinatia, data, ora, aeroport, Integer.parseInt(nrLocuri));
                    zbor.setId(id);
                    logger.traceExit(zbor);
                    return zbor;
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
    public Iterable<Zbor> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Zbor> zboruri = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Zbor")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    String id = result.getString("id");
                    String destinatia = result.getString("destinatia");
                    String stringData = result.getString("dataPlecare");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate data = LocalDate.from(formatter.parse(stringData));
                    String oraString = result.getString("ora");
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("H:mm");
                    LocalTime ora = LocalTime.from(formatter2.parse(oraString));
                    String aeroport = result.getString("aeroport");
                    String nrLocuri = result.getString("nrLocuri");
                    Zbor zbor = new Zbor(destinatia, data, ora, aeroport, Integer.parseInt(nrLocuri));
                    zbor.setId(id);
                    zboruri.add(zbor);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(zboruri);
        return zboruri;
    }
}
