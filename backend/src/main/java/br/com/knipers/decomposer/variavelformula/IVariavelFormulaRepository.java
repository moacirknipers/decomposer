package br.com.knipers.decomposer.variavelformula;

import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface IVariavelFormulaRepository extends MongoRepository<VariavelFormula, String>, QuerydslPredicateExecutor<VariavelFormula>, QuerydslBinderCustomizer<QVariavelFormula> {
    boolean existsByNome(String nome);

    VariavelFormula findByNome(String nome);

    Page<VariavelFormula> findAllByTipoDeCalculo_IdAndNomeLike(String grupo, Pageable pageable, String nome);

    @Override
    default public void customize(QuerydslBindings bindings, QVariavelFormula root) {
        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(root.calculatedValue);
    }
}
