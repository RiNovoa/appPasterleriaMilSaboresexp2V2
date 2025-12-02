# 🍰 Pastelería Mil Sabores

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Room](https://img.shields.io/badge/Room%20Database-FFA000?style=for-the-badge&logo=sqlite&logoColor=white)

> **Experiencia de pastelería digital completa: desde la selección del producto hasta la simulación de compra y gestión de inventario.**

---

## 🎓 Contexto Académico

| Campo | Detalle |
|:---|:---|
| **Institución** | Duoc UC - Ingeniería en Informática |
| **Asignatura** | Desarrollo de Aplicaciones Móviles |
| **Docente** | Vicente Zapata |
| **Año** | 2025 |

---

## 📱 Visión del Proyecto

**Pastelería Mil Sabores** no es solo un catálogo; es una simulación robusta de un ecosistema de comercio electrónico móvil. Diseñada bajo los estándares modernos de desarrollo en Android, esta aplicación implementa una arquitectura escalable que gestiona usuarios, sesiones, inventario en tiempo real y geolocalización.

### 🌟 Propuesta de Valor
*   **Persistencia Real:** Los datos no se pierden. Gracias a **Room Database**, el stock y los productos viven en el dispositivo.
*   **Experiencia de Usuario (UX):** Interfaz fluida construida 100% en **Jetpack Compose**, siguiendo los lineamientos de Material 3.
*   **Funcionalidad Completa:** Desde el login de usuario hasta el comprobante de pago detallado.

---

## 🛠️ Stack Tecnológico

El proyecto utiliza las últimas librerías estables del ecosistema Android:

| Categoría | Tecnología | Propósito |
|-----------|------------|-----------|
| **Core** | [Kotlin](https://kotlinlang.org/) (v2.0.21) | Lenguaje principal, robusto y conciso. |
| **UI** | [Jetpack Compose](https://developer.android.com/jetpack/compose) | Construcción de interfaces declarativas y animaciones. |
| **Navegación** | Navigation Compose | Gestión de flujo entre pantallas y paso de argumentos. |
| **Persistencia** | **[Room Database](https://developer.android.com/training/data-storage/room)** | Base de datos local SQLite abstraída para gestión de stock. |
| **Sesión** | DataStore Preferences | Almacenamiento ligero y asíncrono para credenciales y tokens. |
| **Red/Mapas** | Google Maps Embed API | Visualización interactiva de la sucursal física mediante WebView. |
| **Imágenes** | Coil | Carga y caché eficiente de imágenes asíncronas. |

---

## 🚀 Funcionalidades Clave

### 1. Gestión de Inventario Inteligente (Room) 📦
El sistema carga un catálogo inicial y gestiona el stock localmente.
*   **Validación de Stock:** No permite agregar al carrito más unidades de las disponibles.
*   **Persistencia:** Al cerrar la app, el stock modificado se mantiene.
*   **Estados:** Productos con stock 0 se visualizan automáticamente como "Agotado".

### 2. Ciclo de Compra Completo 🛒
*   **Carrito Dinámico:** Agregación, eliminación y cálculo de subtotales en tiempo real.
*   **Pasarela de Pago Simulada:** Validación de tarjeta de crédito y formulario seguro.
*   **Boleta Electrónica:** Generación de comprobante con ID único, fecha y detalle ítem por ítem.

### 3. Historial de Pedidos 📜
*   Registro histórico de todas las transacciones.
*   Visualización detallada de compras pasadas con formato de ticket desplegable.

### 4. Módulo de Contacto y Ubicación 📍
*   Mapa interactivo integrado apuntando a la sede real (Duoc UC Puente Alto).
*   Accesos directos a redes sociales y canales de atención.

---

## 📂 Arquitectura de Datos

El núcleo de la persistencia reside en la entidad `Producto`, mapeada directamente a una tabla SQL mediante Room:

```kotlin
@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true) 
    val id: Int = 0,
    val nombre: String,
    val precio: Int,
    val stock: Int,      // Controla la disponibilidad
    val imagen: String?, // Ruta al asset local
    val categoria: String?,
    val descripcion: String?
)
```

---

## ⚙️ Instalación y Despliegue

### Requisitos Previos
*   **Android Studio:** Versión Koala, Ladybug o superior.
*   **JDK:** Versión 17.
*   **Dispositivo:** Emulador o físico con Android 7.0 (API 24) o superior.

### Pasos
1.  **Clonar el proyecto:**
    ```bash
    git clone https://github.com/crisperezzz/appPasterleriaMilSaboresexp2.git
    ```
2.  **Sincronizar:** Abrir en Android Studio y permitir la sincronización de Gradle.
3.  **Ejecutar:** Seleccionar el módulo `app` y correr en el emulador.

> **Nota:** Al iniciar por primera vez, la aplicación poblará automáticamente la base de datos local con los datos del archivo `Pasteles.json`.

---

## 👥 Equipo de Desarrollo

<div align="center">

| **Cristóbal Pérez** | **Ricardo Novoa** | **Javier Rojas** |
|:---:|:---:|:---:|
| *Desarrollador Android* | *Desarrollador Android* | *Desarrollador Android* |

</div>

---
*Desarrollado con ❤️ y Kotlin en Santiago, Chile.*
