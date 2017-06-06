package net.rooting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.esteh.mobile.domain.Company;

@Entity
@Table(name="es_shipment_route")
public class ShipmentRoute {
	
	private Long id;
	private String extCode;
	private long company_id;
	private String storageId;
	
	
	
	
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	@Column(name="ext_code")
	public String getExtCode() {
		return extCode;
	}
	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}
	
	@Column(name="company_id")
	public long getCompanyId() {
		return company_id;
	}
	public void setCompanyId(long company_id) {
		this.company_id = company_id;
	}
	
	@Column(name="storage_id")
	public String getStorageId() {
		return storageId;
	}
	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}
	
	
	
	
}
