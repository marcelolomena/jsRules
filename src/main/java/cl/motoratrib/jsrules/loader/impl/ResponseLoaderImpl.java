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

import cl.motoratrib.jsrules.config.ResponseConfig;
import cl.motoratrib.jsrules.exception.ClassHandlerException;
import cl.motoratrib.jsrules.exception.InvalidConfigException;
import cl.motoratrib.jsrules.loader.ResponseLoader;
import cl.motoratrib.jsrules.util.ClassHandler;

/**
 *
 * @author Marcelo
 */
public class ResponseLoaderImpl<T> implements ResponseLoader<T> {

    /**
     *
     * @param config
     * @return
     * @throws cl.motoratrib.jsrules.exception.InvalidConfigException
     */
    @Override
    public T load(ResponseConfig config) throws InvalidConfigException {
        if (config == null || config.getResponseClass() == null) {
            throw new InvalidConfigException("Response class must not be null");
        }
        String responseClassName = config.getResponseClass().toUpperCase();
        ClassHandler handler;
        try {
            handler = ClassHandler.valueOf(responseClassName);
        } catch (IllegalArgumentException ex) {
            throw new InvalidConfigException(responseClassName+" is not a supported class", ex);
        }
        
        String responseValue = config.getResponse();
        T response;
        try {
            response = handler.convertString(responseValue);
        } catch (ClassHandlerException|IllegalArgumentException ex) {
            throw new InvalidConfigException(responseValue+" is not a valid "+responseClassName, ex);
        }
        
        return response;
    }

}
