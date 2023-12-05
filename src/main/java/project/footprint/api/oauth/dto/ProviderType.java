package project.footprint.api.oauth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProviderType {
    GOOGLE("google"),
    NAVER("naver");

    private final String type;
}
