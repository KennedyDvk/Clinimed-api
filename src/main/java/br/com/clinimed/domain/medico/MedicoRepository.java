package br.com.clinimed.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Page<Medico> findAllByAtivoTrue(Pageable paginacao);
    Page<Medico> findAllByNomeContainingAndAtivoTrue(String nome, Pageable paginacao);
    List<Medico> findAllByEspecialidadeAndAtivoTrue(Especialidade especialidade, Pageable paginacao);


}
