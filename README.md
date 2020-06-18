# prog03_poo2_ej2020
Base de Cliente Android para Programa 03 Ene Jun 2020 POO2

Este código tiene la funcionalidad de mostrar la información de Usuarios y se debe completar para mostrar la información de Actores, realizando
una funcionalidad similar, ademas de completar las validaciones faltantes al momento de agregar un nuevo usuario o modificar un 
usuario existente. Para los actores también deben hacerse las validaciones correspondientes.

NOTAS IMPORTANTES:
Para poder ejecutar el programa exitosamente debe estar corriendo su servicio REST (y por tanto Glassfish), así que ponga a correr primero
esos elementos, y después una vez corriendo, determine la dirección IP que tiene su computadora y use tal dirección en lugar de localhost en
el URL del servicio, modifique el string url_base en strings.xml para que contenga la dirección correcta.
Así, si por ejemplo su computadores tiene la dirección IP 192.10.2.201 por ejemplo, entonces el URL base tiene el siguiente formato:
http://192.10.2.201/ServidorControlPeliculasXXXXXXX/webresources/

Recuerde que las XXXXXXX se sustituyen por su matricula, la diagonal final en el URL base debe incluirse en el string.

Las fechas de nacimiento y muerte pueden ser campos de texto, aunque en tal caso debe validarse que se teclee en el formato YYYY-MM-DD, para
que al agregarse en la base de datos en el servicio REST no causen problema. Puede si desea, investigar componentes de Android que dan una 
funcionalidad similar al JCalendar que se uso en la GUI del programa 1 y manejar las fechas con tal componente, lo cual le daria 10 puntos
extra. 
El boton de ver Imagen es opcional, y si lo implementan correctamente, tendrán 10 puntos extra adicionales.

