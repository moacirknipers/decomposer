package br.com.knipers.decomposer.calcular;

import br.com.knipers.decomposer.calcular.utils.CalculoInput;
import br.com.knipers.decomposer.calcular.utils.CalculoOutput;
import br.com.knipers.decomposer.calcular.utils.Params;
import br.com.knipers.decomposer.groovy.ScriptRunner;
import br.com.knipers.decomposer.variavelformula.VariavelFormula;
import br.com.knipers.decomposer.variavelformula.VariavelUtil;
import br.com.knipers.decomposer.variavelformula.enums.TipoDado;
import br.com.knipers.decomposer.variavelformula.enums.TipoVariavel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class CalcularServiceTest {

    @Spy
    ScriptRunner runner;

    @InjectMocks
    CalcularService calcularService;

    @Spy
    VariavelUtil variavelUtil;

    @Test
    public void testeVariavelNaoCalculada() {
        List<Params> parametros = new ArrayList<>();
        Params vBCIpi = new Params("vBCIpi", 100.0);
        parametros.add(vBCIpi);

        VariavelFormula formula = new VariavelFormula("vBCIpi", false, "vBCIpi", TipoVariavel.CAMPO, TipoDado.DECIMAL, "Valor bc ipi", 10, 4, null);

        CalculoInput input = new CalculoInput(formula, parametros);
        doAnswer(i -> {
            Map<String, VariavelFormula> formulas= i.getArgument(1);
            Map<String, VariavelFormula> variables = i.getArgument(2);
            formulas.put("vBCIpi", formula);
            variables.put("vBCIpi", new VariavelFormula("vBCIpi", false, "vBCIpi"));
            return null;
        } ).when(variavelUtil).getFormulasAndVariables(any(),any(),any());
        CalculoOutput x = calcularService.calc(input);
        Optional<VariavelFormula> result = x.getOutput().stream().filter(c -> c.getNome().equals("vBCIpi")).findFirst();
        Assert.assertEquals(BigDecimal.valueOf(100.0), result.orElse(null).getCalculatedValue());
    }


    @Test
    public void testeCalculoNormal() {
        List<Params> parametros = new ArrayList<>();
        Params vBCIcms = new Params("vBCIcms", 100.0);
        Params pIcms = new Params("pIcms", 0.17);
        parametros.add(vBCIcms);
        parametros.add(pIcms);

        VariavelFormula formula = new VariavelFormula("vIcms", true, "vBCIcms * pIcms", TipoVariavel.CALCULO, TipoDado.DECIMAL, "Valor icms", 10, 4, null);

        CalculoInput input = new CalculoInput(formula, parametros);
        doAnswer(i -> {
            Map<String, VariavelFormula> formulas= i.getArgument(1);
            Map<String, VariavelFormula> variables = i.getArgument(2);
            formulas.put("vIcms", formula);
            variables.put("vBCIcms", new VariavelFormula("vBCIcms", false, "vBCIcms"));
            variables.put("pIcms", new VariavelFormula("pIcms", false, "pIcms"));
            return null;
        } ).when(variavelUtil).getFormulasAndVariables(any(),any(),any());

        CalculoOutput x = calcularService.calc(input);
        Optional<VariavelFormula> result = x.getOutput().stream().filter(c -> c.getNome().equals("vIcms")).findFirst();
        Assert.assertEquals(BigDecimal.valueOf(17.0), result.orElse(null).getCalculatedValue());
    }

    @Test
    public void testeCalculoComposto() {
        List<Params> parametros = new ArrayList<>();
        Params vBCIcms = new Params("vBCIcms", 100.0);
        Params pIcms = new Params("pIcms", 0.17);
        Params vBCIpi = new Params("vBCIpi", 100.0);
        Params pIpi = new Params("pIpi", 0.045);
        Params valorLiquido = new Params("valorLiquido", 110);
        parametros.add(vBCIcms);
        parametros.add(pIcms);
        parametros.add(vBCIpi);
        parametros.add(pIpi);
        parametros.add(valorLiquido);
        VariavelFormula formula = new VariavelFormula("precoFinal", true, "valorLiquido + vIcms + vIpi", TipoVariavel.CALCULO, TipoDado.DECIMAL, "Valor liquido", 10, 4, null);
        CalculoInput input = new CalculoInput(formula, parametros);
        doAnswer(i -> {
            Map<String, VariavelFormula> formulas= i.getArgument(1);
            Map<String, VariavelFormula> variables = i.getArgument(2);
            variables.put("vBCIcms", new VariavelFormula("vBCIcms", false, "vBCIcms"));
            variables.put("pIcms", new VariavelFormula("pIcms", false, "pIcms"));
            formulas.put("vIcms", new VariavelFormula("vIcms", true, "vBCIcms * pIcms"));
            variables.put("vBCIpi", new VariavelFormula("vBCIpi", false, "vBCIpi"));
            variables.put("pIpi", new VariavelFormula("pIpi", false, "pIpi"));
            formulas.put("vIpi", new VariavelFormula("vIpi", true, "vBCIpi * pIpi"));
            variables.put("valorLiquido", new VariavelFormula("valorLiquido", false, "valorLiquido"));
            formulas.put("precoFinal", formula);
            return null;
        } ).when(variavelUtil).getFormulasAndVariables(any(),any(),any());
        CalculoOutput x = calcularService.calc(input);
        Optional<VariavelFormula> result = x.getOutput().stream().filter(c -> c.getNome().equals("precoFinal")).findFirst();
        Assert.assertEquals(BigDecimal.valueOf(131.5), result.orElse(null).getCalculatedValue());
    }
}
