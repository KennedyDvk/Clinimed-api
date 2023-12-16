package br.com.clinimed.controller;

import br.com.clinimed.medico.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    //No método de POST deve-se usar o procolo http "201 created" mas devemos devolver também um cabeçalho do
    //protocolo http, esse protocolo chama-se (Location)
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {
        var medico = new Medico(dados);
        repository.save(medico);
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));

    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
    }

    //Detalhar um Médico Especifico por ID
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    // Listar Médicos por Nome
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Page<DadosListagemMedico>> listarPorNome(@PathVariable String nome, @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByNomeContainingAndAtivoTrue(nome, paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
    }

    //Listar Médicos por Especialidade
    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<Page<DadosListagemMedico>> listarPorEspecialidade(
            @PathVariable Especialidade especialidade,
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        List<Medico> medicos = repository.findAllByEspecialidadeAndAtivoTrue(especialidade, paginacao);
        List<DadosListagemMedico> dadosListagem = medicos.stream()
                .map(medico -> new DadosListagemMedico(medico))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PageImpl<>(dadosListagem, paginacao, medicos.size()));
    }


    @PutMapping
    @Transactional
    public ResponseEntity atualizar (@RequestBody @Valid DadosAtualicaoMedico dados) {
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

//    Ao colocar o metodo como "void" o spring usa o protocolo http padrão que é o codigo 200, como boas práticas deve se usar o codigo correto
//    no caso de delete é "204 No Content"
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        medico.excluir();

        return ResponseEntity.noContent().build();
    }

}
