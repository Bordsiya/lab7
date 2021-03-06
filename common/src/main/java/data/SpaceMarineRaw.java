package data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SpaceMarineRaw implements Serializable {
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Float health;
    private String achievements;
    private Weapon weaponType;
    private MeleeWeapon meleeWeapon;
    private Chapter chapter;

    public SpaceMarineRaw(String name, Coordinates coordinates, LocalDateTime creationDate,
                          Float health, String achievements, Weapon weaponType, MeleeWeapon meleeWeapon, Chapter chapter){
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.achievements = achievements;
        this.weaponType = weaponType;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Float getHealth() {
        return health;
    }

    public String getAchievements() {
        return achievements;
    }

    public Weapon getWeaponType() {
        return weaponType;
    }

    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    public Chapter getChapter() {
        return chapter;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        SpaceMarineRaw spaceMarineRaw = (SpaceMarineRaw) o;
        if (this.hashCode() != spaceMarineRaw.hashCode()) return false;
        return (this.getName().equals(spaceMarineRaw.getName()) && this.getCoordinates().equals(spaceMarineRaw.getCoordinates())
                && this.getCreationDate().equals(spaceMarineRaw.getCreationDate()) && this.getHealth().equals(spaceMarineRaw.getHealth())
                && this.getAchievements().equals(spaceMarineRaw.getAchievements()) && this.getWeaponType().equals(spaceMarineRaw.getWeaponType())
                && this.getMeleeWeapon().equals(spaceMarineRaw.getMeleeWeapon()) && this.getChapter().equals(spaceMarineRaw.getChapter()));
    }

    @Override
    public int hashCode(){
        int ans = 0;
        for (int i = 0; i < this.getName().length(); i++){
            ans = (ans + (int)this.getName().charAt(i)) % 2000000000;
        }
        ans = (ans + this.getAchievements().length()) % 2000000000;
        return ans;
    }

    @Override
    public String toString(){
        String res = "???????????????? ??????????????: " + this.getName() + "\n";
        res += "????????????????????: \n" + this.getCoordinates().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        res += "???????? ???????????????? " + this.getCreationDate().format(formatter) + "\n";
        res += "????????????????: " + this.getHealth() + "\n";
        res += "????????????????????: " + this.getAchievements() + "\n";
        if(this.getWeaponType() == null){
            res += "?????? ????????????: null\n";
        }
        else res += "?????? ????????????: " + this.getWeaponType().toString() + "\n";
        if(this.getMeleeWeapon() == null){
            res += "?????? ???????????? ???????????????? ??????: null\n";
        }
        else res += "?????? ???????????? ???????????????? ??????: " + this.getMeleeWeapon().toString() + "\n";
        res += "??????????:\n" + this.getChapter().toString() + "\n";
        return res;
    }
}
