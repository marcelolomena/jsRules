package cl.motoratrib.jsrules.util;

import cl.motoratrib.jsrules.Executor;
import cl.motoratrib.jsrules.RulesetExecutor;
import cl.motoratrib.jsrules.impl.AllTrueRulesetExecutorImpl;
import cl.motoratrib.jsrules.impl.AllTrueRulesetListExecutorImpl;
import cl.motoratrib.jsrules.impl.FirstTrueRulesetExecutorImpl;
import cl.motoratrib.jsrules.impl.FirstTrueRulesetListExecutorImpl;
import cl.motoratrib.jsrules.impl.BooleanArrayExecutorImpl;
import cl.motoratrib.jsrules.impl.ChainTrueRulesetListExecutorImpl;
import java.util.List;

/**
 * Created by Marcelo Lomeña 5/14/2018
 */
public enum RulesetTypeHandler {
    ALLTRUE {
        @Override
        @SuppressWarnings("unchecked")
        public RulesetExecutor getRulesetExecutor(String name, String type, List<Executor> ruleSet, Object response) {
            return new AllTrueRulesetExecutorImpl(name, type, ruleSet, response);
        }

        @Override
        public boolean isRulesetListExecutor() {
            return false;
        }
    },
    FIRSTTRUE {
        @Override
        @SuppressWarnings("unchecked")
        public RulesetExecutor getRulesetExecutor(String name, String type, List<Executor> ruleSet, Object response) {
            return new FirstTrueRulesetExecutorImpl(name, type, ruleSet);
        }

        @Override
        public boolean isRulesetListExecutor() {
            return false;
        }
    },
    BOOLEANARRAY {
        @Override
        @SuppressWarnings("unchecked")
        public RulesetExecutor getRulesetExecutor(String name, String type, List<Executor> ruleSet, Object response) {
            return new BooleanArrayExecutorImpl(name, type, ruleSet, response);
        }

        @Override
        public boolean isRulesetListExecutor() {
            return false;
        }
    },
    ALLTRUELIST {
        @Override
        @SuppressWarnings("unchecked")
        public RulesetExecutor getRulesetExecutor(String name, String type, List<Executor> ruleSet, Object response) {
            return new AllTrueRulesetListExecutorImpl(name, type, ruleSet, response);
        }

        @Override
        public boolean isRulesetListExecutor() {
            return true;
        }
    },
    FIRSTTRUELIST {
        @Override
        @SuppressWarnings("unchecked")
        public RulesetExecutor getRulesetExecutor(String name, String type, List<Executor> ruleSet, Object response) {
            return new FirstTrueRulesetListExecutorImpl(name, type, ruleSet);
        }

        @Override
        public boolean isRulesetListExecutor() {
            return true;
        }
    },
    CHAINTRUELIST {
        @Override
        @SuppressWarnings("unchecked")
        public RulesetExecutor getRulesetExecutor(String name, String type, List<Executor> ruleSet, Object response) {
            return new ChainTrueRulesetListExecutorImpl(name, type, ruleSet);
        }

        @Override
        public boolean isRulesetListExecutor() {
            return true;
        }
    };

    public abstract RulesetExecutor getRulesetExecutor(String name, String type, List<Executor> ruleSet, Object response);

    public abstract boolean isRulesetListExecutor();
}
