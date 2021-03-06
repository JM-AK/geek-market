package ru.geekbrains.market.utils.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.market.services.UserServiceImpl;

import javax.persistence.Access;
import java.io.IOException;
import java.util.logging.Logger;

@Service
public class RoleServiceServerGRPC {
    private static final Logger logger = Logger.getLogger(RoleServiceServerGRPC.class.getName());
    private final int PORT_GRPC = 9089;
    private final Server server;

    @Autowired
    public RoleServiceServerGRPC(UserServiceImpl userService) {
        RoleServiceImplGRPC roleServiceImplGRPC = new RoleServiceImplGRPC(userService);
        this.server = ServerBuilder.forPort(PORT_GRPC).addService(roleServiceImplGRPC).build();
    }

    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + PORT_GRPC);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    RoleServiceServerGRPC.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });

    }
    public void stop() throws InterruptedException {
        logger.info("Server stop, listening on " + PORT_GRPC);
        if (server != null) {
            server.shutdown().awaitTermination();
        }
    }

}
