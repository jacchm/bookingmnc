package com.mnc.booking.controller

import com.mnc.booking.controller.dto.room.RoomDTO
import com.mnc.booking.controller.dto.room.URIDTO

trait RoomControllerTestData {

    static RoomDTO prepareRoomDTO(final String roomNo) {
        [
                "roomNo": roomNo,
                "noPeople": 4,
                "description": "First test room",
                "roomType": "STANDARD",
                "pricePerNight": [
                    "value": 100,
                    "currency": "PLN"
                ],
                "isBalcony": true,
                "isOutstandingView": true,
                "isTv": true,
                "bathroomType": "SHOWER",
                "isCoffeeMachine": true,
                "isRestArea": true,
                "roomSize": [
                    "value": 30,
                    "unit": "m2"
                ],
                "images": [new URIDTO(null, "https://media.timeout.com/images/105859033/image.jpg")],
                "status": "ACTIVE"
        ]
    }

    static URIDTO prepareURIDTO(final String roomNo) {
        [
                "roomNo" : roomNo,
                "uri": "https://www.sn2.eu/media/k2/items/cache/07a555952c8718faae0fa1f39fe31eb3_XL.jpg"
        ]
    }

}