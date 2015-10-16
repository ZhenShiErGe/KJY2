package com.xyz.kjy.db;


public class Customer  {
	private Integer id;
	private String storeName;//店名，唯一
	private String storeOwnerName;
	private String phone;
	private String address;
	private String productionName;
	private Integer unitPrice;
	public Customer(){}
	public Customer(Integer id, String storeName, String storeOwnerName,
			String phone, String address, String productionName,
			Integer unitPrice) {
		super();
		this.id = id;
		this.storeName = storeName;
		this.storeOwnerName = storeOwnerName;
		this.phone = phone;
		this.address = address;
		this.productionName = productionName;
		this.unitPrice = unitPrice;
		
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreOwnerName() {
		return storeOwnerName;
	}
	public void setStoreOwnerName(String storeOwnerName) {
		this.storeOwnerName = storeOwnerName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProductionName() {
		return productionName;
	}
	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}
	public Integer getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Integer unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", storeName=" + storeName
				+ ", storeOwnerName=" + storeOwnerName + ", phone=" + phone
				+ ", address=" + address + ", productionName=" + productionName
				+ ", unitPrice=" + unitPrice  + "]";
	}
	
	
	
}
