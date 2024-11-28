
# Aplicación Web Simple con Balanceador de Carga y Almacenamiento de Datos Replicado

Este proyecto es una aplicación web distribuida diseñada para permitir que los clientes registren sus nombres y vean actualizaciones en tiempo real. El sistema utiliza JGroups para la comunicación entre servicios backend y Spring para el balanceo de carga. Además, implementa un mecanismo de descubrimiento de servicios para registrar dinámicamente las instancias de backend con el balanceador de carga.

----------

## Características

-   **Registro de Clientes**: Los clientes pueden registrar sus nombres, que se almacenan junto con marcas de tiempo.
-   **Interacción en Tiempo Real**: Construida con JavaScript asincrónico para una experiencia fluida e inmediata.
-   **Balanceador de Carga**: Implementa una estrategia de _round-robin_ para distribuir las solicitudes de los clientes entre los servicios backend.
-   **Descubrimiento de Servicios**: Registra dinámicamente los servicios backend con el balanceador de carga, lo que permite escalabilidad.
-   **Almacenamiento de Datos Replicado**: Los servicios backend utilizan JGroups para garantizar un sistema consistente y tolerante a fallos.

----------

## Arquitectura

### Componentes

1.  **Balanceador de Carga (Spring)**:
    
    -   Distribuye las solicitudes de los clientes de manera uniforme utilizando una estrategia _round-robin_.
    -   Rastrea dinámicamente las instancias activas de los servicios backend mediante un mecanismo de descubrimiento.
2.  **Servicios Backend (JGroups)**:
    
    -   Utilizan una estructura de datos replicada para almacenar la información de los clientes.
    -   Sincronizan los datos en tiempo real entre las instancias.
3.  **Interfaz del Cliente**:
    
    -   Construida con JavaScript asincrónico.
    -   Proporciona una interfaz simple para las interacciones de los usuarios.

----------

## Explicación del Código

La lógica principal del backend está implementada en la clase `SimpleClass`. A continuación, se describe su funcionalidad:

### **`SimpleClass`**

    package AYGO.demo;
    
    import org.jgroups.*;
    
    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    
    public class SimpleClass implements Receiver {
        JChannel channel; 
        String user_name = System.getProperty("user.name", "n/a");
    
        public static void main(String[] args) throws Exception {
            new SimpleClass().start();
        }
    
        private void start() throws Exception {
            channel = new JChannel().setReceiver(this).connect("ChatCluster");
            eventLoop();
            channel.close();
        }
    
        private void eventLoop() {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    System.out.print("> "); System.out.flush();
                    String line = in.readLine().toLowerCase();
                    if (line.startsWith("quit") || line.startsWith("exit"))
                        break;
                    line = "[" + user_name + "] " + line;
                    Message msg = new ObjectMessage(null, line);
                    channel.send(msg);
                } catch (Exception e) {
                }
            }
        }
    
        public void viewAccepted(View new_view) {
            System.out.println("** view: " + new_view);
        }
    
        public void receive(Message msg) {
            System.out.println(msg.getSrc() + ": " + msg.getObject());
        }
    }

### Métodos Clave

-   **`start()`**:
    
    -   Conecta al clúster de JGroups llamado `ChatCluster`.
    -   Escucha mensajes entrantes y permite la interacción del usuario.
-   **`eventLoop()`**:
    
    -   Proporciona una interfaz de línea de comandos para enviar mensajes al clúster.
    -   Los mensajes se etiquetan con el nombre del usuario y se transmiten a los demás miembros.
-   **`viewAccepted(View new_view)`**:
    
    -   Reacciona a los cambios en el clúster, como la unión o salida de nuevos miembros.
-   **`receive(Message msg)`**:
    
    -   Maneja los mensajes recibidos de otros miembros del clúster.

----------

## Despliegue

Esta aplicación fue desplegada en una máquina virtual (VM) de Google Cloud y probada en un entorno distribuido. Los pasos para replicar el despliegue son los siguientes:

1.  **Clonar el Repositorio**:
    
    bash
    
    Copiar código
    
    `git clone <url-del-repositorio>
    cd AYGO_Novoa` 
    
2.  **Compilar la Aplicación**:
    
    bash
    
    Copiar código
    
    `javac -cp jgroups-5.x.jar:. AYGO/demo/SimpleClass.java` 
    
3.  **Ejecutar la Aplicación**:
    
    bash
    
    Copiar código
    
    `java -cp jgroups-5.x.jar:. AYGO.demo.SimpleClass` 
    
4.  **Configurar el Balanceador de Carga**:
    
    -   Desplegar el balanceador de carga basado en Spring con configuración _round-robin_.
    -   Asegurarse de que los servicios backend sean detectables.
5.  **Acceder a la Interfaz de Cliente**:
    
    -   Utilizar un navegador web para interactuar con la interfaz de cliente.
    -   Asegurarse de que JavaScript está habilitado para actualizaciones en tiempo real.

----------

## Tecnologías Utilizadas

-   **Java**: Lógica central de la aplicación.
-   **JGroups**: Comunicación y replicación entre servicios backend.
-   **Spring**: Balanceo de carga y descubrimiento de servicios.
-   **JavaScript**: Interacciones asincrónicas del cliente.
-   **Google Cloud**: Hospedaje en máquina virtual.

----------

## Mejoras Futuras

-   Mejorar la interfaz de cliente con un framework moderno (React o Angular).
-   Implementar un mecanismo más robusto de tolerancia a fallos para el backend.
-   Extender el descubrimiento de servicios con Kubernetes para un despliegue en contenedores.

----------

## Licencia

Este proyecto está licenciado bajo la Licencia MIT.
