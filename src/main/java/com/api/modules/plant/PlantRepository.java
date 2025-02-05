package com.api.modules.plant;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    List<Plant> findByUser_Uuid(String userUuid);
    Optional<Plant> findByUuid(String uuid);
    @Modifying
    @Query("DELETE FROM Plant p WHERE p.uuid = :plantUuid AND p.user.uuid = :userUuid")
    int deleteByUuidAndUserUuid (@Param("plantUuid") String plantUuid, @Param("userUuid") String userUuid);
}
