package br.com.jogatinastore.domain.inventory.stock.repository;

import br.com.jogatinastore.domain.inventory.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID>, JpaSpecificationExecutor<Stock> {

    @Modifying
    @Query("""
        UPDATE Stock i
            SET i.minimumQuantity = :newMinimumQuantity
        WHERE i.id = :id
    """)
    int updateMinimumQuantity(
        @Param("id") UUID id,
        @Param("newMinimumQuantity") Integer newMinimumQuantity
    );

    @Modifying
    @Query("""
        UPDATE Stock i
            SET i.availableQuantity = i.availableQuantity + :amount
        WHERE i.id = :id
    """)
    int increase(
        @Param("id") UUID id,
        @Param("amount") Integer amount
    );

    @Modifying
    @Query("""
        UPDATE Stock i
            SET i.availableQuantity = i.availableQuantity - :amount
        WHERE i.id = :id
            AND i.availableQuantity >= :amount
    """)
    int writeOff(
        @Param("id") UUID id,
        @Param("amount") Integer amount
    );

    @Modifying
    @Query("""
        UPDATE Stock i
            SET i.availableQuantity = i.availableQuantity - :amount,
                i.reservedQuantity = i.reservedQuantity + :amount
        WHERE i.id = :id
            AND i.availableQuantity >= :amount
    """)
    int reserve(
        @Param("id") UUID id,
        @Param("amount") Integer amount
    );

    @Modifying
    @Query("""
        UPDATE Stock i
            SET i.availableQuantity = i.availableQuantity + :amount,
                i.reservedQuantity = i.reservedQuantity - :amount
        WHERE i.id = :id
            AND i.reservedQuantity >= :amount
    """)
    int release(
        @Param("id") UUID id,
        @Param("amount") Integer amount
    );

    @Modifying
    @Query("""
        UPDATE Stock i
            SET i.reservedQuantity = i.reservedQuantity - :amount
        WHERE i.id = :id
            AND i.reservedQuantity >= :amount
    """)
    int commit(
        @Param("id") UUID id,
        @Param("amount") Integer amount
    );
}
