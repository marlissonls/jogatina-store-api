package br.com.jogatinastore.domain.catalog.inventory.repository;

import br.com.jogatinastore.domain.catalog.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID>, JpaSpecificationExecutor<Inventory> {

    @Modifying
    @Query("""
        UPDATE Inventory i
            SET i.minQuantity = :newMinQuantity
        WHERE i.id = :id
    """)
    int updateMinQuantity(
        @Param("id") UUID id,
        @Param("newMinQuantity") Integer newMinQuantity
    );

    @Modifying
    @Query("""
        UPDATE Inventory i
            SET i.availableQuantity = i.availableQuantity + :amount
        WHERE i.id = :id
    """)
    int increase(
        @Param("id") UUID id,
        @Param("amount") Integer amount
    );

    @Modifying
    @Query("""
        UPDATE Inventory i
           SET i.availableQuantity = i.availableQuantity - :amount
        WHERE i.id = :id
    """)
    int writeOff(
        @Param("id") UUID id,
        @Param("amount") Integer amount
    );

    @Modifying
    @Query("""
        UPDATE Inventory i
           SET i.availableQuantity = i.availableQuantity - :amount,
               i.reservedQuantity = i.reservedQuantity + :amount
        WHERE i.id = :id
    """)
    int reserve(
        @Param("id") UUID id,
        @Param("amount") Integer amount
    );

    @Modifying
    @Query("""
        UPDATE Inventory i
           SET i.availableQuantity = i.availableQuantity + :amount,
               i.reservedQuantity = i.reservedQuantity - :amount
        WHERE i.id = :id
    """)
    int release(
        @Param("id") UUID id,
        @Param("amount") Integer amount
    );

    @Modifying
    @Query("""
        UPDATE Inventory i
           SET i.reservedQuantity = i.reservedQuantity - :amount
        WHERE i.id = :id
    """)
    int commit(
        @Param("id") UUID id,
        @Param("amount") Integer amount
    );
}
