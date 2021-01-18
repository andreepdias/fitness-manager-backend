package github.andreepdias.fitnessmanager.repository;

import github.andreepdias.fitnessmanager.model.entity.Diet.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    List<Food> findAllByUserIdOrderByNameAsc(Integer userId);

    Page<Food> findAllByUserIdOrderByNameAsc(Pageable pageable, Integer userId);

    List<Food> findByIdIn(List<Integer> foodsIdList);

    boolean existsByIdAndUserId(Integer id, Integer userId);


    Optional<Food> findByIdAndUserId(Integer foodId, Integer userId);
}
