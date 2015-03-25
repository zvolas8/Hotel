
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marek
 */
public class GuestManagerImpl implements GuestManager{

    private static final Logger logger = Logger.getLogger(RoomManagerImpl.class.getName());
    
    private final DataSource dataSource;

    public GuestManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private void checkDataSource() {
        if (dataSource == null) {
            logger.log(Level.SEVERE, "DataSource is null.");
            throw new IllegalStateException("DataSource is not set");
        }
    }
    
    public boolean checkGuest(Guest guest){
        if(guest == null){
            throw new IllegalArgumentException("guest cannot be null");
        }
        
        if(guest.getId() != null){
            throw new IllegalArgumentException("id must be null");
        }
        
        if(guest.getName() == null){
            throw new IllegalArgumentException("null cannot be null");
        }
        
        if(guest.getSurname() == null){
            throw new IllegalArgumentException("surname cannot be null");
        }
        
        if(guest.getAddress() == null){
            throw new IllegalArgumentException("address cannot be null");
        }
        
        if(guest.getPhoneNumber() == null){
            throw new IllegalArgumentException("phone-number cannot be null");
        }
        return true;
    }
    
    @Override
    public void createGuest(Guest guest) {
        checkDataSource();
        checkGuest(guest);
        
        try(Connection conn = dataSource.getConnection()){
            try(PreparedStatement st = conn.prepareStatement("INSERT INTO guest (name,surname,address,phonenumber) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
                st.setString(1, guest.getName());
                st.setString(2, guest.getSurname());
                st.setString(3, guest.getAddress());
                st.setString(4, guest.getPhoneNumber());
                
                int count = st.executeUpdate();
                if (count != 1) {
                    throw new ServiceFailureException("Internal Error: More rows inserted when trying to insert guest " + guest);
                }
                ResultSet keyRS = st.getGeneratedKeys();
                guest.setId(getKey(keyRS, guest));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE,"Error when creating new room.",ex);
        }
    }

    private Long getKey(ResultSet keyRS, Guest guest) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert grave " + guest
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert grave " + guest
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert grave " + guest
                    + " - no key found");
        }
    }
    
    @Override
    public void deleteGuest(Long id) {
        checkDataSource();
        try(Connection conn = dataSource.getConnection()){
            try(PreparedStatement st = conn.prepareStatement("DELETE FROM GUEST WHERE id=?")){
                st.setLong(1, id);
                
                int count = st.executeUpdate();
                if(count != 1){
                    throw new ServiceFailureException("did not delete guest with id ="+id);
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE,"Error when delete guest", ex);
            throw new ServiceFailureException("Error when retrieving all guests", ex);
        }
    }

    @Override
    public void updateGuest(Guest guest) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Guest getGuestById(Long id) throws ServiceFailureException {
        checkDataSource();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT id,name,surname,address,phonenumber FROM guest WHERE id = ?")) {
                st.setLong(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    Guest guest = resultSetToGuest(rs);
                    if (rs.next()) {
                        throw new ServiceFailureException(
                                "Internal error: More entities with the same id found "
                                        + "(source id: " + id + ", found " + guest + " and " + resultSetToGuest(rs));
                    }
                    return guest;
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE,"Error when retrieving guest.", ex);
            throw new ServiceFailureException("Error when retrieving all guests", ex);
        }
    }

    @Override
    public List<Guest> getGuestsBySurname(String Surname) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Guest> findAllGuest() {
        checkDataSource();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT id,name,surname,address,phonenumber FROM guest")) {
                ResultSet rs = st.executeQuery();
                List<Guest> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToGuest(rs));
                }
                return result;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error when retrieving all guests", ex);
            throw new ServiceFailureException("Error when retrieving all guests", ex);
        }
    }
    
    private Guest resultSetToGuest(ResultSet rs) throws SQLException {
        Guest guest = new Guest();
        guest.setId(rs.getLong("id"));
        guest.setName(rs.getString("name"));
        guest.setSurname(rs.getString("surname"));
        guest.setAddress(rs.getString("address"));
        guest.setPhoneNumber(rs.getString("phonenumber"));
        return guest;
    }
    
}
