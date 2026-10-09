package med.clift.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.clift.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<PacienteAtualDTO> cadastrar(@RequestBody @Valid PacienteDTO dados, UriComponentsBuilder uriBuilder) {
        var paciente = new Paciente(dados);
        repository.save(paciente);

        var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new PacienteAtualDTO(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<ListagemPacienteDTO>> listar(@PageableDefault(size = 5, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(ListagemPacienteDTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity<PacienteAtualDTO> atualizar(@RequestBody @Valid AtualizarPacienteDTO dados) {
        var paciente = repository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);

        return ResponseEntity.ok(new PacienteAtualDTO(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<PacienteAtualDTO> remover(@PathVariable Long id) {
        var paciente = repository.getReferenceById(id);
        paciente.inativar();

        return ResponseEntity.ok(new PacienteAtualDTO(paciente));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<PacienteAtualDTO> detalhar(@PathVariable Long id) {
        var paciente = repository.getReferenceById(id);

        return ResponseEntity.ok(new PacienteAtualDTO(paciente));
    }
}
