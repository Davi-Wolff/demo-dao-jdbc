package model.DAO;

import db.DB;
import model.DAO.impl.DepartmentDaoJDBC;
import model.DAO.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDAO createSellerDAO() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	public static DepartmentDAO createDepartmentDAO() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
	
}
