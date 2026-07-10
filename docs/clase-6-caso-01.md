# Clase 6 - Guía para resolver el Caso 01

Esta clase se enfoca en aplicar la estructura estudiada durante el curso a un caso práctico. La solución debe respetar el orden de capas del proyecto.

## 1. Base de datos

1. Crear el esquema solicitado.
2. Crear el usuario de la base de datos.
3. Asignar los privilegios necesarios.
4. Crear las tablas con sus llaves primarias y foráneas.
5. Insertar los datos de prueba indicados en el caso.

El archivo `src/main/resources/application.properties` debe utilizar el esquema y las credenciales del caso.

## 2. Capas de la aplicación

Por cada entidad solicitada se debe revisar o crear:

- `domain`: clase entidad con las anotaciones de JPA.
- `repository`: interfaz que extiende `JpaRepository`.
- `service`: métodos de consulta, guardado y eliminación.
- `controller`: rutas que reciben las solicitudes y preparan el modelo.
- `templates`: páginas y fragmentos de Thymeleaf.

## 3. Fragmentos

Los fragmentos permiten reutilizar secciones de HTML. En este proyecto se aplican para:

- Encabezado y navegación.
- Pie de página.
- Formularios para agregar y modificar.
- Listados de registros.
- Confirmación de eliminación.
- Mensajes tipo toast.

Ejemplo de definición:

```html
<section th:fragment="listado">
    <!-- contenido reutilizable -->
</section>
```

Ejemplo de uso:

```html
<section th:replace="~{producto/fragmentos :: listado}"></section>
```

## 4. Comprobación final

Antes de entregar el caso se debe comprobar que:

- La aplicación inicia sin errores de compilación.
- La conexión a la base de datos utiliza el esquema correcto.
- Las rutas del controlador coinciden con los enlaces de las páginas.
- Los nombres enviados por los formularios coinciden con los atributos de las entidades.
- Los fragmentos se encuentran dentro de la carpeta indicada en `th:replace`.
- El listado, guardado, modificación y eliminación funcionan con datos reales.
