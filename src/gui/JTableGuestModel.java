/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import project.Guest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;
import project.GuestManagerImpl;

/**
 *
 * @author marek
 */
public class JTableGuestModel extends AbstractTableModel{
    private List<Guest> table = new ArrayList<>();
    
    private ResourceBundle localization;    
    
    private static final int COLUMN_COUNT = Guest.class.getDeclaredFields().length - 1; //-1 because of the ID
    private static final int COLUMN_NAME_INDEX = 0;
    private static final int COLUMN_SURNAME_INDEX = 1;
    private static final int COLUMN_ADDRESS_INDEX = 2;
    private static final int COLUMN_PHONENUMBER_INDEX = 3;
    
    public JTableGuestModel(List<Guest> data, ResourceBundle localization){
        table.addAll(data);
        this.localization = localization;
    }

    @Override
    public int getRowCount() {
        return table.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }
    
    @Override
    public String getColumnName(int columnIndex){
        switch(columnIndex){
            case COLUMN_NAME_INDEX: return localization.getString("global.name");
            case COLUMN_SURNAME_INDEX: return localization.getString("global.surname");
            case COLUMN_ADDRESS_INDEX: return localization.getString("global.address");
            case COLUMN_PHONENUMBER_INDEX: return localization.getString("global.phoneNumber");
        }
        
        throw new IllegalArgumentException("Column index out of bounds.");
    }
    
    @Override
    public Class<Guest> getColumnClass(int columnIndex){
        return Guest.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex >= table.size()){
            throw new IllegalArgumentException("Row index out of bounds.");            
        }
        
        Guest guest = table.get(rowIndex);
        
        switch(columnIndex){
            case COLUMN_NAME_INDEX: return guest.getName();
            case COLUMN_SURNAME_INDEX: return guest.getSurname();
            case COLUMN_ADDRESS_INDEX: return guest.getAddress();            
            case COLUMN_PHONENUMBER_INDEX: return guest.getPhoneNumber();            
        }
        
        throw new IllegalArgumentException("column index out of bounds.");
    }
    
    public Guest getRow(int rowIndex){
        return table.get(rowIndex);
    }
    
    public int getRowById(Long id){
        Guest guest = getGuestById(id);
        for(int i = 0; i<table.size();i++){
            
            if(guest.equals(getRow(i))){
                return i;
            }
        }
        return 0;
    }
    
    private Guest getGuestById(Long id){
        for(Guest g : table){
            if(id.equals(g.getId())){
                return g;
            }
        }
        return null;
    }
        
    public void refresh(Collection<Guest> data){
        table.clear();
        table.addAll(data);
        fireTableDataChanged();
    }
}
