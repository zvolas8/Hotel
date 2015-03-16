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
 */
public class GuestManagerImplTest {
    
    private GuestManagerImpl manager;   
        
    @Before
    public void setUp() {
        manager = new GuestManagerImpl();
    }  
    
    @Test
    public void createGuest() {
        Guest guest = newGuest("John","Smith","123 Fake St.", "020202");
        manager.createGuest(guest);

        Long guestId = guest.getId();
        assertNotNull(guestId);
        Guest result = manager.getGuestById(guestId);
        assertEquals(guest, result);
        assertNotSame(guest, result);        
    }
    
    @Test
    public void getGuestBySurname() {        
        String Surname = "";        
        Guest expResult = null;
        Guest result = manager.getGuestBySurname(Surname);
        assertEquals(expResult, result);        
    }
    
    @Test
    public void deleteGuest() {        
        Guest guest = null;        
        //manager.deleteGuest(guest);        
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
