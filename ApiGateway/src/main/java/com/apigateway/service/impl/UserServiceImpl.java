package com.apigateway.service.impl;

import com.apigateway.dto.UpdateUserDTO;
import com.apigateway.model.User;
import com.apigateway.security.util.TokenUtils;
import com.apigateway.service.UserService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import proto.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    @GrpcClient("profilegrpcservice")
    private UserGrpcServiceGrpc.UserGrpcServiceBlockingStub stub;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public UpdateUserResponseProto update(UpdateUserDTO dto) {
        UpdateUserProto updateUserProto = UpdateUserProto.newBuilder().setUuid(dto.getUuid()).setFirstName(dto.getFirstName()).setLastName(dto.getLastName()).setEmail(dto.getEmail()).setPhoneNumber(dto.getPhoneNumber()).setGender(dto.getGender()).setDateOfBirth(dto.getDateOfBirth()).setUsername(dto.getUsername()).setBiography(dto.getBiography()).setProfilePublic(dto.isProfilePublic()).build();
        return this.stub.update(updateUserProto);

    }

    @Override
    public FindUserResponseProto find(String firstName, String lastName) {
        FindUserProto findUserProto = FindUserProto.newBuilder().setFirstName(firstName).setLastName(lastName).build();
        return this.stub.find(findUserProto);
    }

    @Override
    public UserNamesResponseProto getFirstAndLastName(String id) {
        UserNamesProto userNamesResponseProto = UserNamesProto.newBuilder().setId(id).build();
        return this.stub.getFirstAndLastName(userNamesResponseProto);
    }

    @Override
    public EmailResponseProto getEmail(String id) {
        EmailProto emailProto = EmailProto.newBuilder().setId(id).build();
        return this.stub.getEmail(emailProto);
    }

    @Override
    public IdResponseProto getId(String email) {
        IdProto idProto = IdProto.newBuilder().setEmail(email).build();
        return this.stub.getId(idProto);
    }

    @Override
    public UserResponseProto getById(String id) {
        UserIdProto idProto = UserIdProto.newBuilder().setId(id).build();
        return this.stub.getById(idProto);
    }

    @Override
    public String getIdByToken(String token) {
        return tokenUtils.getUsernameFromToken(token);
    }


    @Override
    public String getUserIdByToken(String token) {
        return tokenUtils.getUserIdFromToken(token);
    }

    @Override
    public FindUserResponseProto findUsersByIds(RecommendationsResponseProto ids) {
        List<String> idsList = new ArrayList<>();
        for(String id : ids.getUserIdList()){idsList.add(id);}
        UserIdsProto idsProto = UserIdsProto.newBuilder().addAllId(idsList).build();
        return this.stub.findProfilesById(idsProto);
    }
}
