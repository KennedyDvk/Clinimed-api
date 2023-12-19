package br.com.clinimed.domain.paciente;

import br.com.clinimed.domain.endereco.DadosEndereco;
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
