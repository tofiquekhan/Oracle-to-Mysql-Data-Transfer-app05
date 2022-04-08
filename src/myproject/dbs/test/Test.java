package myproject.dbs.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import myproject.dbs.pojo.Employee;

public class Test {

	public static void main(String[] args) {
		SessionFactory oracleSessionFactory = null;
		SessionFactory mysqlSessionFactory = null;
		
		Session oracleSession = null;
		Session mysqlSession = null;
		
		Transaction mysqlTransaction = null;
		try {
			Configuration oracleCfg = new Configuration();
			oracleCfg.configure("/myproject/dbs/resources/hibernate_oracle_cfg.xml");
			
			Configuration mysqlCfg = new Configuration();
			mysqlCfg.configure("/myproject/dbs/resources/hibernate_mysql_cfg.xml");
			
			oracleSessionFactory = oracleCfg.buildSessionFactory();
			mysqlSessionFactory = mysqlCfg.buildSessionFactory();
			
			oracleSession = oracleSessionFactory.openSession();
			mysqlSession = mysqlSessionFactory.openSession();
			
			mysqlTransaction = mysqlSession.beginTransaction();
			Employee emp = (Employee)oracleSession.get(Employee.class, 102);
			if(emp==null) {
				System.out.println("Employee not existed");
			}
			else {
				int pk = (Integer)mysqlSession.save(emp);
				if(pk == emp.getEno()) {
					mysqlTransaction.commit();
					System.out.println("Employee Transfered from Oracle DB to MySQL DB");
				}else {
					mysqlTransaction.rollback();
					System.out.println("Employee is not Transfered from Oracle DB to MySQL DB");
				}
			}
		}catch (Exception e) {
			mysqlTransaction.rollback();
			e.printStackTrace();
		}finally {
			oracleSession.close();
			mysqlSession.close();
			oracleSessionFactory.close();
			mysqlSessionFactory.close();
		}
		
	}
}
