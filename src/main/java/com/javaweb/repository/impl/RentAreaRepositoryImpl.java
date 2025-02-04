package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.utils.ConnectionJDBCUtil;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository{

	@Override
	public List<RentAreaEntity> getValueByBuildingId(Long id) {
		String sql = "SELECT r.value FROM rentarea r WHERE r.buildingid = " + id + ";";
		List<RentAreaEntity> result = new ArrayList<>();
		try (Connection conn = ConnectionJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				RentAreaEntity item = new RentAreaEntity();
				item.setValue(rs.getString("value"));
				result.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connect database error from rentAreaImpl ....");
		}
		return result;
	}

}
