package br.com.clinimed.paciente;

import br.com.clinimed.endereco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

public record DadosAtualicaoPaciente(
        @NotNull
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        DadosEndereco endereco) {
}
