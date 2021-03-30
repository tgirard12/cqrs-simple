rootProject.name = "cqrs-simple"

include(":cqrs-simple-core")
project(":cqrs-simple-core").buildFileName = "cqrs-simple-core.gradle.kts"

include(":cqrs-simple-spring")
project(":cqrs-simple-spring").buildFileName = "cqrs-simple-spring.gradle.kts"
