package com.javaweb.repository.impl;

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

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.ConnectionJDBCUtil;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;
import com.mysql.cj.util.StringUtils;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {
	public static void joinTable(Map<String, Object> params, List<String> typeCode, StringBuilder sql) {
		String staffId = (String) params.get("staffId");
		if (StringUtil.checkString(staffId)) {
			sql.append(" JOIN assignmentbuilding ON b.id = assignmentbuilding.buildingid\n ");
		}
		if (typeCode != null && typeCode.size() != 0) {
			sql.append(" JOIN buildingrenttype ON b.id = buildingrenttype.buildingid\n ");
			sql.append(" JOIN renttype ON renttype.id = buildingrenttype.renttypeid\n ");
		}
	}

	public static void queryNormal(Map<String, Object> params, StringBuilder where) {
		for (Map.Entry<String, Object> it : params.entrySet()) {
			if (!it.getKey().equals("staffId") && !it.getKey().equals("typeCode") && !it.getKey().startsWith("area")
					&& !it.getKey().startsWith("rentPrice")) {
				String value = it.getValue().toString();
				if (StringUtil.checkString(value)) {
					if (NumberUtil.isNumber(value)) {
						where.append(" AND b." + it.getKey() + " = " + value + "\n");
					} else {
						where.append(" AND b." + it.getKey() + " LIKE '%" + value + "%' \n");
					}
				}
			}
		}
	}

	public static void querySpecial(Map<String, Object> params, List<String> typeCode, StringBuilder where) {
		String staffId = (String) params.get("staffId");
		if (StringUtil.checkString(staffId)) {
			where.append(" AND assignmentbuilding.staffid = " + staffId);
		}
		String rentAreaTo = (String) params.get("areaTo");
		String rentAreaFrom = (String) params.get("areaFrom");
		if (StringUtil.checkString(rentAreaTo) || StringUtil.checkString(rentAreaFrom)) {
			where.append(" AND EXISTS (SELECT * FROM rentarea r WHERE b.id = r.buildingid ");
			if (StringUtil.checkString(rentAreaTo)) {
				where.append(" AND r.value <= " + rentAreaTo);
			}
			if (StringUtil.checkString(rentAreaTo)) {
				where.append(" AND r.value >= " + rentAreaFrom);
			}
			where.append(" ) ");
		}

		String rentPriceTo = (String) params.get("rentPriceTo");
		String rentPriceFrom = (String) params.get("rentPriceFrom");
		if (StringUtil.checkString(rentPriceTo) || StringUtil.checkString(rentPriceFrom)) {
			if (StringUtil.checkString(rentPriceTo)) {
				where.append(" AND b.rentprice <= " + rentPriceTo);
			}
			if (StringUtil.checkString(rentPriceFrom)) {
				where.append(" AND b.rentprice >= " + rentPriceFrom);
			}
		}
		// java7
//		if (typeCode != null && typeCode.size() != 0) {
//			List<String> newTypeCode = new ArrayList<>();
//			for (String item : typeCode) {
//			    newTypeCode.add("'" + item + "'");
//			}
//			where.append(" AND renttype.code IN(" + String.join(",", newTypeCode) + ")");
//		}

		// java8
		if (typeCode != null && typeCode.size() != 0) {
			String sql = typeCode.stream().map(it -> "renttype.code LIKE " + "'%" + it + "%' ").collect(Collectors.joining(" OR "));
			where.append(" AND( ");
			where.append(sql);
			where.append(" ) ");
		}
	}

	@Override
	public List<BuildingEntity> findAll(Map<String, Object> params, List<String> typeCode) {
		StringBuilder sql = new StringBuilder(
				"select b.id, b.name, b.districtid, b.street, b.ward, b.numberofbasement, b.floorarea, b.rentprice, b.managername, b.managerphonenumber, b.servicefee, b.brokeragefee, b.createddate"
						+ "\nFROM building b ");
		StringBuilder where = new StringBuilder(" WHERE 1=1 ");

		joinTable(params, typeCode, sql);
		queryNormal(params, where);
		querySpecial(params, typeCode, where);

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
