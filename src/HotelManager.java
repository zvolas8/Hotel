
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
public interface HotelManager {
    public void putGuestInRoom(Guest guest, Room room);
    
    public void removeGuestFromRoom(Guest guest, Room room);
    
    public Room findCurrentRoomWithGuest(Guest guest);
    
    public Guest findCurrentGuestWithRoom(Room room);
    
    public Collection<Room> findAllEmptyRooms();
    
    public Collection<Stay> findStaysForGuest(Guest guest);
    
    public Collection<Stay> findStaysForRoom(Room room);
}
