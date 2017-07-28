package com.operate.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import com.operate.tools.CommonUtil;
import com.operate.tools.Group;
import com.operate.tools.Groups;
import com.operate.tools.PageObj;
import com.operate.tools.PropertyFilter.MatchType;

public class CustomRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, Serializable>
		implements CustomRepository<T, Serializable> {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(CustomRepositoryImpl.class);
	
	/**
	 * 持久化上下文
	 */
	private final EntityManager entityManager;
	
	private Class<T> entityClass;

	public CustomRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityManager = em;
		this.entityClass = domainClass;
	}

	@Override
	public PageObj<T> findEntityPageByGroups(Groups groups, PageObj<T> pageObj) {
		List<Order> orders = new ArrayList<Order>();
		String order = groups.getOrder();
		String orderBy = groups.getOrderby();
		String[] orderbys = groups.getOrderbys();
		String[] orderList = groups.getOrders();
		if (!CommonUtil.isNull(orderBy)) {
			orders.add(new Order(Direction.fromString(order), orderBy));
		} else if (orderbys != null && orderbys.length > 0) {
			for (int i = 0; i < orderbys.length; i++) {
				orderBy = orderbys[i];
				order = orderList[i];
				orders.add(new Order(Direction.fromString(order), orderBy));
			}
		}

		Pageable pageable = new PageRequest(pageObj.getCurrentPage() - 1, pageObj.getPageSize(), new Sort(orders));

		Specification<T> spec = appendGroups(groups);
		Page<T> page = findAll(spec, pageable);
		
		pageObj.setItems(page.getContent());
		pageObj.setTotalCount(page.getTotalElements());
		pageObj.setTotalPageCount(page.getTotalPages());

		return pageObj;
	}
	
	
	public List<T> findEntityByGroups(Groups groups) {
		
		Specification<T> spec = appendGroups(groups);
		
		List<Order> orders = new ArrayList<Order>();
		String order = groups.getOrder();
		String orderBy = groups.getOrderby();
		String[] orderbys = groups.getOrderbys();
		String[] orderList = groups.getOrders();
		if (!CommonUtil.isNull(orderBy)) {
			orders.add(new Order(Direction.fromString(order), orderBy));
		} else if (orderbys != null && orderbys.length > 0) {
			for (int i = 0; i < orderbys.length; i++) {
				orderBy = orderbys[i];
				order = orderList[i];
				orders.add(new Order(Direction.fromString(order), orderBy));
			}
		}
		if(!orders.isEmpty()){
			return findAll(spec, new Sort(orders));
		}
		
		return findAll(spec);
	}
	
	public long findTotalCountByGroups(Groups groups){
		Specification<T> spec = appendGroups(groups);
		return count(spec);
	}
	
	@Override
	public T find(Long id) {
		return (T) entityManager.find(entityClass, id);
	}

	private Specification<T> appendGroups(Groups groups) {

		Specification<T> spec = new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate p = null;
				Predicate tmp = null;
				List<Group> groupList = groups.getGroupList();
				p = appendGroups(groupList, root, cb);
				List<Groups> childGroupsList = groups.getChildGroupsList();
				if(childGroupsList != null){
					for(Groups childGroups : childGroupsList){
						groupList = childGroups.getGroupList();
						tmp = appendGroups(groupList, root, cb);
						MatchType parentRelation = childGroups.getParentRelation();
						if(parentRelation == MatchType.AND){
							p = p == null ? tmp : cb.and(p, tmp);
						}else if(parentRelation == MatchType.OR){
							p = p == null ? tmp : cb.or(p, tmp);
						}
					}
				}
				return p;
			}
		};
		
		
		return spec;
	}
	
	private Predicate appendGroups(List<Group> groupList, Root<T> root, CriteriaBuilder cb){
		Predicate p = null;
		Predicate tmp = null;
		for (Group group : groupList) {
			tmp = matchCase(group,root,cb);
			MatchType relation = group.getRelation();
			if(relation == MatchType.AND){
				p = p == null ? tmp : cb.and(p, tmp);
			}else if(relation == MatchType.OR){
				p = p == null ? tmp : cb.or(p, tmp);
			}
		}
		return p;
	}
	
	@SuppressWarnings({ "incomplete-switch", "unchecked" })
	private Predicate matchCase(Group group,Root<T> root,CriteriaBuilder cb){
		Predicate tmp = null;
		Path<Object> property = null;
		String propertyName = group.getPropertyName();
		Object value1 = group.getPropertyValue1();
		Object value2 = group.getPropertyValue2();
		MatchType matchType = group.getMatchType();
		if(propertyName.contains(".")){
			String[] names = propertyName.split("[.]");
			property = root.<Object> get(names[0]);
			int last = names.length - 1;
			for(int i=1;i<last;i++){
				property = property.get(names[i]);
			}
			propertyName = names[last];
		}
		switch (matchType) {
		case EQ:
			if(property == null){
				property = root.<Object> get(propertyName);
			}else{
				property = property.get(propertyName);
			}
			tmp = cb.equal(property, value1);
			break;
		case LIKE:
			Path<String> property2 = null;
			if(property == null){
				property2 = root.<String> get(propertyName);
			}else{
				property2 = property.get(propertyName);
			}
			tmp = cb.like(property2, "%"+value1.toString()+"%");
			break;
		case LT:
			Path<Number> property3 = null;
			if(property == null){
				property3 = root.<Number> get(propertyName);
			}else{
				property3 = property.get(propertyName);
			}
			tmp = cb.lt(property3, (Number)value1);
			break;
		case LE:
			if(property == null){
				property3 = root.<Number> get(propertyName);
			}else{
				property3 = property.get(propertyName);
			}
			tmp = cb.le(property3, (Number)value1);
			break;
		case GT:
			if(property == null){
				property3 = root.<Number> get(propertyName);
			}else{
				property3 = property.get(propertyName);
			}
			tmp = cb.gt(property3, (Number)value1);
			break;
		case GE:
			if(property == null){
				property3 = root.<Number> get(propertyName);
			}else{
				property3 = property.get(propertyName);
			}
			tmp = cb.ge(property3, (Number)value1);
			break;
		case NE:
			if(property == null){
				property = root.<Object> get(propertyName);
			}else{
				property = property.get(propertyName);
			}
			tmp = cb.notEqual(property, value1);
			break;
		case IN:
			if(property == null){
				property = root.<Object> get(propertyName);
			}else{
				property = property.get(propertyName);
			}
			List<Object> value = (List<Object>) value1;
			tmp = property.in(value);
			break;
		case NOTIN:
			if(property == null){
				property = root.<Object> get(propertyName);
			}else{
				property = property.get(propertyName);
			}
			value = (List<Object>) value1;
			tmp = property.in(value).not();
			break;
		case BETWEEN:
			Path<Date> property4 = null;
			if(property == null){
				property4 = root.<Date> get(propertyName);
			}else{
				property4 = property.get(propertyName);
			}
			Date values1 = (Date) value1;
			Date values2 = (Date) value2;
			tmp = cb.between(property4, values1, values2);
			break;
		case NULL:
			if(property == null){
				property = root.<Object> get(propertyName);
			}else{
				property = property.get(propertyName);
			}
			tmp = cb.isNull(property);
			break;
		case NOTNULL:
			if(property == null){
				property = root.<Object> get(propertyName);
			}else{
				property = property.get(propertyName);
			}
			tmp = cb.isNotNull(property);
			break;
		}
		return tmp;
	}
	
	

}
