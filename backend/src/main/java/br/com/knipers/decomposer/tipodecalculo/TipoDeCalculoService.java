package br.com.knipers.decomposer.tipodecalculo;

import br.com.knipers.decomposer.commons.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDeCalculoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoDeCalculoService.class);
    private final ITipoDeCalculoRepository iTipoDeCalculoRepository;

    public TipoDeCalculoService(ITipoDeCalculoRepository iTipoDeCalculoRepository) {
        this.iTipoDeCalculoRepository = iTipoDeCalculoRepository;
    }

    public ResponseEntity save(TipoDeCalculoDTO tipoDeCalculoDTO) {

        TipoDeCalculo tipoCalculoDB = this.iTipoDeCalculoRepository.findByNome(tipoDeCalculoDTO.getNome());

        if (tipoCalculoDB == null) {
            TipoDeCalculo tipoDeCalculo = TipoDeCalculo.builder().nome(tipoDeCalculoDTO.getNome()).build();

            tipoDeCalculo = this.iTipoDeCalculoRepository.save(tipoDeCalculo);

            return ResponseEntity.ok(tipoDeCalculo);
        } else {
            String message = String.format("Uma tipo de calculo com o nome %s já existe, favor verificar", tipoDeCalculoDTO.getNome());
            LOGGER.debug(message);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(message));
        }
    }

    public TipoDeCalculo findBy(String id) {
        return this.iTipoDeCalculoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id não encontrado"));
    }

    public List<TipoDeCalculo> findAll() {
        return this.iTipoDeCalculoRepository.findAll();
    }
}
