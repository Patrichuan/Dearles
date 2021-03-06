package dear.dearles.customclasses;

import com.parse.ParseGeoPoint;

public class User implements Comparable<User> {

    // Todo - Comprobar al registrarse que el email no esta en uso
    String Username, Email, Age, Description, ProfilePicture, Password;
    ParseGeoPoint Geopoint;
    Double Distance;

    // CONSTRUCTOR
    public User () {
        Username = null;
        Email = null;
        Age = null;
        Description = null;
        ProfilePicture = null;
        Password = null;
        Geopoint = null;
        Distance = Double.valueOf(0);
    }

    // Profile es una URI
    public User (String Username, String Email, String Age, String Description, String ProfilePicture, String Password, ParseGeoPoint Geopoint, Double Distance) {
        this.Username = Username;
        this.Email = Email;
        this.Age = Age;
        this.Description = Description;
        this.ProfilePicture = ProfilePicture;
        this.Password = Password;
        setGeopoint(Geopoint);
        this.Distance = Distance;
    }

    // SETTERS
    public void setUsername(String username) {
        this.Username = username;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setAge(String Age) {
        this.Age = Age;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public void setProfilePicture(String ProfilePicture) {
        this.ProfilePicture = ProfilePicture;
    }

    public void setGeopoint(ParseGeoPoint geopoint) {
        this.Geopoint = geopoint;
    }

    public void setDistance (Double distance) {
        this.Distance = distance;
    }


    // GETTERS
    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getEmail() {
        return Email;
    }

    public String getAge() {
        return Age;
    }

    public String getDescription() {
        return Description;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public ParseGeoPoint getGeopoint() {
        return Geopoint;
    }

    public Double getDistance() {
        return Distance;
    }

    @Override
    public int compareTo(User other) {
        return Double.compare(this.Distance, other.Distance);
    }
}