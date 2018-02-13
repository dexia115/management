package com.operate.vo;

import java.util.Date;
import com.operate.enums.CheckState;
import lombok.Data;

@Data
public class StoreVo {
	
	private CheckState checkState;
	
	private String name;
	
	private String mobile;
	
	private Date times;

}
