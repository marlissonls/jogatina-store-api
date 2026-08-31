package br.com.jogatinastore.iam.role.application.service;

import br.com.jogatinastore.iam.role.application.dto.RoleCreateDto;
import br.com.jogatinastore.iam.role.application.dto.RoleResponseDto;
import br.com.jogatinastore.iam.role.application.dto.RoleUpdateDto;
import br.com.jogatinastore.iam.role.domain.model.Role;
import br.com.jogatinastore.iam.role.domain.exception.RoleErrors;
import br.com.jogatinastore.iam.role.infrastructure.persistence.RoleRepository;
import br.com.jogatinastore.shared.exception.base.ConflictException;
import br.com.jogatinastore.shared.exception.base.ResourceNotFoundException;
import br.com.jogatinastore.shared.pagination.PageResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {

    private final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public PageResponse<RoleResponseDto> findAll(Pageable pageable) {

        logger.debug("Fetching all roles. pagination={}", pageable);

        var page = repository.findAll(pageable);

        var items = page.getContent().stream()
                .map(RoleResponseDto::new)
                .toList();

        return new PageResponse<>(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }

    @Transactional(readOnly = true)
    public RoleResponseDto findById(UUID id) {

        logger.debug("Fetching role. roleId={}", id);

        Role role = getEntityById(id);

        logger.info("Role retrieved successfully. id={}", role.getId());

        return new RoleResponseDto(role);
    }

    @Transactional(readOnly = true)
    public RoleResponseDto findByTitle(String title) {
        logger.debug("Fetching role by title. title={}", title);

        Role role = getByTitle(title);

        logger.info("Role retrieved by title successfully. id={}, title={}",
                role.getId(), role.getTitle());

        return new RoleResponseDto(role);
    }

    public Role getByTitle(String title) {

        return repository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException(
                        RoleErrors.Target.ROLE_TITLE,
                        RoleErrors.Code.ROLE_NOT_FOUND
                ));
    }

    @Transactional(readOnly = true)
    public List<Role> findAllByIdIn(List<UUID> roleIds) {

        logger.debug("Fetching roles by ids={}", roleIds);

        List<Role> roles = repository.findAllByIdIn(roleIds);

        if (roles.size() != roleIds.size()) {
            throw new ResourceNotFoundException(
                    RoleErrors.Target.ROLE_ID,
                    RoleErrors.Code.ROLE_NOT_FOUND
            );
        }

        logger.info("Roles retrieved successfully. roleIds={}", roleIds);

        return roles;
    }

    @Transactional
    public RoleResponseDto create(RoleCreateDto dto) {

        logger.debug("Creating role. title={}", dto.title());

        validateUniqueness(null, dto.title());

        Role role = Role.createFrom(dto);

        Role savedRole = repository.save(role);

        logger.info("Role created successfully. roleId={}", savedRole.getId());

        return new RoleResponseDto(savedRole);
    }

    @Transactional
    public RoleResponseDto update(UUID id, RoleUpdateDto dto) {

        logger.debug("Updating role. roleId={}", id);

        validateUniqueness(id, dto.title());

        Role role = getEntityById(id);

        role.applyUpdate(dto);

        logger.info("Role updated successfully. roleId={}, roleTitle={}", role.getId(), role.getTitle());

        return new RoleResponseDto(role);
    }

    public @NonNull Role getEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        RoleErrors.Target.ROLE_ID,
                        RoleErrors.Code.ROLE_NOT_FOUND
                ));
    }

    private void validateUniqueness(UUID id, String title) {
        repository.findByTitle(title)
                .filter(role -> id == null || !role.getId().equals(id))
                .ifPresent(role -> {
                    throw new ConflictException(
                            RoleErrors.Target.ROLE_TITLE,
                            RoleErrors.Code.ROLE_ALREADY_EXISTS
                    );
                });
    }
}
