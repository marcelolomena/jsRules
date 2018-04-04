package cl.motoratrib.jsrules;

import com.fasterxml.jackson.databind.ObjectMapper;
import cl.motoratrib.jsrules.config.RuleConfig;
import cl.motoratrib.jsrules.config.RulesetConfig;
import cl.motoratrib.jsrules.exception.InvalidConfigException;
import cl.motoratrib.jsrules.exception.JsRulesException;
import cl.motoratrib.jsrules.loader.RuleLoader;
import cl.motoratrib.jsrules.loader.RulesetLoader;
import cl.motoratrib.jsrules.loader.impl.RuleLoaderImpl;
import cl.motoratrib.jsrules.loader.impl.RulesetLoaderImpl;
import cl.motoratrib.tools.CacheMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Marcelo Lomeña 5/13/2018
 */
public class JsRules {

    private static final JsRules INSTANCE = new JsRules();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RuleLoader ruleLoader = new RuleLoaderImpl();
    private final RulesetLoader rulesetLoader = new RulesetLoaderImpl(this);

    // default cache values
    private static final int CACHE_SIZE = 25;
    private static final long TIME_TO_LIVE = 15 * 60 * 1000; // 15 minutes

    // these maps provide rudimentary caching
    private final Map<String, Rule> ruleMap = new CacheMap<>(CACHE_SIZE, TIME_TO_LIVE);
    private final Map<String, RulesetExecutor> rulesetExecutorMap = new CacheMap<>(CACHE_SIZE, TIME_TO_LIVE);

    public static JsRules getInstance() {
        return INSTANCE;
    }

    public Rule loadRuleByJson(String json) throws InvalidConfigException {
        try {
            RuleConfig ruleConfig = objectMapper.readValue(json, RuleConfig.class);
            return getRule(ruleConfig);
        } catch (IOException ex) {
            throw new InvalidConfigException("Unable to parse json: " + json, ex);
        }
    }

    public Rule loadRuleByName(String ruleName) throws InvalidConfigException {
        Rule rule = ruleMap.get(ruleName);

        if (rule == null) {
            String fileName = ruleName + ".json";

            InputStream stream = getFileFromClasspath(fileName);

            if (stream == null) {
                throw new InvalidConfigException("Unable to find rule file: " + fileName);
            }

            try {
                RuleConfig ruleConfig = objectMapper.readValue(stream, RuleConfig.class);
                rule = getRule(ruleConfig);
            } catch (IOException ex) {
                throw new InvalidConfigException("Unable to parse rule file: " + ruleName, ex);
            }
        }

        return rule;
    }

    public RulesetExecutor loadRulesetByJson(String json) throws InvalidConfigException {
        try {
            RulesetConfig rulesetConfig = objectMapper.readValue(json, RulesetConfig.class);
            return getRulesetExecutor(rulesetConfig);
        } catch (IOException ex) {
            throw new InvalidConfigException("Unable to parse json: " + json, ex);
        }
    }

    public RulesetExecutor loadRulesetByName(String rulesetName) throws InvalidConfigException {
        RulesetExecutor ruleset = rulesetExecutorMap.get(rulesetName);

        if (ruleset == null) {
            String fileName = rulesetName + ".json";

            InputStream stream = getFileFromClasspath(fileName);

            if (stream == null) {
                throw new InvalidConfigException("Unable to find ruleset file: " + fileName);
            }

            try {
                RulesetConfig rulesetConfig = objectMapper.readValue(stream, RulesetConfig.class);
                ruleset = getRulesetExecutor(rulesetConfig);
            } catch (IOException ex) {
                throw new InvalidConfigException("Unable to parse ruleset file: " + rulesetName, ex);
            }
        }

        return ruleset;
    }

    @SuppressWarnings("unchecked")
    public <T> T executeRuleset(String rulesetName, Map<String, Object> parameters) throws JsRulesException {
        RulesetExecutor<T> executor = loadRulesetByName(rulesetName);

        return executor.execute(parameters);
    }

    private Rule getRule(RuleConfig ruleConfig) throws InvalidConfigException {
        String ruleName = ruleConfig.getRuleName();
        Rule rule = ruleMap.get(ruleName);
        if (rule == null) {
            rule = ruleLoader.load(ruleConfig);
            ruleMap.put(ruleName, rule);
        }
        return rule;
    }

    private RulesetExecutor getRulesetExecutor(RulesetConfig rulesetConfig) throws InvalidConfigException {
        String rulesetName = rulesetConfig.getRulesetName();
        RulesetExecutor ruleset = rulesetExecutorMap.get(rulesetName);
        if (ruleset == null) {
            ruleset = rulesetLoader.load(rulesetConfig);
            rulesetExecutorMap.put(rulesetName, ruleset);
        }
        return ruleset;
    }

    private InputStream getFileFromClasspath(String fileName) {
        return this.getClass().getResourceAsStream("/" + fileName);
    }
}
