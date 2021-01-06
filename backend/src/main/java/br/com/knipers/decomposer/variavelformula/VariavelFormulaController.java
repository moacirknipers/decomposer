package br.com.knipers.decomposer.variavelformula;

import br.com.knipers.decomposer.variavelformula.beans.ListarVariaveisOutput;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/variavel")
public class VariavelFormulaController {

    @Autowired
    private VariavelFormulaService variavelFormulaService;

    @PostMapping
    public ResponseEntity save(@RequestBody @Valid VariavelFormulaDTO variavelDTO) {
        return this.variavelFormulaService.save(variavelDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") String id, @RequestBody @Valid VariavelFormulaDTO variavelDTO) {
        return this.variavelFormulaService.update(id, variavelDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        return this.variavelFormulaService.deleteBy(id);
    }

    @GetMapping("/{id}")
    public VariavelFormulaDTO findById(@PathVariable("id") String id) {
        return VariavelFormulaDTO.of(this.variavelFormulaService.findBy(id));
    }

    @GetMapping
    public Page<VariavelFormula> findPaginated(Pageable pageable) {
        return this.variavelFormulaService.findPageable(pageable);
    }

    @GetMapping("/list")
    public Page<VariavelFormula> findAll(@QuerydslPredicate(root = VariavelFormula.class) Predicate predicate, Pageable pageable) {
        return this.variavelFormulaService.findAll(predicate, pageable);
    }

    @GetMapping("/find")
    public Page<VariavelFormula> findAllByGrupo(@RequestParam("grupoId") String grupoId, @RequestParam("nome") String nome, Pageable pageable) {
        return this.variavelFormulaService.findAllByTipoAndNomeContaining(pageable, grupoId, nome);
    }

    @PostMapping("/validate")
    public ResponseEntity validateFormula(@RequestBody @Valid VariavelFormulaDTO variavelFormulaDTO) {
        this.variavelFormulaService.validateFormula(variavelFormulaDTO);
        return ResponseEntity.ok("Não há dependência circular e todas as variáveis já estão cadastradas");
    }

    @GetMapping("/{id}/variaveis")
    public ListarVariaveisOutput listarVariaveis(@PathVariable("id") String id) {
        return this.variavelFormulaService.listarVariaveis(id);
    }
}
