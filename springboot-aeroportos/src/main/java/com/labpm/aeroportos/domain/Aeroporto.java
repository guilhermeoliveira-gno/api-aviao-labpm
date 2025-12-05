package com.labpm.aeroportos.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "airports")
public class Aeroporto {
  @Id
  private Long id;

  @NotBlank
  private String name;
  @NotBlank
  private String city;
  @NotBlank
  private String country;

  @Column(length = 3, unique = true)
  private String iata;

  @Column(length = 4)
  private String icao;

  private String latitude;
  private String longitude;
  private Integer altitude;
  private String tz_offset;
  private String dst;
  private String tz;
  private String type;
  private String source;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getCity() { return city; }
  public void setCity(String city) { this.city = city; }
  public String getCountry() { return country; }
  public void setCountry(String country) { this.country = country; }
  public String getIata() { return iata; }
  public void setIata(String iata) { this.iata = iata; }
  public String getIcao() { return icao; }
  public void setIcao(String icao) { this.icao = icao; }
  public String getLatitude() { return latitude; }
  public void setLatitude(String latitude) { this.latitude = latitude; }
  public String getLongitude() { return longitude; }
  public void setLongitude(String longitude) { this.longitude = longitude; }
  public Integer getAltitude() { return altitude; }
  public void setAltitude(Integer altitude) { this.altitude = altitude; }
  public String getTz_offset() { return tz_offset; }
  public void setTz_offset(String tz_offset) { this.tz_offset = tz_offset; }
  public String getDst() { return dst; }
  public void setDst(String dst) { this.dst = dst; }
  public String getTz() { return tz; }
  public void setTz(String tz) { this.tz = tz; }
  public String getType() { return type; }
  public void setType(String type) { this.type = type; }
  public String getSource() { return source; }
  public void setSource(String source) { this.source = source; }
}