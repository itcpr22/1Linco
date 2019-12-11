/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static jdk.nashorn.internal.runtime.Debug.id;

/**
 *
 * @author acer
 */
public class main_product extends javax.swing.JFrame {

    /**
     * Creates new form main_product
     */
    public main_product() {
        initComponents();
        this.setLocationRelativeTo(null);
        refreshThread.start();
        checkLowQty.start();
    }

    public main_product(String fname) {
        initComponents();
        jLabel6.setText("Welcome" + fname);
        this.setLocationRelativeTo(null);
        refreshThread.start();
        checkLowQty.start();
    }
    
        product product_obj = new product();
    conn con = new conn();
    
 Object id = null;
    
    void clearAddProductFields(){
        product.setText(null);
        quantity.setValue(0);
        price.setText(null);
        product.requestFocus();
        label.setText(null);
    }
 Thread refreshThread = new Thread(new Runnable() {     
        @Override
        public void run(){
            try{
                while(true){
                    refresh();
                    //System.out.println("Refresh");
                    Thread.sleep(5000);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(main_product.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    });  
    
    final void checkLowQuantity(){
        Notification n = new Notification();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            com.mysql.jdbc.Connection conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(con.url,con.username,con.password);
            
            String sql = "select * from products;";
            String status_sql = "UPDATE products SET status=? WHERE Product_id=?;";
            com.mysql.jdbc.Statement stmt = (com.mysql.jdbc.Statement) conn.createStatement();
            
            com.mysql.jdbc.PreparedStatement pstmt = (com.mysql.jdbc.PreparedStatement) conn.prepareStatement(status_sql);
            
            ResultSet rs = stmt.executeQuery(sql);
            
            while(rs.next()){
                String Product_id = rs.getString("Product_id");
                int qty = Integer.parseInt(rs.getString("qt"));
                int status = rs.getInt("status");
                String products = rs.getString("product");
                
                if (qty < 5 &&  status != 1){
                    pstmt.setInt(1, 1);
                    pstmt.setString(2, Product_id);
                    pstmt.executeUpdate();
                    n.displayNotification(products);
                }else if(qty > 5 &&  status == 1){
                    
                    pstmt.setInt(1, 2);
                    pstmt.setString(2, Product_id);
                    pstmt.executeUpdate();
                
                }
            }
                
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(main_product.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(main_product.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AWTException ex) {
            Logger.getLogger(main_product.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    Thread checkLowQty = new Thread(new Runnable(){
        
        @Override
        public void run(){
            try{
                while(true){
                    checkLowQuantity();
                    Thread.sleep(5000);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(main_product.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });


    final void refresh() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String conURL = "jdbc:mysql://localhost/lincodb?"
                    + "user=root&password=";

            Connection con = DriverManager.getConnection(conURL);

            PreparedStatement pstmt = con.prepareStatement("select * from products;");

            ResultSet rs = pstmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) protable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("Product_id"), rs.getString("product"), rs.getString("qt"), rs.getString("price")});
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(main_product.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    final void search(String keyword){
        
        try{
             Class.forName("com.mysql.jdbc.Driver");
            String conURL = "jdbc:mysql://localhost/lincodb"
                    + "?user=root&password=";
            Connection con = DriverManager.getConnection(conURL);
        
            String sql = "SELECT * FROM products WHERE Product_id LIKE ? OR product LIKE ?";
            PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sql);
            
            
            pstmt.setString(1, "%"+keyword+"%");
            pstmt.setString(2, "%"+keyword+"%");
            
            ResultSet rs = pstmt.executeQuery();
            DefaultTableModel model = (DefaultTableModel) protable.getModel();
            model.setRowCount(0);
            while(rs.next()){
                model.addRow(new Object[]{rs.getString("Product_id"),rs.getString("product"),rs.getString("qt"),rs.getString("price")});
            }     
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(product.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      final void enableAddProductFields(){
        product.setEnabled(true);
        quantity.setEnabled(true);
        price.setEnabled(true);
        clearAddProductFields();
    }
       class Notification{
    
        void displayNotification(String product) throws AWTException{
        
        SystemTray tray = SystemTray.getSystemTray();
        
        Image image = Toolkit.getDefaultToolkit().createImage("img/a.png");
        
        TrayIcon trayIcon = new TrayIcon(image,"Tray Icon"); 
        
        trayIcon.setImageAutoSize(true);
        tray.add(trayIcon);
        
        trayIcon.displayMessage("LOW QUANTITY", product+" Product Low on Quantity", TrayIcon.MessageType.WARNING);
        
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ap = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        product = new javax.swing.JTextField();
        quantity = new javax.swing.JSpinner();
        price = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        add = new javax.swing.JToggleButton();
        jLabel5 = new javax.swing.JLabel();
        save = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        label = new javax.swing.JLabel();
        addquantity = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        mainproduct = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        protable = new javax.swing.JTable();
        jToggleButton2 = new javax.swing.JToggleButton();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        search = new javax.swing.JButton();
        txt = new javax.swing.JTextField();
        addquant = new javax.swing.JButton();

        ap.setMinimumSize(new java.awt.Dimension(354, 251));
        ap.setResizable(false);

        jPanel2.setBackground(new java.awt.Color(0, 204, 204));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("PRODUCT:");

        quantity.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        price.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("QUANTITY:");

        add.setText("ADD");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("PRICE:");

        save.setText("save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        addquantity.setText("Add quantity");
        addquantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addquantityActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(product, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(100, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addquantity)
                        .addGap(33, 33, 33)
                        .addComponent(add)
                        .addGap(32, 32, 32)
                        .addComponent(save)
                        .addGap(62, 62, 62))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(23, 23, 23))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(product, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3)
                                .addComponent(label, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(price, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(save)
                    .addComponent(addquantity)
                    .addComponent(add))
                .addGap(70, 70, 70))
        );

        javax.swing.GroupLayout apLayout = new javax.swing.GroupLayout(ap.getContentPane());
        ap.getContentPane().setLayout(apLayout);
        apLayout.setHorizontalGroup(
            apLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        apLayout.setVerticalGroup(
            apLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jButton3.setText("jButton3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 204, 204));

        mainproduct.setText("ADD PRODUCT");
        mainproduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainproductActionPerformed(evt);
            }
        });

        protable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "PRODUCT", "QUANTITY", "PRICE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        protable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(protable);
        if (protable.getColumnModel().getColumnCount() > 0) {
            protable.getColumnModel().getColumn(0).setResizable(false);
            protable.getColumnModel().getColumn(1).setResizable(false);
            protable.getColumnModel().getColumn(2).setResizable(false);
            protable.getColumnModel().getColumn(3).setResizable(false);
        }

        jToggleButton2.setText("CLOSE");
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 3, 24)); // NOI18N
        jLabel6.setText("WELCOME ADMIN");

        jButton1.setText("DELETE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setText("Edit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        search.setText("Search");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });

        txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtActionPerformed(evt);
            }
        });
        txt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKeyReleased(evt);
            }
        });

        addquant.setText("Add Quantity");
        addquant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addquantActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(mainproduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addquant)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton2)
                        .addGap(31, 31, 31))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(search)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt))
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(search)
                    .addComponent(txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButton2)
                    .addComponent(mainproduct)
                    .addComponent(jButton1)
                    .addComponent(jButton4)
                    .addComponent(addquant))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mainproductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainproductActionPerformed
       ap.setVisible(true);
        ap.setLocationRelativeTo(rootPane);
        ap.setAlwaysOnTop(true);
        
        add.setVisible(true);
        save.setVisible(false);
        quantity.setVisible(true);
        
      this.enableAddProductFields();
       // TODO add your handling code here:
    }//GEN-LAST:event_mainproductActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        String pro = product.getText();
        int qty = (int) quantity.getValue();
        Object pr = price.getValue();
        String spr = pr.toString();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String conURL = "jdbc:mysql://localhost/lincodb?"
                    + "user=root&password=";

            Connection con = DriverManager.getConnection(conURL);

            PreparedStatement pstmt = con.prepareStatement("insert into products (product,qt,price)"
                    + " values (?,?,?);");
            pstmt.setString(1, pro);
            pstmt.setInt(2, qty);
            pstmt.setString(3, spr);
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(ap, "Successfully Added ");
            refresh();
            product.setText("");
            quantity.setValue(0);
            price.setText("");
            //==========
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(main_product.class.getName()).log(Level.SEVERE, null, ex);

        }

        //  pobj.addProduct(pro, qty, pr);        // TODO add your handling code here:
    }//GEN-LAST:event_addActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selRow = protable.getSelectedRow();
        if (selRow != -1) {
            int column = 0;
            String id = protable
                    .getValueAt(selRow, column).toString();
            int ans = JOptionPane.showConfirmDialog(rootPane,
                    "Are you sure you want to DELETE this Product?",
                    "Delete Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (ans == JOptionPane.YES_OPTION) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    String conURL = "jdbc:mysql://localhost/lincodb"
                            + "?user=root&password=";
                    java.sql.Connection con = DriverManager.getConnection(conURL);
                    PreparedStatement pstmt = con.prepareStatement("DELETE FROM products "
                            + "WHERE Product_id = ? ");
                    pstmt.setString(1, id);
                    pstmt.executeUpdate();

                    JOptionPane.showMessageDialog(rootPane, "One Row has Succesfully Deleted");
                    refresh();
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(main_product.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select row to be Deleted!",
                    "No Row Selected",
                    JOptionPane.WARNING_MESSAGE);

        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:

        int table_row = protable.getSelectedRow();
         this.clearAddProductFields();

        if(table_row != -1){
            id = protable.getValueAt(table_row, 0);
            Object products = protable.getValueAt(table_row, 1);
            Object quan = protable.getValueAt(table_row, 2);
            Object pri = protable.getValueAt(table_row, 3);

            product.setText((String) products);
            quantity.setValue(Integer.valueOf((String) quan));
            price.setValue(Double.valueOf((String) pri));

            ap.setVisible(true);
            ap.setLocationRelativeTo(rootPane);
            ap.setAlwaysOnTop(true);
            save.setVisible(true);
            add.setVisible(false);
            addquantity.setVisible(false);

            quantity.setEnabled(false);
        }else{
            JOptionPane.showMessageDialog(rootPane, "Please Select a product", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
 String newpn = product.getText();
        Object newpr = price.getValue();
        
        int r = product_obj.editProduct(id, newpn, newpr);
        if(r==1){
            JOptionPane.showMessageDialog(ap, "Product Edit Successfully");
            ap.setVisible(false);
            this.refresh();
        }else{
            JOptionPane.showMessageDialog(ap, "Problem Editing Product", "Error", JOptionPane.ERROR_MESSAGE);
        }
        


        // TODO add your handling code here:
    }//GEN-LAST:event_saveActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
 String keyword = txt.getText();
        this.search(keyword);

        // TODO add your handling code here:
    }//GEN-LAST:event_searchActionPerformed

    private void txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtActionPerformed
 
        // TODO add your handling code here:
    }//GEN-LAST:event_txtActionPerformed

    private void txtKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeyReleased
String keyword = txt.getText();
        this.search(keyword);


        // TODO add your handling code here:
    }//GEN-LAST:event_txtKeyReleased

    private void addquantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addquantActionPerformed
        // TODO add your handling code here:
        int row = protable.getSelectedRow();
        if(row != -1){
            ap.setVisible(true);
            ap.setLocationRelativeTo(this);
            ap.setAlwaysOnTop(true);
            save.setVisible(false);
            add.setVisible(false);
            addquant.setVisible(true);

            id = protable.getValueAt(row, 0);
            Object products = protable.getValueAt(row, 1);
            Object quan = protable.getValueAt(row, 2);
            Object pri = protable.getValueAt(row, 3);

            product.setEnabled(false);
            price.setEnabled(false);
            quantity.setEnabled(true);

            product.setText(products.toString());
            label.setText(quan.toString());
            price.setValue(Double.valueOf(pri.toString()));
            quantity.setValue(0);
        }else{
            JOptionPane.showMessageDialog(rootPane, "Please Select a product", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_addquantActionPerformed

    private void addquantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addquantityActionPerformed
  String pro = product.getText();
       Object qty = quantity.getValue();
        int c = JOptionPane.showConfirmDialog(ap, "Would you like to add\n "+qty+"\n to "+pro+" product?", "Add Quantity", JOptionPane.YES_NO_OPTION);
        if(c == JOptionPane.YES_OPTION){
           int r = product_obj.addquantity(id, qty);
            if(r==1){
              JOptionPane.showMessageDialog(ap, "Quantity Updated");
               ap.setVisible(false);
                this.refresh();
            
            }
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_addquantityActionPerformed

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
            java.util.logging.Logger.getLogger(main_product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main_product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main_product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main_product.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main_product().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton add;
    private javax.swing.JButton addquant;
    private javax.swing.JButton addquantity;
    private javax.swing.JDialog ap;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JLabel label;
    private javax.swing.JToggleButton mainproduct;
    private javax.swing.JFormattedTextField price;
    private javax.swing.JTextField product;
    private javax.swing.JTable protable;
    private javax.swing.JSpinner quantity;
    private javax.swing.JButton save;
    private javax.swing.JButton search;
    private javax.swing.JTextField txt;
    // End of variables declaration//GEN-END:variables

   

}
