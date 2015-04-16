package project;


import java.util.Collection;
import java.util.List;

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
    
    public Room findCurrentRoomWithGuest(Guest guest);
    
    public Guest findCurrentGuestWithRoom(Room room);
    
    public List<Room> findAllEmptyRooms();
    
    public List<Stay> findStaysForGuest(Guest guest);
    
    public List<Stay> findStaysForRoom(Room room);
}
