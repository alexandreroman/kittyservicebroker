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

/**
 * Kitty catalog OSBAPI identifier.
 */
const val SERVICE_DEF_ID = "ce938e77-085d-47e9-bac3-5216d77ef507"

/**
 * Standard plan identifier.
 */
const val STANDARD_PLAN_ID = "d2e202fe-9711-4f6c-823e-b214c04e0a05"

inline fun checkServiceDefinitionAndPlan(serviceDefId: String, planId: String) {
    if (serviceDefId != SERVICE_DEF_ID) {
        throw ServiceBrokerInvalidParametersException("Unsupported service definition: $serviceDefId")
    }
    if (planId != STANDARD_PLAN_ID) {
        throw ServiceBrokerInvalidParametersException("Unsupported plan: $planId")
    }
}
