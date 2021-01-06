package br.com.knipers.decomposer.tipodecalculo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITipoDeCalculoRepository extends MongoRepository<TipoDeCalculo, String> {

    TipoDeCalculo findByNome(String nome);

}
