/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

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
    private DataSource dataSource;
    
    @Before
    public void setUp() throws SQLException{
        BasicDataSource bds = new BasicDataSource();
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
        manager = new StayManagerImpl(bds);
    }
    
   @After
    public void tearDown() throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            con.prepareStatement("DROP TABLE STAY").executeUpdate();
        }
    }
    
    @Test
    public void createRoom(){
        BigDecimal price = new BigDecimal(2000);
        Date start = new Date(2015, 3, 20);
        Date end = new Date(2015, 3, 26);
        Room room = newRoom(Long.valueOf(1),4, 3, "Pokoj s bezbarierovým přístupem",price);
        Guest guest = newGuest(Long.valueOf(2),"Karim","Benzema","Santiago Bernabeu","111222333");
        Stay stay = newStay(guest, room, start, end, price);
        
        Long roomID = room.getId();
        assertNotNull(roomID);
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
    
    private static Room newRoom(Long id,int floor, int capacity, String note, BigDecimal price){
        Room room = new Room();
        room.setId(id);
        room.setFloor(floor);
        room.setCapacity(capacity);
        room.setNote(note);
        room.setPricePerNight(price);
        return room;
    }
    
    private static Guest newGuest(Long id,String name, String surname, String address, String phoneNumber) {
        Guest guest = new Guest();
        guest.setId(id);
        guest.setName(name);
        guest.setSurname(surname);
        guest.setAddress(address);
        guest.setPhoneNumber(phoneNumber);
        return guest;
    }
}
