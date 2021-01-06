package br.com.knipers.decomposer.variavelformula.enums;

public enum TipoVariavel {

    CALCULO("CÁLCULO"),
    CAMPO("CAMPO");

    private final String descricao;

    TipoVariavel(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
