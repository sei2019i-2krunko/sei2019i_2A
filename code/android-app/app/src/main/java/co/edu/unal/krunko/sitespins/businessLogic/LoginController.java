package co.edu.unal.krunko.sitespins.businessLogic;

public class LoginController {

    //User usuario;
    //getUser(){return repositorio.user;}
    //Instancia de lo que maneja firebase

    public String loginWithEmailAndPassword(String email, String password){
        if(email != null && !email.isEmpty()){
            if(password != null && !password.isEmpty()){

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
