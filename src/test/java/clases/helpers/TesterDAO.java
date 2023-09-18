package clases.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class TesterDAO {
	
	private static Connection conexion = null;
	private static String conecctionURI;
	private static String user;
	private static String password;
	
	static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String TABLE_INFO = "SELECT TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, DATA_TYPE, COLUMN_KEY FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME=?";
	
	public static final Connection getSharedConnection(){
			try {
				if(conexion==null || conexion.isClosed()) {
					conexion = DriverManager.getConnection(conecctionURI,
							user,
							password);
					//Nos aseguramos de que el Autocommit es true siempre en la conexion compartida
					if(!conexion.getAutoCommit())
						conexion.setAutoCommit(true);

				}
			} catch (SQLException e) {
				System.out.println("Error conectando a BD");
				e.printStackTrace();
			}
		return conexion;
	}
	
	public static final Connection getIndividualConnection(){
		Connection conexion=null;
			try {
				conexion = DriverManager.getConnection(conecctionURI,
						user,
						password);
			} catch (SQLException e) {
				System.out.println("Error conectando a BD");
				e.printStackTrace();
			}
		
		return conexion;
	}

	public static String getConecctionURI() {
		return conecctionURI;
	}

	public static void setConecctionURI(String conecctionURI) {
		TesterDAO.conecctionURI = conecctionURI;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		TesterDAO.user = user;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		TesterDAO.password = password;
	}
}
