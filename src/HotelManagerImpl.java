
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class HotelManagerImpl implements HotelManager{

    private static final Logger logger = Logger.getLogger(HotelManagerImpl.class.getName());
    
    private final DataSource dataSource;

    public HotelManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private void checkDataSource() {
        if (dataSource == null) {
            logger.log(Level.SEVERE, "DataSource is null.");
            throw new IllegalStateException("DataSource is not set");
        }
    }
    
    @Override
    public void putGuestInRoom(Guest guest, Room room) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeGuestFromRoom(Guest guest, Room room) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Room findCurrentRoomWithGuest(Guest guest) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Guest findCurrentGuestWithRoom(Room room) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Room> findAllEmptyRooms() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Stay> findStaysForGuest(Guest guest) {
        if(guest.getId()==null){
            throw new IllegalArgumentException("guest id cannot be null");
        }
        checkDataSource();
        try(Connection conn = dataSource.getConnection()){
            try(PreparedStatement st = conn.prepareStatement("SELECT * FROM stay,room,guest WHERE (stay.guest_id = guest.id AND stay.room_id = room.id) AND guest_id = ?")){
                st.setLong(1, guest.getId());
                List<Stay> result = new ArrayList();
                ResultSet rs = st.executeQuery();
                while(rs.next()){
                    result.add(resultSetToStay(rs));
                }
                
                
                return result;
            }
            
        } catch (SQLException ex) {
            logger.log(Level.SEVERE,"Error when find stays for guest",ex);
            throw new ServiceFailureException("Error when find stays for guest");
        }
        
    }

    @Override
    public List<Stay> findStaysForRoom(Room room) {
        if(room.getId() == null){
            throw new IllegalArgumentException("room id cannot be null");
        }
        checkDataSource();
        try(Connection conn = dataSource.getConnection()){
            try(PreparedStatement st = conn.prepareStatement("SELECT * FROM stay,room,guest WHERE (stay.guest_id = guest.id AND stay.room_id = room.id) AND room_id = ?")){
                st.setLong(1, room.getId());
                List<Stay> result = new ArrayList();
                ResultSet rs = st.executeQuery();
                while(rs.next()){
                    result.add(resultSetToStay(rs));
                }
                
                
                return result;
            }
            
        } catch (SQLException ex) {
            logger.log(Level.SEVERE,"Error when find stays for room",ex);
            throw new ServiceFailureException("Error when find stays for room");
        }
    }
    
    private Stay resultSetToStay(ResultSet rs) throws SQLException {
        Stay stay = new Stay();
        stay.setId(rs.getLong("id"));
        stay.setGuest(getGuest(rs));
        stay.setRoom(getRoom(rs));
        stay.setStartOfStay(rs.getDate("start_of_stay"));
        stay.setEndOfStay(rs.getDate("end_of_stay"));
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
