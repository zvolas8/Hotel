
import common.DBUtils;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
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

    
    public static void main(String args[]) throws SQLException{
    
    
        
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
        Date start = new Date(2015, 3, 20);
        Date start2 = new Date(2015, 3, 24);
        Date start3 = new Date(2014, 1, 14);
        Date end = new Date(2015, 3, 26);
        Date end2 = new Date(2015, 3, 27);
        Date end3 = new Date(2014, 7, 27);
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
        Stay stay3 = newStay(guest, room2, start3, end3, price2);
        manager.createStay(stay);
        manager.createStay(stay2);
        manager.createStay(stay3);
    
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
