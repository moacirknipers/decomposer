package br.com.knipers.decomposer.variavelformula.beans;

import br.com.knipers.decomposer.variavelformula.VariavelFormula;
import br.com.knipers.decomposer.variavelformula.enums.TipoDado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariavelCampoOutput {

    private String id;
    private String nome;
    private TipoDado tipoDado;
    private int tamanho;
    private int decimais;

    public static VariavelCampoOutput fromEntity(VariavelFormula entity) {
        return new VariavelCampoOutput(entity.getId(), entity.getNome(), entity.getTipoDado(),
                entity.getTamanho(), entity.getDecimais());
    }
}
