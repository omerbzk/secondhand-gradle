package com.folksdev.secondhand.user.service;

import com.folksdev.secondhand.user.TestSupport;
import com.folksdev.secondhand.user.dto.CreateUserRequest;
import com.folksdev.secondhand.user.dto.UserDto;
import com.folksdev.secondhand.user.dto.UserDtoConverter;
import com.folksdev.secondhand.user.exception.UserNotFoundException;
import com.folksdev.secondhand.user.model.UserInformation;
import com.folksdev.secondhand.user.repository.UserInformationRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;

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
        String mail = "omer@hotmail.com";
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
        String mail = "omer@hotmail.com";

        when(repository.findByMail(mail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                userService.getUserByMail(mail)
        );

        verify(repository).findByMail(mail);
        verifyNoInteractions(converter);
    }

//    @Test
//    public void testCreateUser_itShouldReturnCreatedUserDto() {
//
//        String mail = "omer@hotmail.com";
//        CreateUserRequest request = new CreateUserRequest(
//                mail,
//                "firstName",
//                "lastName",
//                "");
//
//
//        when(repository.findByMail(mail)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () ->
//                userService.getUserByMail(mail)
//        );
//
//        verify(repository).findByMail(mail);
//        verifyNoInteractions(converter);
//    }




}