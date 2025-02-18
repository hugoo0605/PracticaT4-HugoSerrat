import org.apache.commons.net.ftp.FTPClient;

import java.io.*;
import java.util.Scanner;

public class ClienteFTP {
    private static final String SERVER = "127.0.0.1";

    public static void main(String[] args) {
        String user;
        String password;
        Scanner sc = new Scanner(System.in);

        System.out.println("Bienvenido al programa de conexión al servidor.");
        System.out.print("Introduce el usuario: ");
        user = sc.nextLine();
        System.out.print("Introduce la contraseña: ");
        password = sc.nextLine();

        FTPClient ftpClient = new FTPClient();
        try {
            // Conectar al servidor FTP
            ftpClient.connect(SERVER);
            System.out.println("Conectado a " + SERVER);

            // Iniciar sesión con usuario y contraseña
            if (ftpClient.login(user, password)) {
                System.out.println("Inicio de sesión exitoso como " + user);

                // Listar archivos
                System.out.println("Archivos en el servidor:");
                String[] files = ftpClient.listNames();
                if (files != null && files.length > 0) {
                    for (String file : files) {
                        System.out.println(file);
                    }
                } else {
                    System.out.println("No se encontraron archivos en el servidor o falta permiso.");
                }

                String archivoDescargar;
                System.out.print("Escriba el nombre del arhivo a descargar: ");
                archivoDescargar = sc.nextLine();
                descargarFichero(archivoDescargar,ftpClient);

                String archivoSubir;
                System.out.print("Introduzca el nombre del archivo a subir desde su proyecto: ");
                archivoSubir = sc.nextLine();
                subirFichero(archivoSubir,ftpClient);

                // Cerrar sesión y desconectar
                ftpClient.logout();
            } else {
                System.out.println("No se pudo iniciar sesión.");
            }
            ftpClient.disconnect();
            System.out.println("Desconectado del servidor.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void descargarFichero(String archivo, FTPClient ftpClient) throws IOException {
        // Ruta donde se guardará el archivo descargado en el cliente (local)
        FileOutputStream outputStream = new FileOutputStream(archivo);

        // Descargar el archivo desde el servidor FTP
        boolean descargado = ftpClient.retrieveFile(archivo, outputStream);
        if (descargado) {
            System.out.println("Archivo descargado correctamente.");
        } else {
            System.out.println("Error al descargar el archivo.");
        }
        outputStream.close();
    }

    private static void subirFichero(String archivoSubir, FTPClient ftpClient) throws IOException {
        File file = new File(archivoSubir);

        //Subir el archivo al servidor FTP
        FileInputStream inputStream = new FileInputStream(file);
        ftpClient.setFileType(org.apache.commons.net.ftp.FTPClient.BINARY_FILE_TYPE);
        boolean subido = ftpClient.storeFile(file.getName(),inputStream);
        if(subido){
            System.out.println("Archivo subido correctamente");
        }else{
            System.out.println("Error al subir el archivo");
        }
    }
}
