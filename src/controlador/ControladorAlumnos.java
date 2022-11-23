package controlador;
import java.io. * ;
import java.util.Scanner;
import javax.swing.*;

import modelo.Alumno;
import modelo.Sede;

public class ControladorAlumnos {
    public String ruta;
    public  ConexionBaseDatosJDBC conexionBaseDatosJDBC = new ConexionBaseDatosJDBC();
    public  ControladorAlumnos(){

    }
    public void abrirArchivo() throws IOException {
        System.out.println("llego 1");

        JFileChooser jf = new JFileChooser();
        System.out.println("llego 2");

        jf.showOpenDialog(jf);
        System.out.println("llego 3");
        File archivo = jf.getSelectedFile();
        if(archivo !=null) {
            ruta = archivo.getAbsolutePath();
            carga(ruta);
        }
    }
    private void carga(String ruta) throws FileNotFoundException, IOException {     
            Scanner sc = new Scanner(new File(ruta));
            //parsing a CSV file into the constructor of Scanner class 
            sc.useDelimiter(",");
            //setting comma as delimiter pattern
            while (sc.hasNext()) {
              System.out.print(sc.next());
            }
            sc.close();
    }
    
}