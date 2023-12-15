package br.com.clinimed.paciente;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRpository extends JpaRepository<Paciente, Long> {

}
