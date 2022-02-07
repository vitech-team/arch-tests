package com.vitech.archtests.spring.rules;

import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;

/**
 * Rules applied to controllers
 */
@SuppressWarnings({"unused", "java:S1118"}) // rules are used indirectly by arch-unit runner
public class ControllerRules {

    /**
     * Forbids using `ResponseEntity` as endpoint return type due to the following reasons:
     *      * in most cases it is better to just return the real response type without wrapping it `ResponseEntity`
     *      * custom success response codes (e.g. 204) could be configured via `@ResponseStatus` annotation
     *      * error status codes are handled in application-wide exception handler
     *      * in case we need to modify response headers - we could still use `ResponseEntity`, but we just don't need that
     */
    public static final ArchRule RESPONSE_ENTITY_SHOULD_NOT_BE_USED = FreezingArchRule.freeze(
        ArchRuleDefinition
            .noClasses()
            .that().haveNameMatching(".*Controller")
            .should().dependOnClassesThat().haveNameMatching(".*ResponseEntity")
            .as("ResponseEntity should not be used in controllers, because usually it could replaced with appropriate annotations")
    );

}
