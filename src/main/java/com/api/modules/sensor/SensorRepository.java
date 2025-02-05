package com.api.modules.sensor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
    @Modifying
    @Query("DELETE FROM Sensor s WHERE s.id = :sensorId AND s.plant.user.uuid = :userUuid")
    int deleteSensorByIdAndUserUuid(@Param("sensorId") Long sensorId, @Param("userUuid") String userUuid);
}
