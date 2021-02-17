package ru.geekbrains.market.utils.grpc;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.geekbrains.market.RoleRequest;
import ru.geekbrains.market.RoleResponse;
import ru.geekbrains.market.RoleServiceGrpc;
import ru.geekbrains.market.entities.Role;
import ru.geekbrains.market.entities.User;
import ru.geekbrains.market.services.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImplGRPC extends RoleServiceGrpc.RoleServiceImplBase {
    private final UserServiceImpl theUserService;
    private final Logger logger = LoggerFactory.getLogger(RoleServiceImplGRPC.class);

    public RoleServiceImplGRPC(UserServiceImpl theUserService) {
        this.theUserService = theUserService;
    }

    @Override
    public void listRoles(RoleRequest request, StreamObserver<RoleResponse> responseObserver) {
        String userName = request.getUsername();
        logger.info("Try to find user: " + userName);

        User user = theUserService.findByUserName(userName).orElse(null);
        if (user == null) {
            responseObserver.onError(new UsernameNotFoundException("User not found"));
        }
        logger.info("User found: " + user.getUserName());

        List<Role> roleList = new ArrayList<>(user.getRoles());
        for (Role r : roleList) {
            responseObserver.onNext(RoleResponse.newBuilder().setRolename(r.getName()).build());
        }
        responseObserver.onCompleted();
    }

}
