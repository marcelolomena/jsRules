package cl.motoratrib.jsrules.loader.impl;

import cl.motoratrib.jsrules.*;
import cl.motoratrib.jsrules.config.ResponseConfig;
import cl.motoratrib.jsrules.config.RulesetConfig;
import cl.motoratrib.jsrules.exception.ClassHandlerException;
import cl.motoratrib.jsrules.exception.InvalidConfigException;
import cl.motoratrib.jsrules.impl.RuleExecutorImpl;
import cl.motoratrib.jsrules.loader.RulesetLoader;
import cl.motoratrib.jsrules.service.JRule;
import cl.motoratrib.jsrules.service.JRuleImpl;
import cl.motoratrib.jsrules.util.ClassHandler;
import cl.motoratrib.jsrules.util.RulesetTypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcelo Lomeña 2018/04/06
 */
@Service
public class RulesetMigraLoaderImpl implements RulesetLoader {
    private final static Logger LOGGER = LoggerFactory.getLogger(RulesetMigraLoaderImpl.class);

    private JRuleImpl jRule;

    @Autowired
    public RulesetMigraLoaderImpl() {
        this.jRule = new JRuleImpl();
    }

    @Override
    public RulesetExecutor load(RulesetConfig config) throws InvalidConfigException {
        String type;
        if (config.getRulesetType() != null) {
            type = config.getRulesetType().toUpperCase();
        } else {
            throw new InvalidConfigException("Ruleset Type must be provided");
        }
        //LOGGER.debug("tipo -------->" + type);
        RulesetTypeHandler rulesetTypeHandler = RulesetTypeHandler.valueOf(type);

        ResponseConfig responseConfig = config.getResponseConfig();
        ClassHandler classHandler;
        try {
            classHandler = ClassHandler.valueOf(responseConfig.getResponseClass().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidConfigException("Invalid response class: " + responseConfig.getResponseClass());
        }
        Object response;
        String responseString = responseConfig.getResponse();
        try {
            response = classHandler.convertString(responseString);
        } catch (ClassHandlerException ex) {
            throw new InvalidConfigException("Unable to parse response " + responseString, ex);
        }

        List<Executor> ruleSet = new ArrayList<>();
        List<String> components = config.getComponents();
        for (String component : components) {
            if (rulesetTypeHandler.isRulesetListExecutor()) {
                RulesetExecutor rulesetExecutor = jRule.loadRulesetByName(component);
                ruleSet.add(rulesetExecutor);
            } else {
                Rule rule = jRule.loadRuleByName(component);
                RuleExecutor ruleExecutor = new RuleExecutorImpl(rule);
                ruleSet.add(ruleExecutor);
            }
        }

        String name = config.getRulesetName();

        return rulesetTypeHandler.getRulesetExecutor(name, type, ruleSet, response);
    }
}
