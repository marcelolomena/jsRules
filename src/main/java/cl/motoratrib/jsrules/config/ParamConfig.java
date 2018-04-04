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
package cl.motoratrib.jsrules.config;

import cl.motoratrib.jsrules.util.JsonBean;

/**
 *
 * @author Marcelo
 */
public class ParamConfig extends JsonBean implements Config  {
    private String parameterName;
    private String parameterClass;
    private String parameterStaticValue;

    public ParamConfig() {

    }

    public ParamConfig(String parameterName, String parameterClass, 
            String parameterStaticValue) {
        this.parameterName = parameterName;
        this.parameterClass = parameterClass;
        this.parameterStaticValue = parameterStaticValue;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterClass() {
        return parameterClass;
    }

    public void setParameterClass(String parameterClass) {
        this.parameterClass = parameterClass;
    }

    public String getParameterStaticValue() {
        return parameterStaticValue;
    }

    public void setParameterStaticValue(String parameterStaticValue) {
        this.parameterStaticValue = parameterStaticValue;
    }
    
}
