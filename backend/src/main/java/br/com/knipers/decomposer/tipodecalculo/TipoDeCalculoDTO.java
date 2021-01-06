package br.com.knipers.decomposer.tipodecalculo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoDeCalculoDTO {


    @NotEmpty(message = "Preenchimento do nome é obrigatório")
    private String nome;
}
