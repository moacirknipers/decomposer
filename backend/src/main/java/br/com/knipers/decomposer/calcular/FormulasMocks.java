package br.com.knipers.decomposer.calcular;

import br.com.knipers.decomposer.variavelformula.VariavelFormula;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FormulasMocks {

    public Map<String, VariavelFormula> findFormulas(String key) {
        return mockVariaveis().entrySet().stream().filter(x -> x.getKey().equals(key))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, VariavelFormula> mockVariaveis() {
        Map<String, VariavelFormula> result = new HashMap<>();
        result.put("taxaMensal", new VariavelFormula("taxaMensal", false, "taxaMensal"));
        result.put("prazoNormal", new VariavelFormula("prazoNormal", false, "prazoNormal"));
        result.put("adicionalPlus", new VariavelFormula("adicionalPlus", false, "adicionalPlus"));
        result.put("vBCIcmsSt", new VariavelFormula("vBCIcmsSt", false, "vBCIcmsSt"));
        result.put("pIcmsSt", new VariavelFormula("pIcmsSt", false, "pIcmsSt"));
        result.put("ttvPraticado", new VariavelFormula("ttvPraticado", false, "ttvPraticado"));
        result.put("pIcms", new VariavelFormula("pIcms", false, "pIcms"));
        result.put("pIpi", new VariavelFormula("pIpi", false, "pIpi"));
        result.put("pFecop", new VariavelFormula("pFecop", false, "pFecop"));
        result.put("pAdf", new VariavelFormula("pAdf", true, "(((taxaMensal / 100) + 1) ** (prazoNormal / 30)) - 1"));
        result.put("pAdfPlus", new VariavelFormula("pAdfPlus", true, "(((adicionalPlus / 100) + 1) ** (prazoNormal / 30)) - 1"));
        result.put("vIcmsSt", new VariavelFormula("vIcmsSt", true, "vBCIcmsSt * pIcmsSt"));
        result.put("formulaP1", new VariavelFormula("formulaP1", true, "ttvPraticado - vIcmsSt"));
        result.put("formulaP2", new VariavelFormula("formulaP2", true, "ttvPraticado - (vIcmsSt - ((1 + pAdf + pAdfPlus) * pIcms)) - pIpi - pFecop - pAdf - pAdfPlus"));
        result.put("precoFabrica", new VariavelFormula("precoFabrica", true, "formulaP1 / (1 - formulaP2 + formulaP1)"));
        return result;
    }
}
