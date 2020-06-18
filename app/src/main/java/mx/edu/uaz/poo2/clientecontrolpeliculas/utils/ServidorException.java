package mx.edu.uaz.poo2.clientecontrolpeliculas.utils;

import java.io.IOException;

public class ServidorException extends IOException {
    public ServidorException(String mensaje,
                             Throwable causa) {
        super(mensaje,causa);
    }

    public ServidorException(int codigo) {
        super(String.valueOf(codigo));
    }
}
