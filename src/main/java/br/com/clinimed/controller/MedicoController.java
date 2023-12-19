package br.com.clinimed.controller;

import br.com.clinimed.domain.medico.DadosListagemMedico;
import br.com.clinimed.domain.medico.Especialidade;
import br.com.clinimed.domain.medico.Medico;
import br.com.clinimed.domain.medico.MedicoRepository;
import br.com.clinimed.domain.medico.*;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    // Método para cadastrar um novo médico.
    // Ao utilizar o método POST, o protocolo HTTP "201 Created" é apropriado.
    // Além disso, é necessário retornar o cabeçalho HTTP "Location" que indica a URI do recurso recém-criado.
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {

        // Cria uma nova instância de Medico a partir dos dados fornecidos.
        var medico = new Medico(dados);

        // Salva o novo médico no banco de dados.
        repository.save(medico);

        // Constrói a URI para o recurso recém-criado.
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        // Retorna a resposta com o código HTTP 201 Created e os detalhes do novo médico.
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));

    }

    // Utiliza o método findAllByAtivoTrue para recuperar uma lista paginada de médicos ativos.
    // Converte a lista de entidades Medico para uma lista de DTOs DadosListagemMedico.
    // Retorna a resposta com o código HTTP 200 OK e a página de médicos.
    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
    }

    // Detalhar um Médico Especifico por ID
    // Utiliza o método getReferenceById para obter uma referência ao médico pelo ID.
    // Retorna a resposta com o código HTTP 200 OK e os detalhes do médico.
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    // Listar Médicos por Nome
    // Utiliza o método findAllByNomeContainingAndAtivoTrue para recuperar uma lista paginada de médicos ativos por nome.
    // Converte a lista de entidades Medico para uma lista de DTOs DadosListagemMedico.
    // Retorna a resposta com o código HTTP 200 OK e a página de médicos por nome.
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Page<DadosListagemMedico>> listarPorNome(@PathVariable String nome, @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByNomeContainingAndAtivoTrue(nome, paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
    }

    // Listar Médicos por Especialidade
    // Utiliza o método findAllByEspecialidadeAndAtivoTrue para recuperar uma lista paginada de médicos ativos por especialidade.
    // Converte a lista de entidades Medico para uma lista de DTOs DadosListagemMedico.
    // Retorna a resposta com o código HTTP 200 OK e a página de médicos por especialidade.
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

    // Método para atualizar as informações de um médico.
    @PutMapping
    @Transactional
    public ResponseEntity atualizar (@RequestBody @Valid DadosAtualicaoMedico dados) {

        // Obtém uma referência ao médico pelo ID.
        var medico = repository.getReferenceById(dados.id());

        // Chama o método para atualizar as informações do médico com os dados fornecidos.
        medico.atualizarInformacoes(dados);

        // Retorna a resposta com o código HTTP 200 OK e os detalhes atualizados do médico.
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    // Chama o método de exclusão lógica no médico, marcando-o como inativo.
    // Retorna a resposta com o código HTTP 204 No Content para indicar que a operação foi bem-sucedida sem conteúdo.
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var medico = repository.getReferenceById(id);
        medico.excluir();

        return ResponseEntity.noContent().build();
    }

}
