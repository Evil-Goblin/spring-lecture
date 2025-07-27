package com.splearn

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.Architectures

@AnalyzeClasses(packages = ["com.splearn"], importOptions = [ImportOption.DoNotIncludeTests::class])
class HexagonalArchitectureTest {

    @ArchTest
    fun hexagonalArchitectureTest(classes: JavaClasses) {
        Architectures.layeredArchitecture()
            .consideringAllDependencies()
            .layer("domain").definedBy("com.splearn.domain..")
            .layer("application").definedBy("com.splearn.application..")
            .layer("adapter").definedBy("com.splearn.adapter..")
            .whereLayer("domain").mayOnlyBeAccessedByLayers("application", "adapter")
            .whereLayer("application").mayOnlyBeAccessedByLayers("adapter")
            .whereLayer("adapter").mayNotBeAccessedByAnyLayer()
            .check(classes)

    }
}
