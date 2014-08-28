

import java.util.Date;


public class CriminalRecord extends Record{

	/**
	 * @param args
	 */
	criminalStatus status;
	String description;
	public CriminalRecord(String Number,String firstName,String lastName,String description,criminalStatus status){
		super(firstName,lastName,Number);
		this.description = description;
		this.status = status;
	}
	public CriminalRecord(String all){
		super();

		int i;
		
		i=all.lastIndexOf('|');
		String  status=all.substring(i+1,all.length());
		if(status.equals("capture"))
			this.status = Record.criminalStatus.capture;
			else
			this.status = Record.criminalStatus.onTheRun;
		all = all.substring(0,i);
		
		
		i=all.lastIndexOf('|');
		String description = all.substring(i+1,all.length());
		this.description = description;
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
	public void print(){
	super.print();
	System.out.println(description+" |"+status);
	}
	public String toString(){
		return 	Number+'|'+firstName+"|"+lastName+"|"+description+'|'+status.toString();
	}
	public boolean equals(CriminalRecord cr){
		if(cr.Number==this.Number)
		return true;
		else
			return false;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CriminalRecord c =new  CriminalRecord("CR00001","Adam3", "Carte", "Tall", Record.criminalStatus.capture);
		System.out.println(c.toString());
		CriminalRecord b = new CriminalRecord(c.toString());
		System.out.println(b.toString());
	}

}
