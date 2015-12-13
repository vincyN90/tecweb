/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(urlPatterns = {"/autenticazione.jsp"})
public class autenticazione extends HttpServlet {

    private String user; //username inserito dall'utente
    private String pass;//password iserita dall'utente
    private Integer verificaadmin;//variabile per identificare il tipo di accesso al sito
    private database dba;//Variabile che si occupa dell'interazione col database
    private ResultSet rs;//Contiene i risultati provenienti dalle querry
    private HttpSession Se;//Variabile di sessione
    
    /*
     * Init inizializza @see dba,@see user e @see pass
    */
    @Override
    public void init() throws ServletException {
        this.dba=new database();
        this.user="";
        this.pass="";
    }
    
    /** Invocato da @see doGet @see doPost
     * 
     * Verifica se i dati inseriti sono corretti in tal caso setta le variabili di sessione 'ID' e 'Username'
     * @param request servlet request inviato da @see doGet o @see doPost
     * @param response servlet response
    */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        this.user = request.getParameter("username");
        this.pass = request.getParameter("password");
        if (database.VerificaDato(user) == false && database.VerificaDato(pass) == false) {
            this.Se = request.getSession();
            this.dba.ApriConnessione();
            String str = "SELECT count(*) as numero FROM utenti where user='" + this.user + "' and password='" + this.pass + "'";
            this.rs = this.dba.EseguiQuery(str);
            this.rs.next();
            if (this.rs.getInt("numero") == 1) {
                str = "SELECT user , id , admin FROM utenti where user='" + user + "' and password='" + pass + "'";
                this.rs = this.dba.EseguiQuery(str);
                this.rs.next();
                this.Se.setAttribute("id_user", rs.getString("id"));
                this.Se.setAttribute("username", rs.getString("user"));
                this.verificaadmin = rs.getInt("admin");
                this.dba.ChiudiConnessione();
                if( this.verificaadmin == 1){ 
                    System.out.println("Sono amministratore");
                }else{
                    System.out.println("Sono altro");
                }
                response.sendRedirect("index.html");
            } else {
                dba.ChiudiConnessione();
                response.sendRedirect("erroreautenticazione.html");
            }
        }
    }

    /**
     * Intercetta le richieste di tipo get indirizzate alla servlet ed esegue la funzione
     * @see processRequest.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            System.out.println("Errore nella servlet 'autenticazione' " + ex);
            Logger.getLogger(autenticazione.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Intercetta le richieste di tipo set indirizzate alla servlet ed esegue la funzione
     * @see processRequest.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(autenticazione.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

