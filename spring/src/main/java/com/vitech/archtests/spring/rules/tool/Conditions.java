package com.vitech.archtests.spring.rules.tool;

import com.tngtech.archunit.core.domain.AccessTarget;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.lang.annotation.Annotation;

/**
 * Shared custom ArchUnit conditions.
 */
public class Conditions {
  public static ArchCondition<JavaClass> callAnnotatedMethodFromTheSameClass(
      Class<? extends Annotation> annotationClass
  ) {
    return new ArchCondition<JavaClass>(
        String.format("call methods annotated with %s from the same class",
            annotationClass.getName())
    ) {
      @Override
      public void check(JavaClass javaClass, ConditionEvents events) {
        javaClass.getMethodCallsFromSelf().forEach(call -> {
          AccessTarget.MethodCallTarget target = call.getTarget();
          JavaClass targetOwner = call.getTargetOwner();
          boolean originIsTarget = call.getOriginOwner().equals(targetOwner);

          if (target.isAnnotatedWith(annotationClass) || targetOwner.isAnnotatedWith(annotationClass) ) {
            events.add(new SimpleConditionEvent(call, originIsTarget, call.getDescription()));
          }
        });
      }
    };
  }
}
