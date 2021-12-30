package controller.utente;
import model.carrello.Carrello;
import model.utente.Utente;
import model.utente.UtenteDAO;
import model.utente.UtenteDAOMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "ServletIscrizione", value = "/ServletIscrizione")
public class ServletIscrizione extends HttpServlet {
    private String message;
    private String address;

    UtenteDAOMethod service;


    public ServletIscrizione(UtenteDAOMethod utenteDAO){
        super();
        service=utenteDAO;
    }

    public ServletIscrizione(){
        super();
        service= new UtenteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       // String cf= request.getParameter("CodiceFiscale");
       // this.request=request1;
        //this.response=response;
        address="index.jsp";
        message="";
        try {
            saveParameter(request,response);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    /**
     * Questo metodo permette ad Utente di registrarsi al sistema, per poter effettuare questa operazione
     * il codiceFiscale non deve essere già esistente nel database
     * @pre isNotPresentCf(codiceFiscale)
     * @param fn nome
     * @param ln cognome
     * @param cf codice fiscale
     * @param email e-mail
     * @param psw password
     * @param psw_rip ripetizione password
     * @param via via
     * @param numeroCivico numero civico
     * @param cap CAP
     * @param telefono numero di cellulare
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     * @post not isNotPresentCf(codiceFiscale)
     * @post doRetrieveByAllUtenti = @pre doRetrieveByAllUtenti+1
     */
    private void registraUtente(String fn,String ln,String cf, String email,String psw,String psw_rip,String via,
                                int numeroCivico,String cap,String telefono,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, SQLException {
            service=new UtenteDAO();
            Utente utente = new Utente();
            Pattern nome = Pattern.compile("^([a-z A-Z]{3,})$");
            Matcher matcher = nome.matcher(fn);
            if (!matcher.matches()) {
                address = "WEB-INF/pagine/iscriviti.jsp";
                message = "Il nome deve essere formato solo da lettere e deve contenere almeno tre caratteri.";
            }
            Pattern cognome = Pattern.compile("^([a-z A-Z]{3,})$");
            matcher = cognome.matcher(ln);
            if (!matcher.matches()) {
                address = "WEB-INF/pagine/iscriviti.jsp";
                message = "Il cognome deve essere formato solo da lettere e deve contenere almeno tre caratteri.";
            }

            Pattern codiceFiscale = Pattern.compile("(^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$)");
            matcher = codiceFiscale.matcher(cf);
            if (!matcher.matches()) {
                address = "WEB-INF/pagine/iscriviti.jsp";
                message = "Codice fiscale non valido.";
            }

            Pattern e_mail = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$");
            matcher = e_mail.matcher(email);
            if (!matcher.matches()) {
                address = "WEB-INF/pagine/iscriviti.jsp";
                message = "Formato email non valido.";
            }

            Pattern password = Pattern.compile("(^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$)");
            matcher = password.matcher(psw);
            if (!matcher.matches()) {
                address = "WEB-INF/pagine/iscriviti.jsp";
                message = "La password deve contenere almeno una lettera minuscola, una maiuscola e un numero.";
            }

            if (!psw_rip.equals(psw)) {
                address = "WEB-INF/pagine/iscriviti.jsp";
                message = "La password non coincide con quella digitata precedentemente.";
            }

            Pattern numCivico = Pattern.compile(("^[0-9]{1,3}$"));
            matcher = numCivico.matcher(Integer.toString(numeroCivico));
            if (!matcher.matches()) {
                address = "WEB-INF/pagine/iscriviti.jsp";
                message = "Il numero civico deve contenere solo numeri (da una a tre cifre).";
            }
            Pattern codicePostale = Pattern.compile(("^[0-9]{5}$"));
            matcher = codicePostale.matcher(cap);
            if (!matcher.matches()) {
                address = "WEB-INF/pagine/iscriviti.jsp";
                message = "Il CAP deve contenere esattamente 5 cifre.";
            }
            Pattern numTelefono = Pattern.compile(("^[0-9]{10}$"));
            matcher = numTelefono.matcher(telefono);
            if (!matcher.matches()) {
                address = "WEB-INF/pagine/iscriviti.jsp";
                message = "Il numero di telefono deve contenere esattamente 10 cifre.";
            }

            utente.setNome(fn);
            utente.setCognome(ln);
            utente.setCodiceFiscale(cf);
            utente.setEmail(email);
            utente.criptPassword(psw);
            utente.setVia(via);
            utente.setNumeroCivico(numeroCivico);
            utente.setCap(cap);
            utente.setTelefono(telefono);

            if (address.contains("index")) {
                service.insertUtente(utente);
                HttpSession session = request.getSession();

                if (session.getAttribute("carrello") != null) {
                    Carrello carrello = (Carrello) session.getAttribute("carrello");
                    utente.setCarrello(carrello);
                }
                session.setAttribute("utente", utente);

            } else {
                request.setAttribute("iscriviti", message);
            }
        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }

    private boolean isNotPresentCf(String codiceFiscale) throws SQLException {

        ArrayList<String> codiciFiscali=service.doRetraiveByAllCodiciFiscali();
        if (codiciFiscali.contains(codiceFiscale)){
            address = "WEB-INF/pagine/iscriviti.jsp";
            message="Questo codice fiscale è già presente nel sistema!";
            return  false;
        }
        return true;
    }

    private  void saveParameter(HttpServletRequest request,HttpServletResponse response ) throws SQLException, ServletException, IOException {

        String fn=request.getParameter("nome");
        String ln=request.getParameter("cognome");
        String cf= request.getParameter("CodiceFiscale");
        String email=request.getParameter("email");
        String psw=request.getParameter("psw");
        String psw_rip=request.getParameter("psw-rip");
        String via=request.getParameter("via");
        int numeroCivico=Integer.parseInt(request.getParameter("numeroCivico"));
        String cap=request.getParameter("cap");
        String telefono=request.getParameter("telefono");
        if (isNotPresentCf(cf)){
            registraUtente(fn,ln,cf,email,psw,psw_rip,via,numeroCivico,cap,telefono,request,response);
        }else {
            request.setAttribute("iscriviti", message);
            RequestDispatcher dispatcher = request.getRequestDispatcher(address);
            dispatcher.forward(request, response);
        }
    }

}
