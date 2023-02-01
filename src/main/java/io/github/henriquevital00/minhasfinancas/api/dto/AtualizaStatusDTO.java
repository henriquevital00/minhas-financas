package io.github.henriquevital00.minhasfinancas.api.dto;

import io.github.henriquevital00.minhasfinancas.model.enums.StatusLancamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtualizaStatusDTO {
    private String status;
}
