/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import common.DBUtils;
import java.awt.Dimension;
import static java.awt.EventQueue.invokeLater;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;
import project.*;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author marek
 */
public class MainFrame extends javax.swing.JFrame {

    private BasicDataSource dataSource;
    
    private GuestManagerImpl guestManager;
    private RoomManagerImpl roomManager;
    private StayManagerImpl stayManager;
    private HotelManagerImpl hotelManager;
    
    private JTableGuestModel guestsTableModel;
    private JTableRoomModel roomsTableModel;
    private JTableStayModel stayTableModel;
    private JTableChooseGuestModel  chooseGuestTableModel; 
    
    private ResourceBundle localization = ResourceBundle.getBundle("gui.resourcebundles.GUITexts");
    
    private Properties configuration;
    
    private static final Logger LOGGER = Logger.getLogger(MainFrame.class.getCanonicalName());
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        
        
        loadConfiguration();
        configureLogging();   
        
        try {
            prepareDataSource();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to initialize DataSource", ex);
        }
        
        guestManager = new GuestManagerImpl();
        guestManager.setDataSource(dataSource);
        
        roomManager = new RoomManagerImpl();
        roomManager.setDataSource(dataSource);
        
        stayManager = new StayManagerImpl();
        stayManager.setDataSource(dataSource);
        
        hotelManager = new HotelManagerImpl();
        hotelManager.setDataSource(dataSource);
        
        guestsTableModel = new JTableGuestModel(guestManager.findAllGuest(), localization); 
        roomsTableModel = new JTableRoomModel(roomManager.findAllRoom(), localization);
        stayTableModel = new JTableStayModel(stayManager.findAllStay(), localization);
        chooseGuestTableModel = new JTableChooseGuestModel(guestManager.findAllGuest(), localization);
        
        initComponents();        
        
        centerFrame();
    }

    private void loadConfiguration(){
        configuration = new Properties();        
        //TODO check correct load
        try {
            FileInputStream in = new FileInputStream("config.properties");   
            configuration.load(in);            
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to read configuration file.", ex);
        }       
        
    }
    
    private void configureLogging(){
        Handler fileHandler = null;
        try {
            fileHandler = new FileHandler(configuration.getProperty("logFile"));
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to initialize FileHandler", ex);
        } catch (SecurityException ex) {
            LOGGER.log(Level.SEVERE, "Unable to initialize FileHandler.", ex);
        }
        
        Logger.getLogger("").addHandler(fileHandler);       
    }
    
    private void prepareDataSource() throws SQLException {
        dataSource = new BasicDataSource(); 
        dataSource.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
        
        String url = "jdbc:" + configuration.getProperty("dbDriver") + "://" + configuration.getProperty("dbHost") + 
                     ":" + configuration.getProperty("dbPort") + "/" + configuration.getProperty("dbDatabaseName");
        dataSource.setUrl(url);
        
        DBUtils.tryCreateTables(dataSource,GuestManager.class.getResource("createTables.sql"));       
    }

    private void centerFrame() {
        Toolkit toolkit = this.getToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        
        setLocation((screenSize.width / 2) - (this.getWidth() / 2), (screenSize.height / 2) - (this.getHeight() / 2));
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        guestJTabbedPane = new javax.swing.JTabbedPane();
        guetsJPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        guestJTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        createGuestJB = new javax.swing.JButton();
        updateGuetsJB = new javax.swing.JButton();
        deleteGuestJB = new javax.swing.JButton();
        currentRoomForGuestJB = new javax.swing.JButton();
        roomJPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        roomJTable = new javax.swing.JTable();
        createRoomJB = new javax.swing.JButton();
        updateRoomJB = new javax.swing.JButton();
        deleteRoomJB = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        stayJPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        stayJTable = new javax.swing.JTable();
        createStayJB = new javax.swing.JButton();
        updateStayJB = new javax.swing.JButton();
        deleteStayJB = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        guestJTable.setModel(guestsTableModel);
        jScrollPane2.setViewportView(guestJTable);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("gui/resourcebundles/GUITexts"); // NOI18N
        createGuestJB.setText(bundle.getString("mainFrame.guest.create")); // NOI18N
        createGuestJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createGuestJBActionPerformed(evt);
            }
        });

        updateGuetsJB.setText(bundle.getString("mainFrame.guest.update")); // NOI18N
        updateGuetsJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateGuetsJBActionPerformed(evt);
            }
        });

        deleteGuestJB.setText(bundle.getString("mainFrame.guest.delete")); // NOI18N
        deleteGuestJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteGuestJBActionPerformed(evt);
            }
        });

        currentRoomForGuestJB.setText(bundle.getString("mainFrame.guest.currentRoom")); // NOI18N
        currentRoomForGuestJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentRoomForGuestJBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(updateGuetsJB)
                    .addComponent(createGuestJB))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteGuestJB, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(currentRoomForGuestJB, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {createGuestJB, currentRoomForGuestJB, deleteGuestJB, updateGuetsJB});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(createGuestJB)
                .addGap(4, 4, 4)
                .addComponent(updateGuetsJB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteGuestJB)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentRoomForGuestJB)
                .addContainerGap(154, Short.MAX_VALUE))
        );

        updateGuetsJB.getAccessibleContext().setAccessibleName(bundle.getString("mainFrame.guest.update")); // NOI18N
        deleteGuestJB.getAccessibleContext().setAccessibleName(bundle.getString("mainFrame.guest.delete")); // NOI18N

        javax.swing.GroupLayout guetsJPanelLayout = new javax.swing.GroupLayout(guetsJPanel);
        guetsJPanel.setLayout(guetsJPanelLayout);
        guetsJPanelLayout.setHorizontalGroup(
            guetsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(guetsJPanelLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        guetsJPanelLayout.setVerticalGroup(
            guetsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        guestJTabbedPane.addTab(bundle.getString("mainFrame.tabPanel.guest"), guetsJPanel); // NOI18N

        roomJTable.setModel(roomsTableModel);
        jScrollPane3.setViewportView(roomJTable);

        createRoomJB.setText(bundle.getString("mainFrame.room.create")); // NOI18N
        createRoomJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createRoomJBActionPerformed(evt);
            }
        });

        updateRoomJB.setText(bundle.getString("mainFrame.room.update")); // NOI18N
        updateRoomJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateRoomJBActionPerformed(evt);
            }
        });

        deleteRoomJB.setText(bundle.getString("mainFrame.room.delete")); // NOI18N
        deleteRoomJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRoomJBActionPerformed(evt);
            }
        });

        jButton1.setText("Free rooms");

        jButton2.setText("Current guest");

        javax.swing.GroupLayout roomJPanelLayout = new javax.swing.GroupLayout(roomJPanel);
        roomJPanel.setLayout(roomJPanelLayout);
        roomJPanelLayout.setHorizontalGroup(
            roomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roomJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, roomJPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(createRoomJB)
                .addGap(18, 18, 18)
                .addComponent(updateRoomJB)
                .addGap(18, 18, 18)
                .addComponent(deleteRoomJB)
                .addGap(50, 50, 50))
        );
        roomJPanelLayout.setVerticalGroup(
            roomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roomJPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(roomJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createRoomJB)
                    .addComponent(updateRoomJB)
                    .addComponent(deleteRoomJB)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(23, 23, 23))
        );

        createRoomJB.getAccessibleContext().setAccessibleName(bundle.getString("mainFrame.room.create")); // NOI18N
        updateRoomJB.getAccessibleContext().setAccessibleName(bundle.getString("mainFrame.room.update")); // NOI18N
        deleteRoomJB.getAccessibleContext().setAccessibleName(bundle.getString("mainFrame.room.delete")); // NOI18N

        guestJTabbedPane.addTab(bundle.getString("mainFrame.tabPanel.room"), roomJPanel); // NOI18N

        stayJTable.setModel(stayTableModel);
        jScrollPane4.setViewportView(stayJTable);

        createStayJB.setText(bundle.getString("mainFrame.stay.create")); // NOI18N
        createStayJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createStayJBActionPerformed(evt);
            }
        });

        updateStayJB.setText(bundle.getString("mainFrame.stay.update")); // NOI18N
        updateStayJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateStayJBActionPerformed(evt);
            }
        });

        deleteStayJB.setText(bundle.getString("mainFrame.stay.delete")); // NOI18N
        deleteStayJB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteStayJBActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout stayJPanelLayout = new javax.swing.GroupLayout(stayJPanel);
        stayJPanel.setLayout(stayJPanelLayout);
        stayJPanelLayout.setHorizontalGroup(
            stayJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stayJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(stayJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(stayJPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane4)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, stayJPanelLayout.createSequentialGroup()
                        .addGap(0, 294, Short.MAX_VALUE)
                        .addComponent(createStayJB)
                        .addGap(18, 18, 18)
                        .addComponent(updateStayJB)
                        .addGap(18, 18, 18)
                        .addComponent(deleteStayJB)
                        .addGap(62, 62, 62))))
        );
        stayJPanelLayout.setVerticalGroup(
            stayJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stayJPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(stayJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(updateStayJB)
                    .addComponent(createStayJB)
                    .addComponent(deleteStayJB))
                .addGap(0, 17, Short.MAX_VALUE))
        );

        guestJTabbedPane.addTab(bundle.getString("mainFrame.tabPanel.stay"), stayJPanel); // NOI18N

        jMenu1.setText(bundle.getString("mainFrame.help")); // NOI18N
        jMenu1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jMenu1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(guestJTabbedPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(guestJTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void createGuestJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createGuestJBActionPerformed
        invokeLater(new Runnable() {
            @Override
            public void run() {
                new CreateGuestJDialog(guestManager, guestsTableModel).setVisible(true);
            }
        });
    }//GEN-LAST:event_createGuestJBActionPerformed

    private void updateGuetsJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateGuetsJBActionPerformed
        int row = guestJTable.getSelectedRow();
        if(row<0){
            JOptionPane.showMessageDialog(null, localization.getString("warnings.noRowSelected"));
        }else{            
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int row = guestJTable.getSelectedRow();
                    Guest selectedGuest = guestsTableModel.getRow(row);
                    new CreateGuestJDialog(guestManager, guestsTableModel, selectedGuest).setVisible(true);
                }
            });
        }
    }//GEN-LAST:event_updateGuetsJBActionPerformed

    private void deleteGuestJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteGuestJBActionPerformed
        int row = guestJTable.getSelectedRow();
        if(row<0){
            JOptionPane.showMessageDialog(null, localization.getString("warnings.noRowSelected"));
        }else{
            Guest selectedGuest = guestsTableModel.getRow(row);
            guestManager.deleteGuest(selectedGuest.getId());
            guestsTableModel.refresh(guestManager.findAllGuest());
        }
        
    }//GEN-LAST:event_deleteGuestJBActionPerformed

    private void createRoomJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createRoomJBActionPerformed
            invokeLater(new Runnable() {
            @Override
            public void run() {
                new CreateRoomJDialog(roomManager, roomsTableModel).setVisible(true);
            }
        });
    }//GEN-LAST:event_createRoomJBActionPerformed

    private void updateRoomJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateRoomJBActionPerformed
        int row = roomJTable.getSelectedRow();
        if(row<0){
            JOptionPane.showMessageDialog(null, localization.getString("warnings.noRowSelected"));
        }else{            
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int row = roomJTable.getSelectedRow();
                    Room selectedRoom = roomsTableModel.getRow(row);
                    new CreateRoomJDialog(roomManager, roomsTableModel, selectedRoom).setVisible(true);
                }
            });
        }
    }//GEN-LAST:event_updateRoomJBActionPerformed

    private void deleteRoomJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRoomJBActionPerformed
        int row = roomJTable.getSelectedRow();
        if(row<0){
            JOptionPane.showMessageDialog(null, localization.getString("warnings.noRowSelected"));
        }else{
            Room selectedRoom = roomsTableModel.getRow(row);
            roomManager.deleteRoom(selectedRoom.getId());
            roomsTableModel.refresh(roomManager.findAllRoom());
        }
    }//GEN-LAST:event_deleteRoomJBActionPerformed

    private void createStayJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createStayJBActionPerformed
       invokeLater(new Runnable() {
            @Override
            public void run() {
                new CreateStayJDialog(stayManager, guestManager, roomManager, 
                        stayTableModel, roomsTableModel, guestsTableModel,chooseGuestTableModel).setVisible(true);
            }
        });
    }//GEN-LAST:event_createStayJBActionPerformed

    private void updateStayJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateStayJBActionPerformed
        int row =  stayJTable.getSelectedRow();
        if(row<0){
            JOptionPane.showMessageDialog(null, localization.getString("warnings.noRowSelected"));
        }else{            
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int row = stayJTable.getSelectedRow();
                    Stay selectedStay = stayTableModel.getRow(row);
                    new CreateStayJDialog(stayManager, guestManager, roomManager, 
                        stayTableModel, roomsTableModel, guestsTableModel,chooseGuestTableModel,selectedStay).setVisible(true);
                }
            });
        }
    }//GEN-LAST:event_updateStayJBActionPerformed

    private void deleteStayJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteStayJBActionPerformed
        int row = stayJTable.getSelectedRow();
        if(row<0){
            JOptionPane.showMessageDialog(null, localization.getString("warnings.noRowSelected"));
        }else{
            Stay selectedStay = stayTableModel.getRow(row);
            stayManager.deleteStay(selectedStay.getId());
            stayTableModel.refresh(stayManager.findAllStay());
        }
    }//GEN-LAST:event_deleteStayJBActionPerformed

    private void currentRoomForGuestJBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentRoomForGuestJBActionPerformed
        int row = guestJTable.getSelectedRow();
        if(row<0){
            JOptionPane.showMessageDialog(null, localization.getString("warnings.noRowSelected"));
        }else{            
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    int row = guestJTable.getSelectedRow();
                    Guest selectedGuest = guestsTableModel.getRow(row);
                    new viewRoomJDialog(hotelManager, roomsTableModel, selectedGuest).setVisible(true);
                }
            });
        }
    }//GEN-LAST:event_currentRoomForGuestJBActionPerformed

 
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createGuestJB;
    private javax.swing.JButton createRoomJB;
    private javax.swing.JButton createStayJB;
    private javax.swing.JButton currentRoomForGuestJB;
    private javax.swing.JButton deleteGuestJB;
    private javax.swing.JButton deleteRoomJB;
    private javax.swing.JButton deleteStayJB;
    private javax.swing.JTabbedPane guestJTabbedPane;
    private javax.swing.JTable guestJTable;
    private javax.swing.JPanel guetsJPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel roomJPanel;
    private javax.swing.JTable roomJTable;
    private javax.swing.JPanel stayJPanel;
    private javax.swing.JTable stayJTable;
    private javax.swing.JButton updateGuetsJB;
    private javax.swing.JButton updateRoomJB;
    private javax.swing.JButton updateStayJB;
    // End of variables declaration//GEN-END:variables
}
