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

import fr.alexandreroman.demos.kittyservicebroker.service.KittyService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Component managing Kitty service instance bindings.
 */
@Component
class KittyServiceInstanceBindingManager(private val kittyService: KittyService) {
    private val instances = mutableMapOf<String, KittyServiceInstanceBinding>()
    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(instanceBindingId: String): KittyServiceInstanceBinding? {
        if (instances.containsKey(instanceBindingId)) {
            return instances[instanceBindingId]
        }
        logger.debug("Creating Kitty service instance binding: {}", instanceBindingId)
        val uri = kittyService.getRandomImageUrl() ?: return null
        val binding = KittyServiceInstanceBinding(instanceBindingId, uri)
        logger.info("Kitty service instance bound: {} -> {}", instanceBindingId, uri)
        return binding
    }

    fun delete(intanceBindingId: String) {
        if (instances.containsKey(intanceBindingId)) {
            logger.info("Deleting Kitty service instance binding: {}", intanceBindingId)
            instances.remove(intanceBindingId)
        }
    }

    fun instance(instanceId: String): KittyServiceInstanceBinding? {
        return instances[instanceId]
    }

    fun instanceExists(instanceId: String) = instances.containsKey(instanceId)
}
