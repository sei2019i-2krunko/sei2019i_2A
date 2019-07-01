package co.edu.unal.krunko.sitespins.businessLogic;

public class LoginController {

    //User usuario;
    //Instancia de lo que maneja firebase

    public String isAuthenticated(String personal_id, String password){
        if(!personal_id.isEmpty()){
            if(!password.isEmpty()){
                int id = Integer.parseInt(personal_id);
                int pass = Integer.parseInt(password);

                //usuario = Obtener el usuario con id haciendo uso de la instacia de firebase

                if (true/*existe el ususario*/){
                    if(true/*si coicide la contraseña*/){
                        return "Successful Connection";
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
