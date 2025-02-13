// Copyright 2000-2017 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.plugins.gradle.internal.daemon;

import org.gradle.internal.id.IdGenerator;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.launcher.daemon.client.DaemonClientConnection;
import org.gradle.launcher.daemon.client.DaemonClientFactory;
import org.gradle.launcher.daemon.client.DaemonConnector;
import org.gradle.launcher.daemon.client.DaemonStopClient;
import org.gradle.launcher.daemon.protocol.Stop;
import org.gradle.launcher.daemon.registry.DaemonInfo;
import org.gradle.launcher.daemon.registry.DaemonRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Vladislav.Soroka
 */
public class DaemonStopAction extends DaemonAction {
  public DaemonStopAction(String serviceDirectoryPath) {
    super(serviceDirectoryPath);
  }

  public void run(DaemonClientFactory daemonClientFactory) {
    ServiceRegistry daemonServices = getDaemonServices(daemonClientFactory);
    DaemonStopClient stopClient = daemonServices.get(DaemonStopClient.class);
    stopClient.stop();
  }

  public void run(DaemonClientFactory daemonClientFactory, List<byte[]> tokens) {
    ServiceRegistry daemonServices = getDaemonServices(daemonClientFactory);
    DaemonRegistry daemonRegistry = daemonServices.get(DaemonRegistry.class);
    DaemonConnector daemonConnector = daemonServices.get(DaemonConnector.class);
    IdGenerator<?> idGenerator = daemonServices.get(IdGenerator.class);

    List<DaemonInfo> list = new ArrayList<>(daemonRegistry.getAll());
    for (byte[] token : tokens) {
      for (Iterator<DaemonInfo> iterator = list.iterator(); iterator.hasNext(); ) {
        DaemonInfo info = iterator.next();
        if (Arrays.equals(info.getToken(), token)) {
          iterator.remove();
          DaemonClientConnection connection = daemonConnector.maybeConnect(info);
          if (connection != null) {
            try {
              Stop stopCommand = createCommand(Stop.class, idGenerator.generateId(), token);
              connection.dispatch(stopCommand);
            }
            finally {
              connection.stop();
            }
          }
          break;
        }
      }
    }
  }
}
