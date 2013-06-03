package com.artigile.howismyphonedoing.server.dao;

import com.artigile.howismyphonedoing.server.entity.UserDevice;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;

/**
 * @author IoaN, 5/28/13 9:30 PM
 */
@Service
public class UserAndDeviceDaoImpl implements UserAndDeviceDao {
    private final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("transactions-optional");
    private EntityManager em = emfInstance.createEntityManager();
    private CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

    @Override
    public void register(UserDevice userDevice) {
        em.getTransaction().begin();
        em.persist(userDevice);
        em.getTransaction().commit();
    }

    @Override
    public void unregister(String registeredDeviceId) {
        em.getTransaction().begin();
        UserDevice userDevice = em.find(UserDevice.class, registeredDeviceId);
        if (userDevice != null) {
            em.remove(userDevice);
        }
        em.getTransaction().commit();

    }

    @Override
    public void updateRegistration(String oldId, String newId) {
        em.getTransaction().begin();
        UserDevice userDevice = em.find(UserDevice.class, oldId);
        userDevice.setRegisteredId(newId);
        em.persist(userDevice);
        em.getTransaction().commit();
    }

    @Override
    public Set<UserDevice> getDevices(String userEmail) {
        CriteriaQuery<UserDevice> criteria = criteriaBuilder.createQuery(UserDevice.class);
        Root<UserDevice> personRoot = criteria.from(UserDevice.class);
        criteria.select(personRoot).where(criteriaBuilder.equal(personRoot.get("userEmail"), userEmail));
        return new HashSet<>(em.createQuery(criteria).getResultList());

    }
}
