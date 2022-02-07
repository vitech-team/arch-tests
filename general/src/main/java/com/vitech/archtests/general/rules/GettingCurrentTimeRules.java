package com.vitech.archtests.general.rules;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.AccessTarget;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.properties.HasName;
import com.tngtech.archunit.core.domain.properties.HasOwner;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.freeze.FreezingArchRule;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

import static com.tngtech.archunit.core.domain.JavaAccess.Predicates.target;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.assignableTo;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Rules applied to every class in project
 */
@SuppressWarnings({"unused", "SameParameterValue", "deprecation", "java:S1118"})
public class GettingCurrentTimeRules {

    /**
     * OffsetDateTime.now should be never used directly only throw proxy bean
     */
    public static final ArchRule GETTING_CURRENT_TIME_FROM_OFFSET_DATETIME_RULE = FreezingArchRule.freeze(noClasses()
            .should().callMethodWhere(
                    target(name("now")
                            .and(declarationIsWithin(OffsetDateTime.class)))));

    /**
     * ZonedDateTime.now should be never used directly only throw proxy bean
     */
    public static final ArchRule GETTING_CURRENT_TIME_FROM_ZONED_DATETIME_RULE = FreezingArchRule.freeze(noClasses()
        .should().callMethodWhere(
            target(name("now")
                .and(declarationIsWithin(ZonedDateTime.class)))));

    /**
     * Instant.now should be never used directly only throw proxy bean
     */
    public static final ArchRule GETTING_CURRENT_TIME_FROM_INSTANT_RULE = FreezingArchRule.freeze(noClasses()
        .should().callMethodWhere(
            target(name("now")
                .and(declarationIsWithin(Instant.class)))));

    /**
     * LocalDateTime.now should be never used directly only throw proxy bean
     */
    public static final ArchRule GETTING_CURRENT_TIME_FROM_LOCAL_DATETIME_RULE = FreezingArchRule.freeze(noClasses()
            .should().callMethodWhere(
                    target(name("now")
                            .and(declarationIsWithin(LocalDateTime.class)))));

    /**
     * LocalDate.now should be never used directly only throw proxy bean
     */
    public static final ArchRule GETTING_CURRENT_TIME_FROM_LOCAL_DATE_RULE = FreezingArchRule.freeze(noClasses()
            .should().callMethodWhere(
                    target(name("now")
                            .and(declarationIsWithin(LocalDate.class)))));

    /**
     * LocalTime.now should be never used directly only throw proxy bean
     */
    public static final ArchRule GETTING_CURRENT_TIME_FROM_LOCAL_TIME_RULE = FreezingArchRule.freeze(noClasses()
            .should().callMethodWhere(
                    target(name("now")
                            .and(declarationIsWithin(LocalTime.class)))));

    private static DescribedPredicate<AccessTarget> name(String methodName) {
        return HasName.Predicates.name(methodName).forSubType();
    }

    private static DescribedPredicate<HasOwner<JavaClass>> declarationIsWithin(Class<?> type) {
        return HasOwner.Functions.Get.<JavaClass>owner().is(assignableTo(type));
    }
}
