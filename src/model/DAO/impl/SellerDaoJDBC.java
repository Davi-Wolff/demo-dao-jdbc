package model.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.DAO.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDAO {

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller seller) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					  "INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, (new java.sql.Date(seller.getBirthDate().getTime())));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
					DB.closeResultSet(rs);
				}
			} else {
				throw new DbException("Unexpected error! No rows were affected!");
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Seller seller) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					  "UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
					
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, (new java.sql.Date(seller.getBirthDate().getTime())));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartment().getId());
			st.setInt(6, seller.getId());
			
			st.executeUpdate();
			
					}catch(SQLException e) {
			throw new DbException(e.getMessage());
		} 
		 finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					  "DELETE FROM seller WHERE Id = ?");
					
			st.setInt(1,id);
			
			int rowsAffected = st.executeUpdate();
			if(rowsAffected == 0) {
				throw new DbException("This Id don't exist! No seller was deleted!");
			}
			
					}catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} 
		 finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName\r\n" + "FROM seller INNER JOIN department\r\n"
							+ "ON seller.DepartmentId = department.Id\r\n" + "WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Department dep = instatiateDepartment(rs);
				Seller obj = instantiateSeller(rs,dep);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Seller instantiateSeller(ResultSet rs,Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble(("BaseSalary")));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instatiateDepartment(ResultSet rs) throws SQLException {
		
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
		
		
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName\r\n"
					+ "FROM seller INNER JOIN department\r\n"
					+ "ON seller.DepartmentId = department.Id\r\n"
					+ "ORDER BY Name");
			rs = st.executeQuery();
			List<Seller> sellerList = new ArrayList<Seller>();
			Map<Integer,Department> map = new HashMap<Integer, Department>();
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				if(dep == null) {
				dep = instatiateDepartment(rs);
				map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller obj = instantiateSeller(rs,dep);
				sellerList.add(obj);
			}
			return sellerList;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	

	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName\r\n"
					+ "FROM seller INNER JOIN department\r\n"
					+ "ON seller.DepartmentId = department.Id\r\n"
					+ "WHERE DepartmentId = ?\r\n"
					+ "ORDER BY Name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			List<Seller> sellerList = new ArrayList<Seller>();
			Map<Integer,Department> map = new HashMap<Integer, Department>();
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				if(dep == null) {
				dep = instatiateDepartment(rs);
				map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller obj = instantiateSeller(rs,dep);
				sellerList.add(obj);
			}
			return sellerList;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
