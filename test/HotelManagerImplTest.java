/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
public class HotelManagerImplTest {
    
    private HotelManagerImpl manager;
    private RoomManagerImpl roomManager;
    private GuestManagerImpl guestManager;
    
    @Before
    public void setUp(){
        manager = new HotelManagerImpl();
       // roomManager = new RoomManagerImpl();
        guestManager = new GuestManagerImpl();
    }
    
  /*  @Test
    public void findAllEmptyRooms(){
        Guest guest = newGuest("John","Smith","123 Fake St.", "020202");
        Room room1 = newRoom(1, 4, 3, "Pokoj s bezbarierovým přístupem");
        Room room2 = newRoom(2, 4, 3, "nic");
        Room room3 = newRoom(3, 2, 4, "chybí vana");
        roomManager.createRoom(room1);
        roomManager.createRoom(room2);
        roomManager.createRoom(room3);
        guestManager.createGuest(guest);
        manager.putGuestInRoom(guest, room1);
        
        Collection<Room> freeRooms = new ArrayList<>();
        freeRooms = manager.findAllEmptyRooms();
        
        assertEquals(2,freeRooms.size());
    }*/
    
    private static Room newRoom(long id, int floor, int capacity, String note){
        Room room = new Room();
        room.setId(id);
        room.setFloor(floor);
        room.setCapacity(capacity);
        room.setNote(note);
        return room;
    }
    
    private static Guest newGuest(String name, String surname, String address, String phoneNumber) {
        Guest guest = new Guest();
        guest.setName(name);
        guest.setSurname(surname);
        guest.setAddress(address);
        guest.setPhoneNumber(phoneNumber);
        return guest;
    }
        
    
}
