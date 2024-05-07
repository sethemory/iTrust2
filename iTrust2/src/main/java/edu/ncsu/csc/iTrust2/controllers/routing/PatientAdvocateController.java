package edu.ncsu.csc.iTrust2.controllers.routing;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ncsu.csc.iTrust2.models.enums.Role;

/**
 * Controller for the PatientAdvocate landing screen
 *
 * @author Raphael Phillips
 *
 */
@Controller
public class PatientAdvocateController {

    /**
     * Landing screen for a PatientAdvocate when they log in
     *
     * @param model
     *            The data from the front end
     * @return The page to show to the user
     */
    @RequestMapping ( value = "pa/index" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT_ADVOCATE')" )
    public String index ( final Model model ) {
        return Role.ROLE_PATIENT_ADVOCATE.getLanding();
    }

    /**
     * Screen for the Patient Advocate to manage their Patients
     *
     * @param model
     *            The data from the front end
     * @return The page to show to the user
     */
    @GetMapping ( value = "pa/managePatient" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT_ADVOCATE')" )
    public String managePatient ( final Model model ) {
        return "/pa/managePatient";
    }
}
