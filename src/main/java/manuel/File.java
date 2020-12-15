package manuel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class File {
    static final String MIB_FILE_PATH = "./../data/mib.csv";
    static final String OID_FILE_PATH = "./../data/oid.csv";

    static List<String> getCSVContent(String path) throws IOException{
        return Arrays.asList((new String(Files.readAllBytes(Paths.get(path)))).split(";"));
    }

    static void setCSVContent(String path, List<String> contents) throws IOException{
        Files.write(Paths.get(path), String.join(";", contents).getBytes());
    }
}
