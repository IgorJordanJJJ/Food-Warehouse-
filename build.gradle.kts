plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.netflix.dgs.codegen") version "6.2.1"
}

group = "ru.jordan"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(19)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("redis.clients:jedis:5.1.2")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("com.graphql-java:graphql-java-extended-scalars:17.0")
    implementation("com.graphql-java:graphql-java-extended-validation:22.0")
//    implementation("com.tailrocks.graphql:graphql-datetime-kickstart-spring-boot-starter:6.0.0")
//    implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:12.0.0")
//    implementation("com.netflix.graphql.dgs.codegen:graphql-dgs-codegen-shared-core:6.2.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.modelmapper:modelmapper:3.1.1")
    implementation("org.apache.kafka:kafka-streams")
    implementation("org.liquibase:liquibase-core")
    implementation("org.apache.logging.log4j:log4j-api:2.23.1")
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.23.1")
    implementation("com.github.jsqlparser:jsqlparser:4.9")
    implementation("io.minio:minio:8.5.10")
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")
    implementation("org.projectlombok:lombok-mapstruct-binding:0.2.0")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    compileOnly("org.mapstruct:mapstruct:1.5.5.Final")
    compileOnly("org.mapstruct:mapstruct-processor:1.5.5.Final")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webflux")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.slf4j:slf4j-simple:2.0.12")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.generateJava {
    schemaPaths.add("${projectDir}/src/main/resources/graphql-client")
    packageName = "ru.jordan.food_storage.codegen"
    generateClient = true
}

tasks.withType<Test> {
    useJUnitPlatform()
}
