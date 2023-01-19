/**
 * Copyright (c) p-it
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hivemind.synchronizer;

import io.hivemind.synchronizer.configuration.SynchronizerConfiguration;
import io.hivemind.synchronizer.exception.HiveSynchronizationException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.System.Logger.Level.INFO;

/**
 * Synchronizer communicates with the Hivemind
 *
 * @author Patrick-4488
 */
public class HiveSynchronizer {

    private ScheduledThreadPoolExecutor executor;

    private final ResourceProvider resourceProvider;
    private final SynchronizerConfiguration config;

    private static final System.Logger LOGGER = System.getLogger(HiveSynchronizer.class.getName());

    public HiveSynchronizer(final ResourceProvider resourceProvider, final SynchronizerConfiguration config) {
        LOGGER.log(INFO, "Setting up the HiveSynchronizer");

        this.resourceProvider = resourceProvider;
        this.config = config;
    }

    /**
     * Start synchronizing with the Hivemind
     *
     * @throws HiveSynchronizationException when synchronization fails
     */
    public void startSynchronization() throws HiveSynchronizationException {
        LOGGER.log(INFO, "Starting synchronization with Hivemind");

        executor = new ScheduledThreadPoolExecutor(1, new ThreadPoolExecutor.DiscardPolicy());
        SynchronizeTask task = new SynchronizeTask(new HiveEssenceDataProvider(resourceProvider), config);
        executor.scheduleAtFixedRate(task, 0, config.getPeriodBetweenRequests(), TimeUnit.SECONDS);

        LOGGER.log(INFO, "Started synchronization with Hivemind");
    }

    /**
     * Stop the synchronization<br>
     * Initiates an orderly shutdown
     */
    public void stopSynchronization() {
        LOGGER.log(INFO, "Stopping synchronization with Hivemind");

        executor.shutdown();

        LOGGER.log(INFO, "Stopped synchronization with Hivemind");
    }
}