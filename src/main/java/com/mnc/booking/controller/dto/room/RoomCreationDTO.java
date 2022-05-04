package com.mnc.booking.controller.dto.room;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@NoArgsConstructor
@Data
public class RoomCreationDTO {

  @NotBlank(message = "roomNo must be specified.")
  private String roomNo;
  @Min(value = 1, message = "noPeople must be specified and greater than or equal to 1.")
  private int noPeople;
  @NotBlank(message = "description must be specified.")
  private String description;
  @NotNull(message = "roomType must be specified.")
  @Pattern(regexp = "PREMIUM|STANDARD", message = "roomType must be specified. Available room types are PREMIUM and STANDARD.")
  private String roomType;
  //  private RoomType roomType;
  @NotNull(message = "pricePerNight must be specified.")
  @Valid
  private PriceDTO pricePerNight;
  @NotNull(message = "isBalcony must be specified.")
  private Boolean isBalcony;
  @NotNull(message = "isOutstandingView must be specified.")
  private Boolean isOutstandingView;
  @NotNull(message = "isTv must be specified.")
  private Boolean isTv;
  @NotNull(message = "bathroomType must be specified.")
  @Pattern(regexp = "BATH|SHOWER", message = "bathroomType must be specified. Available bathroom types are BATH and SHOWER.")
  private String bathroomType;
  //  private BathroomType bathroomType;
  @NotNull(message = "isCoffeeMachine must be specified.")
  private Boolean isCoffeeMachine;
  @NotNull(message = "isRestArea must be specified.")
  private Boolean isRestArea;
  @NotNull(message = "isRestArea must be specified.")
  @Valid
  private MeasurementUnitDTO roomSize;
  @NotNull(message = "images list must be specified.")
  @Valid
  private List<URIDTO> images;
  @NotNull(message = "status must be specified.")
  @Pattern(regexp = "ACTIVE|INACTIVE", message = "status must be specified. Available statuses are ACTIVE and INACTIVE.")
  private String status;
//  private Status status;

}
