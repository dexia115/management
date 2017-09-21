package com.operate.config;

import java.io.Serializable;

/**
 * Rabbit消息体
 * @author Administrator
 *
 */
public class MessageBody implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	
	//操作类型  add/update/delete
	private String type;
	
	//操作的实体名称
	private String entityName;
	
	//备注新
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
