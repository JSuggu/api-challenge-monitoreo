package com.api.handler;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {
    private Boolean flag;
    private Integer code;
    private String message;
    @Builder.Default
    private Object data = null;
}
