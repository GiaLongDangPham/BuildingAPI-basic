package com.javaweb.repository.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.ConnectionJDBCUtil;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;
import com.mysql.cj.util.StringUtils;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {
	public static void joinTable(BuildingSearchBuilder buildingSearchBuilder, StringBuilder sql) {
		Long staffId = buildingSearchBuilder.getStaffId();
		if (staffId != null) {
			sql.append(" JOIN assignmentbuilding ON b.id = assignmentbuilding.buildingid\n ");
		}
		List<String> typeCode = buildingSearchBuilder.getTypeCode();
		if (typeCode != null && typeCode.size() != 0) {
			sql.append(" JOIN buildingrenttype ON b.id = buildingrenttype.buildingid\n ");
			sql.append(" JOIN renttype ON renttype.id = buildingrenttype.renttypeid\n ");
		}
	}

	public static void queryNormal(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
//		for (Map.Entry<String, Object> it : params.entrySet()) {
//			if (!it.getKey().equals("staffId") && !it.getKey().equals("typeCode") && !it.getKey().startsWith("area")
//					&& !it.getKey().startsWith("rentPrice")) {
//				String value = it.getValue().toString();
//				if (StringUtil.checkString(value)) {
//					if (NumberUtil.isNumber(value)) {
//						where.append(" AND b." + it.getKey() + " = " + value + "\n");
//					} else {
//						where.append(" AND b." + it.getKey() + " LIKE '%" + value + "%' \n");
//					}
//				}
//			}
//		}
		try {
			Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
			for (Field item : fields) {
				item.setAccessible(true);
				String fieldKey = item.getName();
				if (!fieldKey.equals("staffId") && !fieldKey.equals("typeCode") && !fieldKey.startsWith("area")
						&& !fieldKey.startsWith("rentPrice")) {
					String fieldValue = (String) item.get(buildingSearchBuilder);
					if (StringUtil.checkString(fieldValue)) {
						if (NumberUtil.isNumber(fieldValue)) {
							where.append(" AND b." + fieldKey + " = " + fieldValue + "\n");
						} else {
							where.append(" AND b." + fieldKey + " LIKE '%" + fieldValue + "%' \n");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void querySpecial(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
		Long staffId = buildingSearchBuilder.getStaffId();
		if (staffId != null) {
			where.append(" AND assignmentbuilding.staffid = " + staffId);
		}
		Long rentAreaTo = buildingSearchBuilder.getAreaTo();
		Long rentAreaFrom = buildingSearchBuilder.getAreaFrom();
		if (rentAreaTo != null || rentAreaFrom != null) {
			where.append(" AND EXISTS (SELECT * FROM rentarea r WHERE b.id = r.buildingid ");
			if (rentAreaTo != null) {
				where.append(" AND r.value <= " + rentAreaTo);
			}
			if (rentAreaFrom != null) {
				where.append(" AND r.value >= " + rentAreaFrom);
			}
			where.append(" ) ");
		}

		Long rentPriceTo = buildingSearchBuilder.getRentPriceTo();
		Long rentPriceFrom = buildingSearchBuilder.getRentPriceFrom();
		if (rentPriceTo != null || rentPriceFrom != null) {
			if (rentPriceTo != null) {
				where.append(" AND b.rentprice <= " + rentPriceTo);
			}
			if (rentPriceFrom != null) {
				where.append(" AND b.rentprice >= " + rentPriceFrom);
			}
		}
		List<String> typeCode = buildingSearchBuilder.getTypeCode();
		if (typeCode != null && typeCode.size() != 0) {
			String sql = typeCode.stream().map(it -> "renttype.code LIKE " + "'%" + it + "%' ")
					.collect(Collectors.joining(" OR "));
			where.append(" AND( ");
			where.append(sql);
			where.append(" ) ");
		}
	}

	@Override
	public List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder) {
		StringBuilder sql = new StringBuilder(
				"select b.id, b.name, b.districtid, b.street, b.ward, b.numberofbasement, b.floorarea, b.rentprice, b.managername, b.managerphonenumber, b.servicefee, b.brokeragefee, b.createddate"
						+ "\nFROM building b ");
		StringBuilder where = new StringBuilder(" WHERE 1=1 ");

		joinTable(buildingSearchBuilder, sql);
		queryNormal(buildingSearchBuilder, where);
		querySpecial(buildingSearchBuilder, where);

		where.append("GROUP BY b.id");
		sql.append(where);

		List<BuildingEntity> resp = new ArrayList<>();
		try (Connection conn = ConnectionJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString());) {
			while (rs.next()) {
				BuildingEntity building = new BuildingEntity();
				building.setId(rs.getLong("b.id"));
				building.setName(rs.getString("b.name"));
				building.setWard(rs.getString("b.ward"));
				building.setDistrictid(rs.getLong("b.districtid"));
				building.setStreet(rs.getString("b.street"));
				building.setFloorArea(rs.getLong("b.floorarea"));
				building.setRentPrice(rs.getLong("b.rentprice"));
				building.setServiceFee(rs.getString("b.servicefee"));
				building.setBrokerageFee(rs.getLong("b.brokeragefee"));
				building.setManagerName(rs.getString("b.managername"));
				building.setManagerPhoneNumber(rs.getString("b.managerphonenumber"));
				resp.add(building);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connect database error ....");
		}
		return resp;
	}

}
