/** 
 * @(#)UserRowMapper.java Jun 2, 2010
 * 
 * Copyright (c) 1995-2010 Wonders Information Co.,Ltd. 
 * 1518 Lianhang Rd,Shanghai 201112.P.R.C.
 * All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Wonders Group.
 * (Social Security Department). You shall not disclose such
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with Wonders Group. 
 *
 * Distributable under GNU LGPL license by gnu.org
 */

package com.sw.mapper;


import com.sw.util.InstanceUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xieguojun
 * @version $Revision$ Jun 2, 2010
 * @author ($Date$ modification by $Author$)
 * @since 1.0
 */
public class UserRowMapper extends AbstractRowResolve implements RowMapper {

	public UserRowMapper(Class clazz) {
		this.clazz = clazz;
	}

	public Object mapRow(ResultSet rs, int pos) throws SQLException {
		Object dto = null;
		dto = InstanceUtils.getInstance(clazz);
		invoke(dto, rs);

		return dto;
	}

}
