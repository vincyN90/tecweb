/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gaetano
 */
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class database {
    private String user;//Username per il login al database 
    private String pass;//password per il login al database 
    private String Dbname;//Nome del database
    private Connection con;//Varaiabile di connessione al database
    private Statement stmt;//Statement per il database
    
    /**
     * Costrutto setta le variabili user,pass e dbname ai valori di acceso
     */
    public database(){
        try{
            user="root";
            pass="";
            Dbname="travelonthewheels";           
        } catch(Exception e){
             System.out.println("errore in costruttore database " + e);
        }

    }
    
    /**
     * Funzione che si occupa dell'apertura di una connessione con il datbase
     * 
     * @return Ritorna l'esito della conessione true esito positivo, false esito negativo 
     */
    public boolean ApriConnessione(){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                con =  DriverManager.getConnection("jdbc:mysql://localhost/"+Dbname,user,pass);
                stmt = (Statement) con.createStatement();
                return true;
            } catch (SQLException e) {
                System.out.println("sql error in ApriConnessione " + e);
                return false;
            } catch (ClassNotFoundException e){
                System.out.println("class not found error in ApriConnessione " + e);
                return false;
            } catch (Exception e){
                System.out.println("errore in ApriConnessione " + e);
                return false;
        }
    }
    /**
     * Si occupa della chiusura della connessione e dello Statement
     */
    public void ChiudiConnessione() {
        try {
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.out.println("sql errore in chiudi connessione " + ex);
        }catch (Exception e){
            System.out.println("errore in chiudi connessione " + e);
        }
    }
    /**
     * Si occupa di eseguire istruzioni sql del tipo DML
     * 
     * @param query contiene la querry da eseguire sul database
     * @return il numero di righe modificate,inserite o eliminate
     */
    public int EseguiUpgrade(String query){
        try{
            if(stmt.isClosed()==true){
               this.ApriConnessione();
           }
            return stmt.executeUpdate(query);
        }catch (SQLException ex){
            System.out.println("Sql errore in EseguiUpgrade " + ex);
            return -1;
        }
    }
    /**
     * Si occupa di eseguire istruzioni sql del tipo DQL
     * 
     * @param query Querry da eseguire nel database
     * @return Se la querry ha esito positivo ritorna il resultSet altrimenti null
     */
    public ResultSet EseguiQuery(String query){
       try {
           if(stmt.isClosed()==true){
               this.ApriConnessione();
           }
            stmt.executeQuery(query);
            ResultSet rs = stmt.getResultSet();
            return rs;
       } catch (SQLException ex) {
           System.out.println("sql error in Esegui query " + ex);
           Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, ex );
           return null;
       }catch (Exception e){
           System.out.println("errore in Esegui query " + e);
             Logger.getLogger(database.class.getName()).log(Level.SEVERE, null, e );
             return null;
        }
    }
    
    /**
     * Restituisce il numero di righe presenti del ResultSet
     * @param rs ResultSet di cui controllare il numero di righe
     * @return Il numero di righe
     */
    public static int GetNumeroRigheRS(ResultSet rs){
        try {
            int numrighe=0;
            if(!rs.isClosed() && rs.last()){
                rs.last();
                numrighe = rs.getRow();
                rs.first();  
            }
            return numrighe;
        } catch (SQLException ex) {
            System.out.println("Sql errore in GetNumeroRigheRS " + ex );
            return -1;
        }
    }
    /**
     * Verifica che nel database non vengano inseriti caratteri speciali in grado
     * di generare anomalie nel sistema.
     * 
     * @param dato dato da verificare
     * @return true se il dato non soddisfa i requisiti false altrimenti
     */
    public static boolean VerificaDato(String dato){
        if(dato.indexOf(" ")>0){
            return true;
        }
        if(dato.indexOf("@ ")>0){
            return true;
        }
        if(dato.indexOf(":")>0){
            return true;
        }
        if(dato.indexOf("\"")>0){
            return true;
        }
        return false;
    }
}
