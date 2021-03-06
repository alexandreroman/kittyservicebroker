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

package fr.alexandreroman.demos.kittyservicebroker.consumer.cloudfoundry

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator
import org.springframework.cloud.cloudfoundry.Tags

/**
 * Component responsible for [KittyServiceInfo] instances,
 * when such services are bound to this app.
 */
class KittyServiceInfoCreator : CloudFoundryServiceInfoCreator<KittyServiceInfo>(Tags("kitty")) {
    override fun createServiceInfo(serviceData: MutableMap<String, Any>?): KittyServiceInfo {
        val credentials = getCredentials(serviceData)
        return KittyServiceInfo(getId(serviceData), getUriFromCredentials(credentials))
    }
}
