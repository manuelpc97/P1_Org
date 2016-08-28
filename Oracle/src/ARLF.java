
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
        borrado = quitarLetras(dir) + "borrado.txt";
        cargarStack();
    }

    public ARLF(String direccion, int sizeRegistro, int sizeCampo) {
        this.direccion = direccion + ".txt";
        this.borrado = direccion + "borrado.txt";
        try {
            RandomAccessFile archivo = new RandomAccessFile(this.direccion, "rw");
            archivo.writeBytes("1" + direccion + "~" + sizeRegistro + "|" + sizeCampo + "|^");
        } catch (Exception e) {
            System.out.println("Error al crear el archivo");
        }
        cargarStack();
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

    public void addCampo(String nuevo_registro, int tamaño, int cantidad) throws FileNotFoundException, IOException {
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
            for (int j = chess + 1; j < archivo.length(); j = j + (tamaño * cantidad)) {
                archivo.seek(j);
                cont++;
                if (cont == primera) {
                    archivo.seek(j);
                    archivo.writeBytes(nuevo_registro);
                }
            }
            borrados.pop();
            saveStack();
        } else {
            archivo.seek(archivo.length());
            archivo.writeBytes(nuevo_registro);
        }
    }

    public DefaultTableModel listar(DefaultTableModel modelo, int sizeCampo, int amountCampos) throws FileNotFoundException, IOException {
        RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
        int numero = 0;
        String[] lista = new String[amountCampos];
        int contador = 1;

        for (int i = 0; i < archivo.length(); i++) {
            archivo.seek(i);
            if (((char) archivo.readByte()) == '}') {
                numero = i + 1;
            }
        }
        while (numero < archivo.length()) {
            if (isBorrado(contador) == false) {
                for (int i = 0; i < amountCampos; i++) {
                    lista[i] = "";
                }
                for (int i = 0; i < amountCampos; i++) {
                    for (int k = 0; k < sizeCampo; k++) {
                        archivo.seek(numero);
                        lista[i] += ((char) archivo.readByte());
                        numero += 1;
                    }
                }
                modelo.addRow(lista);
            } else {
                System.out.println("borrado " + contador);
                numero += amountCampos * sizeCampo;
            }
            contador++;
        }
        return modelo;
    }

    public void eliminar(int num) {
        try {
            RandomAccessFile archivo = new RandomAccessFile(borrado, "rw");
            if (archivo.length() == 0) {
                archivo.writeBytes(num + ",");
            } else {
                archivo.seek(archivo.length());
                archivo.writeBytes(num + ",");
            }
            borrados.push(num);
        } catch (Exception e) {
            System.out.println("Error al crear el archivo");
        }

    }

    public boolean isBorrado(int num) {
        boolean retorno = false;
        Stack temporal = new Stack();

        while (borrados.isEmpty() == false) {
            if (((int) borrados.peek()) == num) {
                retorno = true;
            }
            temporal.push(borrados.pop());
        }

        while (temporal.isEmpty() == false) {
            borrados.push(temporal.pop());
        }
        return retorno;
    }

    public String quitarLetras(String word) {
        String retorno = "";

        for (int i = 0; i < word.length() - 4; i++) {
            retorno += word.charAt(i);
        }
        return retorno;
    }

    public void cargarStack() {
        String temporal = "";
        try {
            RandomAccessFile eliminados = new RandomAccessFile(borrado, "rw");
            if (eliminados.length() != 0) {
                for (int i = 0; i < eliminados.length(); i++) {
                    eliminados.seek(i);
                    temporal += ((char) eliminados.readByte());

                }

                for (int i = 0; i < temporal.length(); i++) {
                    if (temporal.charAt(i) != ',') {
                        borrados.push(Integer.parseInt(Character.toString(temporal.charAt(i))));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Constructor");
        }
    }

    public void saveStack() throws FileNotFoundException, IOException {
        RandomAccessFile archivo = new RandomAccessFile(borrado, "rw");
        Stack temporal = new Stack();
        int contador = 0;

        if (borrados.isEmpty()) {
            archivo.setLength(0);
        } else {
            archivo.setLength(0);
            while (!borrados.isEmpty()) {
                archivo.seek(contador);
                archivo.writeBytes(borrados.peek() + ",");
                temporal.push(borrados.pop());
                contador += 2;
            }

            while (!temporal.isEmpty()) {
                borrados.push(temporal.pop());
            }
        }
    }
}
