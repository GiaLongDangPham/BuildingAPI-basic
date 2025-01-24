package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;


@Repository
public class BuildingRepositoryImpl implements BuildingRepository {
	static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic";
	static final String USER = "root";
	static final String PASS = "nhuphuong1416";
	
	@Override
	public List<BuildingEntity> findAll(String name, String districtid) {
		StringBuilder sql = new StringBuilder("select * from building WHERE 1 = 1");
		if(name != null && !name.equals("")) {
			sql.append(" AND name like '%" + name + "%'");
		}
		if(districtid != null && !districtid.equals("")) {
			sql.append(" AND districtid like '%" + districtid + "%'");
		}
		List<BuildingEntity> resp = new ArrayList<>();
		try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString());
			){
			while(rs.next()) {
				BuildingEntity building = new BuildingEntity();
				building.setName(rs.getString("name"));
				building.setStreet(rs.getString("street"));
				building.setWard(rs.getString("ward"));
				building.setNumberOfBasement(rs.getString("numberOfBasement"));
				resp.add(building);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("Connect database error ....");
		}
		return resp;
	}

}
