/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import common.DBUtils;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
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
public class StayManagerImplTest {
    
    private StayManagerImpl manager;
    private RoomManagerImpl roomManager;
    private GuestManagerImpl guestManager;
    private DataSource dataSource;
    
    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:derby:memory:stayManagerTest;create=true");
        return dataSource;
    }
    
    @Before
    public void setUp() throws SQLException{
        dataSource = prepareDataSource();        
        DBUtils.executeSqlScript(dataSource,StayManager.class.getResource("createTables.sql"));
        
        manager = new StayManagerImpl(dataSource);
        guestManager = new GuestManagerImpl(dataSource);
        roomManager = new RoomManagerImpl(dataSource);
        //estateAgency = new EstateAgencyImpl();
        //estateAgency.setDataSource(dataSource);  
        
        /*BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:derby:memory:StayManagerTest;create=true");
        this.dataSource = bds;
        
        try (Connection conn = bds.getConnection()) {
            conn.prepareStatement("CREATE TABLE STAY ("
                    + "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "guest_id BIGINT,"
                    + "room_id BIGINT,"
                    + "start_of_stay DATE,"
                    + "end_of_stay DATE,"
                    + "total_price INT)").executeUpdate();
        }
        manager = new StayManagerImpl(bds);*/
    }
    
   @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(dataSource, StayManager.class.getResource("dropTables.sql"));
    }
    
    @Test
    public void createRoom(){
        BigDecimal price = new BigDecimal(2000);
        Date start = new Date(2015, 3, 20);
        Date end = new Date(2015, 3, 26);
        Room room = newRoom(4, 3, "Pokoj s bezbarierovým přístupem",price);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        guestManager.createGuest(guest);
        roomManager.createRoom(room);
        Stay stay = newStay(guest, room, start, end, price);

        manager.createStay(stay);
        
        Long stayID = stay.getId();
        assertNotNull(stayID);
    }
    
    @Test
    public void deleteRoom(){
        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        Date start = new Date(2015, 3, 20);
        Date start2 = new Date(2015, 3, 24);
        Date end = new Date(2015, 3, 26);
        Date end2 = new Date(2015, 3, 27);
        Room room = newRoom(4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(3, 2, "bez oken",price2);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","456987159");
        roomManager.createRoom(room);
        roomManager.createRoom(room2);
        guestManager.createGuest(guest);
        guestManager.createGuest(guest2);
        Stay stay = newStay(guest, room, start, end, price);
        Stay stay2 = newStay(guest2, room2, start2, end2, price2);
        manager.createStay(stay);
        manager.createStay(stay2);
        manager.deleteStay(Long.valueOf(2));
        
        List<Stay> result = manager.findAllStay();
        List<Stay> list = new ArrayList<>();
        list.add(stay);
       
        
        assertEquals(list.size(), result.size());
    }
    
    @Test 
    public void findAllStay(){
        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        Date start = new Date(2015, 3, 20);
        Date start2 = new Date(2015, 3, 24);
        Date end = new Date(2015, 3, 26);
        Date end2 = new Date(2015, 3, 27);
        Room room = newRoom(4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(3, 2, "bez oken",price2);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","456987159");
        roomManager.createRoom(room);
        roomManager.createRoom(room2);
        guestManager.createGuest(guest);
        guestManager.createGuest(guest2);
        Stay stay = newStay(guest, room, start, end, price);
        Stay stay2 = newStay(guest2, room2, start2, end2, price2);
        manager.createStay(stay);
        manager.createStay(stay2);
        List<Stay> result = manager.findAllStay();
        assertEquals(2, result.size());
    }
    
    private static Stay newStay(Guest guest, Room room, Date startOfStay, Date endOfStay, BigDecimal price){
        Stay stay = new Stay();
        stay.setGuest(guest);
        stay.setRoom(room);
        stay.setStartOfStay(startOfStay);
        stay.setEndOfStay(endOfStay);
        stay.setPrice(price);
        return stay;
    }
    
    private static Room newRoom(int floor, int capacity, String note, BigDecimal price){
        Room room = new Room();
       // room.setId(id);
        room.setFloor(floor);
        room.setCapacity(capacity);
        room.setNote(note);
        room.setPricePerNight(price);
        return room;
    }
    
    private static Guest newGuest(String name, String surname, String address, String phoneNumber) {
        Guest guest = new Guest();
        //guest.setId(id);
        guest.setName(name);
        guest.setSurname(surname);
        guest.setAddress(address);
        guest.setPhoneNumber(phoneNumber);
        return guest;
    }
}
