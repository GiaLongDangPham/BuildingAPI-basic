package com.javaweb.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;

@Component
public class BuildingDTOConverter {
	@Autowired
	private DistrictRepository districtRepository;
	
	@Autowired
	private RentAreaRepository rentAreaRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public BuildingDTO toBuildingDTO(BuildingEntity item) {
		BuildingDTO itemDetail = modelMapper.map(item, BuildingDTO.class);
//		BuildingDTO itemDetail = new BuildingDTO();
//		itemDetail.setName(item.getName());
//		itemDetail.setManagerName(item.getManagerName());
//		itemDetail.setManagerPhoneNumber(item.getManagerPhoneNumber());
//		itemDetail.setFloorArea(item.getFloorArea());
//		itemDetail.setEmptyArea(item.getEmptyArea());
//		itemDetail.setRentPrice(item.getRentPrice());
//		itemDetail.setServiceFee(item.getServiceFee());
//		itemDetail.setBrokerageFee(item.getBrokerageFee());
		
		DistrictEntity districtEntity = districtRepository.findNamebyId(item.getDistrictid());
		itemDetail.setAddress(item.getStreet() + ", " + item.getWard() + ", " + districtEntity.getName());
		
		List<RentAreaEntity> rentAreas = rentAreaRepository.getValueByBuildingId(item.getId());
		String rentAreasResult = rentAreas.stream().map(it -> it.getValue()).collect(Collectors.joining(","));
		itemDetail.setRentArea(rentAreasResult);
		
		return itemDetail;
	}
}
