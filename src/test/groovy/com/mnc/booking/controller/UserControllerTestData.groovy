package com.mnc.booking.controller

import com.mnc.booking.controller.dto.auth.AuthRequestDTO
import com.mnc.booking.controller.dto.user.UserCreationDTO
import com.mnc.booking.controller.dto.user.UserRolesUpdateDTO

trait UserControllerTestData {

    static UserCreationDTO prepareUserDTO(final String email, final String username) {
        [
                "email"      : email,
                "username"   : username,
                "password"   : "p@55w0rd",
                "name"       : "Test",
                "surname"    : "User",
                "dateOfBirth": "1993-01-10",
                "phoneNumber": "512983749",
                "photoURI"   : "https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/User_icon-cp.svg/1200px-User_icon-cp.svg.png"
        ]
    }

    static def prepareUserRolesUpdateDTO(final String roles) {
        [
                "roles": roles
        ]
    }

    static AuthRequestDTO prepareAuthRequest(final String username, final String password) {
        [
                "username": username,
                "password": password
        ]
    }

}