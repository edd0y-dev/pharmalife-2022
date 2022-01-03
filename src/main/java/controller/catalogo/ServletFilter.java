package controller.catalogo;

import model.prodotto.Prodotto;
import model.prodotto.ProdottoDAO;
import model.prodotto.ProdottoDAOMethod;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet(name = "ServletFilter", value = "/ServletFilter")
public class ServletFilter extends HttpServlet {

    private ProdottoDAOMethod prodottoDAO;

    public ServletFilter() {
        prodottoDAO = new ProdottoDAO();
    }

    public ServletFilter(ProdottoDAO prodottoDAO) {
        this.prodottoDAO = prodottoDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome=request.getParameter("nome");
        String categoria=request.getParameter("categoria");
        String marchio=request.getParameter("marchio");
        double min=Double.parseDouble(request.getParameter("min"));
        double max=Double.parseDouble(request.getParameter("max"));
        filtraProdotti(nome, categoria, marchio, min, max, request);
        RequestDispatcher dispatcher= request.getRequestDispatcher("WEB-INF/pagine/listaProdotti.jsp");
        dispatcher.forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    /**
     * Questo metodo serve per filtrare i prodotti in base a varie caratteristiche
     * @pre //
     * @param nome
     * @param categoria
     * @param marchio
     * @param min
     * @param max
     * @param request
     * @throws ServletException
     * @throws IOException
     * @post //
     */

    private void filtraProdotti(String nome, String categoria, String marchio, double min, double max, HttpServletRequest request) throws ServletException, IOException {
        String opzione="filtro";
        prodottoDAO=new ProdottoDAO();
        ArrayList<Prodotto> prodotti=prodottoDAO.doRetreiveByAllProdotti();

        if(nome!=null)
            prodotti=prodottoDAO.FiltroNome(prodotti,nome);

        if(categoria!=null)
            prodotti=prodottoDAO.FiltroCategoria(prodotti,categoria);

        if(marchio!=null)
            prodotti=prodottoDAO.FiltroMarchio(prodotti,marchio);

        if(max!=50)
            prodotti=prodottoDAO.FiltroMax(prodotti,max);

        if(min!=0)
            prodotti=prodottoDAO.FiltroMin(prodotti,min);

        request.setAttribute("prodotti",prodotti);
        request.setAttribute("opzione",opzione);
    }
}