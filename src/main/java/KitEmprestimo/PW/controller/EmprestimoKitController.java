package KitEmprestimo.PW.controller;

import KitEmprestimo.PW.entity.EmprestimoKit;
import KitEmprestimo.PW.repository.EmprestimoKitRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kits-maker")
public class EmprestimoKitController {

    private final EmprestimoKitRepository repository;

    public EmprestimoKitController(EmprestimoKitRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<EmprestimoKit>> listarTodos() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoKit> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmprestimoKit> criar(@Valid @RequestBody EmprestimoKit emprestimo) {
        LocalTime agora = LocalTime.now();
        emprestimo.setId(null);
        emprestimo.setHorarioRetirada(agora);
        emprestimo.setHorarioLimiteDevolucao(agora.plusHours(2));
        emprestimo.setHorarioDevolucaoReal(null);
        emprestimo.setStatus("EM_USO");

        EmprestimoKit salvo = repository.save(emprestimo);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmprestimoKit> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmprestimoKit dadosAtualizados) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setCodigoKit(dadosAtualizados.getCodigoKit());
                    existente.setNomeAluno(dadosAtualizados.getNomeAluno());
                    return ResponseEntity.ok(repository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(@PathVariable Long id) {
        var optionalEmprestimo = repository.findById(id);
        if (optionalEmprestimo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        EmprestimoKit emprestimo = optionalEmprestimo.get();
        if (!"EM_USO".equals(emprestimo.getStatus())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensagem", "Este kit já foi devolvido anteriormente."));
        }

        LocalTime horaDevolucao = LocalTime.now();
        emprestimo.setHorarioDevolucaoReal(horaDevolucao);

        if (horaDevolucao.isAfter(emprestimo.getHorarioLimiteDevolucao())) {
            emprestimo.setStatus("DEVOLVIDO_COM_ATRASO");
        } else {
            emprestimo.setStatus("DEVOLVIDO_NO_PRAZO");
        }

        return ResponseEntity.ok(repository.save(emprestimo));
    }
}