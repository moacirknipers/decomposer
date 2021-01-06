package br.com.knipers.decomposer.variavelformula;

import br.com.knipers.decomposer.tipodecalculo.TipoDeCalculoService;
import br.com.knipers.decomposer.variavelformula.beans.FormulaComposicaoOutput;
import br.com.knipers.decomposer.variavelformula.beans.ListarVariaveisOutput;
import br.com.knipers.decomposer.variavelformula.beans.VariavelCampoOutput;
import br.com.knipers.decomposer.variavelformula.enums.TipoVariavel;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class VariavelFormulaService {

    private final IVariavelFormulaRepository iVariavelFormulaRepository;

    private final VariavelUtil variavelUtil;

    private final TipoDeCalculoService tipoDeCalculoService;

    public VariavelFormulaService(IVariavelFormulaRepository iVariavelFormulaRepository, VariavelUtil variavelUtil, TipoDeCalculoService tipoDeCalculoService) {
        this.iVariavelFormulaRepository = iVariavelFormulaRepository;
        this.variavelUtil = variavelUtil;
        this.tipoDeCalculoService = tipoDeCalculoService;
    }

    public ResponseEntity save(VariavelFormulaDTO variavelDTO) {
        if ((!this.iVariavelFormulaRepository.existsByNome(variavelDTO.getNome()))) {
            VariavelFormula variavelToBeSaved = parseVariavelDTOtoEntity(variavelDTO);
            this.variavelUtil.validateCircularDependency(variavelToBeSaved);
            return ResponseEntity.ok(VariavelFormulaDTO.of(this.iVariavelFormulaRepository.save(variavelToBeSaved)));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Variável já cadastrada");
    }

    public VariavelFormula findBy(String id) {
        return this.iVariavelFormulaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Não foi encontrada fórmula com este id: %s", id)));
    }

    public ResponseEntity update(String id, VariavelFormulaDTO variavelDTO) {
        VariavelFormula variavelToBeUpdated = this.findBy(id);
        if ((!this.iVariavelFormulaRepository.existsByNome(variavelDTO.getNome()) || variavelDTO.getNome().equalsIgnoreCase(variavelToBeUpdated.getNome()))) {
            variavelToBeUpdated.setCalculada(variavelDTO.getTipoVariavel().equals(TipoVariavel.CALCULO));
            variavelToBeUpdated.setDecimais(variavelDTO.getDecimais());
            variavelToBeUpdated.setDescricao(variavelDTO.getDescricao());
            variavelToBeUpdated.setFormula(variavelDTO.getFormula());
            variavelToBeUpdated.setTipoDado(variavelDTO.getTipoDado());
            variavelToBeUpdated.setTipoVariavel(variavelDTO.getTipoVariavel());
            variavelToBeUpdated.setTamanho(variavelDTO.getTamanho());
            variavelToBeUpdated.setDecimais(variavelDTO.getDecimais());
            variavelToBeUpdated.setTipoDeCalculo(this.tipoDeCalculoService.findBy(variavelDTO.getTipoDeCalculoId()));
            this.variavelUtil.validateCircularDependency(variavelToBeUpdated);
            return ResponseEntity.ok(VariavelFormulaDTO.of(this.iVariavelFormulaRepository.save(variavelToBeUpdated)));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Variável com este nome já cadastrada");
    }

    public void validateFormula(VariavelFormulaDTO variavelFormulaDTO) {
        this.variavelUtil.validateCircularDependency(this.parseVariavelDTOtoEntity(variavelFormulaDTO));
    }

    public Page<VariavelFormula> findPageable(Pageable pageable) {
        return this.iVariavelFormulaRepository.findAll(pageable);
    }

    public Page<VariavelFormula> findAll(Predicate predicate, Pageable pageable) {
        return this.iVariavelFormulaRepository.findAll(predicate, pageable);
    }

    public ResponseEntity<String> deleteBy(String id) {
        this.iVariavelFormulaRepository.deleteById(id);
        return ResponseEntity.ok("Variável deletada com sucesso");
    }


    private VariavelFormula parseVariavelDTOtoEntity(VariavelFormulaDTO variavelDTO) {
        return new VariavelFormula(
                variavelDTO.getNome(),
                variavelDTO.getTipoVariavel().equals(TipoVariavel.CALCULO),
                variavelDTO.getFormula(),
                variavelDTO.getTipoVariavel(),
                variavelDTO.getTipoDado(),
                variavelDTO.getDescricao(),
                variavelDTO.getTamanho(),
                variavelDTO.getDecimais(),
                this.tipoDeCalculoService.findBy(variavelDTO.getTipoDeCalculoId())
        );
    }

    public Page<VariavelFormula> findAllByTipoAndNomeContaining(Pageable pageable, String grupo, String nome) {
        return this.iVariavelFormulaRepository.findAllByTipoDeCalculo_IdAndNomeLike(grupo, pageable, nome);
    }

    public ListarVariaveisOutput listarVariaveis(String id) {
        VariavelFormula variavelFormula = this.iVariavelFormulaRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Não foi encontrada fórmula com este id: %s", id)));
        Map<String, VariavelFormula> formulas = new HashMap<>();
        Map<String, VariavelFormula> variables = new HashMap<>();
        variavelUtil.getFormulasAndVariables(variavelFormula, formulas, variables);

        List<VariavelCampoOutput> listVar = new ArrayList<>();
        for(Map.Entry<String, VariavelFormula> entry : variables.entrySet()) {
            listVar.add(VariavelCampoOutput.fromEntity(entry.getValue()));
        }
        TreeMap<Integer, String> hierarchy = new TreeMap<>();
        int maxSeqDependency = 9999;
        variavelUtil.getHierarchyCalc(variavelFormula.getNome(), formulas, hierarchy, maxSeqDependency);

        List<FormulaComposicaoOutput> listFormulas = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : hierarchy.entrySet()) {
            VariavelFormula formula = formulas.get(entry.getValue());
            listFormulas.add(new FormulaComposicaoOutput(formula.getNome(), formula.getFormula()));
        }

        return new ListarVariaveisOutput(listVar, listFormulas);
    }
}
