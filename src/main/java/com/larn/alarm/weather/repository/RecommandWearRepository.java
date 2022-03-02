package com.larn.alarm.weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.larn.alarm.weather.dto.RecommandWearDto;


public interface RecommandWearRepository extends JpaRepository<RecommandWearDto, Long>{

	@Query(value="SELECT r.min_temp, r.wear, r.max_temp FROM RECOMMANDWEAR r WHERE r.max_temp > :temp ",nativeQuery=true)
	List<RecommandWearDto> findBytempQuery(@Param("temp") int temp);

}
