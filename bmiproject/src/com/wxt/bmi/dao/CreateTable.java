package com.wxt.bmi.dao;

import java.sql.SQLException;

public class CreateTable {
	public static void main(String[] args){
		String sql="create table bmiinfo(id int not null auto_increment,"
				+"date varchar(45),height varchar(45),weight varchar(45),"
				+"bmi varchar(45),primary key(id))default charset=utf8";
		DBHelper db=new DBHelper(sql);
		
		try {
			int update=db.pst.executeUpdate();
			System.out.println(update);
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		
	}

}
