package manuel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class File {
    static final String MIB_FILE_PATH = "./../data/mib.csv";
    static final String OID_FILE_PATH = "./../data/oid.csv";

    static ArrayList<String> getCSVContent(String path) throws IOException{
        return new ArrayList<>(Arrays.asList((new String(Files.readAllBytes(Paths.get(path)))).split(";")));
    }

    static void setCSVContent(String path, ArrayList<String> contents) throws IOException{
        Files.write(Paths.get(path), String.join(";", contents).getBytes());
    }
}
