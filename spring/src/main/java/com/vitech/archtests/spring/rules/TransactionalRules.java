package com.vitech.archtests.spring.rules;

import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.vitech.archtests.spring.rules.tool.Conditions.callAnnotatedMethodFromTheSameClass;

import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * These rules are related to using @Transactional annotation in Spring to avoid common @Transactional pitfalls.
 * Common @Transactional pitfalls - https://medium.com/@safa_ertekin/common-transaction-propagation-pitfalls-in-spring-framework-2378ee7d6521
 * More about transactional management - https://docs.spring.io/spring-framework/docs/4.2.x/spring-framework-reference/html/transaction.html
 */
@SuppressWarnings({"unused", "java:S1118"}) // rules are used indirectly by arch-unit runner
public class TransactionalRules {

  /**
   * The invocation must come from outside the bean.
   * More about issue and solutions - https://codete.com/blog/5-common-spring-transactional-pitfalls/
   * Understanding proxying mechanism - https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch08s06.html
   */
  public static final ArchRule NO_TRANSACTIONAL_METHODS_CALL_FROM_THE_SAME_INSTANCE = FreezingArchRule.freeze(noClasses()
      .should(callAnnotatedMethodFromTheSameClass(Transactional.class))
      .as("@Transactional methods shouldn't be called from the same class because no proxy will be used then."));

  /**
   * Method visibility should be public.
   * More about issue and solutions - https://codete.com/blog/5-common-spring-transactional-pitfalls/
   * Understanding proxying mechanism - https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch08s06.html
   */
  public static final ArchRule TRANSACTIONAL_METHODS_MUST_BE_PUBLIC = FreezingArchRule.freeze(methods()
      .that().areAnnotatedWith(Transactional.class)
      .and().areDeclaredInClassesThat(annotatedWith(Component.class).or(annotatedWith(Service.class)))
      .or().areDeclaredInClassesThat(
          annotatedWith(Transactional.class).and(annotatedWith(Component.class).or(annotatedWith(Service.class)))
      )
      .should()
      .bePublic()
      .as("@Transactional methods should be public because no proxy will be used then."));
}
