package model.DAO.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.DAO.DepartmentDAO;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDAO {

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		super();
		this.conn = conn;
	}

	@Override
	public void insert(Department dp) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO department " + "(Name) " + "VALUES " + "(?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, dp.getName());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					dp.setId(id);
					DB.closeResultSet(rs);
				}
			} else {
				throw new DbException("Unexpected error! No rows were affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Department dp) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE department " + "SET Name = ? " + "WHERE Id = ?");

			st.setString(1, dp.getName());
			st.setInt(2, dp.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

			st.setInt(1, id);

			int rowsAffected = st.executeUpdate();
			if (rowsAffected == 0) {
				throw new DbException("This Id don't exist! No seller was deleted!");
			}

		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM department WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Department dep = instatiateDepartment(rs);
				return dep;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Department instatiateDepartment(ResultSet rs) throws SQLException {

		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;

	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM department ORDER BY Name");
			rs = st.executeQuery();
			
			List<Department> dpList = new ArrayList<Department>();
			
			while (rs.next()) {
				Department dp = instatiateDepartment(rs);
				dpList.add(dp);
			}
			return dpList;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
