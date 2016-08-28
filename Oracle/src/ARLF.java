
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
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
public class ARLF {

  String direccion = "";
    Stack borrados = new Stack();
    String borrado = "";

    public ARLF() {

    }

    public ARLF(String dir) {
        direccion = dir;
    }

    
    public ARLF(String direccion, int sizeRegistro, int sizeCampo) {
        this.direccion = direccion + ".txt";
        try {
            RandomAccessFile archivo = new RandomAccessFile(this.direccion, "rw");
            archivo.writeBytes(direccion + "~" + sizeRegistro + "|" + sizeCampo + "|^");
        } catch (Exception e) {
            System.out.println("Error al crear el archivo");
        }
    }

    public void addRegistro(String header) {
        try {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            archivo.seek(archivo.length());
            archivo.writeBytes(header + "}");
        } catch (Exception e) {
            System.out.println("Error al agregar header");
        }
    }

    public void addCampo(String nuevo_registro,int tamaño,int cantidad) throws FileNotFoundException, IOException {
               RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
        if ((borrados.size() != 0)) {
            int primera = (int) borrados.peek();
            int chess = 0;
            for (int i = 0; i < archivo.length(); i++) {
                archivo.seek(i);
                if ((char) archivo.readByte() == '}') {
                    chess = i;
                    break;
                }

            }
            int cont = 0;
            for (int j = chess+1; j < archivo.length(); j = j + (tamaño * cantidad)) {
                archivo.seek(j);
                cont++;
                if (cont == primera) {
                    archivo.seek(j);
                    archivo.writeBytes(nuevo_registro);
                }
            }

        } else {
            archivo.seek(archivo.length());
            archivo.writeBytes(nuevo_registro);
        }
    }

    public void listar(DefaultTableModel modelo) {

    }
     public void eliminar(int num) {
        try {
            RandomAccessFile archivo = new RandomAccessFile(this.borrado, "rw");
            archivo.writeBytes(num + "" + ",");

        } catch (Exception e) {
            System.out.println("Error al crear el archivo");
        }

    }
    
}
