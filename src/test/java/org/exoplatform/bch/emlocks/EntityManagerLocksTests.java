package org.exoplatform.bch.emlocks;

import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by bdechateauvieux on 6/18/15.
 */
public class EntityManagerLocksTests {

    @Test
    public void testLocks_2em_H2mem() {
        testLocks("PU_H2_mem");
    }

    @Test
    public void testLocks_2em_H2file() {
        testLocks("PU_H2_file");
    }

    @Test
    public void testLocks_2em_H2_ReadCommitted() {
        testLocks("PU_H2_mem_ReadCommitted");
    }

    @Test
    public void testLocks_2em_H2_Serializable() {
        testLocks("PU_H2_mem_Serializable");
    }

    @Test
    public void testLocks_2em_H2_ReadUncommitted() {
        testLocks("PU_H2_mem_ReadUncommitted");
    }

    @Test
    public void testLocks_2em_MySQL() {
        testLocks("PU_MySQL");
    }

    @Test
    public void testLocks_2em_H2_MVCCFalse() {
        testLocks("PU_H2_mem_MVCC_false");
    }

    private void testLocks(String pu) {
        //Given
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(pu);
        EntityManager em1 = factory.createEntityManager();
        //When
        long startTimestamp = System.currentTimeMillis();
        em1.getTransaction().begin();
        em1.persist(new Activity());
        em1.flush();

        EntityManager em2 = factory.createEntityManager();
        em2.getTransaction().begin();
        List<Activity> activities = em2.createQuery("from Activity").getResultList();
        em2.getTransaction().commit();
        //Then
        assertThat(activities.size(), is(0));
        assertTrue(System.currentTimeMillis() - startTimestamp < 2 * 1000); //less than 2 sec => no lock

        em2.close();
        em1.close();
    }

    @Test
    public void test_SelectForUpdate() {
        //Given
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PU_H2_mem");
        EntityManager em1 = factory.createEntityManager();
        EntityManager em2 = factory.createEntityManager();
        //When
        long startTimestamp = System.currentTimeMillis();
        em1.getTransaction().begin();
        em1.persist(new Activity());
        em1.flush();

        em2.getTransaction().begin();
        List<Activity> activities = em2.createQuery("from Activity")
                                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                                        .getResultList();
        em2.getTransaction().commit();
        //Then
        assertThat(activities.size(), is(0));
        assertTrue(System.currentTimeMillis() - startTimestamp < 2*1000); //less than 2 sec => no lock

        em2.close();
        em1.close();
    }
}
