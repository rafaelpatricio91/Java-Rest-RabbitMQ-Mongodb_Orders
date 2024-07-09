package rafa.dev.btgpactual.controller.dto;

import org.springframework.data.domain.Page;

public record PaginatorResponse(Integer page,
								Integer pageSize,
								Long totalElements,
								Integer totalPages) {
	
	public static PaginatorResponse fromPage(Page<?> page) {
		return new PaginatorResponse(
			page.getNumber(),
			page.getSize(),
			page.getTotalElements(),
			page.getTotalPages()
		);
				
				
				
	}

}
