package model.DAO;

import model.DAO.impl.SellerDaoJDBC;

public class DaoFactory {

	public static SellerDAO createSellerDAO() {
		return new SellerDaoJDBC();
	}
	
}
