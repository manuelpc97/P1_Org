
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
        if (tipoAdministracionRegistro == 1) {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            archivo.seek(archivo.length());
            archivo.writeBytes((registro.length()+1) + "]" + registro);
        } else if (tipoAdministracionRegistro == 2) {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            archivo.seek(archivo.length());
            archivo.writeBytes(registro);
            
        } else if (tipoAdministracionRegistro == 3) {
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
        
        for (int i = 0; i < archivo.length(); i++) {
            archivo.seek(i);
            if (((char) archivo.readByte()) == '}') {
                inicio = i;
            }
        }
        
        if (adminRegistros == 2 && adminCampos == 2) {
            for (int i = inicio + 1; i < archivo.length(); i++) {
                archivo.seek(i);
                registro += ((char) archivo.readByte());
            }
            arrGrande = registro.split("]");
            for (int i = 0; i < arrGrande.length; i++) {
                arr = arrGrande[i].split("!");
                modelo.addRow(arr);
            }
            
        } else if (adminRegistros == 3 && adminCampos == 3) {
            String[] keyValue = new String[2];
            String temporal = "";
            for (int i = inicio + 1; i < archivo.length(); i++) {
                archivo.seek(i);
                registro += ((char) archivo.readByte());
            }
            
            arrGrande = registro.split("]");
            
            for (int i = 0; i < arrGrande.length; i++) {
                String[] retorno = new String[arr.length];
                ultimo = 0;
                arr = arrGrande[i].split("<");
                System.out.println("Registro: " + arrGrande[i]);
                for (int k = 0; k < arr.length; k++) {
                    System.out.println("keyValue: " + arr[k]);
                    keyValue = arr[k].split(":");
                    System.out.println("campo: " + keyValue[1]);
                    retorno[k] = keyValue[1];
                }
                modelo.addRow(retorno);
            }
        } else if (adminRegistros == 1 && adminCampos == 1) {
            String numero = "";
            int number = 0;
            int contador1 = 0;
            int contador2 = 0;
            String temporal = "";
            
            for (int i = inicio+1; i < archivo.length(); i++) {
                archivo.seek(i);
                registro+=((char)archivo.readByte());
            }
            
            while(contador1<registro.length()){
                if(registro.charAt(contador1)!=']'){
                    numero+=registro.charAt(contador1);
                    contador1++;
                }else{
                    number = Integer.parseInt(numero);
                    System.out.println("numero: " + number);
                    for (int i = 0; i < number; i++) {
                        temporal+=registro.charAt(contador1)+"?";
                        contador1++;
                    }
                }
            }
            System.out.println("Temporal: " + temporal);
            
        }
        return modelo;
    }
    
    public boolean isVariable() throws IOException {
        RandomAccessFile file = new RandomAccessFile(direccion, "rw");
        file.seek(0);
        
        if(((char)file.readByte()) == '2'){
            return true;
        }
        return false;
    }
}
