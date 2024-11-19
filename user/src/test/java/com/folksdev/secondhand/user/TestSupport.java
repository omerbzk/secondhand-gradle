package com.folksdev.secondhand.user;

import com.folksdev.secondhand.user.dto.UserDto;
import com.folksdev.secondhand.user.model.UserInformation;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestSupport {

    public static List<UserInformation> generateUsers() {
        return IntStream.range(0, 5).mapToObj(i ->  new UserInformation((long) i,
                    i + "@folksdev.net",
                    "firstName" + i,
                    "lastName" + i,
                    "",
                    new Random(2).nextBoolean())
        ).collect(Collectors.toList());
    }

    public static List<UserDto> generateUserDtoList(List<UserInformation> userList) {
        return userList.stream()
                .map(from -> new UserDto(from.getMail(),
                        from.getFirstName(),
                        from.getLastName(),
                        from.getMiddleName()))
                        .collect(Collectors.toList());
    }
}
