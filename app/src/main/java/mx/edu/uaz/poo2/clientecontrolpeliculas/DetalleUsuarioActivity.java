package mx.edu.uaz.poo2.clientecontrolpeliculas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.TreeMap;

import mx.edu.uaz.poo2.clientecontrolpeliculas.entidades.Usuario;
import mx.edu.uaz.poo2.clientecontrolpeliculas.utils.HttpUtils;
import mx.edu.uaz.poo2.clientecontrolpeliculas.utils.ServidorException;

public class DetalleUsuarioActivity extends AppCompatActivity {
    private ConstraintLayout contenedor;
    private EditText editRFC;
    private EditText editNombre;
    private EditText editApPaterno;
    private EditText editApMaterno;
    private EditText editTelefono;
    private EditText editCalle;
    private EditText editEMail;
    private EditText editColonia;
    private EditText editCodpostal;
    private EditText editEntidad;
    private EditText editMunicipio;
    private Button botonEliminar;
    private Button botonGuardar;
    private Usuario datosUsuario;  // Atributo para guardar la informacion del usuario mostrado
    private boolean esNuevo; // Atributo para determinar si el usuario es nuevo o no
    private AlertDialog dialogoConfirmacionEliminar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_usuario);
        contenedor=findViewById(R.id.container_detalle_usuario);
        editRFC = findViewById(R.id.edit_rfc_usuario);
        editNombre = findViewById(R.id.edit_nombre_usuario);
        editApPaterno = findViewById(R.id.edit_appaterno_usuario);
        editApMaterno = findViewById(R.id.edit_apmaterno_usuario);
        editTelefono = findViewById(R.id.edit_tel_usuario);
        editCalle = findViewById(R.id.edit_calle_usuario);
        editEMail = findViewById(R.id.edit_email_usuario);
        editColonia = findViewById(R.id.edit_colonia_usuario);
        editCodpostal = findViewById(R.id.edit_codpostal_usuario);
        editEntidad = findViewById(R.id.edit_entidad_usuario);
        editMunicipio= findViewById(R.id.edit_mun_usuario);
        botonEliminar = findViewById(R.id.btn_eliminar_usuario);
        botonGuardar = findViewById(R.id.btn_agregar_usuario);

        obtenDatos();
        creaDialogoConfirmacion();
    }

    /**
     * Metodo para crear un cuadro de dialogo de confirmacion de eliminacion
     */
    private void creaDialogoConfirmacion() {
        // Se crea un constructor de cuadros de dialogo
        AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        // Se establece el titulo del cuadro de dialogo
        constructor.setTitle(R.string.titulo_confirmacion_eliminar);
        // Se estable el mensaje del cuadro de dialogo
        constructor.setMessage(R.string.mensaje_confirmacion_eliminar);
        // Se establece el texto del boton de aceptacion asi como el manejador
        constructor.setPositiveButton(R.string.titulo_opcion_si, new ManejadorConfirmacion());
        // Se establece el texto del boton de cancelacio  asi como el manejador
        constructor.setNegativeButton(R.string.titulo_opcion_no, null);
        // Se crea el cuadro de dialogo
        dialogoConfirmacionEliminar = constructor.create();
    }

    /**
     * Metodo que muestra un mensaje temporal en la pantalla cuyo texto esta en
     * algun archivo XML de res/values
     *
     * @param idMensaje   ID del string a mostrar
     */
    private void muestraMensaje(int idMensaje) {
        final Snackbar snack=Snackbar.make(contenedor,idMensaje,Snackbar.LENGTH_INDEFINITE).
                setAction(R.string.texto_cerrar, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // No necesitamos hacer nada, solo que se cierre
                    }
                });
        snack.show();
    }

    /**
     * Este metodo obtiene los datos recibidos de la Activity ListaUsuarioActivity
     */
    private void obtenDatos() {
        Intent intent = getIntent();
        datosUsuario = (Usuario) intent.getSerializableExtra("info");
        // Si no enviaron datos, significa que es porque se esta pidiendo crear uno nuevo
        // por lo que hay que hacer el boton de eliminar invisible
        if (datosUsuario == null) {
            botonEliminar.setVisibility(View.GONE);
            botonGuardar.setText(R.string.etiqueta_boton_guardar_nuevo);
            esNuevo = true;
        } else { // de otra manera se extraen los datos de los campos
            editRFC.setText(datosUsuario.getRfc());
            editNombre.setText(datosUsuario.getNombre());
            editApPaterno.setText(datosUsuario.getApPaterno());
            editEMail.setText(datosUsuario.getEmail());
            editTelefono.setText(datosUsuario.getTelefono());
            if (datosUsuario.getApMaterno()!=null)
                editApMaterno.setText(datosUsuario.getApMaterno());
            if (datosUsuario.getCalle()!=null)
                editCalle.setText(datosUsuario.getCalle());
            if (datosUsuario.getColonia()!=null)
                editColonia.setText(datosUsuario.getColonia());
            if (datosUsuario.getCodpostal()!=null && datosUsuario.getCodpostal()!=0)
                editCodpostal.setText(datosUsuario.getCodpostal().toString());
            if (datosUsuario.getIdEntidad()!=null && datosUsuario.getIdEntidad()!=0)
                editEntidad.setText(datosUsuario.getIdEntidad().toString());
            if (datosUsuario.getIdMunicipio()!=null && datosUsuario.getIdMunicipio()!=0)
                editMunicipio.setText(datosUsuario.getIdMunicipio().toString());
            editRFC.setEnabled(false);
            botonGuardar.setText(R.string.etiqueta_boton_guardar);
            esNuevo = false;
        }
    }

    /**
     * Manejador de la opcion de SI en el cuadro de confirmacion de eliminacion
     *
     */
    private class ManejadorConfirmacion implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Poner a correr un hilo para eliminar el usuario
            ActualizadorInfoUsuario hilo = new ActualizadorInfoUsuario(
                    ListaUsuariosActivity.USUARIO_ELIMINADO);
            hilo.execute(getString(R.string.url_base) + "usuario/" + datosUsuario.getRfc());
        }
    }

    /**
     * Clase que se encarga de hacer un hilo para hacer modificaciones al usuario,
     * ya sea crear uno nuevo, actualizar o eliminar
     * @author Roberto Solis Robles
     *
     */
    private class ActualizadorInfoUsuario
            extends AsyncTask<String, Void, String> {
        private int tipo,  // Atributo que guarda el tipo de actualizacion (eliminar, crear, actualizar)
                status;  // Atributo que guarda el status de la actualizacion solicitada

        /*
         *  Al crear un objeto de este tipo indicamos para que lo queremos:
         *   ListaUsuariosActivity.USUARIO_AGREGADO para crear uno nuevo
         *   ListaUsuariosActivity.USUARIO_ELIMINADO para eliminar uno existente
         *   ListaUsuariosActivity.USUARIO_MODIFICADO para actualizar uno existente
         */

        public ActualizadorInfoUsuario(int tipo) {
            this.tipo = tipo;
        }

        /**
         * Metodo que se encarga de contactar al servicio web correspondiente
         * ayudandose de la clase HttpUtils
         * @param  params Arreglo de elementos del tipo especificado como primer
         *        dato generico en AsyncTask, en este caso String donde la primer
         *        posicion contiene el URL del servicio Web
         */
        @Override
        protected String doInBackground(String... params) {
            String metodo=null;
            String datos=null;
            String tipoContenido=null;
            String respuesta = "false";
            // Determinamos el metodo HTTP a usar
            if(tipo == ListaUsuariosActivity.USUARIO_AGREGADO) {
                metodo="POST";
            }
            else if (tipo == ListaUsuariosActivity.USUARIO_ELIMINADO) {
                metodo="DELETE";
            }
            else if (tipo == ListaUsuariosActivity.USUARIO_MODIFICADO) {
                metodo="PUT";
            }
            // Si queremos actualizar o crear es necesario enviar los
            // datos del usuario a crear o actualizar (en formato JSON)
            if (tipo == ListaUsuariosActivity.USUARIO_MODIFICADO
                    ||tipo == ListaUsuariosActivity.USUARIO_AGREGADO) {
                Gson gson= new Gson();
                datos = gson.toJson(datosUsuario);
                tipoContenido="application/json";
            }
            try {  // nos ayudamos del metodo httpPostPutDelete para solicitar el servicio web
                respuesta= HttpUtils.httpPostPutDelete(
                        params[0], datos, tipoContenido, metodo);
            }
            catch (MalformedURLException mue) {
                status = ListaUsuariosActivity.STATUS_URL_INVALIDO;
                mue.printStackTrace();
            }
            catch (ServidorException exs) {
                exs.printStackTrace();
                status=ListaUsuariosActivity.STATUS_URL_INCORRECTO;
            }
            catch (IOException ioe) {
                status = ListaUsuariosActivity.STATUS_ERROR_IO;
                ioe.printStackTrace();
            }
            return respuesta;  // Regresamos si se pudo o no hacer la actualizacion
        }

        /**
         * Método que se ejecuta al terminar doInBackground Este método si puede
         * modificar la GUI
         *
         * @param result
         *            El dato regresado por doInBackground
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!result.equals("false")) {
                muestraMensaje(R.string.mensaje_exito_operacion);
                regresaDatos(tipo);
            }
            else {
                muestraMensaje(R.string.mensaje_error_operacion);
            }
        }
    }

    /**
     * Método que se ejecutar al dar click en el boton Eliminar Usuario
     * @param origen Origen del Click
     */
    public void eliminaUsuario(View origen) {
        dialogoConfirmacionEliminar.show();
    }

    /**
     * Metodo que crea un Intent para enviar datos de regreso a la activity que llamo a ésta
     * Los datos estaran en un Bundle dentro del intent indicando que operacion se hizo
     * y los datos del usuario (a usarse si la operacion fue crear o actualizar)
     * @param codigo
     */
    private void regresaDatos(int codigo) {
        Intent datos = new Intent();
        Bundle info = new Bundle();
        info.putInt("operacion", codigo);
        info.putSerializable("nuevo", datosUsuario);
        datos.putExtras(info);
        setResult(RESULT_OK, datos);
        finish();
    }

    /**
     * Este metodo se ejecuta al dar click en el boton Guardar Cambios
     * @param origen Origen del click
     */
    public void guardaUsuario(View origen) {
        if (editRFC.getText().toString().isEmpty() ||
                editNombre.getText().toString().isEmpty()
                || editApPaterno.getText().toString().isEmpty() ||
                editTelefono.getText().toString().isEmpty() ||
                editEMail.getText().toString().isEmpty()) {
            muestraMensaje(R.string.mensaje_error_campos);
            return;
        } else {
            if (datosUsuario == null) {
                datosUsuario = new Usuario();
            }
            // NOTA: Faltan validaciones a completar:
            // 1. Telefono de 10 digitos
            // 2. Si hay codigo postal (campo opcional) debe ser de 5 digitos
            // 3. RFC debe tener un formato adecuado (tal como en Programa 1)
            // 4. Campos que no excedan en longitud a lo indicado en la tabla de la base de datos
            datosUsuario.setRfc(editRFC.getText().toString());
            datosUsuario.setNombre(editNombre.getText().toString());
            datosUsuario.setApPaterno(editApPaterno.getText().toString());
            datosUsuario.setTelefono(editTelefono.getText().toString());
            datosUsuario.setEmail(editEMail.getText().toString());

            datosUsuario.setApMaterno(editApMaterno.getText().toString());
            datosUsuario.setCalle(editCalle.getText().toString());
            datosUsuario.setColonia(editColonia.getText().toString());
            if (editCodpostal.getText().toString().length()>0) {
                datosUsuario.setCodpostal(Integer.parseInt(editCodpostal.getText().toString()));
            }
            if (editEntidad.getText().toString().length()>0) {
                datosUsuario.setIdEntidad(Short.parseShort(editEntidad.getText().toString()));
            }
            if (editMunicipio.getText().toString().length()>0) {
                datosUsuario.setIdMunicipio(Integer.parseInt(editMunicipio.getText().toString()));
            }
            if (esNuevo) {
                ActualizadorInfoUsuario hilo = new ActualizadorInfoUsuario(ListaUsuariosActivity.USUARIO_AGREGADO);
                hilo.execute(getString(R.string.url_base) + "usuario");
            } else {
                ActualizadorInfoUsuario hilo = new ActualizadorInfoUsuario(ListaUsuariosActivity.USUARIO_MODIFICADO);
                hilo.execute(getString(R.string.url_base) + "usuario/" + datosUsuario.getRfc());
            }
        }
    }

    /**
     * Metodo que se ejecuta al dar click en el boton Regresar a Lista
     * @param origen View que representa el origen del click
     */
    public void regresarALista(View origen) {
        finish();
    }

}