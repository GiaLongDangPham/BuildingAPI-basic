package com.javaweb.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingDTO;
import com.javaweb.service.BuildingService;

import customExceptions.FieldRequiredException;

@RestController
public class BuildingAPI {
	@Autowired
	private BuildingService buildingService;
	
	@GetMapping(value="/api/building/")
	public List<BuildingDTO> getBuilding(@RequestParam Map<String, Object> params,
										 @RequestParam(value="typeCode", required=false) List<String> typeCode) {
		return buildingService.findAll(params, typeCode);
	}
	
//	public void validate(BuildingDTO buildingDTO) throws FieldRequiredException {
//		if(buildingDTO.getName() == null || buildingDTO.getName().equals("") || buildingDTO.getNumberOfBasement() == null) {
//			throw new FieldRequiredException("Error when name or number is null");
//		}
//	}
	
	@RequestMapping(value="/api/building/", method = RequestMethod.POST)
	public BuildingDTO getBuilding2(@RequestBody BuildingDTO buildingDTO) {
		return buildingDTO;
	}
	
	@DeleteMapping(value="/api/building/{id}") 
	public void deleteBuilding(@PathVariable Integer id) {
		System.out.println("da xoa toa nha co id : " + id);
	}
}





