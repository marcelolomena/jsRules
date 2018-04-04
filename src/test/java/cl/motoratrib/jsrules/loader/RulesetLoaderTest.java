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
package cl.motoratrib.jsrules.loader;

import cl.motoratrib.jsrules.JsRules;
import cl.motoratrib.jsrules.RulesetExecutor;
import cl.motoratrib.jsrules.config.ResponseConfig;
import cl.motoratrib.jsrules.config.RulesetConfig;
import cl.motoratrib.jsrules.exception.InvalidConfigException;
import cl.motoratrib.jsrules.impl.FirstTrueRulesetExecutorImpl;
import cl.motoratrib.jsrules.impl.FirstTrueRulesetListExecutorImpl;
import cl.motoratrib.jsrules.loader.impl.RulesetLoaderImpl;
import cl.motoratrib.jsrules.util.ClassHandler;
import cl.motoratrib.jsrules.util.RulesetTypeHandler;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Marcelo
 */
public class RulesetLoaderTest {
    @org.junit.Rule
    public ExpectedException exception = ExpectedException.none();

    private final String rulesetName = "MockRuleset";
    private final String rulesetType = "FirstTrue";

    private RulesetConfig rulesetConfig;

    @InjectMocks
    private RulesetLoaderImpl rulesetLoader;

    @Mock
    private JsRules jsRulesMock;

    public RulesetLoaderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        rulesetConfig = new RulesetConfig(rulesetName, rulesetType, getResponseConfig(), getComponents());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void loadFromRulesetConfigTest() throws Exception {
        RulesetExecutor rulesetExecutor = rulesetLoader.load(rulesetConfig);

        assertTrue(rulesetExecutor instanceof FirstTrueRulesetExecutorImpl);
    }

    @Test
    public void loadFromRulesetConfigMissingTypeTest() throws Exception {
        exception.expect(InvalidConfigException.class);

        rulesetConfig.setRulesetType(null);

        rulesetLoader.load(rulesetConfig);
    }

    @Test
    public void loadFromRulesetConfigInvalidResponseClassTest() throws Exception {
        exception.expect(InvalidConfigException.class);

        rulesetConfig.getResponseConfig().setResponseClass("bogus");

        rulesetLoader.load(rulesetConfig);
    }

    @Test
    public void loadFromRulesetConfigInvalidResponseTest() throws Exception {
        exception.expect(InvalidConfigException.class);

        rulesetConfig.getResponseConfig().setResponseClass("longset");
        rulesetConfig.getResponseConfig().setResponse("not a long set");

        rulesetLoader.load(rulesetConfig);
    }

    @Test
    public void classHandlerExceptionTest() throws Exception {
        exception.expect(InvalidConfigException.class);

        rulesetConfig.getResponseConfig().setResponseClass(ClassHandler.DATETIME.name());
        rulesetConfig.getResponseConfig().setResponse("bogus");

        rulesetLoader.load(rulesetConfig);
    }

    @Test
    public void rulesetListExecutorTypeTest() throws Exception {
        rulesetConfig = new RulesetConfig(rulesetName, RulesetTypeHandler.FIRSTTRUELIST.name(), getResponseConfig(),
                getComponents());

        RulesetExecutor rulesetExecutor = rulesetLoader.load(rulesetConfig);

        assertTrue(rulesetExecutor instanceof FirstTrueRulesetListExecutorImpl);
    }

    private ResponseConfig getResponseConfig() {
        String responseString = "true";
        String responseClass = "Boolean";

        return new ResponseConfig(responseString, responseClass);
    }

    private List<String> getComponents() {
        List<String> components = new ArrayList<>();

        components.add("rule1");
        components.add("rule2");

        return components;
    }
}
