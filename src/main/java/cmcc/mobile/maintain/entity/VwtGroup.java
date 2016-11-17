package cmcc.mobile.maintain.entity;

public class VwtGroup {
    private Integer id;

    private String customerMobile;

    private String customerName;

    private String areaId;

    private String vId ;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile == null ? null : customerMobile.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId == null ? null : areaId.trim();
    }

	public String getvId() {
		return vId;
	}

	public void setvId(String vId) {
		this.vId = vId;
	}
    
    
}