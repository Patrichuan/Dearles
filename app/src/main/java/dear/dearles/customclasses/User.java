package dear.dearles.customclasses;

/**
 * Created by Pat on 10/08/2015.
 */
public class User {

    String Username, Password, Email;
    String Description, Thumbnail;

    public User (String Username, String Description) {
        this.Username = Username;
        this.Description = Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public void setThumbnail(String thumbnail) {
        this.Thumbnail = thumbnail;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getDescription() {
        return Description;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public String getUsername() {
        return Username;
    }
}
