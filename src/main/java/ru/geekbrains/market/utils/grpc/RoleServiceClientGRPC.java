package ru.geekbrains.market.utils.grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Service;
import ru.geekbrains.market.RoleRequest;
import ru.geekbrains.market.RoleResponse;
import ru.geekbrains.market.RoleServiceGrpc;
import ru.geekbrains.market.entities.Role;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RoleServiceClientGRPC {

    private static final Logger logger = Logger.getLogger(RoleServiceClientGRPC.class.getName());

    private final RoleServiceGrpc.RoleServiceBlockingStub blockingStub;

    public RoleServiceClientGRPC() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9089)
                .usePlaintext()
                .build();
        this.blockingStub = RoleServiceGrpc.newBlockingStub(channel);
    }

    public List<String> getListRoleNameByUsername (String username) throws InterruptedException {
        List<String> roleList = new ArrayList<>();

        RoleRequest request = RoleRequest.newBuilder().setUsername(username).build();

        Iterator<RoleResponse> responseItr;
        try {
            responseItr = blockingStub.listRoles(request);
            while (responseItr.hasNext()){
                roleList.add(responseItr.next().getRolename());
            }
        } catch (StatusRuntimeException e) {
            logger.warning("GRPC failed: {0}" + e.getStatus());
        }
        return roleList;
    }

}
