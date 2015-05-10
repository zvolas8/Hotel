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

/**
 *
 * @author marek
 */
public class JTableChooseRoomModel extends AbstractTableModel {
    private List<Room> table = new ArrayList<>();
    
    private ResourceBundle localization;    
    
    private static final int COLUMN_COUNT = Room.class.getDeclaredFields().length - 1; //-1 because of the ID
    private static final int COLUMN_NUMBER_INDEX = 0;
    private static final int COLUMN_FLOOR_INDEX = 1;
    private static final int COLUMN_CAPACITY_INDEX = 2;
    private static final int COLUMN_NOTE_INDEX = 3;
    private static final int COLUMN_PRICEPERNIGHT_INDEX = 4;
    
    public JTableChooseRoomModel(List<Room> data, ResourceBundle localization){
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
            case COLUMN_NUMBER_INDEX: return localization.getString("global.number");
            case COLUMN_FLOOR_INDEX: return localization.getString("global.floor");
            case COLUMN_CAPACITY_INDEX: return localization.getString("global.capacity");
            case COLUMN_NOTE_INDEX: return localization.getString("global.note");
            case COLUMN_PRICEPERNIGHT_INDEX: return localization.getString("global.pricePerNight");
        }
        
        throw new IllegalArgumentException("Column index out of bounds.");
    }
    
    @Override
    public Class<Room> getColumnClass(int columnIndex){
        return Room.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(rowIndex >= table.size()){
            throw new IllegalArgumentException("Row index out of bounds.");            
        }
        
        Room room = table.get(rowIndex);
        
        switch(columnIndex){
            case COLUMN_NUMBER_INDEX: return room.getNumber();
            case COLUMN_FLOOR_INDEX: return room.getFloor();
            case COLUMN_CAPACITY_INDEX: return room.getCapacity();
            case COLUMN_NOTE_INDEX: return room.getNote();            
            case COLUMN_PRICEPERNIGHT_INDEX: return room.getPricePerNight();            
        }
        
        throw new IllegalArgumentException("column index out of bounds.");
    }
    
    public Room getRow(int rowIndex){
        return table.get(rowIndex);
    }
    
    public void refresh(Collection<Room> data){
        table.clear();
        table.addAll(data);
        fireTableDataChanged();
    }
}
