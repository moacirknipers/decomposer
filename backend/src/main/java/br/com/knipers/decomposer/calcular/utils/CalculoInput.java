package br.com.knipers.decomposer.calcular.utils;

import br.com.knipers.decomposer.variavelformula.VariavelFormula;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoInput {

    private VariavelFormula variavel;
    private List<Params> params;

}
