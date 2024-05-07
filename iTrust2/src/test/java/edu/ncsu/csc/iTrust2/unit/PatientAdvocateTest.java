package edu.ncsu.csc.iTrust2.unit;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.PatientAdvocate;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.PatientAdvocateService;

/**
 * Tests the patient model
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
@ActiveProfiles ( { "test" } )
public class PatientAdvocateTest {

    @Autowired
    private PatientAdvocateService<PatientAdvocate> service;

    @Autowired

    private static final String                     USER_1 = "demoTestUser1";

    private static final String                     USER_2 = "demoTestUser2";

    private static final String                     PW     = "123456";

    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Tests creating a patient
     */
    @Test
    @Transactional
    public void testCreatePatientRecord () {

        Assertions.assertEquals( 0, service.count(), "There should be no Patient records in the system!" );

        final PatientAdvocate p1 = new PatientAdvocate( new UserForm( USER_1, PW, Role.ROLE_PATIENT_ADVOCATE, 1 ) );

        service.save( p1 );

        final List<PatientAdvocate> savedRecords = service.findAll();

        Assertions.assertEquals( 1, savedRecords.size(),
                "Creating a Patient record should results in its creation in the DB" );

        Assertions.assertEquals( USER_1, savedRecords.get( 0 ).getUsername(),
                "Creating a Patient record should results in its creation in the DB" );

        p1.setFirstName( "Karl" );
        p1.setLastName( "Liebknecht" );

        final User userRecord = service.findByName( USER_1 );

        Assertions.assertEquals( USER_1, userRecord.getUsername() );

        Assertions.assertEquals( PatientAdvocate.class, userRecord.getClass() );

        final PatientAdvocate retrieved = (PatientAdvocate) userRecord;

        try {
            final PatientAdvocate p2 = new PatientAdvocate( new UserForm( USER_2, PW, Role.ROLE_ADMIN, 1 ) );
            Assertions.assertNotNull( p2 ); // Otherwise we get compilation
                                            // warnings
            Assertions.fail( "Should not be able to create a Patient Advocate from a non-Patient user" );
        }
        catch ( final Exception e ) {
            // expected
        }

    }
}