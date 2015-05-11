/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import project.RoomManagerImpl;
import project.StayManager;
import project.StayManagerImpl;
import project.Guest;
import project.Stay;
import project.Room;
import project.GuestManagerImpl;
import common.DBUtils;
import java.math.BigDecimal;
import java.sql.Connection;
//import java.sql.Date;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        
        manager = new StayManagerImpl();
        guestManager = new GuestManagerImpl();
        roomManager = new RoomManagerImpl();
        manager.setDataSource(dataSource);
        guestManager.setDataSource(dataSource);
        roomManager.setDataSource(dataSource);
    }
    
   @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(dataSource, StayManager.class.getResource("dropTables.sql"));
    }
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Date date(String date) {
        try {
            return DATE_FORMAT.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    @Test
    public void createRoom(){
        BigDecimal price = new BigDecimal(2000);
        //Date start = new Date(2015, 3, 20);
        //Date end = new Date(2015, 3, 26);
        Room room = newRoom(401, 4, 3, "Pokoj s bezbarierovým přístupem",price);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        guestManager.createGuest(guest);
        roomManager.createRoom(room);
        Stay stay = newStay(guest, room, date("2014-08-11"), date("2014-08-11"));
        
        manager.createStay(stay);
        
        Long stayID = stay.getId();
        assertNotNull(stayID);
    }
    
    @Test
    public void deleteRoom(){
        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        //Date start = new Date(2015, 3, 20);
        //Date start2 = new Date(2015, 3, 24);
        //Date end = new Date(2015, 3, 26);
        //Date end2 = new Date(2015, 3, 27);
        Room room = newRoom(401, 4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(301, 3, 2, "bez oken",price2);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","456987159");
        roomManager.createRoom(room);
        roomManager.createRoom(room2);
        guestManager.createGuest(guest);
        guestManager.createGuest(guest2);
        Stay stay = newStay(guest, room, date("2014-08-12"), date("2015-08-12"));
        Stay stay2 = newStay(guest2, room2, date("2014-08-12"), date("2014-09-12"));
        manager.createStay(stay);
        manager.createStay(stay2);
        manager.deleteStay(Long.valueOf(2));
        
        List<Stay> result = manager.findAllStay();
        List<Stay> list = new ArrayList<>();
        list.add(stay);
       
        
        assertEquals(list.size(), result.size());
    }
    
    @Test
    public void updateStay(){
        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        //Date start = new Date(2015, 3, 20);
        //Date start2 = new Date(2015, 3, 24);
        //Date end = new Date(2015, 3, 26);
        //Date end2 = new Date(2015, 3, 27);
        Room room = newRoom(401, 4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(301, 3, 2, "bez oken",price2);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","456987159");
        roomManager.createRoom(room);
        roomManager.createRoom(room2);
        guestManager.createGuest(guest);
        guestManager.createGuest(guest2);
        Stay stay = newStay(guest, room, date("2015-02-21"), date("2015-03-24"));
        manager.createStay(stay);
        
        stay = manager.getStayByID(Long.valueOf(1));
        stay.setPrice(price2);
        manager.updateStay(stay);
        assertEquals(BigDecimal.valueOf(1500), stay.getPrice());
        assertEquals(date("2015-02-21"), stay.getStartOfStay());
        
        stay = manager.getStayByID(Long.valueOf(1));
        stay.setStartOfStay(date("2015-01-24"));
        manager.updateStay(stay);
        assertEquals(BigDecimal.valueOf(1500), stay.getPrice());
        assertEquals(date("2015-01-24"), stay.getStartOfStay());
        
        stay = manager.getStayByID(Long.valueOf(1));
        stay.setGuest(guest2);
        manager.updateStay(stay);
        assertEquals(BigDecimal.valueOf(1500), stay.getPrice());
        assertEquals(date("2015-01-24"), stay.getStartOfStay());
        assertEquals(guest2.getName(), stay.getGuest().getName());
    }
    
    @Test
    public void getStayById(){
        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        //Date start = new Date(2015, 3, 20);
        //Date start2 = new Date(2015, 3, 24);
        //Date end = new Date(2015, 3, 26);
        //Date end2 = new Date(2015, 3, 27);
        Room room = newRoom(401, 4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(301, 3, 2, "bez oken",price2);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","456987159");
        roomManager.createRoom(room);
        roomManager.createRoom(room2);
        guestManager.createGuest(guest);
        guestManager.createGuest(guest2);
        Stay stay = newStay(guest, room, date("2015-01-17"), date("2015-01-25"));
        Stay stay2 = newStay(guest2, room2, date("2015-01-14"), date("2015-01-26"));
        manager.createStay(stay);
        manager.createStay(stay2);
        
        Stay result = manager.getStayByID(Long.valueOf(2));
        assertEquals(stay2.getId(), result.getId());
        assertEquals(stay2.getEndOfStay(), result.getEndOfStay());
    }
    
    @Test 
    public void findAllStay(){
        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        //Date start = new Date(2015, 3, 20);
        //Date start2 = new Date(2015, 3, 24);
        //Date end = new Date(2015, 3, 26);
        //Date end2 = new Date(2015, 3, 27);
        Room room = newRoom(401, 4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(301, 3, 2, "bez oken",price2);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","456987159");
        roomManager.createRoom(room);
        roomManager.createRoom(room2);
        guestManager.createGuest(guest);
        guestManager.createGuest(guest2);
        Stay stay = newStay(guest, room, date("2015-01-17"), date("2015-01-25"));
        Stay stay2 = newStay(guest2, room2, date("2015-01-14"), date("2015-01-26"));
        manager.createStay(stay);
        manager.createStay(stay2);
        List<Stay> result = manager.findAllStay();
        assertEquals(2, result.size());
    }
    
    private static Stay newStay(Guest guest, Room room, Date startOfStay, Date endOfStay){
        Stay stay = new Stay();
        stay.setGuest(guest);
        stay.setRoom(room);
        stay.setStartOfStay(startOfStay);
        stay.setEndOfStay(endOfStay);
        //stay.setPrice(price);
        return stay;
    }
    
    private static Room newRoom(int number, int floor, int capacity, String note, BigDecimal price){
        Room room = new Room();
       // room.setId(id);
        room.setNumber(number);
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
