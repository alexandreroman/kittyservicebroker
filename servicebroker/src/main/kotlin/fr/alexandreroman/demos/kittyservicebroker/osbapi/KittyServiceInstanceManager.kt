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

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Component managing Kitty service instances.
 */
@Component
class KittyServiceInstanceManager {
    private val instances = mutableMapOf<String, KittyServiceInstance>()
    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(instanceId: String, planId: String): KittyServiceInstance? {
        if (instances.containsKey(instanceId)) {
            return instances[instanceId]
        }
        val service = KittyServiceInstance(instanceId, planId)
        instances[instanceId] = service
        logger.info("Kitty service instance created: {}", instanceId)
        return service
    }

    fun delete(instanceId: String) {
        if (instances.containsKey(instanceId)) {
            logger.info("Deleting Kitty service instance: {}", instanceId)
            instances.remove(instanceId)
        }
    }

    fun instance(instanceId: String): KittyServiceInstance? {
        return instances[instanceId]
    }

    fun instanceExists(instanceId: String) = instances.containsKey(instanceId)
}
