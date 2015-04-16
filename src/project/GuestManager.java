package project;


import java.util.Collection;
import java.util.List;

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
    
    public void deleteGuest(Long id);
    
    public void updateGuest(Guest guest);
    
    public Guest getGuestById(Long id);
    
    public List<Guest> getGuestsBySurname(String surname);
    
    public List<Guest> findAllGuest();
}
