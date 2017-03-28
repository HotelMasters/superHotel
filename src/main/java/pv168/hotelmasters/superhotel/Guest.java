package pv168.hotelmasters.superhotel;

import java.time.LocalDate;


/**
 * @author Gabriela Godiskova
 */
public class Guest {
    private Long id;
    private String name;
    private String adress;
    private LocalDate birthDay;
    private Long crCardNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public Long getCrCardNumber() {
        return crCardNumber;
    }

    public void setCrCardNumber(Long crCardNumber) {
        this.crCardNumber = crCardNumber;
    }

    public int hashCode(){
        return this.id.hashCode();
    }

    public boolean equals(Object other) {
        return this.id != null && other instanceof Guest && this.id.equals(((Guest)other).getId());
    }
}
