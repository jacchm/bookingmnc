package com.mnc.booking.repository;

import com.mnc.booking.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
