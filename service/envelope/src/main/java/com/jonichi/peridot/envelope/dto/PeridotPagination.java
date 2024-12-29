package com.jonichi.peridot.envelope.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PeridotPagination<T> {

    private List<T> content;
    private Integer totalPages;
    private Long totalElements;
    private Integer numberOfElements;
    private Integer currentPage;
    private Boolean first;
    private Boolean last;

}
