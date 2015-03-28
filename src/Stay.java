
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marek
 */
public class Stay {
    private Long id;
    private Guest guest;
    private Room room;
    private Date startOfStay;
    private Date endOfStay;
    private BigDecimal price;
    
    public Stay(){}

     /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return the guest
     */
    public Guest getGuest() {
        return guest;
    }

    /**
     * @param guest the guest to set
     */
    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }
    
    /**
     * @return the startOfStay
     */
    public Date getStartOfStay() {
        return startOfStay;
    }

    /**
     * @param startOfStay the startOfStay to set
     */
    public void setStartOfStay(Date startOfStay) {
        this.startOfStay = startOfStay;
    }

    /**
     * @return the endOfStay
     */
    public Date getEndOfStay() {
        return endOfStay;
    }

    /**
     * @param endOfStay the endOfStay to set
     */
    public void setEndOfStay(Date endOfStay) {
        this.endOfStay = endOfStay;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString(){
        return "objednávka číslo: " + getId() + "pro hosta: " + guest + " a pokoj: " + room;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj){
        if(obj == null || !(obj instanceof Guest)){
            return false;
        }
        Stay s =(Stay)obj;
        return (s.getId()==id && s.getGuest().equals(guest) && s.getRoom().equals(room));
    }
    
    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 97 * hash + Objects.hashCode(this.guest);
        hash = 97 * hash + Objects.hashCode(this.room);
        return hash;
    }
}
