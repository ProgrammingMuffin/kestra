package io.kestra.core.services;

import io.kestra.core.models.validations.ManualConstraintViolation;
import io.kestra.core.repositories.FlowRepositoryInterface;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Singleton
@Slf4j
public class NamespaceService {

    private final Optional<FlowRepositoryInterface> flowRepository;

    @Inject
    public NamespaceService(Optional<FlowRepositoryInterface> flowRepository) {
        this.flowRepository = flowRepository;
    }

    /**
     * Checks whether a given namespace exists.
     *
     * @param tenant        The tenant ID
     * @param namespace     The namespace - cannot be null.
     * @return  {@code true} if the namespace exist. Otherwise {@link false}.
     */
    public boolean isNamespaceExists(String tenant, String namespace) {
        Objects.requireNonNull(namespace, "namespace cannot be null");

        if (flowRepository.isPresent()) {
            List<String> namespaces = flowRepository.get().findDistinctNamespace(tenant);
            return namespaces.stream().anyMatch(ns -> ns.equals(namespace) || ns.startsWith(namespace));
        }
        return false;
    }

    /**
     * Checks whether a given namespace exists.
     *
     * @param tenant        The tenant ID
     * @param namespace     The namespace - cannot be null.
     * @return constraintViolation
     */
    public ConstraintViolation<?> CheckNamespaceExists(String tenant, String namespace) {
        Objects.requireNonNull(namespace, "namespace cannot be null");

        Boolean namespaceExists = false;

        if (flowRepository.isPresent()) {
            List<String> namespaces = flowRepository.get().findDistinctNamespace(tenant);
            namespaceExists = namespaces.stream().anyMatch(ns -> ns.equals(namespace) || ns.startsWith(namespace));
        }
        
        if (!namespaceExists) {
            return (ConstraintViolation<?>) ManualConstraintViolation.of("Namespace does not exist for tenant " + tenant, namespace, String.class, "flow.namespace", namespace);
        }
        return null;
    }
}
