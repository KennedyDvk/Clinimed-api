package br.com.clinimed.domain.consulta.validacoes.agendamento;

import br.com.clinimed.domain.consulta.DadosAgendamentoConsulta;

public interface ValidadorAgendamentoDeConsulta {
    void validar(DadosAgendamentoConsulta dados);
}
