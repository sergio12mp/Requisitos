package modelo;

public class Materia{

    String IdMateria;

    public Materia() {
    	
    }
    
    public Materia(String IdMateria){
        this.IdMateria= IdMateria;
    }

    public String getIdMateria() {
        return IdMateria;
    }

    public void setIdMateria(String idMateria) {
        this.IdMateria = idMateria;
    }
}