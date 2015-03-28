
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
public interface StayManager {
    public void createStay(Stay stay);
    
    public void deleteStay(Long id);
    
    public void updateStay(Stay stay);
    
    public Stay getStayByID(Long id);
    
    public List<Stay> findAllStay();
}
