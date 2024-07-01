import com.google.protobuf.gradle.id

plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.netflix.dgs.codegen") version "6.2.1"
    id("com.google.protobuf") version "0.9.4"
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

extra["grpcVersion"] = "1.64.0"
extra["protobufVersion"] = "3.25.3"

protobuf{
    protoc {
        artifact = "com.google.protobuf:protoc:${property("protobufVersion")}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${property("grpcVersion")}"
        }
    }
    generateProtoTasks {
        all().forEach {task ->
            task.plugins {
                id("grpc") {
                    // Указываем выходную поддиректорию для сгенерированного gRPC кода
                    //outputSubDir = "grpc"
                }
            }
        }
    }
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
    implementation("com.google.protobuf:protobuf-java:3.25.3")
    implementation("net.devh:grpc-server-spring-boot-starter:3.0.0.RELEASE")
//    implementation("io.grpc:grpc-netty:${property("grpcVersion")}")
    implementation("io.grpc:grpc-netty-shaded:${property("grpcVersion")}")
    implementation("io.grpc:grpc-protobuf:${property("grpcVersion")}")
    implementation("io.grpc:grpc-stub:${property("grpcVersion")}")
    implementation("org.apache.tomcat:tomcat-annotations-api:10.1.25")
    implementation("net.devh:grpc-server-spring-boot-autoconfigure:2.15.0.RELEASE")
//    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
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
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.1")
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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
    testImplementation("org.testcontainers:testcontainers:1.19.8")
    testImplementation("org.testcontainers:postgresql:1.19.8")
    testImplementation("org.testcontainers:junit-jupiter:1.19.8")
    testImplementation("org.springframework.boot:spring-boot-testcontainers:3.3.0")
                //testImplementation("org.slf4j:slf4j-simple:2.0.12")
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
