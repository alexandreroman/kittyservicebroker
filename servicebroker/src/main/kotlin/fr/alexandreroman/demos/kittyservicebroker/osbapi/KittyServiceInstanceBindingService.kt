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

import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException
import org.springframework.cloud.servicebroker.model.binding.*
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * [ServiceInstanceBindingService] implementation, providing Kitty image URLs.
 */
@Service
class KittyServiceInstanceBindingService(
        private val instanceBindingManager: KittyServiceInstanceBindingManager) : ServiceInstanceBindingService {
    override fun createServiceInstanceBinding(req: CreateServiceInstanceBindingRequest?): Mono<CreateServiceInstanceBindingResponse> {
        checkServiceDefinitionAndPlan(req!!.serviceDefinitionId, req.planId)

        val bindingExisted = instanceBindingManager.instanceExists(req.bindingId)
        val binding = instanceBindingManager.create(req.bindingId)
                ?: throw UnsupportedOperationException("Cannot bind Kitty service instance")

        // Create a service binding, no user credentials required.
        return Mono.just(CreateServiceInstanceAppBindingResponse.builder()
                .bindingExisted(bindingExisted)
                .credentials("uri", binding.uri)
                .build())
    }

    override fun deleteServiceInstanceBinding(req: DeleteServiceInstanceBindingRequest?): Mono<DeleteServiceInstanceBindingResponse> {
        checkServiceDefinitionAndPlan(req!!.serviceDefinitionId, req.planId)
        instanceBindingManager.delete(req.bindingId)
        return Mono.just(DeleteServiceInstanceBindingResponse.builder()
                .build())
    }

    override fun getServiceInstanceBinding(req: GetServiceInstanceBindingRequest?): Mono<GetServiceInstanceBindingResponse> {
        val binding = instanceBindingManager.instance(req!!.bindingId)
                ?: throw ServiceInstanceBindingDoesNotExistException(req.bindingId)
        return Mono.just(GetServiceInstanceAppBindingResponse.builder()
                .credentials("uri", binding.uri)
                .build())
    }
}
