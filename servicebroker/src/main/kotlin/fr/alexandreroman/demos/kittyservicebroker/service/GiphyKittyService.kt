/*
 * Copyright (c) 2018 Pivotal Software, Inc.
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

package fr.alexandreroman.demos.kittyservicebroker.service

import com.fasterxml.jackson.databind.node.TextNode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * [KittyService] service implementation using Giphy.
 */
@Component
class GiphyKittyService(
        private val giphyClient: GiphyClient,
        private val giphyConfig: GiphyConfig) : KittyService {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getRandomImageUrl(): String? {
        logger.debug("Fetching random image URL: apiKey={}", giphyConfig.apiKey)
        val json = giphyClient.random(giphyConfig.apiKey)
        val url = (json.get("data")?.get("images")?.get("original")?.get("url") as TextNode?)?.asText()
        logger.info("Got random image: {}", url)
        return url
    }
}
