package ch.sze.ecommerce.entity;

public enum ProfilePicture {
    AVATAR_1("avatar1.png"),
    AVATAR_2("avatar2.png"),
    AVATAR_3("avatar3.png"),
    AVATAR_4("avatar4.png"),
    AVATAR_5("avatar5.png"),
    AVATAR_6("avatar6.png");

    private final String fileName;

    ProfilePicture(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
