
import common.DBUtils;
import java.math.BigDecimal;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marek
 */
public class main {
    StayManagerImpl manager;
    RoomManagerImpl roomManager;
    GuestManagerImpl guestManager;  
    DataSource dataSource;
    
    private static DataSource prepareDataSource() throws SQLException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:derby://localhost:1527/hotelDB;create=true");
        return dataSource;
    }

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd");
    private Date date(String date) {
        try {
            return DATE_FORMAT.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public static void main(String args[]) throws SQLException{
        
      
        //System.out.println(LocalDateTime.now());
        
        StayManagerImpl manager;
        RoomManagerImpl roomManager;
        GuestManagerImpl guestManager;  
        DataSource dataSource;
        dataSource = prepareDataSource();   

        DBUtils.executeSqlScript(dataSource,StayManager.class.getResource("dropTables.sql"));
        DBUtils.executeSqlScript(dataSource,StayManager.class.getResource("createTables.sql"));

        manager = new StayManagerImpl(dataSource);
        guestManager = new GuestManagerImpl(dataSource);
        roomManager = new RoomManagerImpl(dataSource);    

        BigDecimal price = new BigDecimal(2000);
        BigDecimal price2 = new BigDecimal(1500);
        Date start = new Date(115, 2, 20);
        Date start2 = new Date(115, 2, 24);
        Date start3 = new Date(114, 0, 14);
        Date start4 = new Date(115, 2, 3);
        Date start5 = new Date(114, 1, 15);
        Date end = new Date(115, 3, 26);
        Date end2 = new Date(115, 3, 27);
        Date end3 = new Date(115, 1, 27);
        Date end4 = new Date(115, 7, 27);
        Date end5 = new Date(114, 4, 4);
        Room room = newRoom(4, 3, "Pokoj s bezbarierovým přístupem",price);
        Room room2 = newRoom(3, 2, "bez oken",price2);
        Room room3 = newRoom(3, 2, "bez oken",price2);
        Room room4 = newRoom(3, 2, "bez oken",price2);
        Room room5 = newRoom(3, 2, "bez oken",price2);
        Room room6 = newRoom(3, 2, "bez oken",price2);
        Room room7 = newRoom(3, 2, "bez oken",price2);
        Room room8 = newRoom(3, 2, "bez oken",price2);
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
        Stay stay = newStay(guest, room, start, end, price);
        Stay stay2 = newStay(guest2, room2, start2, end2, price2);
        Stay stay3 = newStay(guest, room3, start3, end3, price2);
        Stay stay4 = newStay(guest, room4, start3, end3, price2);
        Stay stay5 = newStay(guest, room5, start4, end4, price2);
        Stay stay6 = newStay(guest, room7, start2, end2, price2);
        Stay stay7 = newStay(guest, room8, start5, end5, price2);
        manager.createStay(stay);
        manager.createStay(stay2);
        manager.createStay(stay3);
        manager.createStay(stay4);
        manager.createStay(stay5);
        manager.createStay(stay6);
        manager.createStay(stay7);
    
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
