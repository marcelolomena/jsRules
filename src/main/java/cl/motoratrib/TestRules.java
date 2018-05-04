package cl.motoratrib;

import cl.motoratrib.jsrules.*;
import java.io.File;
import java.util.*;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRules {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoRules.class);
    public static void main (String [ ] args) throws Exception {

        ClaseGenerica response = null;
        String fileJson = "";
        Parameter p5_fechaPep, p5_fechaVencMac = null;


        if (args.length == 0) {
            LOGGER.error("Falta el nombre del archivo");
        } else if (args.length == 1) {

            long startTime = System.currentTimeMillis();

            //lee nombre archivo json desde connsola
            fileJson = args[0];

            //lee archivo
            InJson in = DemoRules.readJsonFullFromFile(new File(fileJson));

            //instancia de jsJules
            JsRules jsRules = JsRules.getInstance();

            Map<String, Object> parameters = new HashMap<>();

            List<Parameter> listParam = in.getParameterList();

            List<Parameter> lParam = DemoRules.containsParameters(listParam, "p5_fechaPep", "p5_fechaVencMac");
            p5_fechaPep = DemoRules.containsParameter(lParam, "p5_fechaPep");
            p5_fechaVencMac = DemoRules.containsParameter(listParam, "p5_fechaVencMac");

            if (p5_fechaPep != null && p5_fechaVencMac != null) {
                DateTime end = DateTime.parse(p5_fechaVencMac.getParameterValue());
                DateTime start = DateTime.parse(p5_fechaPep.getParameterValue());
                int days = Days.daysBetween(start, end).getDays();
                parameters.put("p5_diffMacPep", Long.valueOf(days));
                //LOGGER.debug("agregando la variable p5_diffMacPep con valor : " + days);
                listParam.remove(p5_fechaPep);
                listParam.remove(p5_fechaVencMac);
            }


            for (Parameter p : listParam) {
                if (p.getParameterClass().equals("Long")) {
                    parameters.put(p.getParameterName(), Long.valueOf(p.getParameterValue()));
                } else if (p.getParameterClass().equals("String")) {
                    parameters.put(p.getParameterName(), p.getParameterValue());
                } else if (p.getParameterClass().equals("DateTime")) {
                    parameters.put(p.getParameterName(), DateTime.parse(p.getParameterValue()));
                }
            }

            Object o = jsRules.executeRuleset(in.getRulesetName(), parameters);

            if (o != null)
                response = new ClaseGenerica(o);

            if (response != null) {
                if (response.classType().equals("java.lang.String")) {
                    LOGGER.info("Respuesta : " + response.obj.toString());
                } else {
                    LOGGER.error("ERROR");
                }

                long endTime = System.currentTimeMillis();
                LOGGER.info("Eso es todo " + (endTime - startTime) + " milliseconds");

            }

        }
    }

}
