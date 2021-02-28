package ru.geekbrains.market.services;

import org.springframework.stereotype.Service;
import ru.geekbrains.market.entities.OrderItem;
import ru.geekbrains.market.entities.dto.OrderItemDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemService {
    public List<OrderItemDto> mapEntityListToDtoList(List<OrderItem> orderItemList) {
        return orderItemList.stream().map(OrderItemDto::new).collect(Collectors.toList());
    }
}
