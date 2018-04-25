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
 * Este ejecutor evalúa una serie de reglas en orden, y acumula su valor de verdad.
 *
 * Si todas las reglas se evalúan como verdaderas, devuelve la respuesta dada. De lo contrario, la respuesta es nula.
 *
 * @author Marcelo
 * @param <T>
 */
public class BooleanArrayExecutorImpl<T> extends RulesetExecutor<T> {
    private final static Logger LOGGER = LoggerFactory.getLogger(BooleanArrayExecutorImpl.class);

    private final List<RuleExecutor> ruleSet;
    private final T response;
    private String name;
    private String type;

    public BooleanArrayExecutorImpl(String name, String type, List<RuleExecutor> ruleSet, T response) {
        this.name = name;
        this.type = type;
        this.ruleSet = ruleSet;
        this.response = response;
    }

    @Override
    public T execute(Map<String, Object> parameters) throws InvalidParameterException {
        T result = response;
        String accum = "";
        for(RuleExecutor rule:ruleSet) {
            LOGGER.debug("RULE NAME --------> " + rule.getRule().getRuleName());
            Parameter ruleParamRight = rule.getRightParameter();
            Object leftParameter = parameters.get(rule.getLeftParameter().getName());
            Object rightParameter = parameters.get(ruleParamRight.getName());
            String booleanValue = "";
            if (ruleParamRight.getStaticValue() == null) {
                // verifique ambos parámetros - las verificaciones de reglas fallidas devuelven nulo
                booleanValue=(rule.execute(leftParameter, rightParameter) == null) ? "F" : "V";

            } else {
                // verifique solo el parámetro izquierdo - las verificaciones de reglas fallidas devuelven nulo
                booleanValue=(rule.execute(leftParameter) == null) ? "F" : "V";
            }
            accum += booleanValue;
        }

        result = (T)accum;

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
