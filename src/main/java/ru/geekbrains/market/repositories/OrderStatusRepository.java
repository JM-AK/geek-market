package ru.geekbrains.market.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.market.entities.OrderStatus;

@Repository
public interface OrderStatusRepository extends CrudRepository<OrderStatus, Long> {
}
