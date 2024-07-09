package rafa.dev.btgpactual.controller.dto;

import java.math.BigDecimal;

import rafa.dev.btgpactual.entity.OrderEntity;

public record OrderResponse(Long orderId,
							Long customerId,
							BigDecimal total) {
	
	public static OrderResponse fromEntity(OrderEntity entity) {
		return new OrderResponse(entity.getOrderId(), entity.getCustomerId(),entity.getTotal());
	}

}
