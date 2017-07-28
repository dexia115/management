package com.operate.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import com.operate.tools.CommonUtil;
import com.operate.tools.Group;
import com.operate.tools.Groups;
import com.operate.tools.PageObj;
import com.operate.tools.PropertyFilter.MatchType;

@NoRepositoryBean
@Transactional(readOnly=true)
public class HibernateRepositoryImpl<T> implements HibernateRepository<T> {


	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Resource
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public PageObj<T> findPageByGroups(Groups groups, PageObj<T> page, String sql, Class<?> classes) {
		Integer currentPage = page.getCurrentPage();
		Integer pageSize = page.getPageSize();
		Map<String,Object> map = new HashMap<String,Object>();
		sql = createSqlByGroupsAll(sql, groups, map);

		String countSql = prepareCount(sql);
		
		Long totalCount = namedParameterJdbcTemplate.queryForObject(countSql, map, Long.class);

		String querySql = sql + " limit :offset ,:pageSize";
		long offset = (currentPage - 1) * pageSize; // 起始下标
		offset = offset > 0 ? offset : 0;
		offset = offset < totalCount ? offset : totalCount;
		map.put("offset",offset);
		map.put("pageSize",pageSize);// 分页用的
		
		List<?> items = namedParameterJdbcTemplate.query(querySql, map, BeanPropertyRowMapper.newInstance(classes));

		page.setTotalCount(totalCount);
		page.setItems(items);

		return page;
	}
	
	
	
	/**
	 * 复杂的sql
	 * 
	 * */
	public PageObj<T> findPageByGroupsNoSplit(Groups groups, PageObj<T> page, String sql, Class<?> classes) {
		Integer currentPage = page.getCurrentPage();
		Integer pageSize = page.getPageSize();
		Map<String,Object> map = new HashMap<String,Object>();
		sql = createSqlByGroupsAll(sql, groups, map);

		
		String countSql = prepareNoSplitCount(sql);
		
		Long totalCount = namedParameterJdbcTemplate.execute(countSql, map, null);

		String querySql = sql + " limit :offset ,:pageSize";
		long offset = (currentPage - 1) * pageSize; // 起始下标
		offset = offset > 0 ? offset : 0;
		offset = offset < totalCount ? offset : totalCount;
		map.put("offset",offset);
		map.put("pageSize",pageSize);// 分页用的
		
		List<?> items = namedParameterJdbcTemplate.query(querySql, map, BeanPropertyRowMapper.newInstance(classes));
		page.setTotalCount(totalCount);
		page.setItems(items);

		return page;
	}

	@SuppressWarnings("rawtypes")
	public List findByGroups(Groups groups, String sql, Class<?> classes) {
		Map<String, Object> map = new HashMap<String, Object>();
		sql = createSqlByGroupsAll(sql, groups, map);
		List<?> items = namedParameterJdbcTemplate.query(sql,  map, BeanPropertyRowMapper.newInstance(classes));
		return items;
	}

	
	



	private String prepareCount(String query) {
		query = "from " + StringUtils.substringAfterLast(query, "from");
		query = StringUtils.substringBefore(query, "order by");

		String count = "select count(*) " + query;
		return count;
	}
	
	private String prepareNoSplitCount(String query) {
		String count = "select count(*) from (" + query+") as total_sum";
		return count;
	}
	
	private String createSqlByGroupsAll(String sql, Groups groups, Map<String,Object> values) {
		// from段
		StringBuffer fromBuffer = new StringBuffer(" ");
		// where 段
		StringBuffer whereBufferQian = new StringBuffer(" where 1=1 ");
		StringBuffer whereBufferHou = new StringBuffer("");

		// 存取相同前缀
		List<String> Alias1 = new LinkedList<String>();
		fromBuffer.append(sql);

		appendGroups(groups, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
		
		sql = fromBuffer.toString() + whereBufferQian.toString() + whereBufferHou.toString();

		if (groups.getGroupby() != null && !"".equals(groups.getGroupby())) {
			sql += " group by " + groups.getGroupby();
		}

		String temp = "";
		String[] orderbys = groups.getOrderbys();
		String orderby = groups.getOrderby();
		if (orderbys != null && orderbys.length > 0) {
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < orderbys.length; i++) {// groups接受orderby数组进行多条件排序
				if (i == 0) {
					sBuffer.append(" order by ");
				}
				// order by 别名处理
				temp = orderbys[i].trim();

				if (temp.isEmpty())
					continue;

				sBuffer.append(temp + " "+orderbys[i]+",");
			}
			sql += sBuffer.deleteCharAt(sBuffer.length() - 1).toString();
		} else if (orderby != null && !"".equals(orderby.trim())) {
			// 处理order by的别名
			temp = orderby;

			sql += " order by " + temp + " "+groups.getOrder();
		}

		return sql;
	}

	private void appendGroups(Groups groups, StringBuffer fromBuffer, StringBuffer whereBufferQian,
			List<String> Alias1, StringBuffer whereBufferHou, Map<String,Object> values) {
		List<Group> groupList = groups.getGroupList();
		if (groupList != null) {
			List<Groups> childGroupsList = groups.getChildGroupsList();
			if (childGroupsList != null && !childGroupsList.isEmpty()) {
				for (Group group : groupList) {
					appendGroup(group, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
				}
				for (Groups childGroups : childGroupsList) {
					MatchType parentRelation = childGroups.getParentRelation();
					if (parentRelation == MatchType.AND) {
						whereBufferHou.append(" and ( ");
						appendGroups(childGroups, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
						whereBufferHou.append(" )");
					} else if (parentRelation == MatchType.OR) {
						whereBufferHou.append(" or ( ");
						appendGroups(childGroups, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
						whereBufferHou.append(" )");
					}
				}
			} else {
				StringBuffer whereAnd = new StringBuffer();
				StringBuffer whereOr = new StringBuffer();
				for (Group group : groupList) {
					if (group.getRelation().equals(MatchType.AND)) {
						appendGroup(group, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
					} else {
						if ("".equals(whereOr.toString())) {
							whereOr.append(" and ( ");
						}
					}
				}
				if (!"".equals(whereOr.toString())) {
					whereOr.append(" ) ");
				}
				whereBufferHou.append(whereAnd).append(whereOr);
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void appendGroup(Group group, StringBuffer fromBuffer, StringBuffer whereBufferQian, List<String> Alias1,
			StringBuffer whereBufferHou, Map<String, Object> values) {

		String propertyName = group.getPropertyName();

		boolean isOver = buildCase(group, whereBufferHou, propertyName);

		// 没有到最后（可能是 NULL/ NOT NULL）
		if (!isOver) {
			String matchCase = "";
			String whereHou = whereBufferHou.toString();
			if (whereHou.trim().length() > 0) {
				matchCase = whereHou.trim().substring(whereHou.trim().length() - 1, whereHou.trim().length());
			}
			MatchType matchType = group.getMatchType();
			if (matchType == MatchType.NULL || matchType == MatchType.NOTNULL) {
				whereBufferHou.append(" ");
				if (group.getRelation() == MatchType.AND) {
					if (!matchCase.equals("(")) {
						whereBufferHou.append(" and ");
					}
				} else {
					if (!matchCase.equals("(")) {
						whereBufferHou.append(" or ");
					}
				}

				switch (matchType) {
				case NOTIN:
					whereBufferHou.append(propertyName).append(" not in  ");
					break;
				case NOTNULL:
					whereBufferHou.append(propertyName).append(" is not null ");
					break;
				case NULL:
					whereBufferHou.append(propertyName).append(" is null ");
					break;
				}
			}
		}

		addValues(group, whereBufferHou, values);
	}

	private void addValues(Group group, StringBuffer whereBufferHou, Map<String, Object> values) {
		String propertyName = group.getPropertyName();
		Object propertyValue1 = group.getPropertyValue1();
		Object propertyValue2 = group.getPropertyValue2();
		MatchType matchType = group.getMatchType();
		if(!CommonUtil.isNull(propertyName)){
			propertyName = propertyName.replace(".", "");
			// 判断错误
			if (propertyValue1 == null && !(matchType == MatchType.NULL || matchType == MatchType.NOTNULL)) {
				System.out.println("传入值为空，但并不是查询NULL OR NOT NULL 请查证！");
			}else if ("".equals(group.getPropertyValue1()) && matchType == MatchType.NE) {
				whereBufferHou.append(" :"+propertyName+" ");
				values.put(propertyName, group.getPropertyValue1());
			} else if (propertyValue1 != null && !"".equals(propertyValue1)) {
				// 是in 或not in
				if (matchType == MatchType.IN || matchType == MatchType.NOTIN) {
					Collection<?> collection = (Collection<?>) propertyValue1;
					StringBuffer inBuffer = new StringBuffer(" ( ");
					inBuffer.append((":"+propertyName));
					List<Object> list = new ArrayList<Object>();
					for (Object object : collection) {
						list.add(object);
					}
					values.put(propertyName, list);
					inBuffer.append(" ) ");
					whereBufferHou.append(inBuffer);
				} else if (matchType == MatchType.BETWEEN) {
					if (propertyValue1.getClass() == Date.class || group.isDate() || propertyValue1.getClass() == java.sql.Timestamp.class) {
						values.put(propertyName, propertyValue1);
						values.put(propertyName+"2", propertyValue2);
						whereBufferHou.append(" :"+propertyName+" and :"+propertyName+"2 ");
					} else {
						System.out.println("BETWEEN 规定只能用于时间，数字请用大于小于进行");
					}
				} else {
					if (propertyValue1.getClass() == Date.class) {
						values.put(propertyName, propertyValue1);
						whereBufferHou.append(" :"+propertyName+" ");
					} else if (group.isDate()) {
						values.put(propertyName, propertyValue1);
						whereBufferHou.append(" :"+propertyName+" ");
					} else if (propertyValue1.getClass() == java.sql.Timestamp.class) {
						whereBufferHou.append(" :"+propertyName+" ");
					} else {
						whereBufferHou.append(" :"+propertyName+" ");
						if (group.getMatchType() == MatchType.LIKE) {
							values.put(propertyName, "%" + propertyValue1 + "%");
						} else {
							values.put(propertyName, propertyValue1);
						}
					}
				}
			} else if (propertyValue2 != null && !"".equals(propertyValue2)) {
				whereBufferHou.append(" :"+propertyName+" ");
				if (matchType == MatchType.LIKE) {
					values.put(propertyName, "%" + propertyValue2 + "%");
				} else {
					values.put(propertyName, propertyValue2);
				}
			}
		}
		
	}

	private boolean buildCase(Group group, StringBuffer whereBufferHou, String alisStr) {
		boolean isOver;
		isOver = true;
		whereBufferHou.append(" ");
		String matchCase = "";
		String whereHou = whereBufferHou.toString();
		if (whereHou.trim().length() > 0) {
			matchCase = whereHou.trim().substring(whereHou.trim().length() - 1,
					whereHou.trim().length());
		}
		if (group.getRelation() == MatchType.AND) {
			if (!matchCase.equals("(")) {
				whereBufferHou.append(" and ");
			}
		} else {
			if (!matchCase.equals("(")) {
				whereBufferHou.append(" or ");
			}
		}

		matchCase(group, whereBufferHou, alisStr);

		return isOver;
	}

	/**
	 * @param group
	 * @param whereBufferHou
	 * @param temName
	 *            别名
	 * @param alisStr
	 */
	@SuppressWarnings("incomplete-switch")
	private void matchCase(Group group, StringBuffer whereBufferHou, String alisStr) {
		switch (group.getMatchType()) {
		case EQ:
			whereBufferHou.append(alisStr).append(" = ");
			break;
		case LIKE:
			whereBufferHou.append(alisStr).append(" like ");
			break;
		case LT:
			whereBufferHou.append(alisStr).append(" < ");
			break;
		case LE:
			whereBufferHou.append(alisStr).append(" <= ");
			break;
		case GT:
			whereBufferHou.append(alisStr).append(" > ");
			break;
		case GE:
			whereBufferHou.append(alisStr).append(" >= ");
			break;
		case NE:
			whereBufferHou.append(alisStr).append(" <> ");
			break;
		case IN:
			whereBufferHou.append(alisStr).append(" in ");
			break;
		case NOTIN:
			whereBufferHou.append(alisStr).append(" not in  ");
			break;
		case BETWEEN:
			whereBufferHou.append(alisStr).append(" between  ");
			break;
		case NULL:
			whereBufferHou.append(alisStr).append(" is null ");
			break;
		case NOTNULL:
			whereBufferHou.append(alisStr).append(" is not null ");
			break;
		}
	}	

}
