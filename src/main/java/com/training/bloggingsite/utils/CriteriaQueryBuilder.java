package com.training.bloggingsite.utils;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CriteriaQueryBuilder {

    private CriteriaBuilder cb;

    @Autowired
    EntityManager em;

    @PostConstruct
    public void init() {
        this.cb = em.getCriteriaBuilder();
    }


    public <F,T> List<T> getResultWhereColumnEqual(List<String> column, List<F> value,Class<T> clazz) {
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);

        Predicate[] predicates = new Predicate[column.size()];

        for(int i=0;i<column.size();++i){
            predicates[i] = cb.equal(root.get(column.get(i)),value.get(i));
        }

        cq.select(root).where(predicates);
        return em.createQuery(cq).getResultList();
    }

    public <F,T> List<T> getResultWhereColumnEqual(String column, F value,Class<T> clazz) {
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);

        cq.select(root).where(cb.equal(root.get(column),value));
        return em.createQuery(cq).getResultList();
    }

    public <T> List<T> getResultWhereColumnIsNull(String column,Class<T> clazz) {
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);
        cq.select(root).where(cb.isNull( root.get(column)));
        return em.createQuery(cq).getResultList();
    }

    public <T> List<T> getAll(Class<T> clazz) {
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);
        cq.select(root).where(cb.gt(root.get("id"),0));
        return em.createQuery(cq).getResultList();
    }

    public <T,F> void deleteById(String column,F id,Class<T> clazz){
        CriteriaDelete<T> cq = cb.createCriteriaDelete(clazz);
        Root<T> root = cq.from(clazz);
        cq.where(root.get(column).in(id));
        em.createQuery(cq).executeUpdate();
    }

    public <T,V,F> void updateByColumn(String column,F id, Class<T> clazz, V value){
        CriteriaUpdate<T> cu = cb.createCriteriaUpdate(clazz);
        Root<T> root = cu.from(clazz);
        cu.set(column,value);
        cu.where(cb.equal(root.get("id"),id));
        em.createQuery(cu).executeUpdate();
    }

    public <T,F> List<T> getPaginatedData(int offset, int limit, String columnName, F value,Class<T> clazz) {
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);

        if (!((Boolean) value)) {
            cq.select(root);//if false then show all post to Admin
            return em.createQuery(cq).setFirstResult(offset).setMaxResults(limit).getResultList();

        }
        else {
            cq.select(root).where(cb.equal(root.get(columnName), true));
        }

        return em.createQuery(cq.orderBy(cb.asc(root.get("title")))).setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    public <T,F> int getCount(String columnName, F value,Class<T> clazz) {
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> root = cq.from(clazz);

        if ((boolean) value == false) {

            return   em.createQuery(cq.select(root)).getResultList().size();///when verified is false return all post for admin vie

        }

        cq.select(root).where(cb.equal(root.get(columnName), value));
        int count = em.createQuery(cq).getResultList().size();

        return count;

    }




}
