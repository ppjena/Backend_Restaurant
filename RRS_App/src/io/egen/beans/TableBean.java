package io.egen.beans;

public class TableBean {
	
	public TableBean(String tableName, String confirmationCode, int size, String since) {
		super();
		this.tableName = tableName;
		this.confirmationCode = confirmationCode;
		this.size = size;
		this.status = (confirmationCode != null) ? "OCCUPIED" : "EMPTY";
		this.since = since;
	}
	
	
	
	public String getTableName() {
		return tableName;
	}
	public String getConfirmationCode() {
		return confirmationCode;
	}
	public int getSize() {
		return size;
	}
	public String getStatus() {
		return status;
	}
	public String getSince() {
		return since;
	}



	private String tableName;
	private String confirmationCode;
	private int size;
	private String status;
	private String since;

}
