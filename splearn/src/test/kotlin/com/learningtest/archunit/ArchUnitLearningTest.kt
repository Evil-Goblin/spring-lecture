package com.learningtest.archunit

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

@AnalyzeClasses(packages = ["com.learningtest.archunit"])
class ArchUnitLearningTest {

    /**
     * application 클래스를 의존하는 클래스는 application, adapter 에만 존재해야 한다.
     */
    @ArchTest
    fun application(classes: JavaClasses) {
        classes().that().resideInAPackage("..application..")
            .should().onlyHaveDependentClassesThat().resideInAnyPackage("..application..", "..adapter..")
            .check(classes)
    }

    /**
     * application 클래스는 adapter 클래스를 의존해서는 안된다.
     */
    @ArchTest
    fun adapter(classes: JavaClasses) {
        noClasses().that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..adapter..")
            .check(classes)
    }

    /**
     * Domain 의 클래스는 domain, java, kotlin 만 의존한다.
     */
    @ArchTest
    fun domain(classes: JavaClasses) {
        classes().that().resideInAPackage("..domain..")
            .should().onlyDependOnClassesThat().resideInAnyPackage("..domain..", "java..", "kotlin..")
            .check(classes)
    }
}
