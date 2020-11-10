# arch-tests

Repository contains architectural, framework / lib specific ArchUnit rules that can be reused across different projects.
Rules are grouped in artifacts based on their specialization.

For more info how to write rules in ArchUnit, please see [User Guide](https://www.archunit.org/userguide/html/000_Index.html)

## Distribution
Rules can be integrated into project by next plugins:
- [ArchUnit gradle plugin](https://github.com/societe-generale/arch-unit-gradle-plugin)
- [ArchUnit maven plugin](https://github.com/societe-generale/arch-unit-maven-plugin)

Using these plugins bring a way to manage the rules through build configuration and to easily share and enforce 
them across projects.

### Configuring ArchUnit gradle plugin to run verification over specific rules
To use the plugin, build.gradle require next changes:
1. Declare the dependency to the plugin
2. Configure GitHubPackages repository to have access to artifact contained needed rules, add it to plugin classpath

    ```Gradle
    buildscript {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/vitech-team/arch-tests")
                credentials {
                    username = '<username>'
                    password = '<access_key>'
                }
            }
        }
        dependencies {
            classpath "com.societegenerale.commons:arch-unit-gradle-plugin:2.6.1"
            classpath 'com.vitech.archtests:spring:0.4'
        }
    }
    ``` 

3. Configure the plugin, add needed rules to configurableRules property 
```Gradle
allprojects {

    apply plugin: 'java'
    apply plugin: 'com.societegenerale.commons.plugin.gradle.ArchUnitGradlePlugin'

    archUnit {

        configurableRules=[
            configurableRule("com.vitech.archtests.spring.rules.TransactionalRules", applyOn("com.example.project","main"))
        ]

    }
}
``` 
4. After plugin setup, task `checkRules` will be created to run all available ArchUnit tests. Build your project 
with `gradlew clean build` (pipeline will contain task `checkRules`): if some of your code is not compliant with the 
rules defined, the build will fail, pointing you to the rule(s) and the class(es) that are violating it.


### ArchUnit rules configuration

#### Adding new rule to configuration
To add new rule(s) for verification, you have to add new configurableRule to configurableRules.
Arguments required for construction:
1. path to class with rules
2. applyOn, arguments:
    * packageName - package, that will be analyzed
    * scope - available options: main, test
```Gradle
allprojects {

    archUnit {

        configurableRules=[
            configurableRule("com.vitech.archtests.spring.rules.TransactionalRules", applyOn("com.example.project","main"))
        ]

    }
}
``` 
Third optional param is List<String> checks - list of concrete rules in class to be applied.

For example, we have class with two rules NO_TRANSACTIONAL_METHODS_CALL_FROM_THE_SAME_INSTANCE, TRANSACTIONAL_METHODS_MUST_BE_PUBLIC,
```Java
public class TransactionalRules {
  @ArchTest
  public static final ArchRule NO_TRANSACTIONAL_METHODS_CALL_FROM_THE_SAME_INSTANCE; // Implementation of the rule is skipped

  @ArchTest
  public static final ArchRule TRANSACTIONAL_METHODS_MUST_BE_PUBLIC; // Implementation of the rule is skipped
}
``` 

If we want to apply second one in our project, next configuration should be added:
```Gradle
allprojects {

    archUnit {

        configurableRules=[
                configurableRule("com.vitech.archtests.spring.rules.TransactionalRules", applyOn("com.example.archunitplugintest","main"), ["TRANSACTIONAL_METHODS_MUST_BE_PUBLIC"])
        ]

    }
}
``` 
If **no rules** are added to configurableRules, **check will fail** with message like:
> 'Arch unit Gradle Plugin should have at least one preconfigured/configurable rule!'
****
#### Excluding classes from verification
You can exclude classes that have a path containing any of the specified paths (pay attention that you have to use slash 
instead of dot)
```
allprojects {

    archUnit {
        excludedPaths=["com/example/excludePackage"]
    }
}
``` 
