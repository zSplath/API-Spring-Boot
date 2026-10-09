package med.clift.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.clift.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicos")
@SecurityRequirement(name = "bearer-key")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<MedicoAtualDTO> cadastrar(@RequestBody @Valid MedicoDTO dados, UriComponentsBuilder uriBuilder) {
        var medico = new Medico(dados);
        repository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new MedicoAtualDTO(medico));
    }

    @GetMapping
    public ResponseEntity<Page<ListagemMedicoDTO>> listar(@PageableDefault(size = 5, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(ListagemMedicoDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<MedicoAtualDTO> atualizar(@RequestBody @Valid AtualizarMedicolDTO dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new MedicoAtualDTO(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<MedicoAtualDTO> excluir(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        medico.inativar();

//        return ResponseEntity.noContent().build();
        return ResponseEntity.ok(new MedicoAtualDTO(medico));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoAtualDTO> detalhar(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);

        return ResponseEntity.ok(new MedicoAtualDTO(medico));
    }
}
