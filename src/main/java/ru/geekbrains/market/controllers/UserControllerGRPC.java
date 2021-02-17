package ru.geekbrains.market.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.market.entities.Role;
import ru.geekbrains.market.services.UserService;
import ru.geekbrains.market.utils.grpc.RoleServiceClientGRPC;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserControllerGRPC {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerGRPC.class);
    private UserService userService;
    private RoleServiceClientGRPC roleServiceClientGRPC;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleServiceClientGRPC(RoleServiceClientGRPC roleServiceClientGRPC) {
        this.roleServiceClientGRPC = roleServiceClientGRPC;
    }


    @GetMapping("/{id}/id")
    public String getUserRoles(@PathVariable("id") Long id){
        String username = userService.findById(id).orElseThrow().getUserName();
        List<String> userRoles;
        try{
            userRoles = roleServiceClientGRPC.getListRoleNameByUsername(username);
        } catch (InterruptedException e) {
            throw new UsernameNotFoundException("User not found with id = " + id);
        }
        return userRoles.toString();
    }

}
