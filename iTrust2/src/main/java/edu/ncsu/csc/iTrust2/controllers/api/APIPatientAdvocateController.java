package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.iTrust2.forms.PatientAdvocateForm;
import edu.ncsu.csc.iTrust2.forms.UserForm;
import edu.ncsu.csc.iTrust2.models.Patient;
import edu.ncsu.csc.iTrust2.models.PatientAdvocate;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.Role;
import edu.ncsu.csc.iTrust2.services.PatientAdvocateService;
import edu.ncsu.csc.iTrust2.services.PatientService;
import edu.ncsu.csc.iTrust2.services.UserService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * Controller that contains REST API endpoints regarding Patient Advocate
 * objects.
 *
 * @author Raphael Phillips
 *
 */
@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APIPatientAdvocateController extends APIController {

    /**
     * Patient Advocate service
     */
    @Autowired
    private PatientAdvocateService advocateService;

    /**
     * Patient service
     */
    @Autowired
    private PatientService         patientService;

    /**
     * User Service
     */
    @Autowired
    private UserService            userService;

    /**
     * LoggerUtil for Creating Users in the system
     */
    private LoggerUtil             loggerUtil;

    /**
     * Returns all Patient Advocates stored within the iTrust2 system.
     *
     * @return list of patient advocates
     */
    @GetMapping ( BASE_PATH + "/patientadvocates/" )
    public List<PatientAdvocate> getAdvocates () {
        final List<PatientAdvocate> advocates = advocateService.findAll();

        return advocates;
    }

    /**
     * Saves a new PatientAdvocate into the system
     *
     * @param pa
     *            patient advocate object
     * @return a response containing a patient advocate object
     */
    @PostMapping ( BASE_PATH + "/patientadvocate" )
    public ResponseEntity createPatientAdvocate ( @RequestBody final PatientAdvocateForm pa ) {
        if ( userService.findByName( pa.getUsername() ) != null ) {
            return new ResponseEntity( errorResponse( "User with the id " + pa.getUsername() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        final PatientAdvocate patient = new PatientAdvocate(
                new UserForm( pa.getUsername(), "123456", Role.ROLE_PATIENT_ADVOCATE, 1 ) );

        patient.setAddress1( pa.getAddress1() );
        patient.setFirstName( pa.getFirstName() );
        patient.setMiddleName( pa.getMiddleName() );
        patient.setLastName( pa.getLastName() );
        patient.setPreferredName( pa.getPreferredName() );
        patient.setEmail( pa.getEmail() );
        patient.setPhone( pa.getPhone() );

        // advocateService.save( patient );
        userService.save( patient );

        return new ResponseEntity( pa, HttpStatus.OK );
    }

    /**
     * Returns the Patient Advocate that is currently logged in.
     *
     * @return patient advocate object based on currently logged in patient
     *         advocate
     */
    @GetMapping ( BASE_PATH + "/patientadvocate/" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT_ADVOCATE')" )
    public ResponseEntity getPatientAdvocate () {
        final User user = userService.findByName( LoggerUtil.currentUser() );
        final PatientAdvocate pa = (PatientAdvocate) advocateService.findByName( user.getUsername() );
        if ( pa == null ) {
            return new ResponseEntity(
                    errorResponse( "Could not find a patient advocate with username " + user.getUsername() ),
                    HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity( pa, HttpStatus.OK );

    }

    /**
     * Returns a given Patient Advocate with the id specified
     *
     * @param id
     *            of the patient advocate
     * @return a response containing a patient advocate object
     */
    @GetMapping ( BASE_PATH + "/patientadvocate/{id}" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public ResponseEntity getPatientAdvocate ( @PathVariable ( "id" ) final String id ) {
        final PatientAdvocate pa = (PatientAdvocate) advocateService.findByName( id );
        if ( pa == null ) {
            return new ResponseEntity( errorResponse( "No patient advocate found for id " + id ),
                    HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity( pa, HttpStatus.OK );
    }

    /**
     * Deletes a PatientAdvocate currently in the system.
     *
     * @param id
     *            of the patient advocate in the system
     * @return a response containing the deleted patient advocate object
     */
    @DeleteMapping ( BASE_PATH + "/patientadvocate/{id}" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public ResponseEntity deletePatientAdvocate ( @PathVariable ( "id" ) final String id ) {
        final PatientAdvocate pa = (PatientAdvocate) advocateService.findByName( id );
        if ( pa == null ) {
            return new ResponseEntity( errorResponse( "No patient advocate found for id " + id ),
                    HttpStatus.NOT_FOUND );
        }
        userService.delete( pa );
        advocateService.delete( pa );

        return new ResponseEntity( pa, HttpStatus.OK );
    }

    /**
     * Associates a Patient to a PatientAdvocate
     *
     * @param id
     *            of the patient advocate in the system
     * @param patientID
     *            the patient to be associated with the patient advocate
     * @return a response containing the patient advocate
     */
    @PostMapping ( BASE_PATH + "/patientadvocate/{id}/patients/{patientID}" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public ResponseEntity associatePatientData ( @PathVariable ( "id" ) final String id,
            @PathVariable final String patientID ) {
        final PatientAdvocate pa = (PatientAdvocate) advocateService.findByName( id );
        if ( pa == null ) {
            return new ResponseEntity( errorResponse( "No patient advocate found for id " + id ),
                    HttpStatus.NOT_FOUND );
        }

        final Patient p = (Patient) patientService.findByName( patientID );
        if ( p == null ) {
            return new ResponseEntity( errorResponse( "No patient advocate found for id " + patientID ),
                    HttpStatus.NOT_FOUND );
        }
        p.addAdvocate( pa );
        pa.addPatient( p );

        return new ResponseEntity( pa, HttpStatus.OK );

    }

    /**
     * Returns a list of patients associated with the given patient advocate
     *
     * @param id
     *            of the patient advocate in the system
     * @return list of patients associated with the patient advocate
     */
    @GetMapping ( BASE_PATH + "/patientadvocate/{id}/patients" )
    @PreAuthorize ( "hasAnyRole('ROLE_PATIENT_ADVOCATE', 'ROLE_ADMIN')" )
    public List<String> getAssociatedPatients ( @PathVariable ( "id" ) final String id ) {
        final PatientAdvocate pa = (PatientAdvocate) advocateService.findByName( id );
        if ( pa == null ) {
            return null;
        }
        final List<String> pats = pa.getPatients();

        return pats;
    }
    
    /**
     * Edits a currently existing PatientAdvocate in the system and saves it
     *
     * @param id
     *            of the patient advocate in the system
     * @param paNew
     *            patient advocate object with new details
     * @return a response containing the newly edited patient advocate object
     */
    @PutMapping ( BASE_PATH + "/patientadvocate/{id}" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public ResponseEntity editPatientAdvocate ( @PathVariable ( "id" ) final String id,
            @RequestBody final PatientAdvocateForm paNew ) {
        final PatientAdvocate pa = (PatientAdvocate) advocateService.findByName( id );
        if ( pa == null ) {
            return new ResponseEntity( errorResponse( "No patient advocate found for id " + id ),
                    HttpStatus.NOT_FOUND );
        }
        
        pa.setAddress1( paNew.getAddress1() );
        pa.setFirstName( paNew.getFirstName() );
        pa.setMiddleName( paNew.getMiddleName() );
        pa.setLastName( paNew.getLastName() );
        pa.setPreferredName( pa.getPreferredName() );
        pa.setEmail( paNew.getEmail() );
        pa.setPhone( paNew.getPhone() );
//        advocateService.delete( pa );
//        final PatientAdvocate patienAdvocateNew = new PatientAdvocate(
//                new UserForm( paNew.getUsername(), "123456", Role.ROLE_PATIENT_ADVOCATE, 1 ) );
//        advocateService.save( patienAdvocateNew );
        advocateService.save(pa);
        return new ResponseEntity( paNew, HttpStatus.OK );
    }

}
