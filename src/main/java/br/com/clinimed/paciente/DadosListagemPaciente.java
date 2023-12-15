package br.com.clinimed.paciente;

import br.com.clinimed.medico.DadosListagemMedico;

public record DadosListagemPaciente(Long id, String nome, String cpf, String email, String telefone) {

    public DadosListagemPaciente(Paciente paciente) {

        this(paciente.getId(), paciente.getNome(), paciente.getCpf(), paciente.getEmail(), paciente.getTelefone());

    }

}
