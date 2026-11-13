plugins {
    id("pokedex.android.library")
    alias(libs.plugins.apollo)
}

android {
    namespace = "id.renaldi.pokedex.core.data"
}

apollo {
    service("pokeapi") {
        packageName.set("id.renaldi.pokedex.core.data.graphql")
        introspection {
            endpointUrl.set("https://graphql.pokeapi.co/v1beta2")
            schemaFile.set(file("src/main/graphql/schema.graphqls"))
        }
    }
}

dependencies {
    implementation(libs.koin.core)

    api(projects.core.model)
    api(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.core.database)

    implementation(libs.apollo.runtime)

    testImplementation(projects.core.testing)
    testImplementation(projects.core.fixtures)
}
