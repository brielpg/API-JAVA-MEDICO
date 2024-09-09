package br.com.api.dto;

import br.com.api.models.Especialidade;
import br.com.api.models.Medico;

// DTO exclusivo para utilizar somente estas informacoes na hora de imprimir/mostrar as informacoes do médico na hora
// de listar ele
public record DadosListagemMedico(
        Long id,
        String nome,
        String email,
        String crm,
        Especialidade especialidade
) {
    public DadosListagemMedico(Medico medico) {
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade());
    }
}
