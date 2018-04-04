package cl.motoratrib.jsrules.util;

import cl.motoratrib.jsrules.Executor;
import cl.motoratrib.jsrules.RulesetExecutor;
import cl.motoratrib.jsrules.impl.AllTrueRulesetExecutorImpl;
import cl.motoratrib.jsrules.impl.AllTrueRulesetListExecutorImpl;
import cl.motoratrib.jsrules.impl.FirstTrueRulesetExecutorImpl;
import cl.motoratrib.jsrules.impl.FirstTrueRulesetListExecutorImpl;

import java.util.List;

/**
 * Created by Marcelo Lomeña 5/14/2018
 */
public enum RulesetTypeHandler {
    ALLTRUE {
        @Override
        @SuppressWarnings("unchecked")
        public RulesetExecutor getRulesetExecutor(String name, List<Executor> ruleSet, Object response) {
            return new AllTrueRulesetExecutorImpl(name, ruleSet, response);
        }

        @Override
        public boolean isRulesetListExecutor() {
            return false;
        }
    },
    FIRSTTRUE {
        @Override
        @SuppressWarnings("unchecked")
        public RulesetExecutor getRulesetExecutor(String name, List<Executor> ruleSet, Object response) {
            return new FirstTrueRulesetExecutorImpl(name, ruleSet);
        }

        @Override
        public boolean isRulesetListExecutor() {
            return false;
        }
    },
    ALLTRUELIST {
        @Override
        @SuppressWarnings("unchecked")
        public RulesetExecutor getRulesetExecutor(String name, List<Executor> ruleSet, Object response) {
            return new AllTrueRulesetListExecutorImpl(name, ruleSet, response);
        }

        @Override
        public boolean isRulesetListExecutor() {
            return true;
        }
    },
    FIRSTTRUELIST {
        @Override
        @SuppressWarnings("unchecked")
        public RulesetExecutor getRulesetExecutor(String name, List<Executor> ruleSet, Object response) {
            return new FirstTrueRulesetListExecutorImpl(name, ruleSet);
        }

        @Override
        public boolean isRulesetListExecutor() {
            return true;
        }
    };

    public abstract RulesetExecutor getRulesetExecutor(String name, List<Executor> ruleSet, Object response);

    public abstract boolean isRulesetListExecutor();
}
