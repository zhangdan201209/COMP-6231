

import java.util.Date;


public class MissingRecord extends Record {
	
	/**
	 * @param args
	 */
	String address;
	String lastLocation;
	Date lastDate;
	missingStatus status;
	
	
	public MissingRecord(String Number,String firstName,String lastName,String address,
			String lastLocation,	Date lastDate,missingStatus status){
		super(firstName,lastName,Number);
		this.address = address;
		this.lastLocation =lastLocation;
		this.lastDate = lastDate;	
		this.status = status;
	}
	public boolean equals(MissingRecord mr){
		if(mr.Number==this.Number)
		return true;
		else
			return false;
		
	}
	public MissingRecord(String all){
		super();

		int i;
		
		i=all.lastIndexOf('|');
		String  status=all.substring(i+1,all.length());
		if(status.equals("found"))
			this.status = Record.missingStatus.found;
			else
			this.status = Record.missingStatus.missing;
		all = all.substring(0,i);
		
		
		i=all.lastIndexOf('|');
		String description = all.substring(i+1,all.length());
		this.lastDate = new Date(description);
		all=all.substring(0,i);
	
		i=all.lastIndexOf('|');
		String lastLocation = all.substring(i+1,all.length());
		this.lastLocation = lastLocation;
		all=all.substring(0,i);
		
		i=all.lastIndexOf('|');
		String address = all.substring(i+1,all.length());
		this.address = address;
		all=all.substring(0,i);
		
		i=all.lastIndexOf('|');
		String lastName = all.substring(i+1,all.length());
		this.lastName = lastName;
		all=all.substring(0,i);
		
		i=all.lastIndexOf('|');
		String firstName = all.substring(i+1,all.length());
		this.firstName = firstName;
		all=all.substring(0,i);
		
		i=all.lastIndexOf('|');
		String Number = all.substring(i+1,all.length());
		this.Number = Number;
		
		
		
	
	}
	public String toString(){
		return 	Number+'|'+firstName+"|"+lastName+"|"+address+'|'+lastLocation+'|'+lastDate.toString()+'|'+status.toString();
	}
	public void print(){
	super.print();
	System.out.println(address+" |"+lastLocation+" |"+lastDate+" |"+status);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MissingRecord c =new  MissingRecord("SPB2111","Tianyi","Li","1790 rue maricourt","Verdun",new Date(),Record.missingStatus.found);
		System.out.println(c.toString());
		MissingRecord b = new MissingRecord(c.toString());
		System.out.println(b.toString());
	}

}
