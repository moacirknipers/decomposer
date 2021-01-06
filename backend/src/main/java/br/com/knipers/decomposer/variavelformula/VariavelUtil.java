package br.com.knipers.decomposer.variavelformula;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

@Component
public class VariavelUtil {

    @Autowired
    private IVariavelFormulaRepository iVariavelFormulaRepository;

    public static final String CIRCULAR_REFERENCE_MESSAGE = "Existe uma referência circular entre as fórmulas do cálculo.";

    public void validateCircularDependency(VariavelFormula formula) {
        Map<String, VariavelFormula> formulas = new HashMap<>();
        Map<String, VariavelFormula> variables = new HashMap<>();
        if (nonNull(formula)) {
            List<String> variaveisFormulaPrincipal = getVariablesInFormula(formula.getFormula());
            for (String item: variaveisFormulaPrincipal) {
                VariavelFormula validateVar = getVariavelFormula(item);
                if (validateVar.isCalculada()) {
                    formulas.put(validateVar.getNome(), validateVar);
                    List<String> variablesInFormula = getVariablesInFormula(validateVar.getFormula());
                    getFormulaChain(formulas, variables, variablesInFormula);
                    TreeMap<Integer, String> hierarchy = new TreeMap<>();
                    int maxSeqDependency = 9999;
                    validateCircularReference(item, formulas, hierarchy, maxSeqDependency);
                }
                formulas.clear();
                variables.clear();
            }
        }
    }

    public void validateCircularReference(String keyCalc, Map<String, VariavelFormula> formulas, TreeMap<Integer, String> hierarchy, int sequence) throws ResponseStatusException {
        String formula = formulas.get(keyCalc).getFormula();
        if (Objects.nonNull(formula)) {
            List<String> calulatedVarsInFormula = getCalculatedVarsInFormula(formula, formulas);
            int seqDependency = sequence;
            if (!calulatedVarsInFormula.isEmpty()) {
                for (String calculatedVar : calulatedVarsInFormula) {
                    if (hierarchy.containsValue(calculatedVar))
                        throw new ResponseStatusException(HttpStatus.CONFLICT, CIRCULAR_REFERENCE_MESSAGE);

                    seqDependency--;
                    hierarchy.put(seqDependency, calculatedVar);
                    validateCircularReference(calculatedVar, formulas, hierarchy, seqDependency);
                }
            }
        }
        if (!hierarchy.containsValue(keyCalc)) {
            hierarchy.put(sequence, keyCalc);
        }
    }

    /**
     * As listas devem ser inicializadas externamente.
     * Esta rotina irá buscar todas as variáveis e fórmulas referente a
     * formula principal e adicionar dentro das listas;
     * @param formula Formula principal
     * @param formulas lista que será atribuída as variáveis do tipo calculada que representam uma formula
     * @param variables lista que será atribuída as variáveis do tipo campo que representam um valor vindo externamente
     */
    public void getFormulasAndVariables(VariavelFormula formula, Map<String, VariavelFormula> formulas, Map<String, VariavelFormula> variables) {
        if (nonNull(formula) && nonNull(formulas) && nonNull(variables)){
            formulas.put(formula.getNome(), formula);
            if (formula.isCalculada()) {
                List<String> variablesInFormula = getVariablesInFormula(formula.getFormula());
                getFormulaChain(formulas, variables, variablesInFormula);
            } else {
                variables.put(formula.getNome(), formula);
            }
        }
    }

    /**
     * Rotina que organiza as formulas de cálculo, irá garantir que todas as dependências sejam
     * calculadas antes da fórmula principal. A organização é feito dentro da lista <b>hierarchy</b>.
     * @param keyCalc É a chave do cálculo com base nele que o sistema irá buscar uma formula
     * @param formulas É lista de formulas referente ao cálculo
     * @param hierarchy É lista hierarquica das fórmulas, ou seja, as fórmulas na sequencia que deve ser calculada.
     * @param sequence Controle de hierarquia da fórmula, inicia no número mais alto e vai decrementando.
     */
    public void getHierarchyCalc(String keyCalc, Map<String, VariavelFormula> formulas, TreeMap<Integer, String> hierarchy, int sequence) {
        String formula = formulas.get(keyCalc).getFormula();
        if (Objects.nonNull(formula)) {
            List<String> calulatedVarsInFormula = getCalculatedVarsInFormula(formula, formulas);
            int seqDependency = sequence;
            if (!calulatedVarsInFormula.isEmpty()) {
                for (String calculatedVar : calulatedVarsInFormula) {
                    if (!hierarchy.containsValue(calculatedVar)) {
                        seqDependency--;
                        hierarchy.put(seqDependency, calculatedVar);
                        getHierarchyCalc(calculatedVar, formulas, hierarchy, seqDependency);
                    }
                }
            }
        }
        if (!hierarchy.containsValue(keyCalc)) {
            hierarchy.put(sequence, keyCalc);
        }
    }

    /**
     * A rotina pega a fórmula e quebra em variáveis. Após quebrar em variáveis a função verifica dentro da lista de
     * fórmulas se alguma variável corresponde a outra formula, se corresponder inclui na lista, senão ignora.
     * @param formula Fórmula
     * @param formulas Lista de fórmulas do cálculo
     * @return lista de variáveis calculadas dentro da fórmula
     */
    private List<String> getCalculatedVarsInFormula(String formula, Map<String, VariavelFormula> formulas) {
        List<String> result = new ArrayList<>();
        List<String> variables = getVariablesInFormula(formula);
        for (Map.Entry<String, VariavelFormula> item: formulas.entrySet()) {
            if (item.getValue().isCalculada() && variables.contains(item.getValue().getNome())) {
                result.add(item.getValue().getNome());
            }
        }
        return result;
    }

    public List<String> getVariablesInFormula(String formula) {
        List<String> variables = new ArrayList<>();
        if (Objects.nonNull(formula)) {
            String[] elements = getElements(formula);
            for (String element : elements) {
                if (!isNumeric(element) && !(element.isEmpty() || element.isBlank())) {
                    if ((!variables.contains(element))) {
                        variables.add(element);
                    }
                }
            }
        }
        return variables;
    }

    private String[] getElements(String formula) {
        return formula.replaceAll("[^a-zA-Z0-9]", " ").split(" ");
    }

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public Map<String, VariavelFormula> getFormulaMapToCalc(String formulaKey) {
        VariavelFormula foundFormula = this.iVariavelFormulaRepository.findByNome(formulaKey);
        if(foundFormula != null) {
            Map<String, VariavelFormula> formula = new HashMap<>();
            formula.put(foundFormula.getNome(), foundFormula);
            return formula;
        }
        throw new IllegalArgumentException("Todas as variáveis inseridas na fórmula devem estar previamente cadastradas");
    }

    public VariavelFormula getVariavelFormula(String key) {
        VariavelFormula foundFormula = this.iVariavelFormulaRepository.findByNome(key);
        if(foundFormula != null) {
            return foundFormula;
        }
        throw new IllegalArgumentException("Todas as variáveis inseridas na fórmula devem estar previamente cadastradas");
    }

    /**
     * Essa rotina faz uma varredura na formula principal e se encontrar dependencias de outras fórmulas, irá recursivamente
     * alimentar a lista <b>formulas</b>, caso contrário irá atribuir a variável na lista <b>variables</b>
     * @param formulas Lista de fórmulas do cálculo
     * @param variables Lista de variáveis do cálculo
     * @param variablesFormula Lista de variáveis da fórmula corrente
     */
    private void getFormulaChain(Map<String, VariavelFormula> formulas, Map<String, VariavelFormula> variables, List<String> variablesFormula) {
        for (String variable: variablesFormula) {
            Map<String, VariavelFormula> result = this.getFormulaMapToCalc(variable);
            if (result != null && !result.isEmpty()) {
                VariavelFormula obj = result.get(variable);
                if (obj.isCalculada()) {
                    if (!formulas.containsKey(variable)) {
                        formulas.put(variable, obj);
                        List<String> formulaVariables = this.getVariablesInFormula(obj.getFormula());
                        getFormulaChain(formulas, variables, formulaVariables);
                    }
                } else {
                    variables.put(obj.getNome(), obj);
                }
            }
        }
    }
}
