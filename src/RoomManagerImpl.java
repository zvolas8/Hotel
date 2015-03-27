
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
public class RoomManagerImpl implements RoomManager{
    
    private static final Logger logger = Logger.getLogger(RoomManagerImpl.class.getName());
    
    private final DataSource dataSource;

    public RoomManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    private void checkDataSource() {
        if (dataSource == null) {
            logger.log(Level.SEVERE, "DataSource is null.");
            throw new IllegalStateException("DataSource is not set");
        }
    }
    
    public boolean checkRoom(Room room){
        if(room==null){
            throw new IllegalArgumentException("room cannot be null!");
        }
        if(room.getId() != null){
            throw new IllegalArgumentException("id must be null!");
        }
        
        if(room.getCapacity() < 1){
            throw new IllegalArgumentException("capacity must be less than one");
        }
        
        if(room.getFloor() < 0){
            throw new IllegalArgumentException("floor must be less than zero");
        }
        
        if(room.getNote() == null){
            throw new IllegalArgumentException("note cannot be null");
        }    
        
        if(room.getPricePerNight() == null){
            throw new IllegalArgumentException("PricePerNight cannot be null");
        }
        return true;
    }
    
    @Override
    public void createRoom(Room room) {
        checkRoom(room);
        checkDataSource();
        
        try(Connection conn = dataSource.getConnection()){
            try(PreparedStatement st = conn.prepareStatement("INSERT INTO room (floor,capacity,note,price) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
                st.setInt(1, room.getFloor());
                st.setInt(2, room.getCapacity());
                st.setString(3, room.getNote());
                st.setBigDecimal(4, room.getPricePerNight());
                
                int count = st.executeUpdate();
                if (count != 1) {
                    throw new ServiceFailureException("Internal Error: More rows inserted when trying to insert room " + room);
                }
                ResultSet keyRS = st.getGeneratedKeys();
                room.setId(getKey(keyRS, room));
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error when creating new room.", ex);
        }
        
    }

    private Long getKey(ResultSet keyRS, Room room) throws ServiceFailureException, SQLException {
        if (keyRS.next()) {
            if (keyRS.getMetaData().getColumnCount() != 1) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert grave " + room
                        + " - wrong key fields count: " + keyRS.getMetaData().getColumnCount());
            }
            Long result = keyRS.getLong(1);
            if (keyRS.next()) {
                throw new ServiceFailureException("Internal Error: Generated key"
                        + "retriving failed when trying to insert grave " + room
                        + " - more keys found");
            }
            return result;
        } else {
            throw new ServiceFailureException("Internal Error: Generated key"
                    + "retriving failed when trying to insert grave " + room
                    + " - no key found");
        }
    }
    
    @Override
    public void deleteRoom(Long id) {
        checkDataSource();
        try (Connection conn = dataSource.getConnection()) {
            try(PreparedStatement st = conn.prepareStatement("DELETE FROM room WHERE id=?")) {
                st.setLong(1,id);
                if(st.executeUpdate()!=1) {
                    throw new ServiceFailureException("did not delete room with id ="+id);
                }
            }
        } catch (SQLException ex) {
           logger.log(Level.SEVERE, "Error when deleting room.", ex);
           throw new ServiceFailureException("Error when retrieving all rooms", ex);
        }
    }

    @Override
    public void updateRoom(Room room) {
        checkDataSource();
        if(room==null) throw new IllegalArgumentException("room pointer is null");
        if(room.getId()==null) throw new IllegalArgumentException("room with null id cannot be updated");        
        if(room.getFloor()<0) throw new IllegalArgumentException("room floor is negative number");
        if(room.getCapacity()<1) throw new IllegalArgumentException("room capacity is not positive number");
        if(room.getPricePerNight() == null) throw new IllegalArgumentException("pricePerNight cannot be null");
        
        try (Connection conn = dataSource.getConnection()) {
            try(PreparedStatement st = conn.prepareStatement("UPDATE room SET floor=?,capacity=?,note=?, price=? WHERE id=?")) {
                st.setInt(1,room.getFloor());
                st.setInt(2,room.getCapacity());
                st.setString(3,room.getNote());
                st.setBigDecimal(4, room.getPricePerNight());
                st.setLong(5,room.getId());
                
                if(st.executeUpdate()!=1) {
                    throw new IllegalArgumentException("cannot update room "+room);
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error when updating room", ex);
            throw new ServiceFailureException("Error when updating room", ex);
        }
    }

    @Override
    public Room getRoomById(Long id) throws ServiceFailureException {
        checkDataSource();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT id,floor,capacity,note, price FROM room WHERE id = ?")) {
                st.setLong(1, id);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    Room room = resultSetToRoom(rs);
                    if (rs.next()) {
                        throw new ServiceFailureException(
                                "Internal error: More entities with the same id found "
                                        + "(source id: " + id + ", found " + room + " and " + resultSetToRoom(rs));
                    }
                    return room;
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE,"Error when retrieving room.", ex);
            throw new ServiceFailureException("Error when retrieving all rooms", ex);
        }
    }
    
    private Room resultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setId(rs.getLong("id"));
        room.setFloor(rs.getInt("floor"));
        room.setCapacity(rs.getInt("capacity"));
        room.setNote(rs.getString("note"));
        room.setPricePerNight(rs.getBigDecimal("price"));
        return room;
    }

    @Override
    public List<Room> findAllRoom() {
        checkDataSource();
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT id,floor,capacity,note,price FROM room")) {
                ResultSet rs = st.executeQuery();
                List<Room> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(resultSetToRoom(rs));
                }
                return result;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error when retrieving all rooms", ex);
            throw new ServiceFailureException("Error when retrieving all rooms", ex);
        }
    }
    
}
