package cl.motoratrib.jsrules.service;

import cl.motoratrib.jsrules.Rule;
import cl.motoratrib.jsrules.RulesetExecutor;
import cl.motoratrib.jsrules.exception.InvalidConfigException;
import cl.motoratrib.jsrules.exception.JsRulesException;

import java.util.Map;

public interface JRule {
    <T> T executeRuleset(String rulesetName, Map<String, Object> parameters) throws JsRulesException;
    Rule loadRuleByName(String ruleName) throws InvalidConfigException;
    RulesetExecutor loadRulesetByName(String rulesetName) throws InvalidConfigException;
}
