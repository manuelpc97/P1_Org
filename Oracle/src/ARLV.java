
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
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
public class ARLV {

    String direccion = "";

    public ARLV(String nombre, int adminRegistro, int adminDato) {
        direccion = "./" + nombre + ".txt";
        try {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            archivo.writeChars(nombre+"~"+adminRegistro+"|"+adminDato+"|^");
        } catch (Exception e) {
            System.out.println("Error al crear archivo");
        }
    }

    public void addHeader(String header){
        try {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            
        } catch (Exception e) {
            System.out.println("Error al cargar header");
        }
        
    }
}
