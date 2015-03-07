
import java.util.Collection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marek
 */
public interface RoomManager {
    public void createRoom(Room room);
    
    public void deleteRoom(Room room);
    
    public void updateRoom(Room room);
    
    public Room getRoomById(long id);
    
    public Room getRoomByNumber(int number);
    
    public Collection<Room> findAllRoom();
}
