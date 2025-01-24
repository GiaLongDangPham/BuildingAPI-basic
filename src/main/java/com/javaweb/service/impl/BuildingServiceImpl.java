package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.service.BuildingService;


@Service
public class BuildingServiceImpl implements BuildingService{
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Override
	public List<BuildingDTO> findAll(String name, String districtid) {
		
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(name, districtid);
		//filter
		List<BuildingDTO> result = new ArrayList<BuildingDTO>();
		for(BuildingEntity item : buildingEntities) {
			BuildingDTO itemDetail = new BuildingDTO();
			itemDetail.setName(item.getName());
			itemDetail.setNumberOfBasement(item.getNumberOfBasement());
			itemDetail.setAddress(item.getStreet() + ", " + item.getWard());
			result.add(itemDetail);
		}
		
		return result;
	}


}
