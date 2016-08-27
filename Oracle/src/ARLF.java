
import java.io.RandomAccessFile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author manuel
 */
public class ARLF {
    
    String direccion = "";

    public ARLF() {
    
    }
    
    public ARLF(String direccion, int sizeRegistro, int sizeCampo) {
        this.direccion = direccion + ".txt";
        try {
            RandomAccessFile archivo = new RandomAccessFile(this.direccion, "rw");
            archivo.writeBytes(direccion + "~" + sizeRegistro + "|" + sizeCampo+"|^");
        } catch (Exception e) {
            System.out.println("Error al crear el archivo");
        }
    }
    
    public void addRegistro(String header){
        try {
            RandomAccessFile archivo = new RandomAccessFile(direccion,"rw");
            archivo.seek(archivo.length());
            archivo.writeBytes(header+"}");
        } catch (Exception e) {
            System.out.println("Error al agregar header");
        }
    }
    
    public void addCampo(String campo){
        try {
            RandomAccessFile archivo = new RandomAccessFile(direccion,"rw");
            archivo.seek(archivo.length());
            archivo.writeBytes(campo);
        } catch (Exception e) {
            System.out.println("Error al agregar el campo.");
        }
    }
}
