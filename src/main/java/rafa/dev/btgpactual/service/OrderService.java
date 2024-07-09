package rafa.dev.btgpactual.service;

import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import rafa.dev.btgpactual.controller.dto.OrderResponse;
import rafa.dev.btgpactual.entity.OrderEntity;
import rafa.dev.btgpactual.entity.OrderItem;
import rafa.dev.btgpactual.listener.dto.OrderCreatedEvent;
import rafa.dev.btgpactual.repository.OrderRepository;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final MongoTemplate mongoTemplate;
	
	public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
		this.orderRepository = orderRepository;
		this.mongoTemplate = mongoTemplate;
	}
	
	public void save(OrderCreatedEvent event) {
		var entity = new OrderEntity();
		entity.setOrderId(event.codigoPedido());
		entity.setCustomerId(event.codigoCliente());
		entity.setTotal(getTotal(event));
		
		entity.setItems(getOrderItens(event));
		
		orderRepository.save(entity);
		
	}
	
	public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
		
		var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);
		
		return orders.map(OrderResponse::fromEntity);
	}
	
	public BigDecimal findTotalOrdersByCustomerId(Long customerId) {
		var aggregations = newAggregation(
				match(Criteria.where("customerId").is(customerId)),
                group().sum("total").as("total")
        );
		
		 var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);

	     return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
	}

	private BigDecimal getTotal(OrderCreatedEvent event) {
		return event.itens()
				.stream()
				.map(i -> i.preco().multiply(BigDecimal.valueOf(i.quantidade())))
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO)
				;
	}

	private List<OrderItem> getOrderItens(OrderCreatedEvent event) {
		return event.itens().stream()
				.map(i -> new OrderItem(i.produto(),i.quantidade(),i.preco()) )
				.toList();
	}
}
