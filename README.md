# Yugen Store: E-commerce Enterprise con Integración CRM y Despliegue Cloud

## Tecnologías
* **Backend:** Java 17, Spring Boot, Spring Security (JWT/Session).
* **Persistencia:** Spring Data JPA con Oracle Autonomous Database (uso de Oracle Wallet).
* **Storage:** Oracle Cloud Object Storage (Buckets) para almacenamiento escalable de imágenes de productos.
* **Frontend:** Thymeleaf (Server-side rendering).
* **Infraestructura:** Docker & Docker Compose, Nginx Proxy Manager, Oracle Cloud (OCI).
* **Integraciones:** Salesforce API (Service Layer).

## Análisis de Datos y Business Intelligence (BI)
Como aspirante a **Analista de Datos**, este proyecto se enfocó en el ciclo de vida completo del dato:
* **Pipeline de Datos (ETL):** Extracción desde el core de la aplicación hacia Tableau Desktop.
* **Consistencia:** Uso de `Random Seed` para generación de datos sintéticos reproducibles.
* **Visualización Avanzada:** Dashboard interactivo en Tableau que analiza:
    * **Distribución Geográfica:** Mercado por regiones (Mendoza, Rosario, Córdoba, entre otras).
    * **Rendimiento de Productos:** Análisis de ingresos por categorías.
    * **Comportamiento del Cliente:** Participación de mercado por género y método de pago.
      
<img width="1365" height="767" alt="Dashboard de Ventas" src="https://github.com/user-attachments/assets/bb4034f1-c25b-462d-a9a1-e21dedb7792d" />

**Acceso al Tablero Interactivo:** [Ver Dashboard en Tableau Public](https://public.tableau.com/app/profile/nicolas.carrizo/viz/E-commerceDashboarddeVentas/DashboarddeVentas?publish=yes)

### Flujo de Datos y Arquitectura
La arquitectura está diseñada para separar las responsabilidades y garantizar la escalabilidad:

1. **Capa de Aplicación (Backend):** Spring Boot gestiona la lógica de negocio y la seguridad con Spring Security.
2. **Capa de Datos (Persistencia):** Los datos transaccionales residen en **Oracle Autonomous DB**, mientras que los archivos multimedia (fotos de productos) se sirven directamente desde **Oracle Object Storage** para optimizar el rendimiento del servidor.
3. **Capa de Integración:** Se utiliza una Service Layer para la sincronización de clientes y ventas con **Salesforce API**.
4. **Capa de Inteligencia (BI):** Los datos se extraen y procesan para alimentar un dashboard interactivo en **Tableau**, permitiendo la toma de decisiones basada en métricas reales de la tienda.

## Desafíos Técnicos Resueltos
* **Arquitectura Cloud:** Implementación de **Oracle Object Storage** para desacoplar los assets multimedia de la lógica de aplicación. Despliegue automatizado mediante Docker Compose con una arquitectura de microservicios aislada.
* **Redes y Seguridad:** Configuración de **Reverse Proxy** con Nginx para gestión de SSL (Let's Encrypt) y aislamiento de contenedores.
* **Optimización Linux:** Configuración de **Memory Swap** para estabilizar la JVM en instancias con recursos limitados.

## Demo en Vivo
Puedes acceder a la aplicación desplegada en Oracle Cloud aquí: 
[https://yugen-store.duckdns.org/](https://yugen-store.duckdns.org/)

## Credenciales de Acceso (Demo)

| Rol | Usuario | Contraseña | Acceso a... |
| :--- | :--- | :--- | :--- |
| **Cliente** | `cliente@gmail.com` | `123456` | Flujo de compra y carrito |
| **Administrador** | `admin@yugen.com` | `Yugen2026` | Dashboard de Salesforce y Reportes |

> [!IMPORTANT]
> **Cómo ver los gráficos de BI en la App:**
> Para visualizar la integración de Tableau dentro de la plataforma:
> 1. Inicie sesión con la cuenta de **Administrador**.
> 2. Diríjase a **Cuenta** > **Mi Perfil**.
> 3. Haga clic en el botón **"Ver gráficos"**.

## Roadmap
* **Analítica Predictiva:** Implementación de modelos de stock basados en historial de ventas.
* **Optimización de Capas:** Refactorización de DTOs para mejorar la eficiencia del transporte de datos.
* **Módulo Administrativo:** Refactorización de las funciones de persistencia (Guardar/Eliminar) para implementar un manejo de excepciones robusto y evitar páginas de error genéricas (Error 404/500).
* **Gestión de Inventario en Tiempo Real:** Finalización de la integración de alertas de stock con Salesforce para asegurar la sincronización bidireccional entre el core de Java y el CRM.

## Contacto

¡Estoy abierto a nuevas oportunidades y colaboraciones! Podés encontrarme en:

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/nicolascarrizo938/)
[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:carrizonicolasd8@gmail.com)

> Si querés ver el código en acción o discutir la arquitectura de datos, no dudes en escribirme.
