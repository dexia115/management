package com.operate.repository;

import java.lang.reflect.Field;
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
import com.operate.pojo.BaseEntity;
import com.operate.tools.CommonUtil;
import com.operate.tools.Group;
import com.operate.tools.Groups;
import com.operate.tools.PageObj;
import com.operate.tools.PropertyFilter.MatchType;
import com.operate.tools.ReflectionUtils;
import com.operate.tools.SearchAnnotation;

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
	 * 不分割sql
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

	
	



	protected String prepareCountHql(String orgHql) {
		String fromHql = orgHql;
		// select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");

		String countHql = "select count(id) " + fromHql;
		return countHql;
	}
	



	private String prepareCount(String query) {
		// select子句与order by子句会影响count查询,进行简单的排除.
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

		try {
			appendGroups2(groups, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sql = fromBuffer.toString() + whereBufferQian.toString() + whereBufferHou.toString();

		if (groups.getGroupby() != null && !"".equals(groups.getGroupby())) {
			sql += " group by " + groups.getGroupby();
		}

		String temp = "";
		if (groups.getOrderbys() != null && groups.getOrderbys().length > 0) {
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < groups.getOrderbys().length; i++) {// groups接受orderby数组进行多条件排序
				if (i == 0) {
					sBuffer.append(" order by ");
				}
				// order by 别名处理
				temp = groups.getOrderbys()[i];

				if (temp.isEmpty())
					continue;

				sBuffer.append(temp + " "+groups.getOrders()[i]+",");
			}
			sql += sBuffer.deleteCharAt(sBuffer.length() - 1).toString();
		} else if (null != groups.getOrderby() && !"".equals(groups.getOrderby().trim())) {
			// 处理order by的别名
			temp = groups.getOrderby();

			sql += " order by " + temp + " "+groups.getOrder();
		}

		return sql;
	}

	private void appendGroups2(Groups groups, StringBuffer fromBuffer, StringBuffer whereBufferQian,
			List<String> Alias1, StringBuffer whereBufferHou, Map<String,Object> values) {
		if (groups.getGroupList() == null) {
			System.out.println("groups的GroupList不能为空！");
		} else {
			if (groups.getChildGroupsList() != null && !groups.getChildGroupsList().isEmpty()
					|| groups.getParentRelation() != null) {
				for (Group group : groups.getGroupList()) {
					appendGroup2(group, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
				}
				for (Groups tGroup : groups.getChildGroupsList()) {
					if (tGroup.getParentRelation() == MatchType.AND) {
						whereBufferHou.append(" and ( ");
						appendGroups2(tGroup, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
						whereBufferHou.append(" )");
					} else if (tGroup.getParentRelation() == MatchType.OR) {
						whereBufferHou.append(" or ( ");
						appendGroups2(tGroup, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
						whereBufferHou.append(" )");
					}
				}
			} else {
				StringBuffer whereAnd = new StringBuffer();
				StringBuffer whereOr = new StringBuffer();
				for (Group group : groups.getGroupList()) {
					if (group.getRelation().equals(MatchType.AND)) {
						appendGroup2(group, fromBuffer, whereBufferQian, Alias1, whereBufferHou, values);
					} else {
						if (whereOr.toString().equals("")) {
							whereOr.append(" and ( ");
						}
					}

				}
				if (!whereOr.toString().equals("")) {
					whereOr.append(" ) ");
				}
				whereBufferHou.append(whereAnd).append(whereOr);
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void appendGroup2(Group group, StringBuffer fromBuffer, StringBuffer whereBufferQian, List<String> Alias1,
			StringBuffer whereBufferHou, Map<String, Object> values) {

		String propertyName = group.getPropertyName();

		boolean isOver = buildCase2(group, whereBufferHou, propertyName);

		// 没有到最后（可能是 NULL/ NOT NULL）
		if (!isOver) {
			String matchCase = "";
			if (whereBufferHou.toString().trim().length() > 0) {
				matchCase = whereBufferHou.toString().trim().substring(whereBufferHou.toString().trim().length() - 1,
						whereBufferHou.toString().trim().length());
			}
			if (group.getMatchType() == MatchType.NULL || group.getMatchType() == MatchType.NOTNULL) {
				whereBufferHou.append(" ");
				if (group.getRelation() == MatchType.AND) {
					try {
						if (!matchCase.equals("(")) {
							whereBufferHou.append(" and ");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						if (!matchCase.equals("(")) {
							whereBufferHou.append(" or ");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				switch (group.getMatchType()) {
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
			} else {
				System.out.println("拼接处的name不能以对象结尾，统一要以基本类型结尾！");
			}
		}

		addValues(group, whereBufferHou, values);
	}

	private void addValues(Group group, StringBuffer whereBufferHou, Map<String, Object> values) {
		String propertyName = group.getPropertyName();
		if(!CommonUtil.isNull(propertyName)){
			propertyName = propertyName.replace(".", "");
			// 判断错误
			if (group.getPropertyValue1() == null
					&& !(group.getMatchType() == MatchType.NULL || group.getMatchType() == MatchType.NOTNULL)) {
				System.out.println("传入值为空，但并不是查询NULL OR NOT NULL 请查证！");
			}else if ("".equals(group.getPropertyValue1()) && group.getMatchType() == MatchType.NE) {
				whereBufferHou.append(" :"+propertyName+" ");
				values.put(propertyName, group.getPropertyValue1());
			} else if (group.getPropertyValue1() != null && !"".equals(group.getPropertyValue1())) {
				// 是in 或not in
				if (group.getMatchType() == MatchType.IN || group.getMatchType() == MatchType.NOTIN) {
					Collection<?> collection = (Collection<?>) group.getPropertyValue1();
					StringBuffer inBuffer = new StringBuffer(" ( ");
					inBuffer.append((":"+propertyName));
					List<Object> list = new ArrayList<Object>();
					for (Object object : collection) {
						list.add(object);
					}
					values.put(propertyName, list);
					inBuffer.append(" ) ");
					whereBufferHou.append(inBuffer);
				} else if (group.getMatchType() == MatchType.BETWEEN) {
					// 如果是bwt
					if (group.getPropertyValue2() == null) {
						System.out.println("第二个参数不能为空");
					}
					if (group.getPropertyValue1().getClass() == Date.class || group.isDate()
							|| group.getPropertyValue1().getClass() == java.sql.Timestamp.class) {
						values.put(propertyName, group.getPropertyValue1());
						values.put(propertyName+"2", group.getPropertyValue2());
						whereBufferHou.append(" :"+propertyName+" and :"+propertyName+"2 ");
					} else {
						System.out.println("BETWEEN 规定只能用于时间，数字请用大于小于进行");
					}

				} else {
					if (group.getPropertyValue1().getClass() == Date.class) {
						values.put(propertyName, group.getPropertyValue1());
						whereBufferHou.append(" :"+propertyName+" ");
					} else if (group.isDate()) {
						values.put(propertyName, group.getPropertyValue1());
						whereBufferHou.append(" :"+propertyName+" ");
					} else if (group.getPropertyValue1().getClass() == java.sql.Timestamp.class) {
						whereBufferHou.append(" :"+propertyName+" ");
					} else {
						whereBufferHou.append(" :"+propertyName+" ");
						if (group.getMatchType() == MatchType.LIKE) {
							values.put(propertyName, "%" + group.getPropertyValue1() + "%");
						} else {
							values.put(propertyName, group.getPropertyValue1());
						}
					}
				}
			} else if (group.getPropertyValue2() != null && !"".equals(group.getPropertyValue2())) {
				whereBufferHou.append(" :"+propertyName+" ");
				if (group.getMatchType() == MatchType.LIKE) {
					values.put(propertyName, "%" + group.getPropertyValue2() + "%");
				} else {
					values.put(propertyName, group.getPropertyValue2());
				}
			}
		}
		
	}

	private boolean buildCase2(Group group, StringBuffer whereBufferHou, String alisStr) {
		boolean isOver;
		isOver = true;
		whereBufferHou.append(" ");
		String matchCase = "";
		if (whereBufferHou.toString().trim().length() > 0) {
			matchCase = whereBufferHou.toString().trim().substring(whereBufferHou.toString().trim().length() - 1,
					whereBufferHou.toString().trim().length());
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
			if (group.getRelation() == MatchType.AND)
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


	@SuppressWarnings("incomplete-switch")
	private void appendGroup(Group group, StringBuffer fromBuffer, StringBuffer whereBufferQian, Class<?> baseClass,
			List<String> Alias1, StringBuffer whereBufferHou, Map<String, Object> values) {

		String[] strings = CommonUtil.split(group.getPropertyName(), ".");
		if (strings == null || strings.length == 0) {
			System.out.println("查询参数错误，请查证！");
		}
		Class<?> temClass = baseClass;
		Field value = null;
		// 存放临时字符串
		StringBuffer temBuffer = new StringBuffer();
		// 得到基类的别名
		String temName = baseClass.getSimpleName();
		// 循环（）
		boolean isOver = false;
		List<String> tempStrs = new ArrayList<String>();
		int t = 0;
		for (String string : strings) {
			boolean isSame = false;
			// 有一样的名字
			if (tempStrs.contains(string)) {
				t++;
				isSame = true;
			}

			tempStrs.add(string);

			// 反射得到字段
			value = ReflectionUtils.getAllField(temClass, string);
			// 赋值到基类
			temClass = value.getType();
			// 加上全名
			String alisStr = "";
			if (!isSame)
				alisStr = string;
			else {
				alisStr = string + t;

			}
			temBuffer.append(alisStr + ".");
			// 如果是集合
			if (ReflectionUtils.isInherit(temClass, List.class, true)) {
				SearchAnnotation searchAnnotation = value.getAnnotation(SearchAnnotation.class);
				if (searchAnnotation != null) {// 查看别名是否存在

					if (!Alias1.contains(temBuffer.subSequence(0, temBuffer.length() - 1))) {
						Alias1.add(temBuffer.subSequence(0, temBuffer.length() - 1).toString());
						fromBuffer.append(" left join ").append(temName).append(".").append(alisStr).append(" ")
								.append(" as ").append(alisStr);

					}
					temClass = searchAnnotation.Class();
					temName = alisStr;
				} else {
					System.out.println("多对多关系必须要配置好注解searchAnnotation的别名");
				}
			} else if (ReflectionUtils.isInherit(temClass, BaseEntity.class, false)) {// 如果是类
				String propertyValue = group.getPropertyName().toString();
				if (temName.equals(strings[0])) {
					propertyValue = propertyValue.substring(temName.length() + 1);
				}
				isOver = buildCase(group, whereBufferHou, temName, propertyValue);

				break;
			} else {
				isOver = buildCase(group, whereBufferHou, temName, alisStr);
			}
		} // 遍历结束

		// 没有到最后（可能是 NULL/ NOT NULL）
		if (!isOver) {
			String matchCase = "";
			if (whereBufferHou.toString().trim().length() > 0) {
				matchCase = whereBufferHou.toString().trim().substring(whereBufferHou.toString().trim().length() - 1,
						whereBufferHou.toString().trim().length());
			}
			if (group.getMatchType() == MatchType.NULL || group.getMatchType() == MatchType.NOTNULL) {
				whereBufferHou.append(" ");
				if (group.getRelation() == MatchType.AND) {
					try {
						if (!matchCase.equals("(")) {
							whereBufferHou.append(" and ");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						if (!matchCase.equals("(")) {
							whereBufferHou.append(" or ");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				switch (group.getMatchType()) {
				case NOTIN:
					whereBufferHou.append(temName).append(" not in  ");

					break;
				case NOTNULL:
					whereBufferHou.append(temName).append(" is not null ");
					break;

				case NULL:
					whereBufferHou.append(temName).append(" is null ");
					break;
				}
			} else {
				System.out.println("拼接处的name不能以对象结尾，统一要以基本类型结尾！");
			}
		}

		addValues(group, whereBufferHou, values);
	}

	private boolean buildCase(Group group, StringBuffer whereBufferHou, String temName, String alisStr) {
		boolean isOver;
		isOver = true;
		whereBufferHou.append(" ");
		String matchCase = "";
		if (whereBufferHou.toString().trim().length() > 0) {
			matchCase = whereBufferHou.toString().trim().substring(whereBufferHou.toString().trim().length() - 1,
					whereBufferHou.toString().trim().length());
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

		matchCase(group, whereBufferHou, temName, alisStr);

		return isOver;
	}

	@SuppressWarnings("incomplete-switch")
	private void matchCase(Group group, StringBuffer whereBufferHou, String temName, String alisStr) {
		switch (group.getMatchType()) {
		case EQ:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" = ");
			break;
		case LIKE:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" like ");

			break;

		case LT:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" < ");
			break;
		case LE:
			if (group.getRelation() == MatchType.AND)
				whereBufferHou.append(temName).append(".").append(alisStr).append(" <= ");
			break;
		case GT:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" > ");

			break;
		case GE:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" >= ");

			break;
		case NE:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" <> ");
			break;

		case IN:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" in ");
			break;
		case NOTIN:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" not in  ");

			break;
		case BETWEEN:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" between  ");

			break;
		case NULL:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" is null ");
			break;
		case NOTNULL:
			whereBufferHou.append(temName).append(".").append(alisStr).append(" is not null ");
			break;
		}
	}

	

}
