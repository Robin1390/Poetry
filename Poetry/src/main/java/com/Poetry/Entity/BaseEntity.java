package com.Poetry.Entity;



import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class BaseEntity implements Serializable{
	
	private static final long serialVersionUID = -5553219396645924973L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	protected Integer id;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on", nullable = true, updatable = false)
	protected Date createdOn;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_on", nullable = true)
	protected Date updatedOn;
	@Column(name = "created_by", nullable = true, updatable = false)
	protected Integer createdBy;
	@Column(name = "updated_by", nullable = true)
	protected Integer updatedBy;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
/*	@Override
	public abstract int hashCode();
	@Override
	public abstract boolean equals(Object obj);
	@Override
	public abstract String toString();*/
	
}