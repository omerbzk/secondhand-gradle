package com.folksdev.secondhand.user.service;

import com.folksdev.secondhand.user.TestSupport;
import com.folksdev.secondhand.user.dto.UserDto;
import com.folksdev.secondhand.user.dto.UserDtoConverter;
import com.folksdev.secondhand.user.model.UserInformation;
import com.folksdev.secondhand.user.repository.UserInformationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest extends TestSupport {

    private UserDtoConverter converter;
    private UserInformationRepository repository;

    private UserService userService;

    @BeforeEach
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




}