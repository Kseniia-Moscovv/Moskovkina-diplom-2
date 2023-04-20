package praktikum.stellar.model.user;

public class UserDelete {
    private String authorization;

    public UserDelete(String authorization) {
        this.authorization = authorization;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }
}
