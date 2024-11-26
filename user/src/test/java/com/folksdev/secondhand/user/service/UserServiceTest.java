package com.folksdev.secondhand.user.service;

import com.folksdev.secondhand.user.TestSupport;
import com.folksdev.secondhand.user.dto.CreateUserRequest;
import com.folksdev.secondhand.user.dto.UpdateUserRequest;
import com.folksdev.secondhand.user.dto.UserDto;
import com.folksdev.secondhand.user.dto.UserDtoConverter;
import com.folksdev.secondhand.user.exception.UserNotFoundException;
import com.folksdev.secondhand.user.model.UserInformation;
import com.folksdev.secondhand.user.repository.UserInformationRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@TestInstance(Lifecycle.PER_CLASS)
class UserServiceTest extends TestSupport {

    private UserDtoConverter converter;
    private UserInformationRepository repository;

    private UserService userService;

    @BeforeAll
    public void setUp() {
        converter = mock(UserDtoConverter.class);
        repository = mock(UserInformationRepository.class);

        userService = new UserService(repository, converter);
    }

    @Test
    public void testGetAllUsers_itShouldReturnUserList() {
        List<UserInformation> userList = generateUsers();
        List<UserDto> userDtoList = generateUserDtoList(userList);

        when(repository.findAll()).thenReturn(userList);
        when(converter.convert(userList)).thenReturn(userDtoList);

        List<UserDto> result = userService.getAllUsers();

        assertEquals(userDtoList, result);
        verify(repository).findAll();
        verify(converter).convert(userList);
    }

    @Test
    public void testGetUserByMail_whenUserMailExist_itShouldReturnUserDto() {
        String mail = "omertrer@hotmail.com";
        UserInformation user = generateUser(mail);
        UserDto userDto = generateUserDto(mail);

        when(repository.findByMail(mail)).thenReturn(Optional.of(user));
        when(converter.convert(user)).thenReturn(userDto);

        UserDto result = userService.getUserByMail(mail);

        assertEquals(userDto, result);
        verify(repository).findByMail(mail);
        verify(converter).convert(user);
    }

    @Test
    public void testGetUserByMail_whenUserMailDoesNotExist_itShouldThrowUserNotFoundException() {
        String mail = "omeeerrrr@hotmail.com";

        when(repository.findByMail(mail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getUserByMail(mail)
        );

        verify(repository).findByMail(mail);
        verifyNoInteractions(converter);
    }

    @Test
    public void testCreateUser_itShouldReturnCreatedUserDto() {

        String mail = "omeeer@hotmail.com";
        CreateUserRequest request = new CreateUserRequest(mail, "firstName", "lastName", "");
        UserInformation user = new UserInformation(mail, "firstName", "lastName","", false);
        UserInformation savedUser = new UserInformation(1L, mail, "firstName", "lastName","", false);
        UserDto userDto = new UserDto(mail, "firstName", "lastName","");

        when(repository.save(user)).thenReturn(savedUser);
        when(converter.convert(savedUser)).thenReturn(userDto);


        UserDto result = userService.createUser(request);

        assertEquals(userDto, result);

        verify(repository).save(user);
        verify(converter).convert(savedUser);
    }

    @Test
    public void testUpdateUser_whenUserMailExistAndUserIsActive_itShouldReturnUpdatedUserDto() {
        String mail = "omerrr@hotmail.com";
        UpdateUserRequest request = new UpdateUserRequest( "firstName2", "lastName2", "middleName");
        UserInformation user = new UserInformation(1L, mail, "firstName", "lastName","", true);
        UserInformation updatedUser = new UserInformation(1L, mail, "firstName2", "lastName2","middleName", true);
        UserInformation savedUser = new UserInformation(1L, mail, "firstName2", "lastName2","middleName", false);
        UserDto userDto = new UserDto(mail, "firstName2", "lastName2","middleName");

        when(repository.findByMail(mail)).thenReturn(Optional.of(user));
        when(repository.save(updatedUser)).thenReturn(savedUser);
        when(converter.convert(savedUser)).thenReturn(userDto);


        UserDto result = userService.updateUser(mail, request);

        assertEquals(userDto, result);

        verify(repository).findByMail(mail);
        verify(repository).save(updatedUser);
        verify(converter).convert(savedUser);
    }




}