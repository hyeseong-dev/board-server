package com.example.boardserver.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {
    private final String nameMessage = "태그 name은 필수입니다.";
    private final String urlMessage = "태그 url은 필수입니다";

    @NotNull(message = nameMessage)
    @NotBlank(message = nameMessage)
    private String name;

    @NotNull(message = urlMessage)
    @NotBlank(message = urlMessage)
    private String url;
}
