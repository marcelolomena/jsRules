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

import cl.motoratrib.jsrules.RulesetExecutor;
import cl.motoratrib.jsrules.RulesetListExecutor;
import cl.motoratrib.jsrules.exception.InvalidParameterException;

import java.util.List;
import java.util.Map;

/**
 * This executor evaluates a series of rulesets in order.
 * <p/>
 * If all rulesets evaluate as true, it returns the given response. Otherwise, the
 * response is null.
 *
 * @param <T>
 * @author Marcelo
 */
public class AllTrueRulesetListExecutorImpl<T> extends RulesetListExecutor<T> {
    private final List<RulesetExecutor> rulesetList;
    private final T response;
    private final String name;
    private final String type;

    public AllTrueRulesetListExecutorImpl(String name, String type, List<RulesetExecutor> rulesetList, T response) {
        this.name = name;
        this.type = type;
        this.rulesetList = rulesetList;
        this.response = response;
    }

    @Override
    public T execute(Map<String, Object> parameters) throws InvalidParameterException {
        T result = response;
        for (RulesetExecutor ruleSet : rulesetList) {
            if (ruleSet.execute(parameters) == null) {
                result = null;
                break;
            }
        }
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
