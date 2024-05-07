package edu.ncsu.csc.iTrust2.unit;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.iTrust2.TestConfig;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.PatientAdvocate;
import edu.ncsu.csc.iTrust2.models.Permission;
import edu.ncsu.csc.iTrust2.models.enums.Role;

/**
 * Class to test permissions with patient and advocates
 *
 * @author johngobran
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
@ActiveProfiles ( { "test" } )
public class PermissionsTest {

    /**
     * Patient used for testing
     */
    Patient                     p1;

    /**
     * Patient advocate used for testing
     */
    PatientAdvocate             p2;

    /**
     * User name 1 used for testing
     */
    private static final String USER_1 = "demoTestUser1";

    /**
     * User name 2 used for testing
     */
    private static final String USER_2 = "demoTestUser2";

    /**
     * Password used for testing
     */
    private static final String PW     = "123456";

    /**
     * Tests the permission addition
     */
    @Test
    @Transactional
    public void testPermission () {
        p1 = new Patient( new UserForm( USER_1, PW, Role.ROLE_PATIENT, 1 ) );
        p2 = new PatientAdvocate( new UserForm( USER_2, PW, Role.ROLE_PATIENT_ADVOCATE, 1 ) );

        assertEquals( 0, p1.getPermissions().size() );

        p1.addAdvocate( p2 );
        assertEquals( 1, p1.getPermissions().size() );

        final Permission perm = p1.getPermission( p2 );
        assertFalse( perm == null );
        assertEquals( USER_2, perm.getAdvocate() );
        assertEquals( USER_1, perm.getPatient() );
        assertTrue( perm.isBilling() );
        assertTrue( perm.isOfficeVis() );
        assertTrue( perm.isPrescript() );

        assertNull( p1.getPermission( null ) );

        p1.removeAdvocate( p2 );
        assertEquals( 0, p1.getPermissions().size() );
    }

}
