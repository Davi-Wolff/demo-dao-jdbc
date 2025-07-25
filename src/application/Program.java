package application;

import model.DAO.DaoFactory;
import model.DAO.SellerDAO;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDAO sellerDao = DaoFactory.createSellerDAO();
// don't need to do the "new Seller()" and don't need to do an instance of the DaoFactory cause the method is static.
		
		System.out.println("=== TEST1: seller findById ========");
		
		Seller seller = sellerDao.findById(3);
		
		System.out.println(seller);
	}
}
