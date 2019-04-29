/*
 * Copyright (c) 2019 Pivotal Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.alexandreroman.demos.kittyservicebroker.osbapi

import org.springframework.cloud.servicebroker.model.catalog.Catalog
import org.springframework.cloud.servicebroker.model.catalog.Plan
import org.springframework.cloud.servicebroker.model.catalog.ServiceDefinition
import org.springframework.cloud.servicebroker.service.CatalogService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * Kitty [CatalogService] implementation.
 */
@Component
class KittyCatalogService : CatalogService {
    override fun getCatalog() =
            Mono.just(Catalog.builder().serviceDefinitions(getServiceDefinition(SERVICE_DEF_ID).block()).build())

    override fun getServiceDefinition(serviceId: String?) =
            Mono.just(ServiceDefinition.builder()
                    .id(SERVICE_DEF_ID)
                    .bindable(true)
                    .name("kitty")
                    .description("Access to kitty images")
                    .bindable(true)
                    .instancesRetrievable(true)
                    .bindingsRetrievable(true)
                    .tags("kitty")
                    .plans(standardPlan())
                    .metadata(mapOf(
                            "displayName" to "Kitty Service Broker",
                            "imageUrl" to "https://i.imgur.com/fl38v1A.png",
                            "providerDisplayName" to "Alexandre Roman",
                            "longDescription" to "Kitty Service Broker brings you the joy of viewing random kitty images.",
                            "documentationUrl" to "https://github.com/alexandreroman/kittyservicebroker",
                            "supportUrl" to "https://github.com/alexandreroman/kittyservicebroker/issues"
                    ))
                    .build())

    private fun standardPlan() =
            Plan.builder()
                    .id(STANDARD_PLAN_ID)
                    .name("standard")
                    .description("Random kitty image for your viewing pleasure")
                    .bindable(true)
                    .free(true)
                    .metadata(mapOf(
                            "shareable" to "true",
                            "displayName" to "Random kitty",
                            "bullets" to arrayOf("Kitties everywhere!")
                    ))
                    .build()
}
