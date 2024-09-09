package br.com.api.dto;

import jakarta.validation.constraints.NotNull;

// DTO exclusivo para utilizar somente estas informacoes na hora de atualizar as informacoes do médico. O valor do id
// deve vir com a anotacao @NotNull pois o id é necessário para sabermos de qual médico devemos atualizar as informacoes
public record DadosAtualizacaoMedico(
        @NotNull
        Long id,
        String telefone,
        String nome,
        DadosEndereco endereco
) {
}
