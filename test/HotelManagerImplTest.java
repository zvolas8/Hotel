/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import project.RoomManagerImpl;
import project.StayManagerImpl;
import project.Guest;
import project.Stay;
import project.HotelManager;
import project.Room;
import project.HotelManagerImpl;
import project.GuestManagerImpl;
import common.DBUtils;
import java.math.BigDecimal;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class HotelManagerImplTest {
    private HotelManagerImpl manager;
    private StayManagerImpl stayManager;
    private RoomManagerImpl roomManager;
    private GuestManagerImpl guestManager;
    private DataSource dataSource;
    
    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:derby:memory:hotelManagerTest;create=true");
        return dataSource;
    }
    
    @Before
    public void setUp() throws SQLException{
        dataSource = prepareDataSource();        
        DBUtils.executeSqlScript(dataSource,HotelManager.class.getResource("createTables.sql"));
        
        manager = new HotelManagerImpl();
        stayManager = new StayManagerImpl();
        guestManager = new GuestManagerImpl();
        roomManager = new RoomManagerImpl();
        manager.setDataSource(dataSource);
        stayManager.setDataSource(dataSource);
        guestManager.setDataSource(dataSource);
        roomManager.setDataSource(dataSource);

    }
    
   @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(dataSource, HotelManager.class.getResource("dropTables.sql"));
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
    public void findCurrentRoomWithGuest(){
        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        Room room = newRoom(4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(3, 2, "bez oken",price2);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","456987159");
        roomManager.createRoom(room);
        roomManager.createRoom(room2);
        guestManager.createGuest(guest);
        guestManager.createGuest(guest2);
        Stay stay = newStay(guest, room, date("2015-03-11"), date("2015-08-05"), price);
        Stay stay2 = newStay(guest, room2, date("2014-09-13"), date("2014-10-08"), price2);
        Stay stay3 = newStay(guest, room2, date("2014-08-11"), date("2014-08-16"), price2);
        stayManager.createStay(stay);
        stayManager.createStay(stay2);
        stayManager.createStay(stay3);
        
        Room result = manager.findCurrentRoomWithGuest(guest);
        assertEquals(room.getId(), result.getId());
        assertEquals(room.getNote(), result.getNote());
        assertEquals(room.getFloor(), result.getFloor());
    }
    
    @Test
    public void findCurrentGuestWithRoom(){
        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        Room room = newRoom(4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(3, 2, "bez oken",price2);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","456987159");
        roomManager.createRoom(room);
        roomManager.createRoom(room2);
        guestManager.createGuest(guest);
        guestManager.createGuest(guest2);
        Stay stay = newStay(guest, room, date("2015-03-11"), date("2015-04-05"), price);
        Stay stay2 = newStay(guest2, room2, date("2014-09-13"), date("2015-10-08"), price2);
        Stay stay3 = newStay(guest, room2, date("2014-08-11"), date("2014-08-16"), price2);
        stayManager.createStay(stay);
        stayManager.createStay(stay2);
        stayManager.createStay(stay3);
        
        Guest result = manager.findCurrentGuestWithRoom(room2);
        assertEquals(guest2.getId(), result.getId());
        assertEquals(guest2.getName(), result.getName());
        assertEquals(guest2.getSurname(), result.getSurname());
    }
    
    @Test
    public void findAllEmptyRooms(){
        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        Room room = newRoom(4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(3, 2, "bez oken",price2);
        Room room3 = newRoom(4, 2, "bez oken",price);
        Room room4 = newRoom(7, 3, "bez oken",price2);
        Room room5 = newRoom(6, 3, "bez oken",price);
        Room room6 = newRoom(2, 4, "bez oken",price);
        Room room7 = newRoom(1, 1, "bez oken",price2);
        Room room8 = newRoom(9, 4, "bez oken",price);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","456987159");
        roomManager.createRoom(room);
        roomManager.createRoom(room2);
        roomManager.createRoom(room3);
        roomManager.createRoom(room4);
        roomManager.createRoom(room5);
        roomManager.createRoom(room6);
        roomManager.createRoom(room7);
        roomManager.createRoom(room8);
        guestManager.createGuest(guest);
        guestManager.createGuest(guest2);
        Stay stay = newStay(guest, room, date("2015-03-11"), date("2015-04-05"), price);
        Stay stay2 = newStay(guest2, room2, date("2014-09-13"), date("2014-10-08"), price2);
        Stay stay3 = newStay(guest, room2, date("2015-01-11"), date("2014-01-16"), price2);
        Stay stay4= newStay(guest, room2, date("2015-02-11"), date("2014-02-16"), price2);
        Stay stay5 = newStay(guest, room3, date("2014-08-11"), date("2014-08-16"), price2);
        Stay stay6 = newStay(guest, room5, date("2015-03-20"), date("2015-03-30"), price2);
        Stay stay7 = newStay(guest, room6, date("2015-03-28"), date("2015-08-05"), price2);
        Stay stay8 = newStay(guest, room8, date("2015-04-02"), date("2015-08-16"), price2);
        stayManager.createStay(stay);
        stayManager.createStay(stay2);
        stayManager.createStay(stay3);
        stayManager.createStay(stay4);
        stayManager.createStay(stay5);
        stayManager.createStay(stay6);
        stayManager.createStay(stay7);
        stayManager.createStay(stay8);
                    
        List<Room> result = manager.findAllEmptyRooms();
        assertEquals(6, result.size());
    }
    
    @Test
    public void findStaysForGuest(){
        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        Room room = newRoom(4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(3, 2, "bez oken",price2);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","456987159");
        roomManager.createRoom(room);
        roomManager.createRoom(room2);
        guestManager.createGuest(guest);
        guestManager.createGuest(guest2);
        Stay stay = newStay(guest, room, date("2014-08-11"), date("2014-08-11"), price);
        Stay stay2 = newStay(guest2, room2, date("2014-09-13"), date("2014-10-08"), price2);
        Stay stay3 = newStay(guest, room2, date("2014-08-11"), date("2014-08-16"), price2);
        stayManager.createStay(stay);
        stayManager.createStay(stay2);
        stayManager.createStay(stay3);
                
        List<Stay> result = manager.findStaysForGuest(guest);
        assertEquals(2, result.size());
    }
    
    @Test
    public void findStaysForRoom(){
        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        Room room = newRoom(4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(3, 2, "bez oken",price2);
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","456987159");
        roomManager.createRoom(room);
        roomManager.createRoom(room2);
        guestManager.createGuest(guest);
        guestManager.createGuest(guest2);
        Stay stay = newStay(guest, room, date("2014-08-11"), date("2014-08-11"), price);
        Stay stay2 = newStay(guest2, room2, date("2014-09-13"), date("2014-10-08"), price2);
        Stay stay3 = newStay(guest, room2, date("2014-08-11"), date("2014-08-16"), price2);
        stayManager.createStay(stay);
        stayManager.createStay(stay2);
        stayManager.createStay(stay3);
        
        List<Stay> result = manager.findStaysForRoom(room);
        List<Stay> result2 = manager.findStaysForRoom(room2);
        assertEquals(1, result.size());
        assertEquals(2, result2.size());
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
