package ch.unige.pinfo3.utils;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

/***
 * This class contains wrapper helper functions to interface
 * with jpa's Query builders. Typical repetitive queries are
 * done here in order to avoid repeating code. 
 */
@ApplicationScoped
public final class QueryUtils {
    @Transactional
    static public <T> List<T> select(Class<T> entity, String whereColumn, String is, EntityManager em) {
        // building queries uses the builder design pattern.
        // the builder allows us to build a complex query (SQL under the hood)
        // we build the query once we've applied all the predicates. 
        CriteriaBuilder builder = em.getCriteriaBuilder();
        // create empty query which gets gradually built.
        CriteriaQuery<T> criteria = builder.createQuery(entity);
        // allows accessing underlying sql schema columns. Root is an interface that allows
        // to get entity fields
        Root<T> root = criteria.from(entity);
        // where takes an expression which is comprised of predicates
        criteria.select(root).where(builder.equal(root.get(whereColumn), is));
        return em.createQuery(criteria).getResultList();
    }

    @Transactional
    static public <T> List<T> select(Class<T> entity, String col1, String is1, String col2, String is2, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(entity);
        Root<T> root = criteria.from(entity);
        criteria.select(root).where(
            builder.and(
                builder.equal(root.get(col1), is1), 
                builder.equal(root.get(col2), is2)
            )
        );
        return em.createQuery(criteria).getResultList();
    }

    @Transactional
    static public <T> List<T> getAll(Class<T> entity, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(entity);
		criteria.from(entity);
		return em.createQuery(criteria).getResultList();
    }

    private QueryUtils() {}
}
