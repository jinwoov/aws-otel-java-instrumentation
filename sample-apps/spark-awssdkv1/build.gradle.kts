plugins {
  java

  application
  id("com.google.cloud.tools.jib")
}

dependencies {
  implementation("commons-logging:commons-logging")
  implementation("com.amazonaws:aws-java-sdk-s3")
  implementation("com.amazonaws:aws-java-sdk-sts")
  implementation("com.sparkjava:spark-core")
  implementation("com.squareup.okhttp3:okhttp")
  implementation("io.opentelemetry:opentelemetry-api")
}

application {
  mainClass.set("com.amazon.sampleapp.App")
}

jib {
  to {
    image = "public.ecr.aws/aws-otel-test/aws-otel-java-spark-awssdkv1"
    tags = setOf("latest", "${System.getenv("COMMIT_HASH")}")
  }
  from {
    image = "public.ecr.aws/aws-otel-test/aws-opentelemetry-java-base:alpha"
    platforms {
      platform {
        architecture = "amd64"
        os = "linux"
      }
      platform {
        architecture = "arm64"
        os = "linux"
      }
    }
  }
}

tasks {
  named("jib") {
    dependsOn(":otelagent:jib")
  }
}
