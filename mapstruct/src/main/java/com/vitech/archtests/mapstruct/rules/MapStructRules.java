package com.vitech.archtests.mapstruct.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.vitech.archtests.mapstruct.rules.tool.Conditions.haveAnnotationWithPropertyValue;

import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * These rules are defined to identify potential issues with configured mappers.
 */
@SuppressWarnings({"unused", "java:S1118"}) // rules are used indirectly by arch-unit runner
public class MapStructRules {
  public static final String UNMAPPED_TARGET_POLICY_METHOD = "unmappedTargetPolicy";

  public static final ArchRule UNMAPPED_TARGET_POLICY_MUST_BE_ERROR = FreezingArchRule.freeze(classes()
      .that().areInterfaces().and().areAnnotatedWith(Mapper.class)
      .should(haveAnnotationWithPropertyValue(Mapper.class, Mapper::unmappedTargetPolicy, ReportingPolicy.ERROR)));

}
