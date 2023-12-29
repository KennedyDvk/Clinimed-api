package br.com.clinimed.domain.consulta;

import br.com.clinimed.domain.ValidacaoException;
import br.com.clinimed.domain.consulta.validacoes.agendamento.ValidadorAgendamentoDeConsulta;
import br.com.clinimed.domain.consulta.validacoes.cancelamento.ValidadorCancelamentoDeConsulta;
import br.com.clinimed.domain.medico.Medico;
import br.com.clinimed.domain.medico.MedicoRepository;
import br.com.clinimed.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AgendaConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRpository;

    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;

    @Autowired
    private List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;

    public AgendaConsultas() {
    }

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {

        if (!pacienteRpository.existsById(dados.idPaciente())) {
            throw new ValidacaoException("ID do Paciente informado não existe!");
        }

        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())) {
            throw new ValidacaoException("ID do Medico informado não existe!");
        }

        validadores.forEach(v -> v.validar(dados));

        var paciente = pacienteRpository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);
        if (medico == null) {
            throw new ValidacaoException("Não existe Médico disponivel nessa data!");
        }
        var consulta = new Consulta(null, medico, paciente, dados.data(), null);
        consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);

    }

    public void cancelar(DadosCancelamentoConsulta dados) {
        if (!consultaRepository.existsById(dados.idConsulta())) {
            throw new ValidacaoException("Id da consulta informado não existe!");
        }

        validadoresCancelamento.forEach(v -> v.validar(dados));

        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        consulta.cancelar(dados.motivo());
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null) {
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if (dados.especialidade() == null) {
            throw new ValidacaoException("Especialidade é Obrigatória quando o medico não for escolhido");
        }
        List<Medico> medicos = medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());
        return medicos.isEmpty() ? null : medicos.get(new Random().nextInt(medicos.size()));
    }

}
