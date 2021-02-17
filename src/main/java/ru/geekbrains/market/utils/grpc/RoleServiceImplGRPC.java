package ru.geekbrains.market.utils.grpc;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.geekbrains.market.RoleRequest;
import ru.geekbrains.market.RoleResponse;
import ru.geekbrains.market.RoleServiceGrpc;
import ru.geekbrains.market.entities.Role;
import ru.geekbrains.market.entities.User;
import ru.geekbrains.market.services.UserServiceImpl;

import java.util.Collection;

@Service
public class RoleServiceImplGRPC extends RoleServiceGrpc.RoleServiceImplBase {
    private UserServiceImpl userService;
    private final Logger logger = LoggerFactory.getLogger(RoleServiceImplGRPC.class);

    @Autowired
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public void listRoles(RoleRequest request, StreamObserver<RoleResponse> responseObserver) {
        String userName = request.getUsername();
        User user = userService.findByUserName(userName).orElse(null);
        if (user == null) {
            responseObserver.onError(new UsernameNotFoundException("User not found"));
        }
        Collection<Role> roles = user.getRoles();
        for (Role r : roles) {
            responseObserver.onNext(RoleResponse.newBuilder().setRolename(r.getName()).build());
        }
        responseObserver.onCompleted();
    }

}
