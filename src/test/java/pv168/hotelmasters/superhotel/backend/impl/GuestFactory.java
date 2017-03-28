package pv168.hotelmasters.superhotel.backend.impl;

import java.time.LocalDate;
import java.time.Month;
import pv168.hotelmasters.superhotel.backend.entities.Guest;

/**
 * @author Gabriela Godiskova
 */
public class GuestFactory {

    private Long id;
    private String name;
    private String address;
    private LocalDate birthday;
    private Long crCardNm;

    public GuestFactory id (Long id) {
        this.id = id;
        return this;
    }

    public GuestFactory name(String name) {
        this.name = name;
        return this;
    }

    public GuestFactory address(String address) {
        this.address = address;
        return this;
    }

    public GuestFactory birthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }

    public GuestFactory birthday(int year, Month month, int day) {
        this.birthday = LocalDate.of(year,month,day);
        return this;
    }

    public Guest build() {
        Guest guest = new Guest();
        guest.setId(id);
        guest.setName(name);
        guest.setAdress(address);
        guest.setBirthDay(birthday);
        guest.setCrCardNumber(crCardNm);
        return guest;
    }

    public GuestFactory crCardNm(Long crCardNm) {
        this.crCardNm = crCardNm;
        return this;
    }
}
