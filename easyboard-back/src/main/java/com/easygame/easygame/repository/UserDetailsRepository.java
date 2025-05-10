package com.easygame.easygame.repository;


import com.easygame.easygame.model.UsersDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UsersDetails, Long> {
}
