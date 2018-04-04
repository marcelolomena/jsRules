/*
 * The MIT License
 *
 * Copyright 2015 Marcelo.
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
package cl.motoratrib.jsrules.loader.impl;

import cl.motoratrib.jsrules.Parameter;
import cl.motoratrib.jsrules.config.ParamConfig;
import cl.motoratrib.jsrules.exception.ClassHandlerException;
import cl.motoratrib.jsrules.exception.InvalidConfigException;
import cl.motoratrib.jsrules.loader.ParamLoader;
import cl.motoratrib.jsrules.util.ClassHandler;

/**
 *
 * @author Marcelo
 */
public class ParamLoaderImpl implements ParamLoader {

    /**
     *
     * @param config
     * @return
     * @throws cl.motoratrib.jsrules.exception.InvalidConfigException
     */
    @Override
    public Parameter load(ParamConfig config) throws InvalidConfigException {
        if (config == null || config.getParameterClass() == null) {
            throw new InvalidConfigException("Parameter class must not be null");
        }
        String parameterClassName = config.getParameterClass().toUpperCase();
        ClassHandler handler;
        try {
            handler = ClassHandler.valueOf(parameterClassName);
        } catch (IllegalArgumentException ex) {
            throw new InvalidConfigException(parameterClassName+" is not a supported class", ex);
        }
        Class paramClass = handler.getMyClass();
        String paramStaticValue = config.getParameterStaticValue();
        Object paramStaticObject = null;
        if (paramStaticValue != null) {
            try {
                paramStaticObject = handler.convertString(paramStaticValue);
            } catch (ClassHandlerException|IllegalArgumentException ex) {
                throw new InvalidConfigException(paramStaticValue+" is not a valid "+parameterClassName, ex);
            }
        }
        return new Parameter(config.getParameterName(), paramClass, 
                paramStaticObject);
    }

}
