package com.vitech.archtests.general.rules;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.slf4j.Marker;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings({"unused", "java:S1118"}) // rules are used indirectly by arch-unit runner
public class ExceptionRules {

    private static final DescribedPredicate<JavaMethod> areNotUselessLogs = new DescribedPredicate<JavaMethod>("are not useless logs") {

        private final List<String> messageAndThrowable = Arrays.asList(String.class.getName(), Throwable.class.getName());
        private final List<String> markerMessageAndThrowable = Arrays.asList(Marker.class.getName(), String.class.getName(), Throwable.class.getName());

        @Override
        public boolean test(JavaMethod input) {
            return
                !Objects.equals(input.getOwner().getName(), "org.slf4j.Logger") ||
                    !Objects.equals(input.getName(), "error") ||
                    (
                        Objects.equals(input.getRawParameterTypes().stream().map(JavaClass::getName).collect(Collectors.toList()), messageAndThrowable) ||
                        Objects.equals(input.getRawParameterTypes().stream().map(JavaClass::getName).collect(Collectors.toList()), markerMessageAndThrowable)
                    );
        }
    };
    /**
     * Enforces passing exception when logging errors with SLF4J logger in order to have some technical information in logs.
     */
    public static final ArchRule FORCE_PASSING_EXCEPTION_TO_LOGS = FreezingArchRule.freeze(
        ArchRuleDefinition
            .classes()
            .should().onlyCallMethodsThat(areNotUselessLogs)
            .as("Add some context to log messages"));

}
