package cl.motoratrib;

import cl.motoratrib.jsrules.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.util.*;

import org.joda.time.DateTime;
import org.joda.time.Days;
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
        Parameter p5_fechaPep,p5_fechaVencMac = null;

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

            List<Parameter> listParam = in.getParameterList();

            List<Parameter> lParam = containsParameters(listParam,"p5_fechaPep","p5_fechaVencMac");
            p5_fechaPep    =   containsParameter(lParam,"p5_fechaPep");
            p5_fechaVencMac    =   containsParameter(listParam,"p5_fechaVencMac");

            if(p5_fechaPep!=null && p5_fechaVencMac!=null){
                DateTime end =  DateTime.parse(p5_fechaVencMac.getParameterValue());
                DateTime start =  DateTime.parse(p5_fechaPep.getParameterValue());
                int days = Days.daysBetween(start, end).getDays();
                parameters.put("p5_diffMacPep", Long.valueOf(days));
                //LOGGER.debug("agregando la variable p5_diffMacPep con valor : " + days);
                listParam.remove(p5_fechaPep);
                listParam.remove(p5_fechaVencMac);
            }


            for(Parameter p : listParam){
                if(p.getParameterClass().equals("Long")) {
                    parameters.put(p.getParameterName(), Long.valueOf(p.getParameterValue()));
                }else if(p.getParameterClass().equals("String")) {
                    parameters.put(p.getParameterName(), p.getParameterValue());
                }else if(p.getParameterClass().equals("DateTime")) {
                    parameters.put(p.getParameterName(), DateTime.parse(p.getParameterValue()));
                }
            }

            Object o = jsRules.executeRuleset(in.getRulesetName(), parameters);

            if(o!=null)
                response = new ClaseGenerica(o);

            if(response!=null) {
                if (response.classType().equals("java.lang.String")) {
                    //LOGGER.info("Respuesta : " + response.obj.toString());

                    ObjectMapper mapper = new ObjectMapper();

                    String responseJson = response.obj.toString().
                            replace("\"{","{").
                            replace("}\"","}").
                            replace("\\","");
                    //LOGGER.debug(responseJson);
                    List<FlowException> errores = mapper.readValue(responseJson, new TypeReference<List<FlowException>>(){});

                    List<FlowException> lstExceptions = new ArrayList<FlowException>();
                    int countException = 0;
                    for(FlowException e : errores){

                        //LOGGER.info("ERROR : " + e.getRef());

                        if(e.getResult() == 0){
                            FlowException fe = new FlowException(e.getResult(),e.getRef(),e.getAlerta());
                            lstExceptions.add(fe);
                            countException++;
                        }

                    }

                    if(countException>0){
                        // return JSON
                        LOGGER.info("COUNT ERROR : " + countException);
                        ObjectMapper map = new ObjectMapper();
                        String s=mapper.writeValueAsString( lstExceptions);

                    }else{
                        // sigue adelante
                    }


                }
            }else{
                LOGGER.error("ERROR");
            }

            long endTime = System.currentTimeMillis();
            LOGGER.info("Eso es todo " + (endTime - startTime) + " milliseconds");

        }

    }

    public static Parameter containsParameter(Collection<Parameter> c, String name) {
        for(Parameter o : c) {
            if(o != null && o.getParameterName().equals(name)) {
                return o;
            }
        }
        return null;
    }

    public static List<Parameter> containsParameters(Collection<Parameter> c, String leftOne, String leftTwo) {
        int indexTrue = 0;
        List<Parameter> params = new ArrayList<Parameter>();
        for(Parameter o : c) {
            if(o != null){
                if(o.getParameterName().equals(leftOne) || o.getParameterName().equals(leftTwo)) {
                    indexTrue++;
                    params.add(o);
                }
            }
            if(indexTrue>2) break;
        }
        return params;
    }


    public static InJson readJsonFullFromFile(File inFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        byte[] json = Files.readAllBytes(inFile.toPath());
        String injson = new String(json, "ISO-8859-1");
        LOGGER.debug(injson);
        InJson in = mapper.readValue(json, InJson.class);
        return in;

    }

    public static FlowException readJsonFromResult(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        LOGGER.debug(json);
        FlowException out = mapper.readValue(json, FlowException.class);
        return out;

    }
}
