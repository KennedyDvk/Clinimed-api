package br.com.clinimed.controller;

import br.com.clinimed.domain.paciente.Paciente;
import br.com.clinimed.domain.paciente.PacienteRpository;
import br.com.clinimed.domain.paciente.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    PacienteRpository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@Valid @RequestBody DadosCadastroPaciente dados) {

        repository.save(new Paciente(dados));
        System.out.println(dados);

    }

//Cadastro de varios pacientes de uma vez para o ambiente de teste
    @PostMapping("/cadastrar")
    @Transactional
    public void cadastrar(@Valid @RequestBody List<DadosCadastroPaciente> dadosList) {
        for (DadosCadastroPaciente dados : dadosList) {
            repository.save(new Paciente(dados));
            System.out.println(dados);
        }
    }

    @PutMapping
    @Transactional
    public void atualizar (@Valid @RequestBody DadosAtualicaoPaciente dados) {
        var paciente = repository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);
    }

    @GetMapping
    public Page<DadosListagemPaciente> listar(@PageableDefault(size = 10, sort = {"nome"})Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemPaciente::new);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id) {
        repository.deleteById(id);
    }


}
