package br.com.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


// DTO exclusivo para utilizar somente estas informacoes na hora de cadastrar o endereco do médico no banco de dados,
// vale resaltar que nem todas as informacoes sao necessária (ex: complemento e numero), as que sao obrigatorias devem
// utilizar a anotacao @NotBlank ou @NotNull, para verificar se nao está vazio.
public record DadosEndereco(
        @NotBlank
        String logradouro,
        @NotBlank
        String bairro,
        @NotBlank
        @Pattern(regexp = "\\d{8}")
        String cep,
        @NotBlank
        String cidade,
        @NotBlank
        String uf,
        String complemento,
        String numero) {
}
