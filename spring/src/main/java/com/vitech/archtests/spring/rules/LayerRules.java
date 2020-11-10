package com.vitech.archtests.spring.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * These rules are defined to identify problems with interactions between different layers in the application.
 */
public class LayerRules {

    /**
     * Controller layer shouldn't call repository directly. You should always use the service layer because:
     * it encapsulates your business logic;
     * you are exposing DB queries implementation details when call repository directly.
     */
    @ArchTest
    public static final ArchRule REPOSITORIES_MUST_NOT_BE_USED_IN_CONTROLLERS = noClasses()
        .that().haveNameMatching(".*Controller")
        .should().dependOnClassesThat().haveNameMatching(".*Repository")
        .as("Repositories should not be used directly in controllers");
}
