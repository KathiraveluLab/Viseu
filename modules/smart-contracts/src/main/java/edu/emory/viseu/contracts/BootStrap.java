package edu.emory.viseu.contracts;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public class BootStrap {

    public static void callStartUpScript(String file_path, String[] args) throws IOException {
        Process p1 = Runtime.getRuntime().exec("pwd");

        URL shell_path = BootStrap.class.getResource(file_path);
        Process p2 = Runtime.getRuntime().exec("./" + shell_path);



    }

    public static void main(String[] args) throws IOException {
        callStartUpScript("/scripts/startup.sh", null);
    }

}
