package ru.geekbrains.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.market.entities.DeliveryAddress;
import ru.geekbrains.market.repositories.DeliveryAddressRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryAddressService {
    private DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    public void setDeliveryAddressRepository(DeliveryAddressRepository deliveryAddressRepository) {
        this.deliveryAddressRepository = deliveryAddressRepository;
    }

    public List<DeliveryAddress> getUserAddresses(Long userId) {
        return deliveryAddressRepository.findAllByUserId(userId);
    }

    public Optional<DeliveryAddress> getDeliveryAddress(Long userId, String address){
        return deliveryAddressRepository.findOneByUserIdAndAddress(userId, address);
    }

    public DeliveryAddress getUserAddressOrCreateOne(Long userId, String address){
        Optional<DeliveryAddress> deliveryAddress = getDeliveryAddress(userId, address);
        return deliveryAddress.orElse(deliveryAddressRepository.save(new DeliveryAddress(userId, address)));
    }


}
