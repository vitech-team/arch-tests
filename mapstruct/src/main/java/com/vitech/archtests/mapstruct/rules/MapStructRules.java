package com.vitech.archtests.mapstruct.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.vitech.archtests.mapstruct.rules.tool.Conditions.haveAnnotationWithPropertyValue;

import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * These rules are defined to identify potential issues with configured mappers.
 */
public class MapStructRules {
  public static final String UNMAPPED_TARGET_POLICY_METHOD = "unmappedTargetPolicy";

  @ArchTest
  public static final ArchRule UNMAPPED_TARGET_POLICY_MUST_BE_ERROR = classes()
      .that().areInterfaces().and().areAnnotatedWith(Mapper.class)
      .should(haveAnnotationWithPropertyValue(Mapper.class, Mapper::unmappedTargetPolicy, ReportingPolicy.ERROR));

}
