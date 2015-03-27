/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.sql.DataSource;
import org.junit.Before;    
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;

/**
 *
 * @author marek
 */
public class RoomManagerImplTest {
    
    private RoomManagerImpl manager;
    private DataSource dataSource;
    
    @Before
    public void setUp() throws SQLException{
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:derby:memory:RoomManagerTest;create=true");
        this.dataSource = bds;
        
        try (Connection conn = bds.getConnection()) {
            conn.prepareStatement("CREATE TABLE ROOM ("
                    + "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "floor INT,"
                    + "capacity INT NOT NULL,"
                    + "note VARCHAR(255))").executeUpdate();
        }
        manager = new RoomManagerImpl(bds);
    }
    
   @After
    public void tearDown() throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            con.prepareStatement("DROP TABLE ROOM").executeUpdate();
        }
    }
    
    @Test
    public void createRoom(){
        Room room = newRoom(4, 3, "Pokoj s bezbarierovým přístupem");
        manager.createRoom(room);
        
        Long roomID = room.getId();
        assertNotNull(roomID);
    }
    
    @Test
    public void createRoomWithWrongArgument(){
        try{
           manager.createRoom(null);
           fail();
        } catch(IllegalArgumentException ex){
            //ok
        }   
        Room room = newRoom(5, 4, "se záporným id");
        try{
           manager.createRoom(null);
           fail();
        } catch(IllegalArgumentException ex){
            //ok
        }
        
        Room room1 = newRoom(-5, 4, "sklep");
        try{
            manager.createRoom(room1);
            fail();
        }catch(IllegalArgumentException ex){
            //ok
        }
        
        Room room2 = newRoom(5, -4, "se záporným počtem postelí");
        try{    
            manager.createRoom(room2);
            fail();
        }catch(IllegalArgumentException ex){
            //ok
        }    
    }
    
    @Test
    public void deleteRoom(){
        Long id = new Long(2);
        Room room1 = newRoom(4, 3, "rozbitá vana");
        Room room2 = newRoom(4, 3, "postýlka pro děti");
        Room room3 = newRoom(3, 5, "ok");
        manager.createRoom(room1);
        manager.createRoom(room2);
        manager.createRoom(room3);
        manager.deleteRoom(id);
        Collection<Room> result = manager.findAllRoom();
        
        Collection<Room> list = new ArrayList<>();
        list.add(room1);
        list.add(room3);
        
        assertEquals(list.size(), result.size());
    }
    
    @Test
    public void updateRoom() {
        Room room = newRoom(3, 6,"nic");
        Room room2 = newRoom(2, 6,"neco");
        manager.createRoom(room);
        manager.createRoom(room2);
        Long roomId = room.getId();

        room = manager.getRoomById(roomId);
        room.setFloor(2);
        manager.updateRoom(room);
        assertEquals(2, room.getFloor());
        assertEquals(6, room.getCapacity());
        assertEquals("nic", room.getNote());

        room = manager.getRoomById(roomId);
        room.setNote("neco");
        manager.updateRoom(room);
        assertEquals(2, room.getFloor());
        assertEquals(6, room.getCapacity());
        assertEquals("neco", room.getNote());

        assertDeepEquals(room2, manager.getRoomById(room2.getId()));
    }

    @Test
    public void updateRoomWithWrongAttributes() {

        Room room = newRoom(2, 4,"záchod");
        manager.createRoom(room);
        Long roomId = room.getId();

        try {
            manager.updateRoom(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoomById(roomId);
            room.setId(null);
            manager.updateRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoomById(roomId);
            room.setId(roomId - 1);
            manager.updateRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoomById(roomId);
            room.setFloor(-1);
            manager.updateRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            room = manager.getRoomById(roomId);
            room.setCapacity(0);
            manager.updateRoom(room);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }
    
    @Test
    public void getRoomById(){
        Long id = new Long(1);
        Room room = newRoom(2, 4, "ok");
        manager.createRoom(room);
        List<Room> list = new ArrayList<>();
        list.add(room);
        Room result = list.get(0);
        Room roomById = manager.getRoomById(id);
        assertEquals(roomById.getId(), result.getId());
        assertEquals(roomById.getFloor(), result.getFloor());
    }
    
    /*@Test
    public void getRoombyNumber(){
        Room room = newRoom(1, 2, 4, "ok");
        manager.createRoom(room);
        List<Room> list = new ArrayList<>();
        list.add(room);
        Room result = list.get(0);

        assertEquals(manager.getRoomByNumber(201).getId(), result.getId());
        assertEquals(manager.getRoomByNumber(201).getCapacity(), result.getCapacity());
    }*/
    
    @Test
    public void findAllRoom(){
        Room room1 = newRoom(4, 3, "rozbitá vana");
        Room room2 = newRoom(4, 3, "postýlka pro děti");
        Room room3 = newRoom(3, 5, "ok");
        Room room4 = newRoom(2, 4, "ok");
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
        assertDeepEquals(list, manager.findAllRoom());
    }
    
    private static Room newRoom(int floor, int capacity, String note){
        Room room = new Room();
        room.setFloor(floor);
        room.setCapacity(capacity);
        room.setNote(note);
        return room;
    }
    
    private void assertDeepEquals(List<Room> expectedList, List<Room> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Room expected = expectedList.get(i);
            Room actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private void assertDeepEquals(Room expected, Room actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFloor(), actual.getFloor());
        assertEquals(expected.getCapacity(), actual.getCapacity());
        assertEquals(expected.getNote(), actual.getNote());
    }
}
