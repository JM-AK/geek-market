package ru.geekbrains.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.market.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
