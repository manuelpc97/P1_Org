
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

    public ARLV() {
    
    }
    
    public ARLV(String nombre, int adminRegistro, int adminCampo) {
        direccion = "./" + nombre + ".txt";
        try {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            archivo.writeBytes("2" + nombre + "~" + adminRegistro + "|" + adminCampo + "|");
        } catch (Exception e) {
            System.out.println("Error al crear archivo");
        }
    }

    public void addHeader(String header, int cantidadCampos) {
        try {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            archivo.seek(archivo.length());
            archivo.writeBytes(cantidadCampos +"|^" +header + "}");
        } catch (Exception e) {
            System.out.println("Error al cargar header");
        }

    }
}
