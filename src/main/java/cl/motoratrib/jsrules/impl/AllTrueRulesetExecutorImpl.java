/*
 * The MIT License
 *
 * Copyright 2018 Marcelo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cl.motoratrib.jsrules.impl;

import cl.motoratrib.jsrules.Parameter;
import cl.motoratrib.jsrules.RuleExecutor;
import cl.motoratrib.jsrules.RulesetExecutor;
import cl.motoratrib.jsrules.exception.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

/**
 *
 * Este ejecutor evalúa una serie de reglas en orden.
 * 
 * Si todas las reglas se evalúan como verdaderas, devuelve la respuesta dada. De lo contrario, la respuesta es nula.
 * 
 * @author Marcelo
 * @param <T>
 */
public class AllTrueRulesetExecutorImpl<T> extends RulesetExecutor<T> {
    private final static Logger LOGGER = LoggerFactory.getLogger(AllTrueRulesetExecutorImpl.class);

    private final List<RuleExecutor> ruleSet;
    private String type;
    private final T response;
    private String name;

    public AllTrueRulesetExecutorImpl(String name, String type, List<RuleExecutor> ruleSet, T response) {
        this.name = name;
        this.type = type;
        this.ruleSet = ruleSet;
        this.response = response;
    }
    
    @Override
    public T execute(Map<String, Object> parameters) throws InvalidParameterException {
        T result = response;
        for(RuleExecutor rule:ruleSet) {
            Parameter ruleParamRight = rule.getRightParameter();
            Object leftParameter = parameters.get(rule.getLeftParameter().getName());
            Object rightParameter = parameters.get(ruleParamRight.getName());

            if (ruleParamRight.getStaticValue() == null) {
                // verifique ambos parámetros - las verificaciones de reglas fallidas devuelven nulo
                if (rule.execute(leftParameter, rightParameter) == null) {
                    result = null;
                    break;
                }
            } else {
                // verifique solo el parámetro izquierdo - las verificaciones de reglas fallidas devuelven nulo
                if (rule.execute(leftParameter) == null) {
                    result = null;
                    break;
                }
            }
        }
        //LOGGER.debug("cool? : " + result);

        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

}
