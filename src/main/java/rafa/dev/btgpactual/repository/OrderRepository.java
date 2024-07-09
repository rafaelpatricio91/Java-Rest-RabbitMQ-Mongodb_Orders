package rafa.dev.btgpactual.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import rafa.dev.btgpactual.controller.dto.OrderResponse;
import rafa.dev.btgpactual.entity.OrderEntity;

@Repository
public interface OrderRepository extends MongoRepository<OrderEntity, Long> {

	Page<OrderEntity> findAllByCustomerId(Long customerId, PageRequest pageRequest);

}
