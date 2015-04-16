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
public interface RoomManager {
    public void createRoom(Room room) throws IllegalArgumentException;
    
    public void deleteRoom(Long id);
    
    public void updateRoom(Room room) throws IllegalArgumentException;
    
    public Room getRoomById(Long id);
    
    public List<Room> findAllRoom();
}
