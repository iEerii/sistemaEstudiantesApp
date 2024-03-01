package mx.com.emeru.DAO;

import mx.com.emeru.dominio.Estudiante;
import static mx.com.emeru.conexion.Conexion.getConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {
    public List<Estudiante> listarEstudiantes(){
        List<Estudiante> estudiantes = new ArrayList<>();
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConexion();
        String sql = "SELECT * FROM estudiante ORDER BY id_estudiante";
        try{
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                var estudiante = new Estudiante();
                estudiante.setIdEstudiante(rs.getInt("id_estudiante"));
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setApellido(rs.getString("apellido"));
                estudiante.setTelefono(rs.getString("telefono"));
                estudiante.setEmail(rs.getString("email"));
                estudiantes.add(estudiante);
            }
        } catch (Exception e){
            System.out.println("Ocurrio un error al seleccionar datos: " + e.getMessage());
        }
        finally {
            try{
                con.close();
            } catch (Exception e){
                System.out.println("Ocurrio un error al cerrar conexion: " + e.getMessage());
            }
        }
        return estudiantes;
    }

    public boolean agregarEstudiante(Estudiante estudiante){
        PreparedStatement ps;
        Connection con = getConexion();
        String sql = "INSERT INTO estudiante(nombre, apellido, telefono, email)" +
                "VALUES(?, ?, ?, ?)";
        try{
            ps = con.prepareStatement((sql));
            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getApellido());
            ps.setString(3, estudiante.getTelefono());
            ps.setString(4, estudiante.getEmail());
            ps.execute();
            return true;
        } catch (Exception e){
            System.out.println("Error al agregar estudiante: " + e.getMessage());
        }
        finally {
            try{
                con.close();
            } catch (Exception e){
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return false;
    }

    public boolean findEstudianteById(Estudiante estudiante){
        PreparedStatement ps;
        ResultSet rs;
        Connection con = getConexion();
        String sql = "SELECT * FROM estudiante WHERE id_estudiante = ?";
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1,estudiante.getIdEstudiante());
            rs = ps.executeQuery();
            if(rs.next()){
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setApellido(rs.getString("apellido"));
                estudiante.setTelefono(rs.getString("telefono"));
                estudiante.setEmail(rs.getString("email"));
                return true;
            }
        } catch(Exception e){
            System.out.println("Ocurrió un error al buscar estudiante: " + e.getMessage());
        }
        finally{
            try{
                con.close();
            } catch (Exception e){
                System.out.println("Ocurrio un error al cerrar: " + e.getMessage());
            }
        }
        return false;
    }

    //Haciendo pruebas de la aplicacion
    public static void main(String[] args) {
        var estudianteDao = new EstudianteDAO();

        //Agregar estudiante
        var nuevoEstudiante = new Estudiante("Carlos", "Lara", "5544112233", "carlos@gmail");
        var agregado = estudianteDao.agregarEstudiante(nuevoEstudiante);
        if(agregado)
            System.out.println("Estudiante agregado: " + nuevoEstudiante);
        else
            System.out.println("No se agrego el estudiante: " + nuevoEstudiante);

        System.out.println("Listado Estudiantes: ");
        List<Estudiante> estudiantes =estudianteDao.listarEstudiantes();
        estudiantes.forEach(System.out::println);

        //Buscar estudiante por ID
        var estudiante1 = new Estudiante(2);
        System.out.println("Estudiante antes de la busqueda: " + estudiante1);
        var encontrado = estudianteDao.findEstudianteById(estudiante1);
        if(encontrado)
            System.out.println("Estudiante encontrado: " + estudiante1);
        else
            System.out.println("No se encontro estudiante: " + estudiante1.getIdEstudiante());
    }
}
