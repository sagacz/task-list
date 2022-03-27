package com.jpa.tasks.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PersonTestSuite {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    void persistPerson(){
        //Given
        EntityManager em = emf.createEntityManager();
        Person person = new Person(null, "FirstName1", "LastName1");

        //When
        em.getTransaction().begin();
        em.persist(person);
        em.flush();
        em.getTransaction().commit();

        //Then
        Long key = person.getId();
        Person readPerson = em.find(Person.class, key);
        em.close();
        assertThat(readPerson.getFirstName()).isEqualTo(person.getFirstName());
    }

}