package rafa.dev.btgpactual.controller;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rafa.dev.btgpactual.controller.dto.ApiResponse;
import rafa.dev.btgpactual.controller.dto.OrderResponse;
import rafa.dev.btgpactual.controller.dto.PaginatorResponse;
import rafa.dev.btgpactual.service.OrderService;

@RestController
public class OrderController {
	
	private final OrderService orderService;
	
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@GetMapping("customers/{customerId}/orders") 
	public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId") Long customerId,
										@RequestParam(name = "page", defaultValue = "0") Integer page,
										@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		
		var pageResponse = orderService.findAllByCustomerId(customerId, PageRequest.of(page, pageSize));
		var totalOnOrders = orderService.findTotalOrdersByCustomerId(customerId);
		
		return ResponseEntity.ok(new ApiResponse<>(
				Map.of("totalOnOrders", totalOnOrders),
				pageResponse.getContent(), 
				PaginatorResponse.fromPage(pageResponse)));
	}

}
