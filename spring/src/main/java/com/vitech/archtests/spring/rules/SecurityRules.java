package com.vitech.archtests.spring.rules;

import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import com.tngtech.archunit.lang.ArchRule;
import javax.annotation.security.RolesAllowed;

import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * These rules are defined to identify potential security issues in spring controllers/services.
 */
@SuppressWarnings({"unused", "java:S1118"}) // rules are used indirectly by arch-unit runner
public class SecurityRules {

    /**
     * Endpoints in controller must be secured by one of the following annotations:
     * Secured, RolesAllowed, PreAuthorize, PostAuthorize (the common approach is to use PreAuthorize,
     * but if this annotation is missed we should at least have PostAuthorize)
     *
     * Spring method's security - https://www.baeldung.com/spring-security-method-security
     */
    public static final ArchRule SPRING_CONTROLLERS_MUST_BE_SECURED = FreezingArchRule.freeze(methods()
        .that().arePublic()
        .and().areDeclaredInClassesThat(annotatedWith(Controller.class).or(annotatedWith(RestController.class)))
        .should()
        .beAnnotatedWith(Secured.class)
        .orShould().beAnnotatedWith(RolesAllowed.class)
        .orShould().beAnnotatedWith(PreAuthorize.class)
        .orShould().beAnnotatedWith(PostAuthorize.class)
        .orShould().beDeclaredInClassesThat(
            annotatedWith(Secured.class)
                .or(annotatedWith(RolesAllowed.class)
                .or(annotatedWith(PreAuthorize.class)
                .or(annotatedWith(PostAuthorize.class))))
        )
        .as("Each public method in Spring Controller should be secured"));
}
