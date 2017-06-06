package net.rooting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="es_org")
public class Org{

	private Long id;
	private String name = "";
	private String extCode = "";
	private Long longitude;	
	private Long latitude;
	private String address1 = "";
	
	
	public Org(Long id, String name, String extCode, Long longitude, Long latitude,String address1) {
		super();
		this.id = id;
		this.name = name;
		this.extCode = extCode;
		this.longitude = longitude;
		this.latitude = latitude;
		this.address1 = address1;
		
	}		
	public Org() {		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "ext_code")
	public String getExtCode() {
		return extCode;
	}
	
	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}
	
	@Column(name = "longitude")
	public Long getLongitude() {
		return longitude;
	}


	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

	@Column(name = "latitude")
	public Long getLatitude() {
		return latitude;
	}


	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}
	
	@Column(name = "address1")
	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	
	
	
	
}
