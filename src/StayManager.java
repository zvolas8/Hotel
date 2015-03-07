
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
public interface StayManager {
    public void createStay(Stay stay);
    
    public void deleteStay(Stay stay);
    
    public void updateStay(Stay stay);
    
    public Stay getStayByID(long id);
    
    public Collection<Stay> findAllStay();
}
