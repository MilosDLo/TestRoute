package net.rooting.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="es_order")
public class Order {

	private Long id;
	private Integer statusId;
	private Long companyId;
	private Long orderTypeId;
	private String shipmentRoute;
	private Long orderDay;
	private Long org_id;
	
	public Order() {
		// TODO Auto-generated constructor stub
	}
	
	public Order(Long id) {
		this.id = id;
	}
		
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "order_day")
	public Long getOrderDay() {
		return orderDay;
	}

	public void setOrderDay(Long dateNo) {
		this.orderDay = dateNo;
	}
	
	@Column(name="shipment_route")
	public String getShipmentRoute() {
		return shipmentRoute;
	}

	public void setShipmentRoute(String shipmentRoute) {
		this.shipmentRoute = shipmentRoute;
	}
	
	@Column(name = "status_id")
	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer status) {
		this.statusId = status;
	}
	
	@Column(name = "order_type_id")
	public Long getOrderTypeId() {
		return orderTypeId;
	}

	public void setOrderTypeId(Long orderTypeId) {
		this.orderTypeId = orderTypeId;
	}
	
	@Column(name = "company_id")
	public Long getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	@Column(name = "org_id")
	public Long getOrgId() {
		return this.companyId;
	}

	public void setOrgId(Long companyId) {
		this.companyId = companyId;
	}
	
	
}
