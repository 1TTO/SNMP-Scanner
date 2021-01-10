package manuel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class File {
    static final String MIB_FILE_PATH = "./../data/mib.csv";
    static final String OID_FILE_PATH = "./../data/oid.csv";

    /**
     * Reads a csv-file and returns the content as list
     * @param path String with the path to the file
     * @return ArrayList of strings with the content
     * @throws IOException when the file was not found
     */
    static ArrayList<String> getCSVContent(String path) throws IOException{
        return new ArrayList<>(Arrays.asList((new String(Files.readAllBytes(Paths.get(path)))).split(";")));
    }

    /**
     * Writes a list of string to a csv-file
     * @param path String to the path of the file
     * @param contents ArrayList of strings with the content to print to the file
     * @throws IOException when the file was not found
     */
    static void setCSVContent(String path, ArrayList<String> contents) throws IOException{
        Files.write(Paths.get(path), String.join(";", contents).getBytes());
    }
}
