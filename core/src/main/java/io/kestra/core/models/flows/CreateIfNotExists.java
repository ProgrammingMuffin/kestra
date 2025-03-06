package io.kestra.core.models.flows;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateIfNotExists {

    @Valid
    @NotNull
    Boolean namespace;

}
