package project.footprint.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleType {
    ADMIN("ADMIN"),
    USER("USER");

    private final String type;
}
