/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;
import project.Room;
import project.Stay;

/**
 *
 * @author marek
 */
public class JTableStayModel extends AbstractTableModel{
    
    private List<Stay> table = new ArrayList<>();
    
    private ResourceBundle localization; 
    
    public JTableStayModel(List<Stay> data, ResourceBundle localization){
        table.addAll(data);
        this.localization = localization;
    }
    
    @Override
    public int getRowCount() {
        return table.size();
    }
    
    private static final int COLUMN_COUNT = Stay.class.getDeclaredFields().length - 1; //-1 because of the ID
    private static final int COLUMN_GUEST_INDEX = 0;
    private static final int COLUMN_ROOM_INDEX = 1;
    private static final int COLUMN_STARTOFSTAY_INDEX = 2;
    private static final int COLUMN_ENDOFSTAY = 3;
    private static final int COLUMN_PRICE_INDEX = 4;

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
    public String getColumnName(int columnIndex){
        switch(columnIndex){
            case COLUMN_GUEST_INDEX: return localization.getString("global.guest");
            case COLUMN_ROOM_INDEX: return localization.getString("global.room");
            case COLUMN_STARTOFSTAY_INDEX: return localization.getString("global.startOfStay");
            case COLUMN_ENDOFSTAY: return localization.getString("global.endOfStay");
            case COLUMN_PRICE_INDEX: return localization.getString("global.price");
        }
        
        throw new IllegalArgumentException("Column index out of bounds.");
    }
    
    @Override
    public Class<Stay> getColumnClass(int columnIndex){
        return Stay.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex >= table.size()){
            throw new IllegalArgumentException("Row index out of bounds.");            
        }
        
        Stay stay = table.get(rowIndex);
        
        switch(columnIndex){
            case COLUMN_GUEST_INDEX: return stay.getGuest();
            case COLUMN_ROOM_INDEX: return stay.getRoom();  
            case COLUMN_STARTOFSTAY_INDEX: return stay.getStartOfStay().toString().substring(0, 10);
            case COLUMN_ENDOFSTAY: return stay.getEndOfStay().toString().substring(0, 10);            
            case COLUMN_PRICE_INDEX: return stay.getPrice();            
        }
        
        throw new IllegalArgumentException("column index out of bounds.");
    }
    
    public Stay getRow(int rowIndex){
        return table.get(rowIndex);
    }
    
    public void refresh(Collection<Stay> data){
        table.clear();
        table.addAll(data);
        fireTableDataChanged();
    }
    
}
