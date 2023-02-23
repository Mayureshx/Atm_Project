package com.miniproject.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;





public class Atm {
public static void main(String[] args) throws NumberFormatException, IOException, SQLException, ParseException {
		
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ WELCOME TO ATM $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$ PLEASE INSERT CARD $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		

		System.out.print("\t\t Enter your Username:");
		String userName=br.readLine();
		System.out.print("\t\t Enter your Pin:");
		String userPin=br.readLine();
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		
		
		Connection conn=DB_Connection.getConnection();
		PreparedStatement ps=conn.prepareStatement("select * from Bank where userName=?");
		ps.setString(1,userName);
		ResultSet result=ps.executeQuery();
		String pin=null;
			
			while(result.next())
			{
				pin=result.getString("userPin");
			}
		
		
		if(userPin.equals(pin))
		{
		
			System.out.println("You have successfully logged in!!");
			
			
			boolean end=true;
			do {//need to close
				
				System.out.println("\nSelect the account you want to access: ");
				System.out.println(" Type 1 - Checkings Account");
				System.out.println(" Type 2 - Savings Account");
				System.out.println(" Type 3 - Exit");
				System.out.print("\nChoice: ");
				
				int selection=Integer.parseInt(br.readLine());
				
				switch(selection) {//close it
				
				case 1:
					System.out.println("Enter the Account Type");
					System.out.println("Must Enter");
					System.out.println("**Checkings**");
					String aType=br.readLine();

					conn=DB_Connection.getConnection();
			        ps=conn.prepareStatement("select * from Bank where userName=?");
			        ps.setString(1,userName);
			        result=ps.executeQuery();
			        String accountType="";
			        
			        while(result.next())
			        {
			        	accountType=result.getString("accType");
			        }
			        
			        if(accountType.equalsIgnoreCase(aType))
			        {
			
			
			boolean login=true;
			do
			{
			
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  WELCOME " + userName.toUpperCase() + " $$$$$$$$$$$$$$$$$$$$$$$");
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Savings Account $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			System.out.println("1  --->   Deposit");
			System.out.println("2  --->   Withdraw");
			System.out.println("3  --->   Fund Transfer");
			System.out.println("4  --->   Balance Check");
			System.out.println("5  --->   Change Pin");
			System.out.println("6  --->   Transaction History/Receipt");
			System.out.println("7  --->   Exit / Logout");
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
			System.out.print("\t\t Enter your choice:"); 
			int operationNumber=Integer.parseInt(br.readLine());
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			
			
			String status=null;
			
			switch(operationNumber)
			{
				case 1: System.out.println("Enter deposit amount:");
						double depositAmount=Double.parseDouble(br.readLine());
						
						if(depositAmount>0)
						{
							conn=DB_Connection.getConnection();
							ps=conn.prepareStatement("select * from Bank where userName=?");
							ps.setString(1, userName);
							result=ps.executeQuery();
							
							double balance=0.0;
							long accId=0;
							while(result.next())
							{
								balance=result.getDouble("accBalance");
								accId=result.getLong("accId");
							}
							
							balance=balance+depositAmount;
							
							ps=conn.prepareStatement("update Bank set accBalance=? where userName=?");
							ps.setDouble(1, balance);
							ps.setString(2, userName);
							
							if(ps.executeUpdate()>0)
							{
								ps=conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
								Timestamp timestamp = new Timestamp(System.currentTimeMillis());
								String transactionId="TN"+timestamp.getTime(); //TN3243432432423
								ps.setString(1, transactionId);
								ps.setDouble(2, depositAmount);
								ps.setDate(3, new Date(System.currentTimeMillis()));
								ps.setString(4, "deposit");
								ps.setLong(5,accId);
								ps.setLong(6,accId);
								
								ps.executeUpdate();
		
								
								System.out.println("Balance Updated!!");
								System.out.println("New Balance: "+balance);
							}
							else
							{
								System.out.println("Something went wrong!!");
							}
							
						}
						
						System.out.println("Do you want to continue??(Y/N)");
						 status=br.readLine();
						
						if(status.equals("n") || status.equals("N"))
						{
							login=false;
						}
						
						break;
				case 2:	 System.out.println("Enter Withdrawal amount:");
				 double withdrawalAmount=Double.parseDouble(br.readLine());
				 if(withdrawalAmount>0)
				 {
				    conn=DB_Connection.getConnection();
					ps=conn.prepareStatement("select * from Bank where userName=?");
					ps.setString(1, userName);
					result=ps.executeQuery();
					
					double balance=0.0;
					long accId=0;
					while(result.next())
					{
						balance=result.getDouble("accBalance");
						accId=result.getLong("accId");
					}
					
					
					if(balance>withdrawalAmount)
					{
						balance=balance-withdrawalAmount;
						ps=conn.prepareStatement("update Bank set accBalance=? where userName=?");
						ps.setDouble(1, balance);
						ps.setString(2, userName);
						
						if(ps.executeUpdate()>0)
						{
							ps=conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
							Timestamp timestamp = new Timestamp(System.currentTimeMillis());
							String transactionId="TN"+timestamp.getTime(); //TN3243432432423
							ps.setString(1, transactionId);
							ps.setDouble(2, withdrawalAmount);
							ps.setDate(3, new Date(System.currentTimeMillis()));
							ps.setString(4, "withdraw");
							ps.setLong(5,accId);
							ps.setLong(6,accId);
							
							ps.executeUpdate();
							
							System.out.println("Balance Updated!!");
							System.out.println("New Balance: "+balance);
						}
						else
						{
							System.out.println("Something went wrong!!");
						}
					}
					else
					{
						System.out.println("Insufficient Balance!!");
					}

				 }
				 System.out.println("Do you want to continue??(Y/N)");
				 status=br.readLine();
					
					if(status.equals("n") || status.equals("N"))
					{
						login=false;
					}
				 	
				 	break;
			    case 3: System.out.println("Please enter the receiver account Id:");
			    		long rcveId=Long.parseLong(br.readLine());
			    		
			    		System.out.println("Enter the amount:");
			    		double amount=Double.parseDouble(br.readLine());
			    		
			    		conn=DB_Connection.getConnection();
			    		
			    		long receiverId=0;
			    		
						ps=conn.prepareStatement("select * from Bank where accId=?");
						ps.setLong(1, rcveId);
						result=ps.executeQuery();
						
						while(result.next())
						{
							receiverId=result.getLong("accId");
						}
						
						double availableBalance=0.0;
						long senderId=0;
						ps=conn.prepareStatement("select accBalance,accId from Bank where userName=?");
						ps.setString(1, userName);
						result=ps.executeQuery();
						
						while(result.next())
						{
							availableBalance=result.getDouble("accBalance");
							senderId=result.getLong("accId");
						}
						
						if(receiverId==0)
						{
							System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
							System.out.println("Wrong receiver id!!");
							System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				

						}
						else if(availableBalance==0 || availableBalance<amount)
						{
							System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
							System.out.println("Insufficient account balance!!");
							System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			

						}
						else
						{
							availableBalance=availableBalance-amount;
							ps=conn.prepareStatement("update Bank set accBalance=? where userName=?");
							ps.setDouble(1, availableBalance);
							ps.setString(2, userName);
							
							if(ps.executeUpdate()>0)
							{
								ps=conn.prepareStatement("select accBalance from Bank where accId=?");
								ps.setLong(1, rcveId);
								double rcvBalance=0.0;
								result=ps.executeQuery();
								while(result.next())
								{
									rcvBalance=result.getDouble("accBalance");
								}
								
								rcvBalance=rcvBalance + amount;
								
								ps=conn.prepareStatement("update Bank set accBalance=? where accId=?");
								ps.setDouble(1, rcvBalance);
								ps.setLong(2, receiverId);
								
								if(ps.executeUpdate()>0)
								{
									 ps=conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
		                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		                                String transactionId="TN"+timestamp.getTime(); //TN3243432432423
		                                ps.setString(1, transactionId);
		                                ps.setDouble(2, amount);
		                                ps.setDate(3, new Date(System.currentTimeMillis()));
		                                ps.setString(4, "fund transfer");
		                                ps.setLong(5,senderId);
		                                ps.setLong(6,rcveId);
		                                
		                                ps.executeUpdate();
		                            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
									System.out.println("Transaction Completed!!");
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				

								}
								else
								{
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
									System.out.println("Transaction Failed!!");
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				

								}
								
							}
							
							
						}
						System.out.println("Do you want to continue??(Y/N)");
						 status=br.readLine();
						
						if(status.equals("n") || status.equals("N"))
						{
							login=false;
						}
						
						break;
						

			    		
					
				 	
				 	
				case 4:	conn=DB_Connection.getConnection();
						ps=conn.prepareStatement("select accBalance from Bank where userName=?");
						ps.setString(1, userName);
						result=ps.executeQuery();
						
						double balance=0.0;
						while(result.next())
						{
							balance=result.getDouble("accBalance");
						}
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");		
						System.out.println("Current Available Balance:"+balance);
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");;				

						 System.out.println("Do you want to continue??(Y/N)");
						 status=br.readLine();
							
							if(status.equals("n") || status.equals("N"))
							{
								login=false;
							}
						 	
						 	break;
				
				case 5: System.out.println("Please enter the existing Pin: ");
						String existingPin=br.readLine();
						
						System.out.println("Set new Pin:");
						String newPin=br.readLine();
						
						System.out.println("Retype new Pin:");
						String retypePin=br.readLine();
						
						
						ps=conn.prepareStatement("select userPin from Bank where userName=?");
						ps.setString(1, userName);
						
						result=ps.executeQuery();
						String accountPin=null;
						while(result.next())
						{
							accountPin=result.getString("userPin");
						}
						
						if(accountPin.equals(existingPin))
						{
							if(newPin.equals(retypePin))
							{
								ps=conn.prepareStatement("update Bank set userPin=? where userName=?");
								ps.setString(1, newPin);
								ps.setString(2, userName);
								
								if(ps.executeUpdate()>0)
								{
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
									System.out.println("Pin Changed!!");
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
									
								}
								
								else
								{
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
									System.out.println("Error in Pin change!!");
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
								}
							}
							else
							{
								System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
								System.out.println("Set new Pin and retype Pin must be same!!");
								System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
								
							}
						}
						else
						{
							System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
							System.out.println("Please enter correct existing Pin !!");
							System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
							
				
						}
						
						System.out.println("Do you want to continue??(Y/N)");
						status=br.readLine();
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
						if(status.equals("n") || status.equals("N"))
						{
							login=false;
						}
						break;
				case 6: ps=conn.prepareStatement("select * from Bank where userName=?");
						ps.setString(1, userName);
						
						result=ps.executeQuery();
						long accId=0;
						while(result.next())
						{
							accId=result.getLong("accId");
						}
						if(accId!=0)
						{
							ps=conn.prepareStatement("select * from transactions where senderAccountId=?");
							ps.setLong(1, accId);
							
							result=ps.executeQuery();
							System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
							System.out.println("TransactionId \t Amount \t Date \t Type ");
							System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
							while(result.next())
							{
								System.out.println(result.getString("transactionId")+"\t"+result.getDouble("transactionAmount")+"\t"+result.getDate("transactiondate")+"\t"+result.getString("transactionType"));
							}
							System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
						}
						System.out.println("Do you want to continue??(Y/N)");
						status=br.readLine();
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");		

						if(status.equals("n") || status.equals("N"))
						{
							login=false;
						}
						break;
						
						
				case 7:  login=false;
						 break;
				
					
						
				default:System.out.println("Wrong Input!!");		
							

			}
			
			
			
		}
			while(login);
			        }
			        else
			        {
			        	System.out.println("Please Enter 3(exit) and login through Checkings Account Credentials.");
			        	
			        }
			
			break;
			
			
				case 2:
					
					System.out.println("Enter the Account Type");
					System.out.println("Must Enter");
					System.out.println("**Savings**");
				    aType=br.readLine();

					conn=DB_Connection.getConnection();
			        ps=conn.prepareStatement("select * from Bank where userName=?");
			        ps.setString(1,userName);
			        result=ps.executeQuery();
			        accountType="";
			        
			        while(result.next())
			        {
			        	accountType=result.getString("accType");
			        }
			        
			        if(accountType.equalsIgnoreCase(aType))
			        {
			

					
					
					
					boolean login=true;
					do
					{
					
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
					System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$  WELCOME " + userName.toUpperCase() + " $$$$$$$$$$$$$$$$$$$$$$$$$$$$");
					System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Checkings Account $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
					System.out.println("1  --->   Deposit");
					System.out.println("2  --->   Withdraw");
					System.out.println("3  --->   Fund Transfer");
					System.out.println("4  --->   Balance Check");
					System.out.println("5  --->   Change Pin");
					System.out.println("6  --->   Transaction History/Receipt");
					System.out.println("7  --->   Exit / Logout");
					System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");		
					System.out.print("\t\t Enter your choice:"); 
					int operationNumber=Integer.parseInt(br.readLine());
					System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
					
					String status=null;
					
					switch(operationNumber)
					{
						case 1: System.out.println("Enter deposit amount:");
								double depositAmount=Double.parseDouble(br.readLine());
								
								if(depositAmount>0)
								{
									conn=DB_Connection.getConnection();
									ps=conn.prepareStatement("select * from Bank where userName=?");
									ps.setString(1, userName);
									result=ps.executeQuery();
									
									double balance=0.0;
									long accId=0;
									while(result.next())
									{
										balance=result.getDouble("accBalance");
										accId=result.getLong("accId");
									}
									
									balance=balance+depositAmount;
									
									ps=conn.prepareStatement("update Bank set accBalance=? where userName=?");
									ps.setDouble(1, balance);
									ps.setString(2, userName);
									
									if(ps.executeUpdate()>0)
									{
										ps=conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
										Timestamp timestamp = new Timestamp(System.currentTimeMillis());
										String transactionId="TN"+timestamp.getTime(); //TN3243432432423
										ps.setString(1, transactionId);
										ps.setDouble(2, depositAmount);
										ps.setDate(3, new Date(System.currentTimeMillis()));
										ps.setString(4, "deposit");
										ps.setLong(5,accId);
										ps.setLong(6,accId);
										
										ps.executeUpdate();
				
										
										System.out.println("Balance Updated!!");
										System.out.println("New Balance: "+balance);
									}
									else
									{
										System.out.println("Something went wrong!!");
									}
									
								}
								
								System.out.println("Do you want to continue??(Y/N)");
								 status=br.readLine();
								
								if(status.equals("n") || status.equals("N"))
								{
									login=false;
								}
								
								break;
						case 2:	 System.out.println("Enter Withdrawal amount:");
						 double withdrawalAmount=Double.parseDouble(br.readLine());
						 if(withdrawalAmount>0)
						 {
						    conn=DB_Connection.getConnection();
							ps=conn.prepareStatement("select * from Bank where userName=?");
							ps.setString(1, userName);
							result=ps.executeQuery();
							
							double balance=0.0;
							long accId=0;
							while(result.next())
							{
								balance=result.getDouble("accBalance");
								accId=result.getLong("accId");
							}
							
							
							if(balance>withdrawalAmount)
							{
								balance=balance-withdrawalAmount;
								ps=conn.prepareStatement("update Bank set accBalance=? where userName=?");
								ps.setDouble(1, balance);
								ps.setString(2, userName);
								
								if(ps.executeUpdate()>0)
								{
									ps=conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
									Timestamp timestamp = new Timestamp(System.currentTimeMillis());
									String transactionId="TN"+timestamp.getTime(); //TN3243432432423
									ps.setString(1, transactionId);
									ps.setDouble(2, withdrawalAmount);
									ps.setDate(3, new Date(System.currentTimeMillis()));
									ps.setString(4, "withdraw");
									ps.setLong(5,accId);
									ps.setLong(6,accId);
									
									ps.executeUpdate();
									
									System.out.println("Balance Updated!!");
									System.out.println("New Balance: "+balance);
								}
								else
								{
									System.out.println("Something went wrong!!");
								}
							}
							else
							{
								System.out.println("Insufficient Balance!!");
							}

						 }
						 System.out.println("Do you want to continue??(Y/N)");
						 status=br.readLine();
							
							if(status.equals("n") || status.equals("N"))
							{
								login=false;
							}
						 	
						 	break;
					    case 3: System.out.println("Please enter the receiver account Id:");
					    		long rcveId=Long.parseLong(br.readLine());
					    		
					    		System.out.println("Enter the amount:");
					    		double amount=Double.parseDouble(br.readLine());
					    		
					    		conn=DB_Connection.getConnection();
					    		
					    		long receiverId=0;
					    		
								ps=conn.prepareStatement("select * from Bank where accId=?");
								ps.setLong(1, rcveId);
								result=ps.executeQuery();
								
								while(result.next())
								{
									receiverId=result.getLong("accId");
								}
								
								double availableBalance=0.0;
								long senderId=0;
								ps=conn.prepareStatement("select accBalance,accId from Bank where userName=?");
								ps.setString(1, userName);
								result=ps.executeQuery();
								
								while(result.next())
								{
									availableBalance=result.getDouble("accBalance");
									senderId=result.getLong("accId");
								}
								
								if(receiverId==0)
								{
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
									System.out.println("Wrong receiver id!!");
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
								}
								else if(availableBalance==0 || availableBalance<amount)
								{
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
									System.out.println("Insufficient account balance!!");
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");		

								}
								else
								{
									availableBalance=availableBalance-amount;
									ps=conn.prepareStatement("update Bank set accBalance=? where userName=?");
									ps.setDouble(1, availableBalance);
									ps.setString(2, userName);
									
									if(ps.executeUpdate()>0)
									{
										ps=conn.prepareStatement("select accBalance from Bank where accId=?");
										ps.setLong(1, rcveId);
										double rcvBalance=0.0;
										result=ps.executeQuery();
										while(result.next())
										{
											rcvBalance=result.getDouble("accBalance");
										}
										
										rcvBalance=rcvBalance + amount;
										
										ps=conn.prepareStatement("update Bank set accBalance=? where accId=?");
										ps.setDouble(1, rcvBalance);
										ps.setLong(2, receiverId);
										
										if(ps.executeUpdate()>0)
										{
											 ps=conn.prepareStatement("insert into transactions values(?,?,?,?,?,?)");
				                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				                                String transactionId="TN"+timestamp.getTime(); //TN3243432432423
				                                ps.setString(1, transactionId);
				                                ps.setDouble(2, amount);
				                                ps.setDate(3, new Date(System.currentTimeMillis()));
				                                ps.setString(4, "fund transfer");
				                                ps.setLong(5,senderId);
				                                ps.setLong(6,rcveId);
				                                
				                                ps.executeUpdate();
				                                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");		
											System.out.println("Transaction Completed!!");
											System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			

										}
										else
										{
											System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
											System.out.println("Transaction Failed!!");
											System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				

										}
										
									}
									
									
								}
								System.out.println("Do you want to continue??(Y/N)");
								 status=br.readLine();
								
								if(status.equals("n") || status.equals("N"))
								{
									login=false;
								}
								
								break;
								

					    		
							
						 	
						 	
						case 4:	conn=DB_Connection.getConnection();
								ps=conn.prepareStatement("select accBalance from Bank where userName=?");
								ps.setString(1, userName);
								result=ps.executeQuery();
								
								double balance=0.0;
								while(result.next())
								{
									balance=result.getDouble("accBalance");
								}
								System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
								System.out.println("Current Available Balance:"+balance);
								System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				

								 System.out.println("Do you want to continue??(Y/N)");
								 status=br.readLine();
									
									if(status.equals("n") || status.equals("N"))
									{
										login=false;
									}
								 	
								 	break;
						
						case 5: System.out.println("Please enter the existing Pin: ");
								String existingPin=br.readLine();
								
								System.out.println("Set new Pin:");
								String newPin=br.readLine();
								
								System.out.println("Retype new Pin:");
								String retypePin=br.readLine();
								
								
								ps=conn.prepareStatement("select userPin from Bank where userName=?");
								ps.setString(1, userName);
								
								result=ps.executeQuery();
								String accountPin=null;
								while(result.next())
								{
									accountPin=result.getString("userPin");
								}
								
								if(accountPin.equals(existingPin))
								{
									if(newPin.equals(retypePin))
									{
										ps=conn.prepareStatement("update Bank set userPin=? where userName=?");
										ps.setString(1, newPin);
										ps.setString(2, userName);
										
										if(ps.executeUpdate()>0)
										{
											System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
											System.out.println("Pin Changed!!");
											System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
											
										}
										
										else
										{
											System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
											System.out.println("Error in Pin change!!");
											System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
											
										}
									}
									else
									{
										System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
										System.out.println("Set new Pin and retype Pin must be same!!");
										System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
										
									}
								}
								else
								{
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
									System.out.println("Please enter correct existing Pin !!");
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				
									
						
								}
								
								System.out.println("Do you want to continue??(Y/N)");
								status=br.readLine();
								System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
								if(status.equals("n") || status.equals("N"))
								{
									login=false;
								}
								break;
						case 6: ps=conn.prepareStatement("select * from Bank where userName=?");
								ps.setString(1, userName);
								
								result=ps.executeQuery();
								long accId=0;
								while(result.next())
								{
									accId=result.getLong("accId");
								}
								if(accId!=0)
								{
									ps=conn.prepareStatement("select * from transactions where senderAccountId=?");
									ps.setLong(1, accId);
									
									result=ps.executeQuery();
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
									System.out.println("TransactionId \t Amount \t Date \t Type ");
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
									while(result.next())
									{
										System.out.println(result.getString("transactionId")+"\t"+result.getDouble("transactionAmount")+"\t"+result.getDate("transactiondate")+"\t"+result.getString("transactionType"));
									}
									System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");	
								}
								System.out.println("Do you want to continue??(Y/N)");
								status=br.readLine();
								System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");				

								if(status.equals("n") || status.equals("N"))
								{
									login=false;
								}
								break;
								
							
						case 7:  login=false;
								 break;
						
							
								
						default:System.out.println("Wrong Input!!");		
									

					}
					
					
					
				}
					while(login);
			        }
			        else
			        {
			        	System.out.println("Please Enter 3(exit) and login through Savings Account Credentials.");
			        	
			        }
					
					break;
					
					
			    case 3:
			    	   end=false;
			            break;
			            
			       default:
			    	   System.out.println("\nInvalid Choice.");
			    	   
				}
				
			}
			while(end);
			
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");			
			System.out.println("Thanks for using ATM");
			System.out.println("Visit Again");
			System.out.println("Have a nice day!!");
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			  
			
		}
		else
		{
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");	
			System.out.println("Wrong userName/userPin!!");
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		}
			            
			            
			                                           
			       
			    	
			
			
			
		}
			



}

