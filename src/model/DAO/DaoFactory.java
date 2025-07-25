package model.DAO;

import db.DB;
import model.DAO.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDAO createSellerDAO() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
}
