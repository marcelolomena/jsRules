package cl.motoratrib.jsrules.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpGetReglaOUT;


public interface RuleService {
    SpGetReglaOUT getRuleByName(String name) throws Exception;
}
