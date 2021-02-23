package com.vitech.archtests.spring.rules;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.assignableTo;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.simpleNameEndingWith;
import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.RepositoryDefinition;

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
        .should().dependOnClassesThat(areRepositories())
        .as("Repositories should not be used directly in controllers");

  private static DescribedPredicate<JavaClass> areRepositories() {
    return simpleNameEndingWith("Repository")
        .or(assignableTo(Repository.class))
        .or(annotatedWith(org.springframework.stereotype.Repository.class))
        .or(annotatedWith(RepositoryDefinition.class));
  }
}
