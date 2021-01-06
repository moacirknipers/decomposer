package br.com.knipers.decomposer.variavelformula;

import br.com.knipers.decomposer.variavelformula.enums.TipoDado;
import br.com.knipers.decomposer.variavelformula.enums.TipoVariavel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class VariavelFormulaDTO {

    private String id;
    @Size(min = 3)
    @NotBlank(message = "Variável é obrigatória")
    private String nome;
    @Size(min = 3)
    private String formula;
    private BigDecimal calculatedValue = BigDecimal.ZERO;
    @NotNull(message = "Tipo de Variável é obrigatório")
    private TipoVariavel tipoVariavel;
    @NotNull(message = "Tipo de dado é obrigatório")
    private TipoDado tipoDado;
    private String descricao;
    private int tamanho;
    @Max(value = 9, message = "As variáveis devem conter no máximo 9 decimais")
    private int decimais;
    private String tipoDeCalculoId;

    public static VariavelFormulaDTO of(VariavelFormula variavelFormula) {
        return new VariavelFormulaDTO(
                variavelFormula.getId(),
                variavelFormula.getNome(),
                variavelFormula.getFormula(),
                variavelFormula.getCalculatedValue(),
                variavelFormula.getTipoVariavel(),
                variavelFormula.getTipoDado(),
                variavelFormula.getDescricao(),
                variavelFormula.getTamanho(),
                variavelFormula.getDecimais(),
                variavelFormula.getTipoDeCalculo().getId()
        );
    }
}
