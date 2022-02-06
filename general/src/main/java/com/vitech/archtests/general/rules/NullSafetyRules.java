package com.vitech.archtests.general.rules;

import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;

@SuppressWarnings({"unused", "java:S1118"}) // rules are used indirectly by arch-unit runner
public class NullSafetyRules {

    /**
     * Forbids using @NotNull annotations on wrapper classes due to the following reasons:
     *      * @NotNull annotation could be only handled by static analysis tool, the annotation itself does nothing
     *      * using appropriate primitive types is null-safe at compile time
     */
    public static final ArchRule FORCE_PRIMITIVE_TYPE_INSTEAD_OF_NOTNULL_OBJECTS = FreezingArchRule.freeze(
        ArchRuleDefinition
            .noFields()
            .that().areAnnotatedWith("javax.validation.constraints.NotNull")
            .or().areAnnotatedWith("org.jetbrains.annotations.NotNull")
            .or().areAnnotatedWith("javax.annotation.Nonnull")
            .or().areAnnotatedWith("lombok.NonNull")
            .or().areAnnotatedWith("org.springframework.lang.NonNull")
            .should().haveRawType(Long.class)
            .orShould().haveRawType(Integer.class)
            .orShould().haveRawType(Boolean.class)
            .orShould().haveRawType(Character.class)
            .orShould().haveRawType(Byte.class)
            .orShould().haveRawType(Short.class)
            .orShould().haveRawType(Float.class)
            .orShould().haveRawType(Double.class)
            .as("Prefer primitive data types to not nullable objects")
    );

    /**
     * Forbids using @NotNull and @Nullable annotations on primitive data types due to the following reasons:
     *      * annotating primitive data types as not nullable makes no sense because they are not nullable by design
     *      * annotating primitive data types as nullable makes no sense because they are not nullable by design
     */
    public static final ArchRule FORBID_NOTNULL_PRIMITIVE_TYPES = FreezingArchRule.freeze(
        ArchRuleDefinition
            .noFields()
            .that().areAnnotatedWith("javax.validation.constraints.NotNull")
            .or().areAnnotatedWith("org.jetbrains.annotations.NotNull")
            .or().areAnnotatedWith("javax.annotation.Nonnull")
            .or().areAnnotatedWith("lombok.NonNull")
            .or().areAnnotatedWith("org.springframework.lang.NonNull")
            .or().areAnnotatedWith("javax.annotation.Nullable")
            .or().areAnnotatedWith("org.springframework.lang.Nullable")
            .or().areAnnotatedWith("org.jetbrains.annotations.Nullable")
            .should().haveRawType(long.class)
            .orShould().haveRawType(int.class)
            .orShould().haveRawType(boolean.class)
            .orShould().haveRawType(char.class)
            .orShould().haveRawType(byte.class)
            .orShould().haveRawType(short.class)
            .orShould().haveRawType(float.class)
            .orShould().haveRawType(double.class)
            .as("Annotating primitive data types as not nullable makes no sense")
    );

}
