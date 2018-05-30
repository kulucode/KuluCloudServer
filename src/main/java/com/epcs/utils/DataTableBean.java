package com.epcs.utils;

/**
 * Device entity. @author MyEclipse Persistence Tools
 */

public class DataTableBean implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2290190087884533678L;

	private String sEcho;
	private Integer iColumns;
	private String sColumns;
	private Integer iDisplayStart;
	private Integer iDisplayLength;
	private String sSearch;
	private boolean bRegex;
	private Integer iSortCol_0;
	private String sSortDir_0;
	private Integer iSortingCols;

	/** params */
	private Integer targetId;
	private String startTimeStr;
	private String endTimeStr;

	private Long carId;
	private Long companyId;
	private Long alertId;
	private String plate;

	private Integer status;
	private String sKey;

	// Constructors

	/** default constructor */
	public DataTableBean() {
	}

	public Integer getTargetId() {
		return targetId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	public String getsEcho() {
		return sEcho;
	}

	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}

	public Integer getiColumns() {
		return iColumns;
	}

	public void setiColumns(Integer iColumns) {
		this.iColumns = iColumns;
	}

	public String getsColumns() {
		return sColumns;
	}

	public void setsColumns(String sColumns) {
		this.sColumns = sColumns;
	}

	public Integer getiDisplayStart() {
		return iDisplayStart;
	}

	public void setiDisplayStart(Integer iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}

	public Integer getiDisplayLength() {
		return iDisplayLength;
	}

	public void setiDisplayLength(Integer iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}

	public String getsSearch() {
		return sSearch;
	}

	public void setsSearch(String sSearch) {
		this.sSearch = sSearch;
	}

	public boolean isbRegex() {
		return bRegex;
	}

	public void setbRegex(boolean bRegex) {
		this.bRegex = bRegex;
	}

	public Integer getiSortCol_0() {
		return iSortCol_0;
	}

	public void setiSortCol_0(Integer iSortCol_0) {
		this.iSortCol_0 = iSortCol_0;
	}

	public String getsSortDir_0() {
		return sSortDir_0;
	}

	public void setsSortDir_0(String sSortDir_0) {
		this.sSortDir_0 = sSortDir_0;
	}

	public Integer getiSortingCols() {
		return iSortingCols;
	}

	public void setiSortingCols(Integer iSortingCols) {
		this.iSortingCols = iSortingCols;
	}

	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getsKey() {
		return sKey;
	}

	public void setsKey(String sKey) {
		this.sKey = sKey;
	}

}