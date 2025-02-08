package com.javaweb.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.service.BuildingService;

@RestController
@Transactional
public class BuildingAPI {
	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	@GetMapping(value="/api/building/")
	public List<BuildingDTO> getAllBuilding(@RequestParam Map<String, Object> params,
										 @RequestParam(value="typeCode", required=false) List<String> typeCode) {
		return buildingService.findAll(params, typeCode);
	}
	
	@GetMapping(value="/api/building/{id}")
	public BuildingDTO getBuildingById(@PathVariable Long id) {
		BuildingDTO result = new BuildingDTO();
		BuildingEntity item = buildingRepository.findById(id).get();
		return result;
	}
	
	@DeleteMapping(value="/api/building/{id}")
	public void removeBuildingById(@PathVariable List<Long> id) {
		buildingRepository.deleteByIdIn(id);
	}
	

	
//	public void validate(BuildingDTO buildingDTO) throws FieldRequiredException {
//		if(buildingDTO.getName() == null || buildingDTO.getName().equals("") || buildingDTO.getNumberOfBasement() == null) {
//			throw new FieldRequiredException("Error when name or number is null");
//		}
//	}
	
	@PostMapping(value="/api/building/")
	
	@DeleteMapping(value="/api/building/{id}") 
	public void deleteBuilding(@PathVariable Integer id) {
		System.out.println("da xoa toa nha co id : " + id);
	}
}





