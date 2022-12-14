package controlador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import controlador.ConexionConBaseDeDatos;
import modelo.Alumno;
import modelo.Aula;
import modelo.Materia;
import modelo.Responsable;
import modelo.ResponsableExamen;
import modelo.Sede;

public class ConexionBaseDatosJDBC extends ConexionConBaseDeDatos {
	PreparedStatement ps;
	ResultSet rs;
    ArrayList<Alumno> lAlumnos = new ArrayList<>();
    ArrayList<Alumno>  lAlumnosSinSede = new ArrayList<>();
	ArrayList<Materia> lMaterias = new ArrayList<>();
    ArrayList<Sede> lSedes = new ArrayList<>();
    ArrayList<Responsable> lResponsables = new ArrayList<>();
    ArrayList<Aula> lAulas = new ArrayList<>();
    List<String> lInstitutosAsignados  = new ArrayList<>();
    List<String> lVocalesAsignados = new ArrayList<>();
    List<String> lResponsablesExamenPorAnadir = new ArrayList<>();
    List<String> lResponsablesExamenAnadidosAExamen = new ArrayList<>();


    private Connection conn;

    private static ConexionBaseDatosJDBC instanciaInterfaz = null;

    public ConexionBaseDatosJDBC() {
    	
    	try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e)
        {
            System.err.println("ERROR DE DRIVER");
        }
    	
        try {
            // create connection for database called DBB_SCHEMA in a server installed in
            // DB_URL, with a user USER with password PASS
            //conn = DriverManager.getConnection(DB_URL + "/" + DB_SCHEMA, USER, PASS);
        	conn = DriverManager.getConnection("jdbc:mysql://database-pevau.cobadwnzalab.eu-central-1.rds.amazonaws.com/grupo11DB", "grupo11", "xtDA3sPVFCE9BRhK");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ConexionBaseDatosJDBC getInstance() {
        if (instanciaInterfaz == null) {
            instanciaInterfaz = new ConexionBaseDatosJDBC();
        }
        return instanciaInterfaz;
    }
    
    public List<Alumno> listaAlumnos() {
        
        String selectQueryBody = "SELECT * FROM ALUMNO";
        Statement querySt;
        try {
            querySt = conn.createStatement();
            ResultSet rs = querySt.executeQuery(selectQueryBody);
            // position result to first
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    String c = rs.getString(2);
                    String nombre = rs.getString(3);
                    String ap1 = rs.getString(4);
                    String ap2 = rs.getString(5);
                    String det = rs.getString(6);
                    lAlumnos.add(new Alumno(id,c,nombre,ap1,ap2,det));
                    
                }
            }
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lAlumnos;        
    }

    public List<Alumno> listaAlumnosDeUnCentro(String centro){
        ArrayList<Alumno> lAlumno = new ArrayList<>();
        String selectQueryBody = "SELECT * FROM ALUMNO WHERE Centro=?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(selectQueryBody);
            preparedStatement.setString(1, centro);
            ResultSet rs = preparedStatement.executeQuery();
            // position result to first
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    String c = rs.getString(2);
                    String nombre = rs.getString(3);
                    String ap1 = rs.getString(4);
                    String ap2 = rs.getString(5);
                    lAlumno.add(new Alumno(id, c, nombre, ap1, ap2, " "));

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lAlumno;
    }

    public List<Materia> listaMaterias() {
        
        String selectQueryBody = "SELECT * FROM MATERIA";
        Statement querySt;
        try {
            querySt = conn.createStatement();
            ResultSet rs = querySt.executeQuery(selectQueryBody);
            // position result to first
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    lMaterias.add(new Materia(id));
                    
                }
            }
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lMaterias;        
    }
   
    
    
    public List<String> getListaMaterias() {
    	List<String> lista=null;
    	for (Materia m: lMaterias) {
          lista.add(m.getIdMateria());
        }
    	return lista;
    }
    public List<Sede> listaSedes() {
        String selectQueryBody = "SELECT * FROM SEDE";
        Statement querySt;
        try {
            querySt = conn.createStatement();
            ResultSet rs = querySt.executeQuery(selectQueryBody);
            // position result to first
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String nombre = rs.getString(2);
                    lSedes.add(new Sede(id, nombre));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lSedes;
    }
    public List<String> getListaSedes() {
    	List<String> lista=null;
    	for (Sede s: lSedes) {
          lista.add(s.getNombre());
        }
    	return lista;
    }
    

    public List<Responsable> listaResponsable() {
        String selectQueryBody = "SELECT * FROM RESPONSABLE";
        Statement querySt;
        try {
            querySt = conn.createStatement();
            ResultSet rs = querySt.executeQuery(selectQueryBody);
            // position result to first
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String nombre = rs.getString(1);
                    lResponsables.add(new Responsable(nombre));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lResponsables;
    }
    
    public List<String> getListaResponsables() {
        List<String> lista=null;
        for (Responsable r: lResponsables) {
            lista.add(r.getNombre());
        }
        return lista;
    }
    public int insertarSede(Sede s) {
        int sedeId = 0;
        String insertBody = "INSERT INTO " + "SEDE" + "(idSede, NombreSede) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertBody,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, s.getIdSede());
            preparedStatement.setString(2, s.getNombre());
            int res = preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while (rs.next()) {
                sedeId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sedeId;
    }

    public int actualizarSede(Sede s) {
        PreparedStatement preparedStatement = null;
        String updateBody = null;
        int res = 0;
        try {
            updateBody = "UPDATE " + "SEDE" + " SET nombre = "+ s.getNombre() +" WHERE (idSede = "+ s.getIdSede() +")";
            preparedStatement = conn.prepareStatement(updateBody);
            if (s.getIdSede() == null) {
                preparedStatement.setNull(1, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(1, s.getIdSede());
            }
            preparedStatement.setString(2, s.getNombre());
            res = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }

    public int borrarSede() {
        String deleteBody = "TRUNCATE TABLE SEDE";
        int res = 0;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(deleteBody);
            System.out.println("paso 2");
            res = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }
    
    
    public int insertarAsignaturas(Materia m) {
        int materiaId = 0;
        String insertBody = "INSERT INTO " + "MATERIA" + "(IdMateria,Tramo) VALUES (?,?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertBody,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, m.getIdMateria());
            preparedStatement.setString(2, m.getTramo());
            int res = preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while (rs.next()) {
                materiaId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materiaId;
    }
    
    
    public int borrarAsignaturas() {
        String deleteBody = "TRUNCATE TABLE MATERIA";
        int res = 0;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(deleteBody);
            System.out.println("paso 2");
            res = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }
    
    public int borrarAlumno() {
        String deleteBody = "TRUNCATE TABLE ALUMNO";
        int res = 0;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(deleteBody);
            System.out.println("paso 2");
            res = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }
    
    public int insertarAlumno(Alumno a) {
        int sedeId = 0;
        String insertBody = "INSERT INTO " + "ALUMNO" + "(DNI, Centro, Nombre, Apellido1, Apellido2,"
                + "DetalleMateria) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertBody,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, a.getDni());
            preparedStatement.setString(2, a.getCentro());
            preparedStatement.setString(3, a.getNombre());
            preparedStatement.setString(4, a.getApellido1());
            preparedStatement.setString(5, a.getApellido2());
            preparedStatement.setString(6, a.getDetalleMateria());
            int res = preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while (rs.next()) {
                sedeId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sedeId;
    }
    
    public int insertarResponsable(Responsable a) {
        int responsableId = 0;
        String insertBody = "INSERT INTO " + "RESPONSABLE" + "(Nombre) VALUES (?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertBody,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            //preparedStatement.setInt(1, a.getIdResponsable());
            preparedStatement.setString(1, a.getNombre());
           // preparedStatement.setString(3, a.getApellido1());
           // preparedStatement.setString(4, a.getApellido2());
            int res = preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while (rs.next()) {
                responsableId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responsableId;
    }
    
    
    public int borrarResponsables() {
        String deleteBody = "TRUNCATE TABLE RESPONSABLE";
        int res = 0;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(deleteBody);
            System.out.println("paso 2");
            res = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }

	@Override
	public List<Materia> verMaterias() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int insertarResponsablesExamen(ResponsableExamen r){
		int responsableId = 0;
        String insertBody = "INSERT INTO " + "RESPONSABLESEXAMEN" + "(idResponsablesExamen) VALUES (?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertBody,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, r.getNombre());
            int res = preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while (rs.next()) {
                responsableId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responsableId;
}
	
	public int borrarResponsablesExamen() {
        String deleteBody = "TRUNCATE TABLE RESPONSABLESEXAMEN";
        int res = 0;

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(deleteBody);
            System.out.println("paso 2");
            res = preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }

	@Override
	public List<ResponsableExamen> listaResponsablesExamen() {
		List<ResponsableExamen> lResponsables = new ArrayList<>(); 
		String selectQueryBody = "SELECT * FROM RESPONSABLESEXAMEN";
	        Statement querySt;
	        try {
	            querySt = conn.createStatement();
	            ResultSet rs = querySt.executeQuery(selectQueryBody);
	            // position result to first
	            if (rs.isBeforeFirst()) {
	                while (rs.next()) {
	                    String nombre = rs.getString(1);
	                    lResponsables.add(new ResponsableExamen(nombre));
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return lResponsables;
	}

    @Override
	public List<String> listaResponsablesExamenPorAnadir() {
		List<ResponsableExamen> lResponsables = new ArrayList<>(); 
		String selectQueryBody = "SELECT * FROM RESPONSABLESEXAMEN where rol is NULL";
	        Statement querySt;
	        try {
	            querySt = conn.createStatement();
	            ResultSet rs = querySt.executeQuery(selectQueryBody);
	            // position result to first
	            if (rs.isBeforeFirst()) {
	                while (rs.next()) {
	                    String nombre = rs.getString(1);
	                    lResponsablesExamenPorAnadir.add(nombre);
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return lResponsablesExamenPorAnadir;
	}

    @Override
	public List<String> listaResponsablesExamenAnadidosAExamen(String examen) {
		List<ResponsableExamen> lResponsables = new ArrayList<>(); 
		String selectQueryBody = "SELECT * FROM RESPONSABLESEXAMEN where rol is not NULL and examen = '"+ examen + "'";
	        Statement querySt;
	        try {
	            querySt = conn.createStatement();
	            ResultSet rs = querySt.executeQuery(selectQueryBody);
	            // position result to first
	            if (rs.isBeforeFirst()) {
	                while (rs.next()) {
	                    String nombre = rs.getString(1);
	                    lResponsablesExamenAnadidosAExamen.add(nombre);
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return lResponsablesExamenAnadidosAExamen;
	}




	@Override
	public int insertarAula(Aula a) {
		int responsableId = 0;
        String insertBody = "INSERT INTO " + "AULA" + "(idAula, aforo) VALUES (?, ?)";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertBody,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, a.getId());
            preparedStatement.setInt(2, a.getAforo());
            int res = preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while (rs.next()) {
                responsableId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responsableId;
	}

	@Override
	public int actualizarAula(Aula a, int aforonuevo) {
		PreparedStatement preparedStatement = null;
	        String updateBody = null;
	        int res = 0;
	        try {
	            updateBody = "UPDATE " + "AULA" + " SET aforo = ? WHERE (idAula = ? )";
	            preparedStatement = conn.prepareStatement(updateBody);
	            if (a.getId() == null) {
	                preparedStatement.setNull(1, java.sql.Types.INTEGER);
	            } else {
	                
	                preparedStatement.setInt(1, a.getAforo());
	                preparedStatement.setString(2, a.getId());
	            }
	            res = preparedStatement.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        return res;
	}

	@Override
	public int borrarAula(Aula a) {
		PreparedStatement preparedStatement = null;
	        String updateBody = null;
	        int res = 0;
	        try {
	        	
	        	
                
	            updateBody = "DELETE FROM AULA WHERE idAula = ?";
	            preparedStatement = conn.prepareStatement(updateBody);
	            if (a.getId() == null) {
	                preparedStatement.setNull(1, java.sql.Types.INTEGER);
	            } else {
	                preparedStatement.setString(1, a.getId());
	            }
	            res = preparedStatement.executeUpdate();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        return res;
	}
	public List<Aula> listaAulas() {
        
        String selectQueryBody = "SELECT * FROM AULA";
        Statement querySt;
        try {
            querySt = conn.createStatement();
            ResultSet rs = querySt.executeQuery(selectQueryBody);
            // position result to first
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String idAula = rs.getString(1);
                    String aforo = rs.getString(2);     
                    lAulas.add(new Aula(idAula, Integer.parseInt(aforo)));               
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lAulas;        
    }
	

    public List<String> getListaAula() {
    	List<String> lista=null;
    	for (Aula a: lAulas) {
            //lista.add();
        }
    	return lista;
    }


    public int asignarInstituto(String nombreInstituto, String nombreSede) {
		PreparedStatement preparedStatement = null;
	        String updateBody = null;
	        int res = 0;
	        System.out.println("paso 1");
            //En cada alumnos donde tiene ese nombre instituto tiene que tener ese valor para nombre sede
            try {
                updateBody = "UPDATE " + "ALUMNO" + " SET Sede = ? WHERE (Centro = ? )";
                preparedStatement = conn.prepareStatement(updateBody);
                if (nombreSede == null) {
                    preparedStatement.setNull(1, java.sql.Types.INTEGER);
                } else {
                    preparedStatement.setString(1, nombreSede);
                    preparedStatement.setString(2, nombreInstituto);
                }
                res = preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
	        return res;
	}

    public int quitarInstituto(String nombreInstituto, String nombreSede) {
		PreparedStatement preparedStatement = null;
	        String updateBody = null;
	        int res = 0;
	        System.out.println("paso 1");
            //En cada alumnos donde tiene ese nombre instituto tiene que tener ese valor para nombre sede
            try {
                updateBody = "UPDATE " + "ALUMNO" + " SET Sede = NULL WHERE (Centro = ? )";
                preparedStatement = conn.prepareStatement(updateBody);
                if (nombreSede == null) {
                    //preparedStatement.setNull(1, java.sql.Types.INTEGER);
                } else {
                    //preparedStatement.setString(1, nombreSede);
                    preparedStatement.setString(1, nombreInstituto);
                }
                res = preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
	        return res;
	}

    @Override
    public boolean consultarListaInstitutos() {
        String selectQueryBody = "SELECT * FROM ALUMNO";
        Statement querySt;
        try {
            querySt = conn.createStatement();
            ResultSet rs = querySt.executeQuery(selectQueryBody);
            // position result to first
            lInstitutosAsignados.clear();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String nombreCentro = rs.getString(2);
                    lInstitutosAsignados.add(nombreCentro);
                }
            }
            

            
        } catch (SQLException e) {
            e.printStackTrace();
        

        }
        return lInstitutosAsignados.isEmpty();
    }

    @Override
    public List<String> listaInstitutosAsginados(String sede) {
        String selectQueryBody = "SELECT * FROM ALUMNO where Sede = '"+sede+"'";
        Statement querySt;
        try {
            querySt = conn.createStatement();
            ResultSet rs = querySt.executeQuery(selectQueryBody);
            // position result to first
            lInstitutosAsignados.clear();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String nombreCentro = rs.getString(2);
                    lInstitutosAsignados.add(nombreCentro);
                }
            }
            

            
        } catch (SQLException e) {
            e.printStackTrace();
        

        }
        return lInstitutosAsignados;
    }

    @Override
    public List<Alumno> listaAlumnosSinSede() {
        String selectQueryBody = "SELECT * FROM ALUMNO WHERE Sede IS NULL";
        Statement querySt;
        try {
            querySt = conn.createStatement();
            ResultSet rs = querySt.executeQuery(selectQueryBody);
            // position result to first
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    String c = rs.getString(2);
                    String nombre = rs.getString(3);
                    String ap1 = rs.getString(4);
                    String ap2 = rs.getString(5);
                    String det = rs.getString(6);
                    lAlumnosSinSede.add(new Alumno(id,c,nombre,ap1,ap2,det));
                    
                }
            }
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lAlumnosSinSede;    
    }

    public int asignarExamenYRol(String id, String rol, String examen) {

		PreparedStatement preparedStatement = null;
	        String updateBody = null;
	        int res = 0;
	        System.out.println("paso 1");
            //En cada alumnos donde tiene ese nombre instituto tiene que tener ese valor para nombre sede
            try {
                updateBody = "UPDATE " + "RESPONSABLESEXAMEN" + " SET rol = ? , examen = ? WHERE idResponsablesExamen = ? ";
                
                preparedStatement = conn.prepareStatement(updateBody);
                if (examen == null) {
                    preparedStatement.setNull(1, java.sql.Types.INTEGER);
                } else {
                    preparedStatement.setString(1, rol);
                    preparedStatement.setString(2, examen);
                    preparedStatement.setString(3, id);
                }
                res = preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
	        return res;
	}

    public int quitarExamenYRol(String id) {
		PreparedStatement preparedStatement = null;
	        String updateBody = null;
	        int res = 0;
	        System.out.println("paso 1");
            //En cada alumnos donde tiene ese nombre instituto tiene que tener ese valor para nombre sede
            try {
                updateBody = "UPDATE " + "RESPONSABLESEXAMEN" + " SET rol = NULL, examen = NULL WHERE (idResponsablesExamen = ? )";
                preparedStatement = conn.prepareStatement(updateBody);
               
                    preparedStatement.setString(1, id);
                res = preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
	        return res;
	}

    @Override
    public boolean vocalAsignado(String rol, String examen) {//devuelve true si tiene algun vocal asignado
        lVocalesAsignados.clear();
        String selectQueryBody = "SELECT * FROM RESPONSABLESEXAMEN WHERE examen = '"+examen+"' AND rol = 'Vocal'";
        Statement querySt;
        try {
            querySt = conn.createStatement();
            ResultSet rs = querySt.executeQuery(selectQueryBody);
            // position result to first
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String nombre = rs.getString(1);   
                    lVocalesAsignados.add(nombre);               
                }
            }
        
            
        } catch (SQLException e) {
            e.printStackTrace();
        }    
    return lVocalesAsignados.isEmpty(); 
    }

    @Override
    public String conseguirCargoResponsableExamen(String r, String nombreExamen) {
        String selectQueryBody = "SELECT rol FROM RESPONSABLESEXAMEN WHERE idResponsablesExamen = '"+r+"'";
        Statement querySt;
        String nombre = null;
        try {
            querySt = conn.createStatement();
            ResultSet rs = querySt.executeQuery(selectQueryBody);
            // position result to first
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    nombre = rs.getString(1);   
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }    
    return nombre; 
    }


    
    
}