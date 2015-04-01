
import java.sql.Connection;
//import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
public class StayManagerImpl implements StayManager{
    
    private static final Logger logger = Logger.getLogger(StayManagerImpl.class.getName());
    
    private final DataSource dataSource;

    public StayManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private void checkDataSource() {
        if (dataSource == null) {
            logger.log(Level.SEVERE, "DataSource is null.");
            throw new IllegalStateException("DataSource is not set");
        }
    }
    
    public boolean checkStay(Stay stay){
        if(stay.getId() != null){
            throw new IllegalArgumentException("");
        }
        
        if(stay.getGuest() == null){
            throw new IllegalArgumentException("");
        }
        
        if(stay.getRoom() == null){
            throw new IllegalArgumentException("");
        }
        
        if(stay.getStartOfStay() == null){
            throw new IllegalArgumentException("");
        }
        
        if(stay.getEndOfStay()== null){
            throw new IllegalArgumentException("");
        }
        
        if(stay.getPrice()== null){
            throw new IllegalArgumentException("");
        }
        return true;
    }
    @Override
    public void createStay(Stay stay) {
        checkStay(stay);
        checkDataSource();
        
        try(Connection conn = dataSource.getConnection()){
            try(PreparedStatement st = conn.prepareStatement("INSERT INTO stay (guest_id, room_id, start_of_stay, end_of_stay, total_price) VALUES (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
                st.setLong(1, stay.getGuest().getId());
                st.setLong(2, stay.getRoom().getId());
                //st.setDate(3, (Date) stay.getStartOfStay());
                //st.setDate(4, (Date) stay.getEndOfStay());
                st.setTimestamp(3, dateToTimestamp(stay.getStartOfStay()));
                st.setTimestamp(4, dateToTimestamp(stay.getEndOfStay()));
                st.setBigDecimal(5, stay.getPrice());
                
                int count = st.executeUpdate();
                if (count != 1) {
                    throw new ServiceFailureException("Internal Error: More rows inserted when trying to insert stay " + stay);
                }
                ResultSet keyRS = st.getGeneratedKeys();
                stay.setId(getKey(keyRS, stay));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE,"Error when creating stay",ex);
            throw new ServiceFailureException("Error when creating stay", ex);
        }
    }
    
    private Timestamp dateToTimestamp(java.util.Date date) {
        if (date == null) {
            return null;
        } else if (date instanceof Timestamp) {
            return (Timestamp) date;
        } else {
            return new Timestamp(date.getTime());
        }
    }
    
    private Long getKey(ResultSet keyRS, Stay stay) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert stay " + stay
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert stay " + stay
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert stay " + stay
                    + " - no key found");
        }
    }

    @Override
    public void deleteStay(Long id) {
        checkDataSource();
        try(Connection conn = dataSource.getConnection()){
            try(PreparedStatement st = conn.prepareStatement("DELETE FROM stay WHERE id=?")){
                st.setLong(1, id);
                if(st.executeUpdate()!=1) {
                    throw new ServiceFailureException("did not delete stay with id ="+id);
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE,"Error when delete stay",ex);
            throw new ServiceFailureException("Error when retrieving all stays", ex);
        }
    }

    @Override
    public void updateStay(Stay stay) {
        checkDataSource();
        if(stay==null) throw new IllegalArgumentException("stay pointer is null");
        if(stay.getId()==null) throw new IllegalArgumentException("stay with null id cannot be updated");
        if(stay.getGuest()==null) throw new IllegalArgumentException("stay with null guest cannot be updated");
        if(stay.getRoom()==null) throw new IllegalArgumentException("stay with null room cannot be updated");
        if(stay.getStartOfStay()==null) throw new IllegalArgumentException("stay with null start of stay cannot be updated");
        if(stay.getEndOfStay()==null) throw new IllegalArgumentException("stay with null end of stay cannot be updated");
        if(stay.getPrice()==null) throw new IllegalArgumentException("stay with null total price cannot be updated");

        try (Connection conn = dataSource.getConnection()) {
            try(PreparedStatement st = conn.prepareStatement("UPDATE stay SET guest_id=?,room_id=?,start_of_stay=?, end_of_stay=?, total_price=? WHERE id=?")) {
                st.setLong(1, stay.getGuest().getId());
                st.setLong(2, stay.getRoom().getId());
                //st.setDate(3, (Date) stay.getStartOfStay());
                //st.setDate(4, (Date) stay.getEndOfStay());
                st.setTimestamp(3, dateToTimestamp(stay.getStartOfStay()));
                st.setTimestamp(4, dateToTimestamp(stay.getEndOfStay()));
                st.setBigDecimal(5, stay.getPrice());
                st.setLong(6,stay.getId());
             
                if(st.executeUpdate()!=1) {
                    throw new IllegalArgumentException("cannot update stay "+stay);
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error when updating stay", ex);
            throw new ServiceFailureException("Error when updating stay", ex);
        }
    }

    @Override
    public Stay getStayByID(Long id) {
        checkDataSource();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT * FROM stay,room,guest WHERE (stay.guest_id = guest.id AND stay.room_id = room.id) AND stay.id=?")) {
                st.setLong(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    Stay stay = resultSetToStay(rs);
                    if (rs.next()) {
                        throw new ServiceFailureException(
                                "Internal error: More entities with the same id found "
                                        + "(source id: " + id + ", found " + stay + " and " + resultSetToStay(rs));
                    }
                    return stay;
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE,"Error when retrieving stay.", ex);
            throw new ServiceFailureException("Error when retrieving stay", ex);
        }
    }

    @Override
    public List<Stay> findAllStay() {
        checkDataSource();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT * FROM stay,room,guest WHERE (stay.guest_id = guest.id AND stay.room_id = room.id)")) {
                ResultSet rs = st.executeQuery();
                List<Stay> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToStay(rs));
                }
                return result;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error when retrieving all stays", ex);
            throw new ServiceFailureException("Error when retrieving all stays", ex);
        }
    }
    
    private Stay resultSetToStay(ResultSet rs) throws SQLException {
        Stay stay = new Stay();
        stay.setId(rs.getLong("id"));
        stay.setGuest(getGuest(rs));
        stay.setRoom(getRoom(rs));
        //stay.setStartOfStay(rs.getDate("start_of_stay"));
        stay.setStartOfStay(rs.getTimestamp("start_of_stay"));
        //stay.setEndOfStay(rs.getDate("end_of_stay"));
        stay.setEndOfStay(rs.getTimestamp("end_of_stay"));
        stay.setPrice(rs.getBigDecimal("total_price"));
        return stay;
    }
    
    private Guest getGuest(ResultSet rs) throws SQLException{
        Guest guest = new Guest();
        guest.setId(rs.getLong("id"));
        guest.setName(rs.getString("name"));
        guest.setSurname(rs.getString("surname"));
        guest.setAddress(rs.getString("address"));
        guest.setPhoneNumber(rs.getString("phonenumber"));
        return guest;
    }
    
    private Room getRoom(ResultSet rs) throws SQLException{
        Room room = new Room();
        room.setId(rs.getLong("id"));
        room.setFloor(rs.getInt("floor"));
        room.setCapacity(rs.getInt("capacity"));
        room.setNote(rs.getString("note"));
        room.setPricePerNight(rs.getBigDecimal("price"));
        return room;
    }
}
