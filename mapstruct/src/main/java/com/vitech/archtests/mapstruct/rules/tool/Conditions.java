package com.vitech.archtests.mapstruct.rules.tool;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.lang.annotation.Annotation;
import java.util.function.Function;

/**
 * Shared custom ArchUnit conditions.
 */
public class Conditions {
  @SuppressWarnings("unchecked")
  public static <T extends Annotation, R> ArchCondition<JavaClass> haveAnnotationWithPropertyValue(
      Class<T> annotation,
      Function<T,R> supplier,
      R expectedValue
  ) {
    return new ArchCondition<JavaClass>("have unmapped target policy ERROR") {
      @Override
      public void check(JavaClass javaClass, ConditionEvents events) {
        Annotation mapperAnnotation = javaClass.getAnnotationOfType(annotation);

        R value = supplier.apply((T)mapperAnnotation);

        if (!value.equals(expectedValue)) {
          String targetClassName = javaClass.getName();
          events.add(
              SimpleConditionEvent.violated(
                  targetClassName,
                  String.format("%s - unmapped target policy is not ERROR", targetClassName))
          );
        }
      }
    };
  }
}
