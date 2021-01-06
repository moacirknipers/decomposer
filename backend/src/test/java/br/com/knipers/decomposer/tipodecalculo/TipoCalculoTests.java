package br.com.knipers.decomposer.tipodecalculo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class TipoCalculoTests {

    @Mock
    private ITipoDeCalculoRepository iTipoDeCalculoRepository;

    @InjectMocks
    private TipoDeCalculoService tipoDeCalculoService;

    @Test
    public void save() {

        TipoDeCalculoDTO tipoDeCalculoDTO = new TipoDeCalculoDTO("ICMS");

        Mockito.when(iTipoDeCalculoRepository.findByNome(Mockito.anyString())).thenReturn(null);
        ResponseEntity response = tipoDeCalculoService.save(tipoDeCalculoDTO);

        Mockito.verify(iTipoDeCalculoRepository, Mockito.times(1)).save(Mockito.any(TipoDeCalculo.class));

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }
}
