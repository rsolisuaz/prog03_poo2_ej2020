package mx.edu.uaz.poo2.clientecontrolpeliculas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISO_ACCESO_A_RED=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checaPermisos();
    }

    public void ponLista(View origen) {
        int idbtnorigen= origen.getId();
        Intent intent;
        if (idbtnorigen==R.id.btn_usuarios) {
            intent=new Intent(this, ListaUsuariosActivity.class);
        }
        else {
            intent=new Intent(this, ListaActoresActivity.class);
        }
        startActivity(intent);
    }

    /**
     * Este metodo solicitara al sistema operativo que avise al usuario de la app
     * que se requieren ciertos permisos para poder proceder
     */
    private void checaPermisos() {
        Log.d("INFO","ENTRO A CHECAR PERMISOS");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ){
            // NO SE HA OTORGADO EL PERMISO REQUERIDO
            Log.d("INFO","No se ha otorgado permiso");
            // Deberiamos mostrar una explicacion?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                // Mostrar una explicacion al usuario *asincronamente* -- no se debe
                // bloquear a este hilo en espera de la respuesta del usuario!
                // Despues de que el usuario vea esta explicacion,
                // intenta nuevamente solicitar el permiso.
                Log.d("INFO","DEBERIA EXPLICAR");
            } else {
                // No se requiere explicación, solicita el permiso
                Log.d("INFO","PEDIMOS PERMISO");
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.INTERNET},
                        PERMISO_ACCESO_A_RED
                );
                // PERMISO_ACCESO_A_RED es una constante entera definida por la app.
                // El metodo de callback obtiene el resultado de esta solicitud.
            }
        }
        else {
            // YA SE OTORGO EL PERMISO
            Log.d("INFO","PERMISO YA OTORGADO");
        }
    }

    // Metodo callback que se llama una vez que el usuario emita la respuesta
    // a la peticion de permisos

    /**
     * Metodo callback que se llama una vez que el usuario de la app
     * emita la respuesta a la peticion de permisos
     * @param requestCode  Codigo asociado a la peticion de los permisos
     * @param permissions  Arreglo con los permisos solicitados
     * @param grantResults  // Arreglo con los permisos otorgados
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode==PERMISO_ACCESO_A_RED) {
            if (grantResults.length>0 &&
                    grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                //Se otorgaron permisos
                Log.d("INFO","SE OTORGO PERMISO");
            }
            else {
                // No se otorgaron permisos
                Log.d("INFO","NO SE OTORGO PERMISO");
            }
        }
    }
}