package dear.dearles.customclasses;

/**
 * Created by Pat on 10/08/2015.
 */
public class User {

    String Username, Age, Description, Thumbnail;

    public User () {

    }

    public User (String Username, String Description) {
        this.Username = Username;
        this.Description = Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public void setThumbnail(String thumbnail) {
        this.Thumbnail = thumbnail;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public void setAge(String Age) {
        this.Age = Age;
    }

    public String getDescription() {
        return Description;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public String getUsername() {
        return Username;
    }

    public String getAge() {
        return Age;
    }
}
