
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
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
public class ARLV {

    String direccion = "";
    Stack borrados = new Stack();
    String dirBorrados = "";

    public ARLV() {

    }

    public ARLV(String dir) {
        direccion = dir;
        dirBorrados = quitarLetras(dir) + "borrado.txt";
        cargarStack();
    }

    public ARLV(String nombre, int adminRegistro, int adminCampo) {
        direccion = "./" + nombre + ".txt";
        this.dirBorrados = direccion + "borrado.txt";
        try {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            archivo.writeBytes("2" + nombre + "~" + adminRegistro + "|" + adminCampo + "|");
        } catch (Exception e) {
            System.out.println("Error al crear archivo");
        }
        cargarStack();
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
        int sizeAnterior = 0;
        int posicionArchivo = 0;
        tipoAdministracionRegistro = this.getTipoAdministracionRegistros();

        if (tipoAdministracionRegistro == 1) {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            if (this.borrados.isEmpty()) {
                archivo.seek(archivo.length());
                archivo.writeBytes((registro.length() + 1) + "]" + registro);
            } else {
                int pos = 0;
                pos = this.compare(registro);
                int contador = 0;
                
                if (pos == 0) {
                    archivo.seek(archivo.length());
                    archivo.writeBytes((registro.length() + 1) + "]" + registro);
                } else {
                    int beginWriting = this.findPositionLong(pos);
                    int endWriting = 0;
                    endWriting = this.findPosition(pos+1);
                    String concatenar = "";
                    concatenar = this.getSubString(endWriting);
                    
                    for (int i = 0; i < registro.length(); i++) {
                        archivo.seek(beginWriting);
                        beginWriting+=1;
                        archivo.writeByte(registro.charAt(i));
                    }
                    
                    System.out.println(concatenar);
                    for (int i = beginWriting; i < archivo.length(); i++) {
                        archivo.seek(i);
                        if(contador<concatenar.length()){
                            archivo.writeByte(concatenar.charAt(contador));
                            contador++;
                        }else{
                            archivo.writeBytes(" ");
                        }
                    }
                }
            }
            if (!borrados.isEmpty()) {
                borrados.pop();
                saveStack();
            }
//*****************************************************************************************
        } else if (tipoAdministracionRegistro == 2 || tipoAdministracionRegistro == 3) {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            if (this.borrados.isEmpty()) {
                archivo.seek(archivo.length());
                archivo.writeBytes(registro);
            } else {
                int pos = compare(registro);
                if (pos == 0) {
                    archivo.seek(archivo.length());
                    archivo.writeBytes(registro);
                } else {
                    String concatenar = "";
                    sizeAnterior = this.getSizeRegistro(pos);
                    posicionArchivo = this.findPosition(pos);
                    int posicionNext = 0;
                    posicionNext = this.findPosition(pos + 1);
                    concatenar = getSubString(posicionNext);
                    int contador = 0;

                    for (int i = 0; i < registro.length(); i++) {
                        archivo.seek(posicionArchivo);
                        posicionArchivo += 1;
                        archivo.writeByte(registro.charAt(i));
                    }

                    System.out.println(concatenar);
                    for (int i = posicionArchivo; i < archivo.length(); i++) {
                        archivo.seek(i);
                        if (contador < concatenar.length()) {
                            archivo.writeByte(concatenar.charAt(contador));
                            contador++;
                        } else {
                            archivo.writeBytes(" ");
                        }
                    }
                }
            }
            if (!borrados.isEmpty()) {
                borrados.pop();
                saveStack();
            }
        }
    }

    public String getSubString(int pos) throws FileNotFoundException, IOException {
        RandomAccessFile file = new RandomAccessFile(direccion, "rw");
        String retorno = "";

        for (int i = pos; i < file.length(); i++) {
            file.seek(i);
            retorno += ((char) file.readByte());
        }
        return retorno;
    }

    public int findPositionLong(int pos) throws FileNotFoundException, IOException {
        int retorno = 0;
        RandomAccessFile file = new RandomAccessFile(direccion, "rw");
        int contador = 0;
        int inicio = 0;

        for (int i = 0; i < file.length(); i++) {
            file.seek(i);
            if (((char) file.readByte()) == '}') {
                inicio = i;
                i = (int) file.length();
            }
        }

        for (int i = inicio + 1; i < file.length(); i++) {
            file.seek(i);
            if(((char)file.readByte()) == ']'){
                if(contador == pos-1){
                    retorno = i+1;
                    i = (int)file.length();
                }
                contador++;
            }
        }

        return retorno;
    }

    public int findPosition(int numRegistro) throws FileNotFoundException, IOException {
        int retorno = 0;
        int inicio = 0;
        int contador = 1;
        int aR = this.getTipoAdministracionRegistros();
        RandomAccessFile file = new RandomAccessFile(direccion, "rw");

        for (int i = 0; i < file.length(); i++) {
            file.seek(i);
            if (((char) file.readByte()) == '}') {
                inicio = i;
                i = (int) file.length();
            }
        }

        if (numRegistro == 1) {
            retorno = inicio + 1;
        } else {
            if (aR == 2 || aR == 3) {
                for (int i = inicio + 1; i < file.length(); i++) {
                    file.seek(i);
                    if (((char) file.readByte()) == ']') {
                        contador++;
                    }

                    if (contador == numRegistro) {
                        retorno = i + 1;
                        i = (int) file.length();
                    }
                }
            } else if (aR == 1) {

            }
        }

        return retorno;
    }

    public int compare(String registro) throws IOException {
        int retorno = 0;
        Stack temporal = new Stack();

        if (registro.length() <= this.getSizeRegistro((int) borrados.peek())) {
            retorno = (int) borrados.pop();
        } else {
            temporal.push((int) borrados.pop());
        }

        while (temporal.isEmpty() == false) {
            borrados.push((int) temporal.pop());
        }
        return retorno;
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
        int contador = 1;
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
//****************************************************************************************************************
        if (adminRegistros == 2 && adminCampos == 2) {
            for (int i = inicio + 1; i < archivo.length(); i++) {
                archivo.seek(i);
                registro += ((char) archivo.readByte());
            }
            arrGrande = registro.split("]");
            for (int i = 0; i < arrGrande.length; i++) {
                if (isBorrado(contador) == false) {
                    arr = arrGrande[i].split("!");
                } else {
                    for (int j = 0; j < arr.length; j++) {
                        arr[j] = "";
                    }
                }
                contador++;
                modelo.addRow(arr);
            }
//****************************************************************************************************************
        } else if (adminRegistros == 3 && adminCampos == 3) {
            String[] keyValue = new String[2];
            String temporal = "";
            for (int i = inicio + 1; i < archivo.length(); i++) {
                archivo.seek(i);
                registro += ((char) archivo.readByte());
            }

            arrGrande = registro.split("]");
            String[] retorno = new String[2];
            for (int i = 0; i < arrGrande.length; i++) {
                retorno = new String[arr.length];
                if (this.isBorrado(contador) == false) {
                    ultimo = 0;
                    arr = arrGrande[i].split("<");
                    for (int k = 0; k < arr.length; k++) {
                        keyValue = arr[k].split(":");
                        retorno[k] = keyValue[1];
                    }
                } else {
                    for (int j = 0; j < retorno.length; j++) {
                        retorno[j] = "";
                    }
                }
                contador++;
                modelo.addRow(retorno);
            }
//*********************************************************************************************************************            
        } else if (adminRegistros == 1 && adminCampos == 1) {
            String numero = "";
            int number = 0;
            int contador1 = 0;
            int contador2 = 0;
            String temporal = "";
            String[] registros;

            for (int i = inicio + 1; i < archivo.length(); i++) {
                archivo.seek(i);
                registro += ((char) archivo.readByte());
            }

            while (contador1 < registro.length()) {
                if (registro.charAt(contador1) != ']') {
                    numero += registro.charAt(contador1);
                    contador1++;
                } else {
                    number = Integer.parseInt(numero);
                    numero = "";
                    contador1++;
                    for (int i = 0; i < number - 1; i++) {
                        temporal += registro.charAt(contador1);
                        contador1++;
                    }
                    temporal += "?";
                }
            }

            registros = temporal.split("\\?");
            String[] campos = new String[cantCampos];
            for (int i = 0; i < registros.length; i++) {
                if (this.isBorrado(contador) == false) {
                    contador1 = 0;
                    numero = "";
                    temporal = "";
                    while (contador1 < registros[i].length()) {
                        if (registros[i].charAt(contador1) != ':') {
                            numero += registros[i].charAt(contador1);
                            contador1++;
                        } else {
                            System.out.println("numero: " + numero);
                            number = Integer.parseInt(numero);
                            numero = "";
                            contador1++;
                            for (int k = 0; k < number; k++) {
                                temporal += registros[i].charAt(contador1);
                                contador1++;
                            }
                            temporal += "?";
                        }
                    }
                    campos = temporal.split("\\?");
                } else {
                    for (int j = 0; j < campos.length; j++) {
                        campos[j] = "";
                    }
                }
                contador++;
                modelo.addRow(campos);
            }
        }
        //*****************************************************************************************************************       
        return modelo;
    }

    public boolean isVariable() throws IOException {
        RandomAccessFile file = new RandomAccessFile(direccion, "rw");
        file.seek(0);

        if (((char) file.readByte()) == '2') {
            return true;
        }
        return false;
    }

    public String findRegistro(int pos) throws IOException {
        String retorno = "";
        int tipoAdminRegistro = 0;
        tipoAdminRegistro = this.getTipoAdministracionRegistros();
        int inicio = 0;
        RandomAccessFile file = new RandomAccessFile(direccion, "rw");

        for (int i = 0; i < file.length(); i++) {
            file.seek(i);
            if (((char) file.readByte()) == '}') {
                inicio = i;
                i = (int) file.length();
            }
        }

        if (tipoAdminRegistro == 1) {
            String registro = "";
            int contador = 0;
            String numero = "";
            int contador2 = 0;
            for (int i = inicio + 1; i < file.length(); i++) {
                file.seek(i);
                registro += ((char) file.readByte());
            }

            while (contador < registro.length()) {
                registro.charAt(contador);

                if (registro.charAt(contador) != ']') {
                    numero += registro.charAt(contador);
                    contador++;
                } else {
                    contador2++;
                    if (contador2 == pos) {
                        retorno = numero;
                        contador = registro.length();
                    }
                    System.out.println(numero);
                    contador += Integer.parseInt(numero);
                    numero = "";
                }
            }
        } else if (tipoAdminRegistro == 2 || tipoAdminRegistro == 3) {
            int contador = 0;
            int pos1 = 0;
            int pos2 = 0;

            for (int i = inicio + 1; i < file.length(); i++) {
                file.seek(i);
                if (contador == (pos - 1)) {
                    pos1 = i;
                    i = (int) file.length();
                }
                if (((char) file.readByte()) == ']') {
                    contador++;
                }
            }

            for (int i = pos1 + 1; i < file.length(); i++) {
                file.seek(i);
                if (((char) file.readByte()) == ']') {
                    contador++;
                }
                if (contador == pos) {
                    pos2 = i;
                    i = (int) file.length();
                }
            }

            for (int i = pos1; i < (pos2 - 1); i++) {
                file.seek(i);
                retorno += ((char) file.readByte());
            }

        }

        return retorno;
    }

    public int getSizeRegistro(int pos) throws IOException {
        int retorno = 0;
        int adRegistro = 0;
        adRegistro = this.getTipoAdministracionRegistros();

        if (adRegistro == 1) {
            retorno = Integer.parseInt(this.findRegistro(pos));
        } else {
            retorno = this.findRegistro(pos).length();
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
            RandomAccessFile eliminados = new RandomAccessFile(dirBorrados, "rw");
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
        RandomAccessFile archivo = new RandomAccessFile(dirBorrados, "rw");
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

    public void eliminar(int num) {
        try {
            RandomAccessFile archivo = new RandomAccessFile(dirBorrados, "rw");
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
}
