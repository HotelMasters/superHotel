package pv168.hotelmasters.superhotel;

import java.time.LocalDate;
import java.time.Month;

/**
 * @author Gabriela Godiskova
 */
public class GuestBuilder {

    private Long id;
    private String name;
    private String address;
    private LocalDate birthday;
    private Long crCardNm;

    public GuestBuilder id (Long id) {
        this.id = id;
        return this;
    }

    public GuestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public GuestBuilder address(String address) {
        this.address = address;
        return this;
    }

    public GuestBuilder birthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }

    public GuestBuilder birthday(int year, Month month, int day) {
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

    public GuestBuilder crCardNm(Long crCardNm) {
        this.crCardNm = crCardNm;
        return this;
    }
}
