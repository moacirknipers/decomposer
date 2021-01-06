package br.com.knipers.decomposer.variavelformula;

import br.com.knipers.decomposer.tipodecalculo.TipoDeCalculo;
import br.com.knipers.decomposer.variavelformula.enums.TipoDado;
import br.com.knipers.decomposer.variavelformula.enums.TipoVariavel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Document(collection = "variavel_formula")
public class VariavelFormula {

    @Id
    private String id;
    private String nome;
    private boolean calculada;
    private String formula;
    private BigDecimal calculatedValue = BigDecimal.ZERO;
    private TipoVariavel tipoVariavel;
    private TipoDado tipoDado;
    private String descricao;
    private int tamanho;
    private int decimais;
    private TipoDeCalculo tipoDeCalculo;

    public VariavelFormula(String nome, boolean calculada, String formula) {
        this.nome = nome;
        this.calculada = calculada;
        this.formula = formula;

    }

    public VariavelFormula(String nome, boolean calculada, String formula, TipoVariavel tipoVariavel, TipoDado tipoDado, String descricao, int tamanho, int decimais, TipoDeCalculo tipoDeCalculo) {
        this.nome = nome;
        this.calculada = calculada;
        this.formula = formula;
        this.tipoVariavel = tipoVariavel;
        this.tipoDado = tipoDado;
        this.descricao = descricao;
        this.tamanho = tamanho;
        this.decimais = decimais;
        this.tipoDeCalculo = tipoDeCalculo;
    }

}
