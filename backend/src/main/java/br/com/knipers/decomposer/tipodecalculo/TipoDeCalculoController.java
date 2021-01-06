package br.com.knipers.decomposer.tipodecalculo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tipocalc")
public class TipoDeCalculoController {

    private TipoDeCalculoService tipoDeCalculoService;

    public TipoDeCalculoController(TipoDeCalculoService tipoDeCalculoService) {
        this.tipoDeCalculoService = tipoDeCalculoService;
    }

    @PostMapping
    public ResponseEntity<TipoDeCalculo> save(@RequestBody @Valid TipoDeCalculoDTO tipoDeCalculoDTO) {
        return tipoDeCalculoService.save(tipoDeCalculoDTO);
    }

    @GetMapping("/all")
    public List<TipoDeCalculo> findAll() {
        return this.tipoDeCalculoService.findAll();
    }
}
