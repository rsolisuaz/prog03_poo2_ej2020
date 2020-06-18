package mx.edu.uaz.poo2.clientecontrolpeliculas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import mx.edu.uaz.poo2.clientecontrolpeliculas.entidades.Usuario;
import mx.edu.uaz.poo2.clientecontrolpeliculas.utils.HttpUtils;
import mx.edu.uaz.poo2.clientecontrolpeliculas.utils.ServidorException;

public class ListaUsuariosActivity extends AppCompatActivity {
    private ConstraintLayout contenedor;
    private ListView listaUsuarios;
    private ArrayAdapter<Usuario> adapterUsuarios;
    private List<Usuario> datos;
    public static final int SOLICITUD_NUEVO = 100;
    public static final int SOLICITUD_EXISTENTE = 101;
    public static final int USUARIO_MODIFICADO = 102;
    public static final int USUARIO_AGREGADO = 103;
    public static final int USUARIO_ELIMINADO = 104;
    public static final int STATUS_OK = 0;
    public static final int STATUS_URL_INVALIDO = 1;
    public static final int STATUS_ERROR_IO = 2;
    public static final int STATUS_URL_INCORRECTO = 3;
    public static final int STATUS_JSON_INVALIDO = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);
        contenedor=findViewById(R.id.container_lista_usuarios);
        listaUsuarios = findViewById(R.id.lista_usuarios);
        Button botonInferior = new Button(this);
        botonInferior.setText(R.string.etiqueta_boton_agregar_usuario);
        botonInferior.setOnClickListener(new ManejadorClickNuevo());
        listaUsuarios.addFooterView(botonInferior);
        listaUsuarios.setOnItemClickListener(new ManejadorListaUsuarios());
        if (savedInstanceState == null) {
            cargarLista();
        }
    }

    /**
     * Metodo que carga los datos de la lista a traves de un hilo
     */
    private void cargarLista() {
        String urlbase = getString(R.string.url_base);
        CargadorInfoUsuarios cargador = new CargadorInfoUsuarios();
        cargador.execute(urlbase + "usuario");
    }


    /**
            * Metodo que se ejecuta al terminar la activity llamada con
     * startActivityForResult
     *
     * @param requestCode    Codigo enviado al usar startActivityForResult
     * @param resultCode
     *            Codigo que especifica el resultado (RESULT_OK o
     *            RESULT_CANCELLED)
     * @param data
     *            Intent que contiene los datos enviados por la Activity que
     *            termino
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SOLICITUD_NUEVO) {
            if (resultCode == RESULT_OK) {
                Bundle datos = data.getExtras();
                Usuario u = (Usuario) datos.getSerializable("nuevo");
                adapterUsuarios.add(u);
            }
        } else {
            if (resultCode == RESULT_OK) {
                Bundle datos = data.getExtras();
                Usuario u = (Usuario)
                        datos.getSerializable("nuevo");
                int operacion = datos.getInt("operacion");
                adapterUsuarios.remove(u);
                if (operacion == USUARIO_MODIFICADO) {
                    adapterUsuarios.add(u);
                }
            }
        }
    }

    /**
     * Manejador de click para el boton que aparece como Footer en la ListView
     *
     * @author Roberto Solis Robles
     *
     */
    private class ManejadorClickNuevo implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent nuevo =
                    new Intent(
                            ListaUsuariosActivity.this,
                            DetalleUsuarioActivity.class);
            startActivityForResult(nuevo, SOLICITUD_NUEVO);
        }
    }

    /**
     * Manejador para el evento de dar click a algun elemento en la ListView,
     * similar al OnItemSelectedListener de un Spinner, pero solo con un metodo:
     * onItemClick, que recibe argumentos similares al onItemSelected del
     * manejador de un Spinner
     *
     * @author Roberto Solis Robles
     *
     */
    private class ManejadorListaUsuarios implements
            AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> lista,
                                View arg1,
                                int indiceSeleccionado, long arg3) {
            Usuario seleccionado = (Usuario) lista.getItemAtPosition(indiceSeleccionado);
            Intent existente = new Intent(ListaUsuariosActivity.this, DetalleUsuarioActivity.class);
            existente.putExtra("info", seleccionado);
            startActivityForResult(existente, SOLICITUD_EXISTENTE);
        }
    }


    /**
     * Metodo que se ejecuta al indicar a la activty que debe ser destruida
     *
     * @param outState Bundle donde deben colocarse todos los datos a mantener
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("datos",
                (ArrayList<Usuario>) datos);

    }

    /**
     * Metodo que se ejecuta al reiniciar la activity que tuvo que ser destruida
     *
     * @param savedInstanceState contiene los datos almacenados antes de destruir la activity
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        datos = (ArrayList<Usuario>) savedInstanceState.getSerializable("datos");
        if (datos != null) {
            adapterUsuarios = new ArrayAdapter<Usuario>(this, android.R.layout.simple_list_item_1, datos);
            listaUsuarios.setAdapter(adapterUsuarios);
        } else {
            cargarLista();
        }
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
     * Clase para representar el trabajo de un hilo en Android
     *
     * @author Roberto Solis Robles
     *
     */
    private class CargadorInfoUsuarios
            extends AsyncTask<String, Void, List<Usuario>> {
        private int status; // Atributo para determinar el status de lo
        // realizado

        /**
         * Método que realiza el trabajo en segundo plano. Este método debe
         * usarse para hacer trabajo que tomará mucho tiempo (calculos grandes,
         * bajar datos de la red, etc.) No puede modificar la GUI.
         *
         * @param params
         *            Arreglo de elementos del tipo especificado como primer
         *            dato generico en AsyncTask, en este caso String donde la primer
         *        posicion contiene el URL del servicio Web
         * @return Un objeto del tercer tipo indicado en el AsyncTask
         */
        @Override
        protected List<Usuario> doInBackground(String... params) {
            List<Usuario> datos = new ArrayList<Usuario>();

            // Por el momento creamos usuarios de manera manual
            status = STATUS_OK;

            try {
                String respuesta =
                        HttpUtils.httpGet(params[0],
                                "application/json");
                Type tipoListaUsuarios =
                        new TypeToken<List<Usuario>>() {}.getType();
                GsonBuilder constructor = new GsonBuilder();
                Gson gson=constructor.create();
                datos = gson.fromJson(respuesta,tipoListaUsuarios);
            }
            catch (MalformedURLException eurl) {
                eurl.printStackTrace();
                status=STATUS_URL_INVALIDO;
            }
            catch (ServidorException exs) {
                exs.printStackTrace();
                status=STATUS_URL_INCORRECTO;
            }
            catch (IOException ex) {
                ex.printStackTrace();
                status=STATUS_ERROR_IO;
            }
            catch (JsonParseException ejson) {
                ejson.printStackTrace();
                status = STATUS_JSON_INVALIDO;
            }
            return datos;
        }

        /**
         * Método que se ejecuta al terminar doInBackground Este método si puede
         * modificar la GUI
         *
         * @param result
         *            El dato regresado por doInBackground
         */
        @Override
        protected void onPostExecute(List<Usuario> result) {
            super.onPostExecute(result);
            if (status == STATUS_OK) {
                adapterUsuarios = new ArrayAdapter<Usuario>(ListaUsuariosActivity.this,
                        android.R.layout.simple_list_item_1, result);
                listaUsuarios.setAdapter(adapterUsuarios);
            } else {
                if (status == STATUS_URL_INVALIDO) {
                    muestraMensaje(R.string.mensaje_error_url_invalido);
                } else if (status == STATUS_URL_INCORRECTO) {
                    muestraMensaje(R.string.mensaje_error_url_incorrecto);
                } else if (status == STATUS_ERROR_IO) {
                    muestraMensaje(R.string.mensaje_error_io);
                } else {
                    muestraMensaje(R.string.mensaje_error_json);
                }
            }
        }

    }

}