package cl.motoratrib;

import cl.motoratrib.jsrules.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

class ClaseGenerica<T> {
    T obj;

    public ClaseGenerica(T o) {
        obj = o;
    }

    public String classType() {
        System.out.println("El tipo de T es " + obj.getClass().getName());
        return obj.getClass().getName();
    }
}

public class DemoRules {

    public static void main (String [ ] args) throws Exception{

        ClaseGenerica response = null;
        String fileJson = "";

        if (args.length == 0) {

            System.out.println("Falta el nombre del archivo");

        } else if (args.length == 1){

            //lee nombre archivo json desde connsola
            fileJson = args[0];

            //lee archivo
            InJson in = readJsonFullFromFile(new File(fileJson));

            //instancia de jsJules
            JsRules jsRules = JsRules.getInstance();

            Map<String, Object> parameters = new HashMap<>();

            for(Parameter p : in.getParameterList()){
                if(p.getParameterClass().equals("Long"))
                    parameters.put(p.getParameterName(), Long.valueOf(p.getParameterValue()));
                else if(p.getParameterClass().equals("String"))
                    parameters.put(p.getParameterName(), p.getParameterValue());
            }

            Object o = jsRules.executeRuleset(in.getRulesetName(), parameters);

            if(o!=null)
                response = new ClaseGenerica(o);

            if(response!=null) {
                if (response.classType().equals("java.lang.String"))
                    System.out.println("Respuesta : " + response.obj.toString());
            }else{
                System.out.println("Respuesta : " + response );
            }


        }

    }

    public static InJson readJsonFullFromFile(File inFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] json = Files.readAllBytes(inFile.toPath());
        String injson = new String(json, "ISO-8859-1");
        System.out.println(injson);
        InJson in = mapper.readValue(json, InJson.class);
        return in;

    }
}
