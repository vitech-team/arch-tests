package com.vitech.archtests.spring.rules;

import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;
import static com.vitech.archtests.spring.rules.tool.Conditions.callAnnotatedMethodFromTheSameClass;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * These rules are related to using @Transactional annotation in Spring to avoid common @Transactional pitfalls.
 * Common @Transactional pitfalls - https://medium.com/@safa_ertekin/common-transaction-propagation-pitfalls-in-spring-framework-2378ee7d6521
 * More about transactional management - https://docs.spring.io/spring-framework/docs/4.2.x/spring-framework-reference/html/transaction.html
 */
public class TransactionalRules {

  /**
   * The invocation must come from outside of the bean.
   * More about issue and solutions - https://codete.com/blog/5-common-spring-transactional-pitfalls/
   * Understanding proxying mechanism - https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch08s06.html
   */

  @ArchTest
  public static final ArchRule NO_TRANSACTIONAL_METHODS_CALL_FROM_THE_SAME_INSTANCE = noClasses()
      .should(callAnnotatedMethodFromTheSameClass(Transactional.class))
      .as("@Transactional methods shouldn't be called from the same class because no proxy will be used then.");

  /**
   * Method visibility should be public.
   * More about issue and solutions - https://codete.com/blog/5-common-spring-transactional-pitfalls/
   * Understanding proxying mechanism - https://docs.spring.io/spring-framework/docs/3.0.0.M3/reference/html/ch08s06.html
   */
  @ArchTest
  public static final ArchRule TRANSACTIONAL_METHODS_MUST_BE_PUBLIC = methods()
      .that().areAnnotatedWith(Transactional.class)
      .and().areDeclaredInClassesThat(annotatedWith(Component.class).or(annotatedWith(Service.class)))
      .or().areDeclaredInClassesThat(
          annotatedWith(Transactional.class).and(annotatedWith(Component.class).or(annotatedWith(Service.class)))
      )
      .should()
      .bePublic()
      .as("@Transactional methods should be public because no proxy will be used then.");

  /**
   * Transactional should be used in Spring beans.
   * Transactional management - https://docs.spring.io/spring-framework/docs/4.2.x/spring-framework-reference/html/transaction.html
   */
  @ArchTest
  public static final ArchRule METHODS_MARKED_TRANSACTIONAL_MUST_BE_LOCATED_IN_SPRING_BEAN = methods()
      .that().areAnnotatedWith(Transactional.class)
      .should().beDeclaredInClassesThat(annotatedWith(Component.class).or(annotatedWith(Service.class)))
      .as("Methods marked @Transactional should be located in Spring beans.");

  /**
   * Although @Transactional is present in both Spring and JavaEE (javax.transaction package), it’s generally a better
   * practice to use from Spring Framework since it is more natural to Spring applications and at the same time it
   * offers more options like timeout, isolation, etc.
   * More about issue and solutions - https://codete.com/blog/5-common-spring-transactional-pitfalls/
   */
  @ArchTest
  public static final ArchRule METHODS_MUST_NOT_BE_MARKED_WITH_JAVAX_TRANSACTIONAL = noMethods()
      .should().beAnnotatedWith(javax.transaction.Transactional.class)
      .as("Methods should not be marked with javax @Transactional.");

  /**
   * Although @Transactional is present in both Spring and JavaEE (javax.transaction package), it’s generally a better
   * practice to use from Spring Framework since it is more natural to Spring applications and at the same time it
   * offers more options like timeout, isolation, etc.
   * More about issue and solutions - https://codete.com/blog/5-common-spring-transactional-pitfalls/
   */
  @ArchTest
  public static final ArchRule CLASSES_MUST_NOT_BE_MARKED_WITH_JAVAX_TRANSACTIONAL = noClasses()
      .should().beAnnotatedWith(javax.transaction.Transactional.class)
      .as("Classes should not be marked with javax @Transactional.");
}
