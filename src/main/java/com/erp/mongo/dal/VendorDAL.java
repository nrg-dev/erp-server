package com.erp.mongo.dal;

import java.util.List;

import com.erp.dto.Member;
import com.ggl.mongo.model.Customer;
import com.ggl.mongo.model.Vendor;

import java.nio.file.Path;
//import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
public interface VendorDAL {

	public Vendor saveVendor(Vendor vendor);
	public List<Vendor> getVendor(String id);
	public Vendor updateVendor(Vendor vendor);
	public List<Vendor> loadVendor(List<Vendor> list);
	public void removeVendor(String id);

	
}