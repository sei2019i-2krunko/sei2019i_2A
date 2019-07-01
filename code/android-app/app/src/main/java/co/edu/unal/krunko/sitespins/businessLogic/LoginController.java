package co.edu.unal.krunko.sitespins.businessLogic;

public class LoginController {

    public String isAuthenticated(String personal_id, String password){
        if(!personal_id.isEmpty()){
            if(!password.isEmpty()){
                int id = Integer.parseInt(personal_id);
                int pass = Integer.parseInt(password);

                // Obtener el usuario con id
                // Handle
                if (true/*existe el ususario*/){
                    if(true/*si coicide la contraseña*/){
                        return "Connection Successful";
                    }
                    return "Incorrect Password";
                }
                return "User Doesn't Exist";
            }else{
                return "Enter Password";
            }
        }else{
            return "Enter Personal ID";
        }
    }

}
