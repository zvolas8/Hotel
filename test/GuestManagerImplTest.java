import java.util.ArrayList;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ondrej Gruza
 * 
 */
public class GuestManagerImplTest {
    
    private GuestManagerImpl manager;   
        
    @Before
    public void setUp() {
        manager = new GuestManagerImpl();
    }  
    
    @Test
    public void createGuest() {
        Guest guest = newGuest(7, "John","Smith","123 Fake St.", "020202");
        manager.createGuest(guest);

        long guestId = guest.getId();
        assertNotNull(guestId);
        Guest result = manager.getGuestById(guestId);
        assertEquals(guest, result);               
    }
    
    @Test
    public void getGuestBySurname() {    
        String Surname = "";        
        Collection<Guest> expResult = null;
        Collection<Guest> result = manager.getGuestsBySurname(Surname);
        assertEquals(expResult, result);        
    }
    
    @Test
    public void getGuestBySurnameII() {        
        String Surname = "Smith"; 
        Guest guest1 = newGuest(7, "John","Smith","123 Fake St.", "020202");
        Guest guest2 = newGuest(8, "Jane","Smith","567 5th Avenue", "158");
        Guest guest3 = newGuest(1, "Bill","Gates","Redmond", "2000");
        manager.createGuest(guest1);
        manager.createGuest(guest2);
        manager.createGuest(guest3);
        
        Collection<Guest> expResult = new ArrayList<>();
        expResult.add(guest1);
        expResult.add(guest2);
        
        Collection<Guest> result = manager.getGuestsBySurname(Surname);        
               
        assertEquals(expResult, result);        
    }
    
    @Test
    public void deleteGuest() {        
        Guest guest1 = newGuest(7, "John","Smith","123 Fake St.", "020202");
        Guest guest2 = newGuest(8, "Jane","Smith","567 5th Avenue", "158");
        Guest guest3 = newGuest(1, "Bill","Gates","Redmond", "2000");
        manager.createGuest(guest1);
        manager.createGuest(guest2);
        manager.createGuest(guest3);
        manager.deleteGuest(7);
        
        Collection<Guest> result = manager.findAllGuest();
        
        Collection<Guest> expResult = new ArrayList<>();        
        expResult.add(guest2);
        expResult.add(guest3);        
        
        assertEquals(result, expResult);       
        
        
               
    }         
   
    private static Guest newGuest(long id, String name, String surname, String address, String phoneNumber) {
        Guest guest = new Guest();
        guest.setId(id);
        guest.setName(name);
        guest.setSurname(surname);
        guest.setAddress(address);
        guest.setPhoneNumber(phoneNumber);
        return guest;
    }
}
