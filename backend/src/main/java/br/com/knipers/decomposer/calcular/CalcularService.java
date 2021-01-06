package br.com.knipers.decomposer.calcular;

import br.com.knipers.decomposer.calcular.utils.CalculoInput;
import br.com.knipers.decomposer.calcular.utils.CalculoOutput;
import br.com.knipers.decomposer.calcular.utils.Params;
import br.com.knipers.decomposer.groovy.ScriptRunner;
import br.com.knipers.decomposer.groovy.Variable;
import br.com.knipers.decomposer.variavelformula.VariavelFormula;
import br.com.knipers.decomposer.variavelformula.VariavelUtil;
import groovy.lang.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;


@Service
public class CalcularService {

    @Autowired
    private ScriptRunner runner;

    @Autowired
    private VariavelUtil variavelUtil;

    public CalculoOutput calc(CalculoInput input) {
        Map<String, VariavelFormula> formulas = new HashMap<>();
        Map<String, VariavelFormula> variables = new HashMap<>();
        variavelUtil.getFormulasAndVariables(input.getVariavel(), formulas, variables);
        return new CalculoOutput(this.calc(input, formulas, variables));
    }

    private List<VariavelFormula> calc(CalculoInput input, Map<String, VariavelFormula> formulas, Map<String, VariavelFormula> variables) {
        TreeMap<Integer, String> orderCalc = getOrderCalc(input.getVariavel().getNome(), formulas);
        for (Map.Entry<Integer, String> entry : orderCalc.entrySet()) {
            VariavelFormula bean = formulas.get(entry.getValue());
            List<Variable> groovyVariables = createGroovyVariables(input, bean, formulas ,variables);
            Script parsedFormula = runner.parse(bean.getFormula());
            Object calculatedValue = runner.executeScript(parsedFormula, groovyVariables);
            bean.setCalculatedValue(BigDecimal.valueOf(((Number) calculatedValue).doubleValue()));
         }
        return new ArrayList<>(formulas.values());
    }

    /**
     * Cria dinamicamente as variáveis para o Groovy efetuar o calculo da fórmula.
     * @param input Dados de entrada da API são utilizado para atribuir valores as variáveis não calculadas.
     * @param bean É o objeto do calculo em questão
     * @param formulas Lista das fórmulas do cálculo
     * @param variables Lista das variáveis do cálculo
     * @return Lista de variáveis do Groovy
     */
    private List<Variable> createGroovyVariables(CalculoInput input, VariavelFormula bean, Map<String, VariavelFormula> formulas, Map<String, VariavelFormula> variables) {
        List<Variable> result = new ArrayList<>();
        List<String> variablesFormulas = this.variavelUtil.getVariablesInFormula(bean.getFormula());
        for (String variableFormula: variablesFormulas) {
            VariavelFormula objVariable = variables.get(variableFormula);
            if (Objects.nonNull(objVariable)) {
               Params param = input.getParams().stream().filter(x -> x.getKey().equals(variableFormula)).findFirst().orElse(null);
               if (Objects.nonNull(param)) {
                   result.add(new Variable(variableFormula, param.getValue()));
               } else  {
                   throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("A variável %s não foi informada", variableFormula));
               }
            } else {
                objVariable = formulas.get(variableFormula);
                if (Objects.nonNull(objVariable)) {
                    result.add(new Variable(variableFormula, objVariable.getCalculatedValue()));
                } else {
                    result.add(new Variable(variableFormula,0));
                }
            }
        }
        return result;
    }

    private TreeMap<Integer, String> getOrderCalc(String keyCalc, Map<String, VariavelFormula> formulas) {
        TreeMap<Integer, String> hierarchy = new TreeMap<>();
        int maxSeqDependency = 9999;
        this.variavelUtil.getHierarchyCalc(keyCalc, formulas, hierarchy, maxSeqDependency);
        return hierarchy;
    }
}

/*
vicms = percentual*bc

totaldosimpostos = vicms + outros impostos



 */