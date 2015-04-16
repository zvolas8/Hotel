import project.Guest;
import project.GuestManagerImpl;
import java.sql.Connection;
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
 * @author Ondrej Gruza
 */
public class GuestManagerImplTest {
    
    private GuestManagerImpl manager;
    private DataSource dataSource;
    
    @Before
    public void setUp() throws SQLException{
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl("jdbc:derby:memory:GuestManagerTest;create=true");
        this.dataSource = bds;
        
        try (Connection conn = bds.getConnection()) {
            conn.prepareStatement("CREATE TABLE GUEST ("
                    + "id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                    + "name VARCHAR(50),"
                    + "surname VARCHAR(70),"
                    + "address VARCHAR(255),"
                    + "phonenumber VARCHAR(15))").executeUpdate();
        }
        manager = new GuestManagerImpl();
        manager.setDataSource(dataSource);
    }
    
   @After
    public void tearDown() throws SQLException {
        try (Connection con = dataSource.getConnection()) {
            con.prepareStatement("DROP TABLE GUEST").executeUpdate();
        }
    }
    
    @Test
    public void createGuest(){
        Guest guest = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        manager.createGuest(guest);
        
        Long guestID = guest.getId();
        assertNotNull(guestID);
    }
    
    @Test
    public void deleteGuest(){
        Long id = new Long(2);
        Guest guest1 = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","444555666");
        Guest guest3 = newGuest("Petr","Čech","on the bench","999888777");
        manager.createGuest(guest1);
        manager.createGuest(guest2);
        manager.createGuest(guest3);
        manager.deleteGuest(id);
        Collection<Guest> result = manager.findAllGuest();
        
        Collection<Guest> list = new ArrayList<>();
        list.add(guest1);
        list.add(guest3);
        
        assertEquals(list.size(), result.size());
    }
    
    @Test
    public void updateGuest() {
        Guest guest1 = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","SO","444555666");
        manager.createGuest(guest1);
        manager.createGuest(guest2);
        Long guestId = guest1.getId();

        guest1 = manager.getGuestById(guestId);
        guest1.setAddress("Camp Nou");
        manager.updateGuest(guest1);
        assertEquals("Camp Nou", guest1.getAddress());
        assertEquals("Karim", guest1.getName());
        assertEquals("Benzema", guest1.getSurname());

        guest1 = manager.getGuestById(guestId);
        guest1.setPhoneNumber("111111111");
        manager.updateGuest(guest1);
        assertEquals("Karim", guest1.getName());
        assertEquals("111111111", guest1.getPhoneNumber());
        assertEquals("Camp Nou", guest1.getAddress());

        assertDeepEquals(guest2, manager.getGuestById(guest2.getId()));
    }

    @Test
    public void updateGuestWithWrongAttributes() {

        Guest guest1 = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        manager.createGuest(guest1);
        Long guestId = guest1.getId();

        try {
            manager.updateGuest(null);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            guest1 = manager.getGuestById(guestId);
            guest1.setId(null);
            manager.updateGuest(guest1);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            guest1 = manager.getGuestById(guestId);
            guest1.setId(guestId - 1);
            manager.updateGuest(guest1);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            guest1 = manager.getGuestById(guestId);
            guest1.setAddress(null);
            manager.updateGuest(guest1);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }

        try {
            guest1 = manager.getGuestById(guestId);
            guest1.setName(null);
            manager.updateGuest(guest1);
            fail();
        } catch (IllegalArgumentException ex) {
            //OK
        }
    }
    
    @Test
    public void getGuestById(){
        Long id = new Long(1);
        Guest guest1 = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        manager.createGuest(guest1);
        List<Guest> list = new ArrayList<>();
        list.add(guest1);
        Guest result = list.get(0);
        Guest guestById = manager.getGuestById(id);
        assertEquals(guestById.getId(), result.getId());
        assertEquals(guestById.getName(), result.getName());
    }
    
    @Test
    public void getGuestBySurname(){
        String name1 = "Ronaldo";
        String name2 = "Pique";
        String name3 = "Ramos";
        Guest guest1 = newGuest("proste","Ronaldo","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","444555666");
        Guest guest3 = newGuest("Petr","Čech","on the bench","999888777");
        Guest guest4 = newGuest("Cristiano","Ronaldo","Santiago Bernabeu","999888777");
        Guest guest5 = newGuest("Nevim","Pique","Camp Nou","123456789");
        Guest guest6 = newGuest("Milan","Pique","Camp Nou","123456789");
        Guest guest7 = newGuest("Shakira","Pique","Barcelona","1111111111");
        manager.createGuest(guest1);
        manager.createGuest(guest2);
        manager.createGuest(guest3);
        manager.createGuest(guest4);
        manager.createGuest(guest5);
        manager.createGuest(guest6);
        manager.createGuest(guest7);
        
        List<Guest> resultRonaldo = new ArrayList<>();
        List<Guest> resultPique = new ArrayList<>();
        List<Guest> resultRamos = new ArrayList<>();
        resultRonaldo = manager.getGuestsBySurname(name1);
        resultPique = manager.getGuestsBySurname(name2);
        resultRamos = manager.getGuestsBySurname(name3);        
        assertEquals(2, resultRonaldo.size());
        assertEquals(3, resultPique.size());
        assertEquals(1, resultRamos.size());
    }
    
    @Test
    public void findAllGuest(){
        Guest guest1 = newGuest("Karim","Benzema","Santiago Bernabeu","111222333");
        Guest guest2 = newGuest("Sergio","Ramos","Santiago Bernabeu","444555666");
        Guest guest3 = newGuest("Petr","Čech","on the bench","999888777");
        manager.createGuest(guest1);
        manager.createGuest(guest2);
        manager.createGuest(guest3);
        List<Guest> list = new ArrayList<>();
        list.add(guest1);
        list.add(guest2);
        list.add(guest3);
        assertEquals(list.size(), manager.findAllGuest().size());
        assertDeepEquals(list, manager.findAllGuest());
    }
    
    private static Guest newGuest(String name, String surname, String address, String phoneNumber) {
        Guest guest = new Guest();
        guest.setName(name);
        guest.setSurname(surname);
        guest.setAddress(address);
        guest.setPhoneNumber(phoneNumber);
        return guest;
    }
    
    private void assertDeepEquals(List<Guest> expectedList, List<Guest> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Guest expected = expectedList.get(i);
            Guest actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private void assertDeepEquals(Guest expected, Guest actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
    }
}
