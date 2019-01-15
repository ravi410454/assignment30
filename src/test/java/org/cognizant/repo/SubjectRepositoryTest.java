package org.cognizant.repo;

import org.cognizant.api.SubBookIdentity;
import org.cognizant.api.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SubjectRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    SubjectRepository subjectRepository;

    @Test
    public void testFindBySubjectId_Existing() {
        Subject subject = new Subject(new SubBookIdentity(2222, 1111), "Sub-2222", 24);
        entityManager.persist(subject);
        entityManager.flush();

        List<Subject> found = subjectRepository.findBySubjectId(2222L);

        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals(subject.getDurationInHours(), found.get(0).getDurationInHours());
        assertEquals(subject.getSubTitle(), found.get(0).getSubTitle());
        assertNotNull(found.get(0).getSubBookIdentity());
        assertEquals(subject.getSubBookIdentity().getBookId(), found.get(0).getSubBookIdentity().getBookId());
        assertEquals(subject.getSubBookIdentity().getSubjectId(), found.get(0).getSubBookIdentity().getSubjectId());
    }

    @Test
    public void testFindBySubjectId_NonExisting() {
        List<Subject> found = subjectRepository.findBySubjectId(2221L);

        assertNotNull(found);
        assertEquals(0, found.size());
    }

    @Test
    public void testSaveSubject() {
        Subject subject = new Subject(new SubBookIdentity(2223L, 1112L), "Sub-2223", 24);

        Subject saved = subjectRepository.save(subject);

        assertNotNull(saved);
        assertEquals(subject.getDurationInHours(), saved.getDurationInHours());
        assertEquals(subject.getSubTitle(), saved.getSubTitle());
        assertNotNull(saved.getSubBookIdentity());
        assertEquals(subject.getSubBookIdentity().getBookId(), saved.getSubBookIdentity().getBookId());
        assertEquals(subject.getSubBookIdentity().getSubjectId(), saved.getSubBookIdentity().getSubjectId());
    }

    @Test
    public void testDeleteSubject() {
        Subject subject = new Subject(new SubBookIdentity(2224L, 1113L), "Sub-2224", 24);
        entityManager.persist(subject);
        entityManager.flush();

        subjectRepository.delete(subject);

        List<Subject> found = subjectRepository.findBySubjectId(2221L);

        assertNotNull(found);
        assertEquals(0, found.size());
    }
}
