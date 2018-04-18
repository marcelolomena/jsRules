package cl.motoratrib;

import cl.motoratrib.jsrules.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ClaseGenerica<T> {
    T obj;

    public ClaseGenerica(T o) {
        obj = o;
    }

    public String classType() {
        return obj.getClass().getName();
    }
}

public class DemoRules {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoRules.class);
    public static void main (String [ ] args) throws Exception{

        ClaseGenerica response = null;
        String fileJson = "";

        if (args.length == 0) {
            LOGGER.error("Falta el nombre del archivo");
        } else if (args.length == 1){

            long startTime = System.currentTimeMillis();

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
                else if(p.getParameterClass().equals("DateTime"))
                    parameters.put(p.getParameterName(), DateTime.parse(p.getParameterValue()));
            }

            Object o = jsRules.executeRuleset(in.getRulesetName(), parameters);

            if(o!=null)
                response = new ClaseGenerica(o);

            if(response!=null) {
                if (response.classType().equals("java.lang.String")) {
                    LOGGER.info("Respuesta : " + response.obj.toString());
                }
            }else{
                LOGGER.info("Respuesta : " + response );
            }

            long endTime = System.currentTimeMillis();
            LOGGER.info("Eso es todo " + (endTime - startTime) + " milliseconds");

        }

    }

    public static InJson readJsonFullFromFile(File inFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] json = Files.readAllBytes(inFile.toPath());
        String injson = new String(json, "ISO-8859-1");
        LOGGER.debug(injson);
        InJson in = mapper.readValue(json, InJson.class);
        return in;

    }
}
