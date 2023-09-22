package com.example.cardtocard.repo;

import com.example.cardtocard.models.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operation,Long> {

    @Query(value = "SELECT * FROM card2card.operation where base_card_id = :base_id or target_card_id = :target_id ;",
            nativeQuery = true)
    List<Operation> findOperationsNative(
            @Param("base_id") Long base_id, @Param("target_id") Long target_id);
}
