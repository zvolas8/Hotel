
import java.util.Collection;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marek
 */
public interface GuestManager {
    public void createGuest(Guest guest);
    
    public void deleteGuest(long id);
    
    public void updateGuest(Guest guest);
    
    public Guest getGuestById(long id);
    
    public Collection<Guest> getGuestsBySurname(String Surname);
    
    public Collection<Guest> findAllGuest();
}
