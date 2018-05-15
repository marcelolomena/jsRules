package cl.motoratrib.jsrules.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpGetReglaOUT;
import cl.motoratrib.jsrules.Rule;
import cl.motoratrib.jsrules.RulesetExecutor;
import cl.motoratrib.jsrules.config.RuleConfig;
import cl.motoratrib.jsrules.config.RulesetConfig;
import cl.motoratrib.jsrules.exception.InvalidConfigException;
import cl.motoratrib.jsrules.exception.JsRulesException;
import cl.motoratrib.jsrules.loader.impl.RuleMigraLoaderImpl;
import cl.motoratrib.jsrules.loader.impl.RulesetMigraLoaderImpl;
import cl.motoratrib.tools.CacheMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.jdbc.OracleClob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.util.Map;

public class JRuleImpl implements JRule {

    private final static Logger LOGGER = LoggerFactory.getLogger(JRuleImpl.class);

    // default cache values
    private static final int CACHE_SIZE = 25;
    private static final long TIME_TO_LIVE = 15 * 60 * 1000; // 15 minutes

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    RuleService ruleService;

    @Autowired
    RuleMigraLoaderImpl ruleLoader;

    @Autowired
    RulesetMigraLoaderImpl rulesetLoader;

    // these maps provide rudimentary caching
    private final Map<String, Rule> ruleMap = new CacheMap<>(CACHE_SIZE, TIME_TO_LIVE);
    private final Map<String, RulesetExecutor> rulesetExecutorMap = new CacheMap<>(CACHE_SIZE, TIME_TO_LIVE);

    @Override
    public <T> T executeRuleset(String rulesetName, Map<String, Object> parameters) throws JsRulesException {
        RulesetExecutor<T> executor = loadRulesetByName(rulesetName);

        return executor.execute(parameters);
    }

    public Rule loadRuleByJson(String json) throws InvalidConfigException {
        try {
            RuleConfig ruleConfig = objectMapper.readValue(json, RuleConfig.class);
            return getRule(ruleConfig);
        } catch (IOException ex) {
            throw new InvalidConfigException("Unable to parse json: " + json, ex);
        }
    }

    @Override
    public Rule loadRuleByName(String ruleName) throws InvalidConfigException {
        Rule rule = ruleMap.get(ruleName);

        if (rule == null) {

            InputStream stream = getRecordFromDatabase(ruleName);

            if (stream == null) {
                throw new InvalidConfigException("Unable to find rule in table record : " + ruleName);
            }

            try {
                RuleConfig ruleConfig = objectMapper.readValue(stream, RuleConfig.class);
                rule = getRule(ruleConfig);
            } catch (IOException ex) {
                throw new InvalidConfigException("Unable to parse rule record : " + ruleName, ex);
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

    @Override
    public RulesetExecutor loadRulesetByName(String rulesetName) throws InvalidConfigException {
        RulesetExecutor ruleset = rulesetExecutorMap.get(rulesetName);


        if (ruleset == null) {
            InputStream stream = getRecordFromDatabase(rulesetName);

            if (stream == null) {
                throw new InvalidConfigException("Unable to find ruleset record : " + rulesetName);
            }

            try {
                RulesetConfig rulesetConfig = objectMapper.readValue(stream, RulesetConfig.class);
                ruleset = getRulesetExecutor(rulesetConfig);
            } catch (IOException ex) {
                throw new InvalidConfigException("Unable to parse ruleset record: " + rulesetName, ex);
            }
        }


        return ruleset;
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

    private InputStream getRecordFromDatabase(String name) {
        InputStream is = null;

        try {

            SpGetReglaOUT spOut = ruleService.getRuleByName(name);

            if (spOut == null) throw new Exception("spOut is null");
            //LOGGER.debug("OK spOut");

            OracleClob oc = spOut.getPJson();

            if (oc == null) throw new Exception("oc is null");
            //LOGGER.debug("OK oc");

            //LOGGER.debug("EL JSON DE ORACLE ------------> [" + convertToString(oc) + "]");
            is = oc.getAsciiStream();
            if (is == null) throw new Exception("is is null");
            //LOGGER.debug("OK is");

        } catch (Exception e) {
            LOGGER.error("#=========================================== " + e.getMessage() + " ===========================================#");
        }

        return is;
    }


    private String convertToString(java.sql.Clob data) {
        final StringBuilder builder = new StringBuilder();

        try {
            if (data == null) throw new Exception("data is null");
            final Reader reader = data.getCharacterStream();
            final BufferedReader br = new BufferedReader(reader);
            if (br == null) throw new Exception("buffer is null");
            int b;
            while (-1 != (b = br.read())) {
                builder.append((char) b);
            }

            br.close();
        } catch (SQLException e) {
            LOGGER.error("Within SQLException, Could not convert CLOB to string", e);
            return e.toString();
        } catch (IOException e) {
            LOGGER.error("Within IOException, Could not convert CLOB to string", e);
            return e.toString();
        } catch (Exception e) {
            LOGGER.error("Within Exception, Could not convert CLOB to string", e);
            return e.toString();
        }

        return builder.toString();
    }
}