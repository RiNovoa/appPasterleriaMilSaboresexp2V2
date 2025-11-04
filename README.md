# 🍰 Pastelería Mil Sabores — Aplicación Android

**Asignatura:** Desarrollo de Aplicaciones Móviles  
**Institución:** Duoc UC  
**Carrera:** Ingeniería en Informática  
**Autores:** Cristóbal Pérez / Ricardo Novoa / Javier Rojas  
**Profesor guía:** Vicente Zapata  
**Año:** 2025

---

## 📱 Descripción general

**Pastelería Mil Sabores** es una aplicación móvil nativa para Android desarrollada con **Kotlin** y **Jetpack Compose**, que permite a los usuarios registrarse, iniciar sesión y acceder a un catálogo interactivo de productos de repostería.  
Además, integra un sistema de beneficios por usuario, manejo de sesiones persistentes y la opción de actualizar la foto de perfil mediante cámara o galería.

El objetivo del proyecto es simular una tienda real de pastelería digital, enfocada en la experiencia de usuario, la navegación intuitiva y la correcta aplicación de los principios de arquitectura moderna en Android.

---

## 🧩 Características principales

- **Registro e inicio de sesión** con almacenamiento de usuarios en archivo local JSON.  
- **Manejo de sesión persistente** mediante **DataStore Preferences**.  
- **Pantalla de Perfil** con edición de foto desde **cámara o galería**.  
- **Catálogo de productos** dinámico (lista de pasteles cargados desde `assets/database/Pasteles.json`).  
- **Sistema de descuentos y beneficios:**
- **Diseño moderno y responsivo** utilizando **Material 3 (Material You)**.  
- **Navegación con BottomBar y DrawerMenu** entre secciones:
  - Inicio
  - Productos
  - Carrito
  - Nosotros
  - Contáctanos
  - Perfil

---

## 🧱 Tecnologías utilizadas

| Tecnología / Librería | Uso principal |
|------------------------|----------------|
| **Kotlin** | Lenguaje base del proyecto |
| **Jetpack Compose** | UI declarativa moderna |
| **Material 3** | Componentes visuales y estilo |
| **Navigation Compose** | Navegación entre pantallas |
| **DataStore Preferences** | Manejo de sesión de usuario |
| **Kotlinx Serialization / Gson** | Lectura y escritura de JSON |
| **Coil Compose** | Carga de imágenes |
| **CameraX** | Acceso a cámara del dispositivo |
| **Gradle KTS** | Sistema de compilación y dependencias |

---

## 🧠 Arquitectura del proyecto

El proyecto sigue una **estructura modular y limpia** orientada a MVVM:

app/
├── data/
│ ├── model/ → Modelos de datos (User, Pastel)
│ ├── database/ → DataStore para sesión de usuario
│ ├── repository/ → Lógica de autenticación (AuthRepository)
│
├── ui/
│ ├── pages/ → Pantallas principales (Login, Perfil, Productos, etc.)
│ ├── theme/ → Configuración de colores, tipografía y estilos
│
├── navigation/ → Estructura del NavHost y rutas
├── viewmodel/ → ViewModels (por ejemplo, ProductoViewModel)
├── view/ → Componentes UI reutilizables
└── MainActivity.kt → Punto de entrada de la aplicación


---

## ⚙️ Requisitos de ejecución

- **Android Studio Giraffe / Koala o superior**
- **Gradle 8.5+**
- **SDK mínimo:** 24 (Android 7.0 Nougat)
- **SDK objetivo:** 34 (Android 14)
- **Emulador recomendado:** Pixel 6 (API 34)

---

## 🚀 Cómo ejecutar el proyecto

1. Clonar el repositorio:  
   ```bash
   git clone https://github.com/crisperezzz/appPasterleriaMilSaboresexp2.git
Abrir la carpeta del proyecto en Android Studio.

Esperar a que Gradle sincronice dependencias.

Conectar un dispositivo físico o iniciar un emulador Android.

Ejecutar desde Run ▶️ → app.

🧾 Funcionalidades destacadas
🔐 Autenticación de usuarios
Registro local con validaciones básicas (correo, contraseña, edad).

Persistencia de sesión con DataStore.

Cierre de sesión manual desde Perfil.

🛍️ Catálogo de productos
Listado de pasteles y tortas con nombre, precio y categoría.

Carga automática desde un archivo Pasteles.json en la carpeta assets/database/.

👤 Perfil del usuario
Muestra el nombre y correo actual.

Permite actualizar la foto de perfil mediante cámara o galería.

Botón de cierre de sesión con limpieza de DataStore.

🎨 Interfaz moderna
Diseño con Material 3 y colores personalizados.

Soporte para modo claro / oscuro.

Navegación fluida y adaptada a pantallas pequeñas.

🧪 Testing y depuración
Se realizaron pruebas de navegación y persistencia en emuladores Android 13 y 14.

Los crashes más comunes (serialización, sesión nula) fueron corregidos.

Validación completa de flujo de autenticación.

📂 Estructura de datos
Usuarios.json
Ejemplo de usuario registrado:


[
  {
    "id": 1,
    "nombre": "Cristóbal",
    "apellido": "Pérez",
    "correo": "crisperez@duoc.cl",
    "contrasena": "1234",
    "role": "user"
  }
]
Pasteles.json
Ejemplo de producto:

[
  {
    "id": 1,
    "nombre": "Torta de Chocolate",
    "precio": 5000,
    "stock": 10,
    "imagen": "torta_chocolate.jpg",
    "categoria": "Tortas",
    "descripcion": "Bizcocho húmedo con relleno de ganache."
  }
]
💬 Conclusiones
El proyecto Pastelería Mil Sabores demuestra la aplicación práctica de los conceptos de desarrollo móvil nativo, integrando UI moderna, almacenamiento local, persistencia de sesión y manejo de recursos multimedia.
El trabajo refleja buenas prácticas de arquitectura, modularidad y experiencia de usuario, alineadas con los resultados de aprendizaje de la asignatura.
