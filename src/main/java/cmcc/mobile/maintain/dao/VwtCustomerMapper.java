package cmcc.mobile.maintain.dao;

import cmcc.mobile.maintain.entity.VwtCustomer;

public interface VwtCustomerMapper {
	
public VwtCustomer getCustomerById(String id);

VwtCustomer selectByEncode(String encode);

public VwtCustomer seleByMobile(String customerMobile);
	
}
