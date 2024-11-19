package com.folksdev.secondhand.user.repository;

import com.folksdev.secondhand.user.model.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInformationRepository extends JpaRepository<UserInformation,Long> {

    Optional<UserInformation> findByMail(String mail);
}
