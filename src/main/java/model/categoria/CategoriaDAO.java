package model.categoria;

import model.utils.ConPool;

import java.sql.*;
import java.util.ArrayList;


@Generated
public class CategoriaDAO implements CategoriaDAOMethod {

    private ConPool conpool=ConPool.getInstance();
    private Connection connection= conpool.getConnection();

    public CategoriaDAO() throws SQLException {
    }

    /**
     * Questo metodo retituisce una Categoria in base ad un nome
     * @param nome è un oggetto di tipo stringa
     * @return un oggetto di tipo Categoria
     */
    @Override
    public Categoria cercaCategoria(String nome) {

        try{

            PreparedStatement ps;
            ps=connection.prepareStatement("select * from Categoria where nomeCategoria=?");
            ps.setString(1,nome);

            ResultSet rs=ps.executeQuery();
            if (rs.next()){
                Categoria categoria= new Categoria();
                categoria.setIdCategoria(rs.getInt(1));
                categoria.setNomeCategoria(rs.getString(2));
                categoria.setRoot(rs.getInt(3));
                return categoria;
            }
        }catch (SQLException sqlException){
            throw  new RuntimeException(sqlException);
        }
        return null;
    }


    /**
     * Questo metodo retituisce una Categoria in base ad un id
     * @param id è un oggetto che serve per identificare la categoria
     * @return un oggetto di tipo Categoria
     */
    @Override
    public Categoria cercaCategoriaById(int id) {
        try{
            PreparedStatement ps=connection.prepareStatement("select * from Categoria where idCategoria=?");
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                Categoria categoria= new Categoria();
                categoria.setIdCategoria(rs.getInt("idCategoria"));
                categoria.setNomeCategoria(rs.getString("nomeCategoria"));
                categoria.setRoot(rs.getInt("root"));
                return  categoria;
            }

        }catch (SQLException sqlException){
            throw  new RuntimeException(sqlException);
        }
        return  null;
    }

    @Override
    public void deleteCategoria(int idCategoria) {

        try{
            PreparedStatement ps;
            ps=connection.prepareStatement("delete from Categoria where idCategoria=?");
            ps.setInt(1,idCategoria);
            ps.execute();
        }catch (SQLException sqlException){
            throw new RuntimeException("delete error");
        }
    }

    @Override
    public void insertCategoria(Categoria c) {

        try{

            PreparedStatement ps= connection.prepareStatement("insert into Categoria value (?,?,?)");
            ps.setInt(1,c.getIdCategoria());
            ps.setString(2,c.getNomeCategoria());
            ps.setInt(3,c.getRoot());

            ps.execute();

        }catch (SQLException sqlException){
            throw new RuntimeException("insert error");
        }
    }

    @Override
    public void updateCategoria(Categoria c, int idCategoria) {
        try  {
            PreparedStatement ps;
            ps = connection.prepareStatement("update Categoria set nomeCategoria = ?, root = ?" +
                    "where idCategoria = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, c.getNomeCategoria());
            ps.setInt(2, c.getRoot());
            ps.setInt(3, c.getIdCategoria());
            if(ps.executeUpdate() != 1) {
                throw new RuntimeException("update error");
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    @Override
    public ArrayList<Categoria> doRetraiveByAllCategorie() {
        try {
            PreparedStatement ps;
            ps = connection.prepareStatement("select * from Categoria");
            ResultSet rs = ps.executeQuery();
            ArrayList<Categoria> lista = new ArrayList<>();
            while (rs.next()) {
                Categoria categoria=new Categoria();
                categoria.setIdCategoria(rs.getInt(1));
                categoria.setNomeCategoria(rs.getString(2));
                categoria.setRoot(rs.getInt(3));
                lista.add(categoria);
            }

            return lista;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }


    /**
     * Questo metodo retituisce tutte le Categorie
     * @throws 'sqlException'
     * @return un ArrayList di tipo Categoria
     */
    @Override
    public ArrayList<Categoria> doRetraiveByAllCategorieRoot() {
        try{
            PreparedStatement ps=connection.prepareStatement("select  * from Categoria where root=0");
            ResultSet rs=ps.executeQuery();
            ArrayList<Categoria> categorie= new ArrayList<>();
            while (rs.next()){
                Categoria categoria= new Categoria();
                categoria.setIdCategoria(rs.getInt(1));
                categoria.setNomeCategoria(rs.getString(2));
                categoria.setRoot(rs.getInt(3));
                categorie.add(categoria);
            }

            return categorie;

        }catch (SQLException sqlException){
            throw  new RuntimeException(sqlException);
        }
    }

    @Override
    public ArrayList<Categoria> cercaCategorie(int start, int end) {
        ArrayList<Categoria> lista =new ArrayList<>();
        try{

            PreparedStatement ps=connection.prepareStatement("select * from Categoria order by idCategoria" +
                    "limit ? offset ?");
            ps.setInt(1,start);
            ps.setInt(2,end);
            ResultSet rs= ps.executeQuery();
            while (rs.next()){
                Categoria categoria=new Categoria();
                categoria.setIdCategoria(rs.getInt(1));
                categoria.setNomeCategoria(rs.getString(2));
                categoria.setRoot(rs.getInt(3));
                lista.add(categoria);
            }

            return lista;
        }catch (SQLException sqlException){
            throw new RuntimeException(sqlException);
        }
    }
}
