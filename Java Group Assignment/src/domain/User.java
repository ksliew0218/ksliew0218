package domain;

public class User {
        public String UserId;
        public String UserGender;
        public String UserPass;
        public String UserAge;
        public String UserRole;
        public String Status;

        public User(){}
        public User(String A,String B)
        {
            UserId = A;
            UserPass = B;
        }

        public User (String A, String B, String C, String D, String E, String F) {
            UserId = A;
            UserGender = B;
            UserPass = C;
            UserAge = D;
            UserRole = E;
            Status = F;
        }
}
