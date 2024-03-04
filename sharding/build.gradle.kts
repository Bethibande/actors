plugins {
    val protobufVersion = libs.versions.protobuf.plugin.get()
    id(libs.plugins.protobuf.get().pluginId) version protobufVersion
}

dependencies {
    implementation(libs.kotlinx.coroutines)

    implementation(libs.io.grpc.kotlin.stub)
    implementation(libs.io.grpc.protobuf)
    implementation(libs.protobuf.kotlin)
}

protobuf {
    val protobufVersion = libs.versions.protobuf.asProvider().get()
    val grpcVersion = libs.versions.io.grpc.asProvider().get()
    val grpcKotlinVersion = libs.versions.io.grpc.kotlin.get()

    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.builtins {
                create("kotlin")
            }
        }
    }
}