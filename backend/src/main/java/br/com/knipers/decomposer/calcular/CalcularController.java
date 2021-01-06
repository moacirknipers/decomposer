package br.com.knipers.decomposer.calcular;

import br.com.knipers.decomposer.calcular.utils.CalculoInput;
import br.com.knipers.decomposer.calcular.utils.CalculoOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/calcular")
public class CalcularController {

    @Autowired
    CalcularService service;

    @PostMapping
    public CalculoOutput retornarCalculo(@RequestBody CalculoInput request) {
        return service.calc(request);
    }

}
