package com.demo.imagese;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class SQLite {
    //Static Variables
    static java.sql.Connection conn  = null;
    static java.sql.Statement stmt = null;
    static String url = "jdbc:sqlite:C:\\Users\\Admin01\\Documents\\NetBeansProjects\\OOP\\src\\com\\demo\\imagese\\db.sqlite";
    static String error = "";
    
    //Open DB Session Method
    public static boolean openDB(){
        boolean result = false;
        try{
            Class.forName("org.sqlite.JDBC");
            conn = java.sql.DriverManager.getConnection(url);

            System.out.println("OK! SQLite DB session connected.");
            result = true;
        }
        catch(Exception e){
            error = e.getMessage();
            System.out.println("Open DB Error:" + e.getMessage());
        } 
        return result;
    }

    //Close DB Session Method
    public static boolean closeDB(){
        boolean result = false;
        try{
            conn.close();

            System.out.println("OK! SQLite DB session closed.");
            result = true;
        }
        catch(Exception e){
            error = e.getMessage();
            System.out.println("Close DB Error: " + e.getMessage());
        }
        return result;
    }    

    //Create Record Method
    public static boolean create(String table, String values){
        boolean result = false;
        String query = null;
        try{
            SQLite.stmt = conn.createStatement();
            query = "INSERT INTO "+ table +" VALUES(" + values + ")";
            stmt.executeUpdate(query);
            //You can include exception handling here. (e.g. Duplicate Data, etc.)
            result = true;
        }
        catch(Exception e){
            System.out.println("Create Error: " + e.getMessage());
            System.out.println("Query: " + query);
        }
        return result;
    }    

    //Update Record Method
    public static boolean update(String table, String set, int id){
        boolean result = false;
        try{
            SQLite.stmt = conn.createStatement();
            String query = "UPDATE "+ table +" SET " + set + " WHERE id=" + id;
            stmt.executeUpdate(query);
            //You can include exception handling here. (e.g. Duplicate Data, etc.)
            result = true;
        }
        catch(Exception e){
            System.out.println("Create Error: " + e.getMessage());
        }
        return result;
    }    

    //Delete Record Method
    public static boolean delete(String table, int id){
        boolean result = false;
        try{
            SQLite.stmt = conn.createStatement();
            String query = "DELETE FROM "+ table + " WHERE id=" + id;
            stmt.executeUpdate(query);
            result = true;
        }
        catch(Exception e){
            System.out.println("Create Error: " + e.getMessage());
        }
        return result;
    }     

    //Read Record Method
    public static String[][] read(String table){
        String[][] records = null;
        try{
            SQLite.stmt = conn.createStatement();

            //Count total rows
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + table);
            int totalRows = rs.getInt(1);

            //Count total columns
            rs = stmt.executeQuery("SELECT * FROM " + table);
            ResultSetMetaData rsmd = rs.getMetaData();
            int totalColumns = rsmd.getColumnCount();

            //Initialize 2D Array "records" with totalRows by totalColumns
            records = new String[totalRows][totalColumns];

            //Retrieve the record and store it to 2D Array "records"
            int row=0;
            while(rs.next()){                
                for(int col=0,index=1;col<totalColumns;col++,index++){
                    records[row][col] = rs.getObject(index).toString();
                }
                row++;
            }            
        }
        catch(Exception e){
            System.out.println("Read Error: " + e.getMessage());
        }
        return records;
    }
    
    //Read Record Method
    public static String[][] read(String table, String[] columns){
        String[][] records = null;
        try{
            SQLite.stmt = conn.createStatement();

            //Count total rows
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + table);
            int totalRows = rs.getInt(1);

            //Count total columns
            int totalColumns = columns.length;
            String cols = "";
            for(int i=0;i<totalColumns;i++){
                cols += columns[i];
                if((i+1)!=totalColumns)cols+=", ";
            }
            rs = stmt.executeQuery("SELECT "+ cols +" FROM " + table);

            //Initialize 2D Array "records" with totalRows by totalColumns
            records = new String[totalRows][totalColumns];

            //Retrieve the record and store it to 2D Array "records"
            int row=0;
            while(rs.next()){                
                for(int col=0,index=1;col<totalColumns;col++,index++){
                    records[row][col] = rs.getObject(index).toString();
                }
                row++;
            }            
        }
        catch(Exception e){
            System.out.println("Read Error: " + e.getMessage());
        }
        return records;
    }  
    
    //Read Record Method with WHERE clause
    public static String[][] read(String table, String where){
        String[][] records = null;
        try{
            SQLite.stmt = conn.createStatement();

            //Count total rows
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + table + " WHERE " + where);
            int totalRows = rs.getInt(1);

            //Count total columns
            rs = stmt.executeQuery("SELECT * FROM " + table + " WHERE " + where);
            ResultSetMetaData rsmd = rs.getMetaData();
            int totalColumns = rsmd.getColumnCount();

            //Initialize 2D Array "records" with totalRows by totalColumns
            records = new String[totalRows][totalColumns];

            //Retrieve the record and store it to 2D Array "records"
            int row=0;
            while(rs.next()){                
                for(int col=0,index=1;col<totalColumns;col++,index++){
                    records[row][col] = rs.getObject(index).toString();
                }
                row++;
            }            
        }
        catch(Exception e){
            System.out.println("Read Error: " + e.getMessage());
        }
        return records;
    }       
    
    //Read Image Method
    public static byte[] read(String table, String column, int id){
        byte[] buffer = null;
        try{
            String query = "SELECT " + column +" FROM " + table + " WHERE id=?"; 
            ResultSet rs = null;
            java.sql.PreparedStatement pstmt = null;            
             
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            
            rs = pstmt.executeQuery();

            while (rs.next()) {
                buffer = rs.getBytes("image");
            }
        }
        catch(Exception e){
            System.out.println("Read Error: " + e.getMessage());
        }
        return buffer;
    }
    
    public static boolean update(String table, byte[] image, String column, int id){
//        byte[] buffer = null;
boolean result = false;
        try{
            String query = "UPDATE " + table +" SET "+ column +"=? WHERE id=" + id; 
            ResultSet rs = null;
            java.sql.PreparedStatement pstmt = null;            
            
            pstmt = conn.prepareStatement(query);
            pstmt.setBytes(1, image);
            
            pstmt.executeUpdate();
            System.out.println("Image saved to database successfully!");
            result = true;
//            while (rs.next()) {
//                buffer = rs.getBytes("image");
//            }
        }
        catch(Exception e){
            System.out.println("Read Error: " + e.getMessage());
        }
        return result;
    }    
    
    /**
     * JDBC Routine
     * 
     * 1. Open a DB Session
     * 2. Execute a query
     * 3. Expect Return Value/s
     * 4. Close DB Session
     ***/
    
    public static void main(String[] args) {
        /*
        //Test a openDB and closeDB Methods
        if(SQLite.openDB()){
            //Execute a query...
            //Expect Return Value/s
            SQLite.closeDB();
        }
        */
        
        /*
        if(SQLite.openDB()){
            String id = "3";
            String name = "Mark";
            String values = id + "," + "'" + name + "'"; // 3,'Mark'
            SQLite.create("tblGuestbook", values);
            SQLite.closeDB();
        }
        */
        
        /** github.com/clydeatuic/java-todo
         * tblGuestbook
         *  ID          INT             Primary Key
         *  NAME        VARCHAR(50)     Not Null
         *  CONTACTNO   VARCHAR(50)     Not Null
         *  EMAIL       VARCHAR(50)     Not Null
         *  GENDER      CHAR(1)         Not Null
         */
        
        /*
        if(SQLite.openDB()){
            String updatedName = "Marky";
            String set = "name=" + "'" + updatedName + "'";
            SQLite.update("tblGuestbook", set, 3);
            SQLite.closeDB();
        }
        */

        /*
        if(SQLite.openDB()){
            
            //System.out.println(readFile("src/com/javasqlite/image/sample.jpg"));
            
            SQLite.insertBlob("tblDemo1", "src/com/javasqlite/image/sample.jpg", "sample.jpg");
            SQLite.closeDB();
        }
        */
        
        if(SQLite.openDB()){
            /*
            javax.swing.JFrame frame = new javax.swing.JFrame("FrameDemo");            
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            
            String query = "SELECT image FROM tblDemo1 WHERE id=?";
            ResultSet rs = null;
            java.sql.PreparedStatement pstmt = null;
            
            java.io.InputStream input = null;
            byte[] buffer = null;
            
            try{
                pstmt = conn.prepareStatement(query);
                int id = 1;
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    //input = rs.getBinaryStream("image");
                    buffer = rs.getBytes("image");
                    //buffer = new byte[1024];
                    //while (input.read(buffer) > 0) {
                        //fos.write(buffer);                        
                    //}                    
                }
            }
            catch(Exception e){
                System.out.println("Error: " + e.getMessage());
            }
            
            String filename = "src/com/javasqlite/image/sample.jpg";
            byte[] in = buffer;
            javax.swing.ImageIcon imageIcon = new javax.swing.ImageIcon(in);
            javax.swing.JLabel label = new javax.swing.JLabel();
            label.setIcon(imageIcon);
            
            frame.getContentPane().add(label, java.awt.BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
            */
            
            javax.swing.JFrame frame = new javax.swing.JFrame("FrameDemo");            
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            byte[] in = SQLite.read("tblDemo1", "image", 1);
            javax.swing.ImageIcon imageIcon = new javax.swing.ImageIcon(in);
            javax.swing.JLabel label = new javax.swing.JLabel();
            label.setIcon(imageIcon);
            
            frame.getContentPane().add(label, java.awt.BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);            
            
            SQLite.closeDB();
        }
        
    }
    
    private static byte[] readFile(String file) {
        java.io.ByteArrayOutputStream bos = null;
        try {
            java.io.File f = new java.io.File(file);
            java.io.FileInputStream fis = new java.io.FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new java.io.ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (java.io.FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (java.io.IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }

    public static void insertBlob(String table, String filename, String desc) {
        // insert sql
        String query = "INSERT INTO " + table
                + " VALUES('', ? , ? "
                + ")";
 
        try (
            java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
 
            // set parameters
            pstmt.setBytes(1, readFile(filename));
            pstmt.setString(2, desc);
 
            pstmt.executeUpdate();
            System.out.println("Stored the file in the BLOB column.");
 
        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
        }
    }   

    
}
