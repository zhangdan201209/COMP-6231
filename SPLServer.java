import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class SPLServer  implements Runnable {

	static private	String CrNumber = Integer.toString(20000);
	static private String MrNumber  = Integer.toString(20000);
	static private Hashtable<Character, List<Record>> data;
	static private int localSize =0;
	static private String DepartmentName = "SPL";
	static private  File file;
	static private OutputStream log;
	static private int port = 5002;
	static int portdst1 = 5001;
	static int portdst2 = 5003;
	static String Namedst1 = "SPVM";
	static String Namedst2 = "SPB";
	static MyDatagramSocket mySocket ;
	static InetAddress receiverHost ;
	static int firstTime=1;
	public SPLServer(){
		if(firstTime==1)
		{
			try {
				mySocket = new MyDatagramSocket(port);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 try {
				receiverHost = InetAddress.getByName("127.0.0.1");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			
			data = new Hashtable<Character,List<Record>>();
			file =new File(DepartmentName+".log");
			try {
				log=new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String temp = new Date().toString()+"   "+DepartmentName+" DataBase Start\n";
			byte[] b=temp.getBytes();
			try {
				log.write(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			temp = "--------------------------------------------------------------------------\n";
			b=temp.getBytes();
			try {
				log.write(b);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			temp = new Date().toString()+"   "+DepartmentName+" UDPServer Start\n";
			b=temp.getBytes();
			try {
				log.write(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			createCRecord("SPL2111","Zhengri", "Jin", "Fat", 1);
			createCRecord("SPL2111","RiCheng", "Jin", "Fat", 1);
			createMRecord("SPL2111","Zhengen","Jin","1789 rue maricourt","jolicouer",new Date().toString(),1);
			localSize =3;
			firstTime=0;
			new Thread(this).start();
		}
		 
		
	}
	public boolean createCRecord(String badgeID,String firstName,String lastName,String description,int status){
		CriminalRecord cr ;
		synchronized	(CrNumber){
			if(status ==1)
			{
				cr = new CriminalRecord("CR"+CrNumber,firstName,lastName,description,Record.criminalStatus.capture);
				String temp = new Date().toString()+"   "+"ID: "+badgeID+" Add CRrecord No"+CrNumber+" "+firstName+" "+lastName+" "+description+" capture\n";
				byte[] b=temp.getBytes();
				try {
					log.write(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			else
			{
				cr = new CriminalRecord("CR"+CrNumber,firstName,lastName,description,Record.criminalStatus.onTheRun);
				String temp = new Date().toString()+"   "+"ID: "+badgeID+" Add CRrecord No"+CrNumber+" "+firstName+" "+lastName+" "+description+" onTheRun\n";
				byte[] b=temp.getBytes();
				try {
					log.write(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
		
				CrNumber=String.valueOf(Integer.parseInt(CrNumber)+1);
		}
		
		
		
		Set<Character> k = data.keySet();
		char newChar = lastName.toCharArray()[0];
		if(k.contains(newChar)){
			synchronized((List<Record>) data.get(newChar)){
				((List<Record>) data.get(newChar)).add(cr);
			}
			
			//System.out.println("Add existed");
		}
		else
		{
			List<Record> L = new LinkedList<Record>();
			L.add(cr);
			data.put(newChar, L);
			//System.out.println("Add NonExisted");
		}
		localSize++;	
		String temp = "--------------------------------------------------------------------------\n";
		byte[]b=temp.getBytes();
		try {
			log.write(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
		
	}
	public void printData(){
		Set<Character> k = data.keySet();
		for(char key = 'A'; key<='Z';key++){
		
			
			if(k.contains(key)){
				List<Record> L =(List<Record>) data.get(key);
				java.util.Iterator<Record> iter =  L.iterator();
				while( iter.hasNext()){
				 iter.next().print();
				}
			}
		}
		System.out.println(localSize);
		System.out.println("------------------------------------------");
	}
	/**
	 * PROVIDE TO CLIENT FOR CREATING MRRECORD
	 */
	public boolean createMRecord(String badgeID,String firstName, String lastName,
			String address, String lastLocation, String lastDate1, int status) {
		// TODO Auto-generated method stub
		MissingRecord mr;
		Date lastDate = new Date(lastDate1);
		synchronized	(MrNumber){
			if(status ==1)
			{
				mr = new MissingRecord("MR"+MrNumber,firstName,lastName,address,lastLocation,lastDate,Record.missingStatus.found);
				String temp = new Date().toString()+"   "+"ID: "+badgeID+" Add MRrecord No"+MrNumber+" "+firstName+" "+lastName+" "+address+" "+lastLocation+" "+lastDate+" found\n";
				byte[] b=temp.getBytes();
				try {
					log.write(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
				
			else
			{
				mr = new MissingRecord("MR"+MrNumber,firstName,lastName,address,lastLocation,lastDate,Record.missingStatus.missing);
				String temp = new Date().toString()+"   "+"ID: "+badgeID+" Add MRrecord No"+MrNumber+" "+firstName+" "+lastName+" "+address+" "+lastLocation+" "+lastDate+" missing\n";
				byte[] b=temp.getBytes();
				try {
					log.write(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
		
				MrNumber=String.valueOf(Integer.parseInt(MrNumber)+1);
			
		}
		
		Set<Character> k = data.keySet();
		char newChar = lastName.toCharArray()[0];
		if(k.contains(newChar)){
			synchronized((List<Record>) data.get(newChar)){
				((List<Record>) data.get(newChar)).add(mr);
			}
		
			//System.out.println("Add existed");
		}
		else
		{
			List<Record> L = new LinkedList<Record>();
			L.add(mr);
			data.put(newChar, L);
			//System.out.println("Add NonExisted");
		}
		localSize++;
		String temp = "--------------------------------------------------------------------------\n";
		byte[] b=temp.getBytes();
		try {
			log.write(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public int getRecordsCount(String badgeID){
		int size;
		String temp =(new Date()) +"ID: "+badgeID+":  Client Request for getting the total count " +DepartmentName+"\n";
		byte []b=temp.getBytes();
		try {
			log.write(b);
		} catch (IOException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		size =0;
		for(char key = 'A'; key<='Z';key++){
		
		Set<Character> k = data.keySet();
		if(k.contains(key)){
			
			List<Record> L =(List<Record>) data.get(key);
			for (Record r : L) {
				
				 size++;
				
				
			}
		}
	}	
		localSize = size;
		@SuppressWarnings("resource")
		MyDatagramSocket mySocket2 = null;
		try {
			mySocket2 = new MyDatagramSocket();
		} catch (SocketException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		try {
			mySocket2.sendMessage(receiverHost, portdst1,"1Request Count from "+DepartmentName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp =(new Date()) +":  Send Count Request to " +Namedst1+"\n";
		b=temp.getBytes();
		try {
			log.write(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		try {
			size+=Integer.parseInt(mySocket2.receiveMessage().trim());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp =(new Date()) +":  Receive Reply from " +Namedst1+"\n";
		b=temp.getBytes();
		try {
			log.write(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			mySocket2.sendMessage(receiverHost, portdst2,"1Request Count from "+DepartmentName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp =(new Date()) +":  Send Count Request to " +Namedst2+"\n";
		b=temp.getBytes();
		try {
			log.write(b);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			size+=Integer.parseInt(mySocket2.receiveMessage().trim());
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		temp =(new Date()) +":  Receive Reply from " +Namedst2+"\n";
		b=temp.getBytes();
		try {
			log.write(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		temp = "--------------------------------------------------------------------------\n";
		b=temp.getBytes();
		try {
			log.write(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return size;
}

/**
 * PROVIDE TO CLIENT FOR EDITING THE RECORD
 */
public boolean editCRecord(String badgeID,String lastName, String Num,int status ){
	Set<Character> k = data.keySet();
	char newChar = lastName.toCharArray()[0];
	if(k.contains(newChar)){
		
		List<Record> L =(List<Record>) data.get(newChar);
		synchronized(L) {
			for (Record r : L)
			{
				if(r.Number.equals(Num))
				{
					if(status==1){
						((CriminalRecord)r).status= Record.criminalStatus.capture;
						String temp = new Date().toString()+"   "+"ID: "+badgeID+"Edit CRrecord "+lastName+" "+Num+" to capture\n";
						byte[] b=temp.getBytes();
						try {
							log.write(b);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return true;
					}
					
					else
					{
						((CriminalRecord)r).status= Record.criminalStatus.onTheRun;	
						String temp = new Date().toString()+"   "+"ID: "+badgeID+"Edit CRrecord "+lastName+" "+Num+" to onTheRun\n";
						byte[] b=temp.getBytes();
						try {
							log.write(b);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return true;
					}
						
					
				}
			}
			
		}
		
	}
	return false;
}
	@Override

	/**
	 * MULTI THREAD RUN THE UDP SERVER LISTIONING FOR THE REQUEST
	 * REQUIRED BY THE GETCOUNT FUNTION
	 */
	public void run() {
		byte[] b;
		String temp = new Date().toString()+"   "+DepartmentName+" UDPServer Start\n";
		b=temp.getBytes();
		try {
			log.write(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			
		
			while(true){
				String rec= mySocket.receiveMessage();
				temp =(new Date()) +":  Receive " +rec+"\n";
				b=temp.getBytes();
				log.write(b);
				System.out.println(rec.substring(0,3));
				if(rec.charAt(0)=='1'){
					mySocket.sendMessage(receiverHost, mySocket.recPort(),Integer.toString(localSize));
					temp =(new Date()) +":  Send Count "+localSize+" to " +Namedst1+"\n";
					b=temp.getBytes();
					log.write(b);
				}
				else
				{
					
					transferRecordServer(rec.substring(rec.lastIndexOf(' ')+1).trim(),rec.substring(1,rec.lastIndexOf(' ')));
					mySocket.sendMessage(receiverHost, mySocket.recPort(),"Transfer Done");
					temp=(new Date()) +":  Transfer Done \n";
					b=temp.getBytes();
					log.write(b);
				}
			
				

			
					
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	 
		public boolean transferRecord(String badgeID, String recordID,
				String StationServerName) {
			// TODO Auto-generated method stub
			//Check all the record in the hash table
		int size=0;;
			for(Iterator<Character> itr = data.keySet().iterator(); itr.hasNext();){ 
				Character key =  (Character) itr.next(); 
				List<Record> value = (List<Record>) data.get(key);
				synchronized(value){
						for(Iterator<Record> litr = value.iterator(); litr.hasNext();)
						{
							Record r = (Record) litr.next(); 
							
							if(r.Number.equals(recordID))  // IF the recordID match, delete local and transfer to the remote;
							{
								
								MyDatagramSocket mySocket2 = null;
								try {
									mySocket2 = new MyDatagramSocket();
								} catch (SocketException e3) {
									// TODO Auto-generated catch block
									e3.printStackTrace();
								}
								
								
								
								
								
									if(StationServerName.equals(Namedst1))
									{
										
										try {
											mySocket2.sendMessage(receiverHost, portdst1,"2"+badgeID+" "+r.toString());
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										String temp =(new Date()) +":  Send Transfer Request to " +Namedst1+"\n";
										byte[] b=temp.getBytes();
										try {
											log.write(b);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
											
										try {
											mySocket2.receiveMessage().trim();
										} 
										catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										temp =(new Date()) +":  Receive Reply from " +Namedst1+"\n";
										b=temp.getBytes();
										try {
											log.write(b);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
									}
									
										else if(StationServerName.equals(Namedst2))
										{
											try {
												mySocket2.sendMessage(receiverHost, portdst2,"2"+badgeID+" "+r.toString());
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											String temp =(new Date()) +":  Send Transfer Request to " +Namedst2+"\n";
											byte[] b=temp.getBytes();
											try {
												log.write(b);
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
												
											try {
												mySocket2.receiveMessage().trim();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											temp =(new Date()) +":  Receive Reply from " +Namedst2+"\n";
											b=temp.getBytes();
											try {
												log.write(b);
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
										}
										value.remove(r);
										//Create the successfully log
										String temp =(new Date()) +":  ID: " +badgeID+" Transfer Record "+recordID+" to "+StationServerName+ "\n";
										byte[] b=temp.getBytes();
										try {
											log.write(b);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										return true;
								
							
							}
							
						}	
						}
				}
			//Create the Fail log
			    String temp =(new Date()) +":  FAIL(FILE NOT EXIST)  ID: " +badgeID+" Transfer Record "+recordID+" to "+StationServerName+ "\n";
				byte[] b=temp.getBytes();
				try {
					log.write(b);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return false;
		}
		/**
		 * PROVIDE TO SERVER TO TRANSFER RECORD
		 */
		
		private boolean transferRecordServer(String record,String badgeID) {
			// TODO Auto-generated method stub
			byte[] b;
			//Build Local Record according to the receiving
			try{
				
				if(record.charAt(0)=='C')
				{
					CriminalRecord cr = new CriminalRecord(record);
				    //ADD to the hashtable
					Set<Character> k = data.keySet();
					char newChar = cr.lastName.toCharArray()[0];
					if(k.contains(newChar)){
						List<Record> i=(List<Record>) data.get(newChar);
						synchronized(i){
							if(!i.contains(cr)){
								((List<Record>) data.get(newChar)).add(cr);
							}
							
						}
					
						//System.out.println("Add existed");
					}
					else
					{
						List<Record> L = new LinkedList<Record>();
						L.add(cr);
						data.put(newChar, L);
						//System.out.println("Add NonExisted");
					}
					localSize++;
				    String temp =(new Date()) +":  Receive Transfer ID: " +badgeID+" Transfer CRecord "+record+ "\n";
					b=temp.getBytes();
					try {
						log.write(b);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				  
				}
				else
				{
					MissingRecord mr = new MissingRecord(record);
					Set<Character> k = data.keySet();
					char newChar = mr.lastName.toCharArray()[0];
					if(k.contains(newChar)){
						List<Record> i=(List<Record>) data.get(newChar);
						synchronized(i){
							if(!i.contains(mr))
							((List<Record>) data.get(newChar)).add(mr);
						}
					
						//System.out.println("Add existed");
					}
					else
					{
						List<Record> L = new LinkedList<Record>();
						L.add(mr);
						data.put(newChar, L);
						//System.out.println("Add NonExisted");
					}
					localSize++;
					  String temp =(new Date()) +":  Receive Transfer ID: " +badgeID+" Transfer MRecord "+record+ "\n";
						b=temp.getBytes();
						try {
							log.write(b);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}catch(Exception e){
				System.out.print(record);
			}
			
			return true;
		}

		
		
}
