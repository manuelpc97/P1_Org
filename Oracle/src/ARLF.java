
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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

    public ARLF() {

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

    public void addCampo(String campo) {
        try {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            archivo.seek(archivo.length());
            archivo.writeBytes(campo);
        } catch (Exception e) {
            System.out.println("Error al agregar el campo.");
        }
    }

    /* public void eliminar(int llave) {
        try {
            RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
            char encontrado;
            int salir = 0;
            int salir2 = 0;
            int contador = 0;
            int contador2 = 0;
            String numero1 = "";
            String numero2 = "";
            while (salir < 1) {

                archivo.seek(contador);
                if (archivo.readChar() == '~') {
                    salir++;
                    contador++;
                    while (salir2 < 2) {
                        archivo.seek(contador);
                        if (salir2 == 0) {
                            
                            if (archivo.readChar() == '|') {
                                salir2++;
                            }else{
                                numero1 = numero1 + archivo.readChar();
                            }

                        } else {
                            if (salir2 == 1) {
                               
                                if (archivo.readChar() == '|') {
                                    salir2++;
                                }else{
                                     numero2 = numero2 + archivo.readChar();
                                }

                            }
                        }
                        contador++;
                    }

                }
                contador++;
            }
            int numero_1 = Integer.parseInt(numero1);
            int numero_2 = Integer.parseInt(numero2);
            int registro = numero_1 * numero_2;
            String llave_primaria = "";
            int fin_header=0;
            for (int i = 0; i < archivo.length(); i++) {
                archivo.seek(i);
                encontrado = archivo.readChar();
                if (encontrado == '}') {
                    fin_header=i;
                    for (int j = i + 1; j < archivo.length(); j = j + registro) {
                        archivo.seek(j);
                        int contador_desde = j;
                        for (int k = j; k <= k + numero_2; k++) {
                            archivo.seek(k);
                            llave_primaria = llave_primaria + archivo.readChar();
                            contador_desde++;

                        }
                        if (llave_primaria == llave) {
                            archivo.seek(j);
                            archivo.writeChars("*");
                            
                        }
                    }
                }
            }
            ;
        } catch (Exception e) {
            System.out.println("Error al agregar el campo.");
        }
    }
     */
    public void agregar(String nuevo_registro) throws FileNotFoundException, IOException {
        RandomAccessFile archivo = new RandomAccessFile(direccion, "rw");
        archivo.seek(archivo.length());
        archivo.writeBytes(nuevo_registro);
    }
}
