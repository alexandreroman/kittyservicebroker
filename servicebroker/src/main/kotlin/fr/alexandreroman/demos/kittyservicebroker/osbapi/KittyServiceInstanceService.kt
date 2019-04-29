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

import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException
import org.springframework.cloud.servicebroker.model.instance.*
import org.springframework.cloud.servicebroker.service.ServiceInstanceService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

/**
 * [ServiceInstanceService] implementation, managing Kitty service instances
 * through the Open Service Broker API.
 */
@Service
class KittyServiceInstanceService(private val instanceManager: KittyServiceInstanceManager) : ServiceInstanceService {
    override fun createServiceInstance(req: CreateServiceInstanceRequest?): Mono<CreateServiceInstanceResponse> {
        checkServiceDefinitionAndPlan(req!!.serviceDefinitionId, req.planId)

        val instanceExisted = instanceManager.instanceExists(req.serviceInstanceId)
        if (!instanceExisted) {
            instanceManager.create(req.serviceInstanceId, req.planId)
                    ?: throw ServiceBrokerInvalidParametersException("Failed to create Kitty service instance")
        }

        return Mono.just(CreateServiceInstanceResponse.builder()
                .async(false)
                .instanceExisted(instanceExisted)
                .operation("Kitties everywhere!")
                .build())
    }

    override fun deleteServiceInstance(req: DeleteServiceInstanceRequest?): Mono<DeleteServiceInstanceResponse> {
        checkServiceDefinitionAndPlan(req!!.serviceDefinitionId, req.planId)
        instanceManager.delete(req.serviceInstanceId)
        return Mono.just(DeleteServiceInstanceResponse.builder()
                .async(false)
                .build())
    }

    override fun getLastOperation(request: GetLastServiceOperationRequest?): Mono<GetLastServiceOperationResponse> {
        return Mono.just(GetLastServiceOperationResponse.builder()
                .operationState(OperationState.SUCCEEDED)
                .description("Kitties everywhere!")
                .build())
    }

    override fun getServiceInstance(req: GetServiceInstanceRequest?): Mono<GetServiceInstanceResponse> {
        instanceManager.instance(req!!.serviceInstanceId)
                ?: throw ServiceInstanceDoesNotExistException(req.serviceInstanceId)

        return Mono.just(GetServiceInstanceResponse.builder()
                .serviceDefinitionId(SERVICE_DEF_ID)
                .planId(STANDARD_PLAN_ID)
                .build())
    }

    override fun updateServiceInstance(request: UpdateServiceInstanceRequest?): Mono<UpdateServiceInstanceResponse> {
        return Mono.just(UpdateServiceInstanceResponse.builder()
                .async(false)
                .build())
    }
}
