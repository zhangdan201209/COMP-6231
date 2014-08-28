


public class Record {

	/**
	 * @param args
	 */
	String firstName;
	String lastName;
	String Number;
	enum missingStatus{found,missing} ;
	enum criminalStatus{capture,onTheRun};
	enum recordType {MissingRecord,CriminalRecord};
	public Record(){
		
	}
	public Record(String firstName,String lastName,String Number){
		this.firstName = firstName;
		this.lastName = lastName;
		this.Number = Number;
		
	}
	public boolean equals(Record cr){
		if(cr.Number==this.Number)
		return true;
		else
			return false;
		
	}
	public void print(){
		System.out.print(Number+" "+firstName+" "+lastName+" |");
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
