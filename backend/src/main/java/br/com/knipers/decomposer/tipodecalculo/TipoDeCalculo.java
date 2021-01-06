package br.com.knipers.decomposer.tipodecalculo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "tipoDeCalculo")
public class TipoDeCalculo {

    @Id
    private String id;

    private String nome;

    public TipoDeCalculo(String nome) {
        this.nome = nome;
    }
}
