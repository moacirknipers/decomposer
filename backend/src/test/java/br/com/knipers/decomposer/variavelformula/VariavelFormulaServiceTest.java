package br.com.knipers.decomposer.variavelformula;

import br.com.knipers.decomposer.tipodecalculo.TipoDeCalculo;
import br.com.knipers.decomposer.tipodecalculo.TipoDeCalculoService;
import br.com.knipers.decomposer.variavelformula.enums.TipoDado;
import br.com.knipers.decomposer.variavelformula.enums.TipoVariavel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class VariavelFormulaServiceTest {

    @Mock
    private IVariavelFormulaRepository iVariavelFormulaRepository;

    @Mock
    private TipoDeCalculoService tipoDeCalculoService;

    @InjectMocks
    private VariavelFormulaService variavelFormulaService;

    @Mock
    private VariavelUtil variavelUtil;

    @Test
    public void save() {
        VariavelFormulaDTO variavelFormulaDTO = getVariavelFormulaDTO();

        when(tipoDeCalculoService.findBy(any())).thenReturn(new TipoDeCalculo("ICMS"));
        when(iVariavelFormulaRepository.save(any())).thenReturn(getVariavelFormula());
        ResponseEntity variavelFormulaEntity = variavelFormulaService.save(variavelFormulaDTO);

        verify(iVariavelFormulaRepository, times(1))
                .save(any(VariavelFormula.class));

        Assert.assertEquals(HttpStatus.OK, variavelFormulaEntity.getStatusCode());
    }

    @Test
    public void findVariavel_ShouldReturnVariavel() {
        String id = "123";

        when(iVariavelFormulaRepository.findById(id)).thenReturn(Optional.of(getVariavelFormula()));
        VariavelFormula variavelFound = this.variavelFormulaService.findBy(id);
        assertThat(variavelFound).isNotNull();
    }

    @Test
    public void update() {

        VariavelFormulaDTO variavelFormulaDTO = getVariavelFormulaDTO();
        when(tipoDeCalculoService.findBy(any())).thenReturn(new TipoDeCalculo("ICMS"));
        when(iVariavelFormulaRepository.save(any(VariavelFormula.class))).thenReturn(getVariavelFormula());
        when(iVariavelFormulaRepository.findById(any(String.class))).thenReturn(Optional.of(new VariavelFormula()));

        ResponseEntity update = this.variavelFormulaService.update("", variavelFormulaDTO);

        assertThat(update).isNotNull();
        assertThat(update.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    private VariavelFormula getVariavelFormula() {
        return new VariavelFormula(
                "valBaseICMS",
                true,
                "a + b",
                TipoVariavel.CALCULO,
                TipoDado.INTEIRO,
                "Teste Unitario",
                5,
                5,
                new TipoDeCalculo("ICMS")
        );
    }

    private VariavelFormulaDTO getVariavelFormulaDTO() {
        return new
                VariavelFormulaDTO(
                "",
                "a + b",
                "valBaseICMS",
                BigDecimal.ZERO,
                TipoVariavel.CALCULO,
                TipoDado.INTEIRO,
                "Teste Unitario",
                5,
                5,
                "ICMS"
                );
    }
}
