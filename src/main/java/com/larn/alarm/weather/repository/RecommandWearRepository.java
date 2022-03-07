package com.larn.alarm.weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.larn.alarm.weather.dto.RecommandWearDto;


public interface RecommandWearRepository extends JpaRepository<RecommandWearDto, Long>{

	@Query(value="SELECT * FROM Recommandwear r WHERE r.max_temp >= :temp and r.min_temp <= :temp", nativeQuery = true)
	List<RecommandWearDto> findBytempQuery(@Param("temp") int temp);

}
