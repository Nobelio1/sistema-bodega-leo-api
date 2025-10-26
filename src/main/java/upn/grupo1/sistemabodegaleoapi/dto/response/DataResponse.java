package upn.grupo1.sistemabodegaleoapi.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataResponse<T> {
    private Boolean success;
    private String message;
    private T data;
}
