package br.com.jogatinastore.model.user.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUserDTO(
    @NotNull(message = "O ID é obrigatório para atualização")
    UUID id,

    @NotBlank(message = "O nome não pode estar vazio")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]*$", message = "O nome deve conter apenas letras")
    String name,

    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "\\d{10,13}", message = "O telefone deve ter entre 10 e 13 dígitos com DDD")
    String phoneNumber,

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser uma data passada")
    LocalDate birthDate
) {}
