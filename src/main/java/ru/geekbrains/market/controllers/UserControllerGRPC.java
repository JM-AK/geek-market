package ru.geekbrains.market.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.geekbrains.market.entities.Role;
import ru.geekbrains.market.services.UserService;
import ru.geekbrains.market.utils.grpc.RoleServiceClientGRPC;
import ru.geekbrains.market.utils.grpc.RoleServiceServerGRPC;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserControllerGRPC {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerGRPC.class);
    private UserService userService;
    private RoleServiceClientGRPC roleServiceClientGRPC;
    private RoleServiceServerGRPC roleServiceServerGRPC;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleServiceClientGRPC(RoleServiceClientGRPC roleServiceClientGRPC) {
        this.roleServiceClientGRPC = roleServiceClientGRPC;
    }

    @Autowired
    public void setRoleServiceServerGRPC(RoleServiceServerGRPC roleServiceServerGRPC) {
        this.roleServiceServerGRPC = roleServiceServerGRPC;
    }

    @GetMapping("/roles/{id}")
    @ResponseBody
    public String getUserRoles(@PathVariable("id") Long id){
        String username = userService.findById(id).orElseThrow().getUserName();
        List<String> userRoles;

        try{
            roleServiceServerGRPC.start();
            userRoles = roleServiceClientGRPC.getListRoleNameByUsername(username);

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User not found with id = " + id);
        }
        return userRoles.toString();
    }

}
