package co.edu.unal.krunko.sitespins.businessLogic;

public class LoginController {

    //User usuario;
    //getUser(){return repositorio.user;}
    //Instancia de lo que maneja firebase

    public String isAuthenticated(String personal_id, String password){
        if(!personal_id.isEmpty()){
            if(!password.isEmpty()){
                int id = Integer.parseInt(personal_id);
                int pass = Integer.parseInt(password);

                //usuario = Obtener el usuario con id haciendo uso de la instacia de firebase

                if (true/*existe el ususario*/){
                    if(true/*si coicide la contraseña*/){

                        return "Ingreso Exitoso";
                    }
                    return "Contraseña incorrecta";
                }
                return "El e-mail no está registrado";
            }else{
                return "Ingrese contraseña";
            }
        }else{
            return "Ingrese e-mail";
        }
    }

}
