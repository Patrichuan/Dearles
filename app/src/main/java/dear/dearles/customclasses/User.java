package dear.dearles.customclasses;

import com.parse.ParseGeoPoint;

public class User {

    // Todo - Comprobar al registrarse que el email no esta en uso
    String Username, Email, Age, Description, ProfilePicture;
    String Password, Geopoint;

    // CONSTRUCTOR
    public User () {
        Username = null;
        Email = null;
        Age = null;
        Description = null;
        ProfilePicture = null;
        Password = null;
        Geopoint = null;
    }

    // Profile es una URI
    public User (String Username, String Email, String Age, String Description, String ProfilePicture, String Password, ParseGeoPoint Geopoint) {
        this.Username = Username;
        this.Email = Email;
        this.Age = Age;
        this.Description = Description;
        this.ProfilePicture = ProfilePicture;
        this.Password = Password;
        setGeopoint(Geopoint);
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
        this.Geopoint = "Lat: " + Double.toString(geopoint.getLatitude()) + " , Long: " + geopoint.getLongitude();;
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

    public String getGeopoint() {
        return Geopoint;
    }

}