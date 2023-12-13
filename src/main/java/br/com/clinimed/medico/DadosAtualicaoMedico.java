package br.com.clinimed.medico;

import br.com.clinimed.endereco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

public record DadosAtualicaoMedico(

        @NotNull
        Long id,
        String nome,
        String telefone,
        DadosEndereco endereco) {
}
