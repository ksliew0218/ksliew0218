public class Register extends User{
    public Register(String A, String B, String C, String D,String E) {
        super(A, B,C,D,E);
    }

    public void Register_Detail()
    {
        try
        {
            File MyData = new File("UserData.txt");
            FileWriter fw = new FileWriter(MyData,true);
            BufferedWriter bw = new BufferedWriter(fw);
            try (PrintWriter pw = new PrintWriter(bw)) {
                String UserData = "\n" + super.UserId + "/" + super.UserGender + "/" + super.UserPass + "/" + super.UserAge + "/" + super.UserRole + "/";
                pw.write(UserData);
                pw.close();
            }
        }
        catch(IOException Ex)
        {
            System.out.println("File Error");
        }
    }
}
