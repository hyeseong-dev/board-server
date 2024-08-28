package com.example.boardserver.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostSearchByTagRequest {
    private final String nammeMessage = "name은 필수 값입니다.";

    @NotNull(message = nammeMessage)
    @NotBlank(message = nammeMessage)
    private String name;
}
