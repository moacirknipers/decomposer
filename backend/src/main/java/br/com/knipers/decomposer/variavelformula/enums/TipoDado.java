package br.com.knipers.decomposer.variavelformula.enums;

public enum TipoDado {

    INTEIRO("INTEIRO"),
    DECIMAL("DECIMAL"),
    TEXTO("TEXTO");

    TipoDado(String descricao) {
        this.descricao = descricao;
    }

    private String descricao;
}
