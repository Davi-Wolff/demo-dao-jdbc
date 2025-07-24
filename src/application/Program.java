package application;

import java.util.Date;

import model.DAO.DaoFactory;
import model.DAO.SellerDAO;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
	
	Department dp = new Department(1,"Books");
	Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, dp);
	
SellerDAO sellerDao = DaoFactory.createSellerDAO();
// don't need to do the "new Seller()" and don't need to do an instance of the DaoFactory cause the method is static.
	
	System.out.println(seller);
	}
}
