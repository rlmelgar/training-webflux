package com.kairosds.webflux.infrastructure.rest.client.worldtime.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Builder
public class WorldTimeDto {
  private String utcOffset;

  private String timezone;

  private int dayOfWeek;

  private int dayOfYear;

  private String datetime;

  private String utcDatetime;

  private long unixtime;

  private int rawOffset;

  private int weekNumber;

  private boolean dst;

  private String abbreviation;

  private int dstOffset;

  private String dstFrom;

  private String dstUntil;

  private String clientIp;
}
