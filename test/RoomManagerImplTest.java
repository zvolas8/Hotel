/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author marek
 */
public class RoomManagerImplTest {
    
    private RoomManagerImpl manager;
    
    @Before
    public void setUp(){
        manager = new RoomManagerImpl();
    }
    
    @Test
    public void createRoom(){
        Room room = newRoom(1, 4, 3, "Pokoj s bezbarierovým přístupem");
        manager.createRoom(room);
        
        long roomID = room.getId();
        long roomNumber = room.getNumber();
        assertNotNull(roomID);
        assertEquals(403, roomNumber);
    }
    
    @Test
    public void deleteRoom(){
        Room room1 = newRoom(1, 4, 3, "rozbitá vana");
        Room room2 = newRoom(2, 4, 3, "postýlka pro děti");
        Room room3 = newRoom(3, 3, 5, "ok");
        manager.createRoom(room1);
        manager.createRoom(room2);
        manager.createRoom(room3);
        manager.deleteRoom(2);
        Collection<Room> result = manager.findAllRoom();
        
        Collection<Room> list = new ArrayList<>();
        list.add(room1);
        list.add(room2);
        list.add(room3);
        list.remove(room2);
        
        assertEquals(list.size(), result.size());  
    }
    
    @Test
    public void updateRoom(){
        Room room1 = newRoom(1, 3, 2, "v Opravě");
        Room room1new = newRoom(1, 3, 2,"opraven");
        manager.createRoom(room1);
        manager.updateRoom(room1new);
        Room result = manager.getRoomById(1);
        assertEquals(room1new.getId(), result.getId());
        assertEquals(room1new.getFloor(), result.getFloor());
        assertEquals(room1new.getNote(), result.getNote());
    }
    
    @Test
    public void getRoomById(){
        Room room = newRoom(1, 2, 4, "ok");
        manager.createRoom(room);
        List<Room> list = new ArrayList<>();
        list.add(room);
        Room result = list.get(0);

        assertEquals(manager.getRoomById(1).getId(), result.getId());
        assertEquals(manager.getRoomById(1).getFloor(), result.getFloor());
    }
    
    @Test
    public void getRoombyNumber(){
        Room room = newRoom(1, 2, 4, "ok");
        manager.createRoom(room);
        List<Room> list = new ArrayList<>();
        list.add(room);
        Room result = list.get(0);

        assertEquals(manager.getRoomByNumber(201).getId(), result.getId());
        assertEquals(manager.getRoomByNumber(201).getCapacity(), result.getCapacity());
    }
    
    @Test
    public void findAllRoom(){
        Room room1 = newRoom(1, 4, 3, "rozbitá vana");
        Room room2 = newRoom(2, 4, 3, "postýlka pro děti");
        Room room3 = newRoom(3, 3, 5, "ok");
        Room room4 = newRoom(1, 2, 4, "ok");
        manager.createRoom(room1);
        manager.createRoom(room2);
        manager.createRoom(room3);
        manager.createRoom(room4);
        List<Room> list = new ArrayList<>();
        list.add(room1);
        list.add(room2);
        list.add(room3);
        list.add(room4);
        assertEquals(list.size(), manager.findAllRoom().size());
    }
    
    private static Room newRoom(long id, int floor, int capacity, String note){
        Room room = new Room();
        room.setId(id);
        room.setFloor(floor);
        room.setCapacity(capacity);
        room.setNote(note);
        return room;
    }
    
    
    
    
}
