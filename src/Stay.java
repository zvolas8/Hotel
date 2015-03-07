
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
    private long id;
    private Guest guest;
    private Room room;
    private Date arrival;
    private Date reservedUntil;
    private Date realDepart;
    private BigDecimal price;
    
    public Stay(){}

     /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
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
     * @return the arrival
     */
    public Date getArrival() {
        return arrival;
    }

    /**
     * @param arrival the arrival to set
     */
    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    /**
     * @return the reservedUntil
     */
    public Date getReservedUntil() {
        return reservedUntil;
    }

    /**
     * @param reservedUntil the reservedUntil to set
     */
    public void setReservedUntil(Date reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    /**
     * @return the realDepart
     */
    public Date getRealDepart() {
        return realDepart;
    }

    /**
     * @param realDepart the realDepart to set
     */
    public void setRealDepart(Date realDepart) {
        this.realDepart = realDepart;
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
