package edu.emory.viseu.contracts;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class BootStrap {
    String file_path;


    public BootStrap(String file_path){
        this.file_path = file_path;
    }

    public int callStartUpScript() throws IOException {
        Process p2 = Runtime.getRuntime().exec("sh src/main/scripts/startup.sh");


        StringBuilder output = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(p2.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        System.out.println(output);

        return p2.getErrorStream() != null ? 0 : 1;


    }



}
