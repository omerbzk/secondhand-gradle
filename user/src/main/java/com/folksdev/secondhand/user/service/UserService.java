package com.folksdev.secondhand.user.service;

import com.folksdev.secondhand.user.dto.CreateUserRequest;
import com.folksdev.secondhand.user.dto.UpdateUserRequest;
import com.folksdev.secondhand.user.dto.UserDto;
import com.folksdev.secondhand.user.dto.UserDtoConverter;
import com.folksdev.secondhand.user.exception.UserIsNotActiveException;
import com.folksdev.secondhand.user.exception.UserNotFoundException;
import com.folksdev.secondhand.user.model.UserInformation;
import com.folksdev.secondhand.user.repository.UserInformationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserInformationRepository userInformationRepository;
    private final UserDtoConverter userDtoConverter;

    public UserService(UserInformationRepository userInformationRepository, UserDtoConverter userDtoConverter) {
        this.userInformationRepository = userInformationRepository;
        this.userDtoConverter = userDtoConverter;
    }

    public List<UserDto> getAllUsers() {
        return userDtoConverter.convert(userInformationRepository.findAll());
    }

    public UserDto getUserByMail(String mail) {
        UserInformation userInformation = findUserByMail(mail);
        return userDtoConverter.convert(userInformation);
    }

    public UserDto createUser(CreateUserRequest userRequest) {
        UserInformation userInformation = new UserInformation(null,
                userRequest.getMail(),
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getMiddleName(),
                true);
        return userDtoConverter.convert(userInformationRepository.save(userInformation));
    }

    public UserDto updateUser(String mail, UpdateUserRequest updateUserRequest) {
        UserInformation userInformation = findUserByMail(mail);
        if (!userInformation.getActive()) {
            logger.warn(String.format( "The user wanted update is not active!, user mail: %s", mail));
            throw new UserIsNotActiveException();
        }
        UserInformation updatedUserInformation = new UserInformation(userInformation.getId(),
                userInformation.getMail(),
                updateUserRequest.getFirstName(),
                updateUserRequest.getLastName(),
                updateUserRequest.getMiddleName());
        return userDtoConverter.convert(userInformationRepository.save(updatedUserInformation));
    }

    public void deactivateUser(Long id) {
        changeActivateUser(id, false);
    }

    public void activeUser(Long id) {
        changeActivateUser(id, true);
    }

    public void deleteUser(Long id) {
        if (doesUserExist(id)) {
            userInformationRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("User couldn't be found by following id: " + id);
        }
    }

    private UserInformation findUserByMail(String mail) {
        return userInformationRepository.findByMail(mail)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found by following mail: " + mail));
    }

    private void changeActivateUser(Long id, Boolean isActive) {
        UserInformation userInformation = findUserById(id);

        UserInformation changeActivateUser = new UserInformation(userInformation.getId(),
                userInformation.getMail(),
                userInformation.getFirstName(),
                userInformation.getLastName(),
                userInformation.getMiddleName(),
                isActive);
        userInformationRepository.save(changeActivateUser);
    }

    private UserInformation findUserById(Long id) {
        return userInformationRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User couldn't be found by following id: " + id));
    }

    private boolean doesUserExist(Long id) {
        return userInformationRepository.existsById(id);
    }
}
