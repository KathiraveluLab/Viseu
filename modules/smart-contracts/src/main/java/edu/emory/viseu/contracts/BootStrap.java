package edu.emory.viseu.contracts;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public class BootStrap {
    String file_path;


    public BootStrap(String file_path){
        this.file_path = file_path;
    }

    public int callStartUpScript() throws IOException {
        Process p1 = Runtime.getRuntime().exec("pwd");

        URL shell_path = BootStrap.class.getResource(file_path);
        Process p2 = Runtime.getRuntime().exec("./" + shell_path);

        return p2.getErrorStream() != null ? 0 : 1;


    }



}
