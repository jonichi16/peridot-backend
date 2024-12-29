package com.jonichi.peridot.envelope.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A class representing pagination information for a list of items.
 *
 * <p>This class contains metadata about the paginated content, including the total number of
 * elements, the current page, and whether the current page is the first or last.</p>
 *
 * @param <T> the type of content being paginated.
 */
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
