package dear.dearles.customclasses;

public class ChatMessage {

    private String name;
    private String text;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public ChatMessage() {
    }

    public ChatMessage(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}