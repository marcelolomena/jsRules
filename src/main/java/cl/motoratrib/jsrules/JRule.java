package cl.motoratrib.jsrules;

import cl.motoratrib.jsrules.exception.JsRulesException;

import java.util.Map;

public interface JRule {
    <T> T executeRuleset(String rulesetName, Map<String, Object> parameters) throws JsRulesException;
}
