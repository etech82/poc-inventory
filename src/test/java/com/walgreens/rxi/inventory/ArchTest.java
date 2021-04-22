package com.walgreens.rxi.inventory;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.walgreens.rxi.inventory");

        noClasses()
            .that()
            .resideInAnyPackage("com.walgreens.rxi.inventory.service..")
            .or()
            .resideInAnyPackage("com.walgreens.rxi.inventory.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.walgreens.rxi.inventory.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
