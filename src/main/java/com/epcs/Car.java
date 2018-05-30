package com.epcs;

public class Car {
	private String id;// 车ID
	private String authKey;// 授权Key
	private long fkCompany;// 公司Id
	private String plate;// 车牌
	private int carType;// 车类型 1:清洗车,2:洗扫车,3:垃圾车,4:路面养护车,5:下水道疏通车
	private String fence;// 电子围栏JSON格式[{lat:xx,lng:xx},{lat:xx,lng:xx}]
	private boolean areAlerted;// 是否保存报警信息

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public long getFkCompany() {
		return fkCompany;
	}

	public void setFkCompany(long fkCompany) {
		this.fkCompany = fkCompany;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public int getCarType() {
		return carType;
	}

	public void setCarType(int carType) {
		this.carType = carType;
	}

	public boolean getAreAlerted() {
		return areAlerted;
	}

	public void setAreAlerted(boolean areAlerted) {
		this.areAlerted = areAlerted;
	}

	public String getFence() {
		return fence;
	}

	public void setFence(String fence) {
		this.fence = fence;
	}

}
