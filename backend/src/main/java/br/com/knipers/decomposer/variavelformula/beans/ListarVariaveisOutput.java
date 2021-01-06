package br.com.knipers.decomposer.variavelformula.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListarVariaveisOutput {
    List<VariavelCampoOutput> variaveis;
    List<FormulaComposicaoOutput> formulasComposicao;
}
