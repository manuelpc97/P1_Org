
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import javax.swing.table.DefaultTableModel;

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

    public ARLV(String dir) {
        direccion = dir;
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
            archivo.writeBytes(cantidadCampos + "|^" + header + "}");
        } catch (Exception e) {
            System.out.println("Error al cargar header");
        }

    }

    public void addRegistro(String registro) throws IOException {
        int tipoAdministracionRegistro = 0;
        tipoAdministracionRegistro = this.getTipoAdministracionRegistros();
        //En este metodo se supone que ya recibis todos los campos compactados y todo, entonces aqui te encargas de 
        //organizar los REGISTROS segun el metodo que te toca, y de una vez lo escribis en el archivo.
        if (tipoAdministracionRegistro == 1) {
            //Indicador de Longitud
        } else if (tipoAdministracionRegistro == 2) {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            archivo.seek(archivo.length());
            archivo.writeBytes(registro);

        }else if(tipoAdministracionRegistro == 3){
             RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            archivo.seek(archivo.length());
            for (int i = 0; i < registro.length(); i++) {
                archivo.writeByte(registro.charAt(i));
            }
        }
    }

    public int getTipoAdministracionRegistros() throws FileNotFoundException, IOException {
        int retorno = 0;
        int posicion = 0;
        int posicion2 = 0;
        String number = "";

        RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");

        for (int i = 0; i < archivo.length(); i++) {
            archivo.seek(i);
            if (((char) archivo.readByte()) == '~') {
                posicion = i;
            } else if (((char) archivo.readByte()) == '|') {
                posicion2 = i;
                i = (int) archivo.length();
            }
        }

        for (int i = posicion + 1; i < posicion2 + 1; i++) {
            archivo.seek(i);
            number += ((char) archivo.readByte());
        }
        retorno = Integer.parseInt(number);
        return retorno;
    }

    public int getTipoAdministracionCampos() throws FileNotFoundException, IOException {
        int retorno = 0;
        int contador = 0;
        int pos1 = 0;
        int pos2 = 0;
        String numero = "";
        RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");

        for (int i = 0; i < archivo.length(); i++) {
            archivo.seek(i);
            if (((char) archivo.readByte()) == '|' && contador == 0) {
                pos1 = i;
                contador++;
            } else if (((char) archivo.readByte()) == '|' && contador == 1) {
                pos2 = i;
                i = (int) archivo.length();
            }
        }

        for (int i = pos1 + 1; i < pos2 + 1; i++) {
            archivo.seek(i);
            numero += ((char) archivo.readByte());
        }

        retorno = Integer.parseInt(numero);
        return retorno;
    }

    public int getCantidadDeCampos() throws FileNotFoundException, IOException {
        int retorno = 0;
        int contador = 0;
        int pos1 = 0;
        int pos2 = 0;
        String numero = "";
        RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");

        for (int i = 0; i < archivo.length(); i++) {
            archivo.seek(i);
            if (((char) archivo.readByte()) == '|') {
                contador++;
            }
            if (contador == 2) {
                pos1 = i;
            } else if (contador == 3) {
                pos2 = i;
                i = (int) archivo.length();
            }
        }

        for (int i = pos1; i < pos2; i++) {
            archivo.seek(i);
            numero += ((char) archivo.readByte());
        }
        retorno = Integer.parseInt(numero);
        return retorno;
    }

    public String[] getHeader() throws IOException {
        String[] arr = new String[this.getCantidadDeCampos()];
        RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
        int posicion = 0;
        int posicion2 = 0;
        int contador = 0;

        for (int i = 0; i < arr.length; i++) {
            arr[i] = "";
        }
        for (int i = 0; i < archivo.length(); i++) {
            archivo.seek(i);
            if (((char) archivo.readByte()) == '^') {
                posicion = i;
            } else if (((char) archivo.readByte()) == '}') {
                posicion2 = i;
                i = (int) archivo.length();
            }
        }
        String word = "";
        for (int k = posicion + 1; k < posicion2; k++) {
            archivo.seek(k);
            word += ((char) archivo.readByte());
        }
        arr = word.split("\\?");
        return arr;
    }

    public DefaultTableModel listar(DefaultTableModel modelo) throws IOException {
        int adminCampos = 0;
        int adminRegistros = 0;
        int cantCampos = 0;
        int inicio = 0;
        int ultimo = 0;
        String registro = "";
        RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");

        adminCampos = this.getTipoAdministracionCampos();
        adminRegistros = this.getTipoAdministracionRegistros();
        cantCampos = this.getCantidadDeCampos();
        String[] arr = new String[cantCampos];
        String[] arrGrande;
        
        if (adminRegistros == 2 && adminCampos == 2) {
            for (int i = 0; i < archivo.length(); i++) {
                archivo.seek(i);
                if (((char) archivo.readByte()) == '}') {
                    inicio = i;
                } 
                
            }
            
            for (int i = inicio+1; i < archivo.length(); i++) {
                archivo.seek(i);
                registro+=((char)archivo.readByte());
            }
            arrGrande = registro.split("]");
            for (int i = 0; i < arrGrande.length; i++) {
                arr = arrGrande[i].split("!");
                modelo.addRow(arr);
            }
            
        }else if(adminRegistros == 3 && adminCampos == 3){
        
        }else if(adminRegistros == 1 && adminCampos == 1){
        
        }
        return modelo;
    }
}
