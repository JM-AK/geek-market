package ru.geekbrains.market.utils.grpc;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import ru.geekbrains.market.RoleRequest;
import ru.geekbrains.market.RoleResponse;
import ru.geekbrains.market.RoleServiceGrpc;
import ru.geekbrains.market.entities.Role;
import ru.geekbrains.market.entities.User;
import ru.geekbrains.market.services.UserServiceImpl;

import java.util.Collection;

@Component
public class RoleServiceImplGRPC extends RoleServiceGrpc.RoleServiceImplBase {
    private UserServiceImpl theUserService;
    private final Logger logger = LoggerFactory.getLogger(RoleServiceImplGRPC.class);

    @Autowired
    public void setUserService(UserServiceImpl theUserService) {
        this.theUserService = theUserService;
    }

    @Override
    public void listRoles(RoleRequest request, StreamObserver<RoleResponse> responseObserver) {
        String userName = request.getUsername();
        logger.info("Try to find user: " + userName);
        //ToDo разобраться, почему выдает ошибку при обращении к сервису. не может инициализировать
        User user = theUserService.findByUserName(userName).orElse(null);
        if (user == null) {
            responseObserver.onError(new UsernameNotFoundException("User not found"));
        }
        Collection<Role> roles = user.getRoles();
        System.out.println(roles.size());
        for (Role r : roles) {
            responseObserver.onNext(RoleResponse.newBuilder().setRolename(r.getName()).build());
        }
        responseObserver.onCompleted();
    }

}
