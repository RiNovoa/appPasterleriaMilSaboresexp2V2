package com.example.proyectologin005d

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectologin005d.data.local.CatalogoProductoJson
import com.example.proyectologin005d.data.model.Post
import com.example.proyectologin005d.data.network.ApiService
import com.example.proyectologin005d.data.network.RetrofitInstance
import com.example.proyectologin005d.data.repository.ProductoRepository
import com.example.proyectologin005d.util.ValidationUtils
import com.example.proyectologin005d.viewmodel.CartViewModel
import com.example.proyectologin005d.viewmodel.NewsViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

// Fábrica de prueba para inyectar dependencias mockeadas
class TestViewModelFactory(
    private val application: Application,
    private val repository: ProductoRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(application, repository) as T
            }
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class AppTests {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- TESTS DE VALIDACIÓN (10 Tests) ---

    @Test
    fun `email valido retorna true`() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"))
    }

    @Test
    fun `email sin arroba retorna false`() {
        assertFalse(ValidationUtils.isValidEmail("testexample.com"))
    }

    @Test
    fun `email vacio retorna false`() {
        assertFalse(ValidationUtils.isValidEmail(""))
    }

    @Test
    fun `password corta retorna false`() {
        assertFalse(ValidationUtils.isValidPassword("12345"))
    }

    @Test
    fun `password larga retorna true`() {
        assertTrue(ValidationUtils.isValidPassword("123456"))
    }

    @Test
    fun `tarjeta valida de 16 digitos retorna true`() {
        assertTrue(ValidationUtils.isValidCardNumber("1234567812345678"))
    }

    @Test
    fun `tarjeta con letras retorna false`() {
        assertFalse(ValidationUtils.isValidCardNumber("123456781234abcd"))
    }

    @Test
    fun `fecha expiracion valida retorna true`() {
        assertTrue(ValidationUtils.isValidExpiryDate("1225"))
    }

    @Test
    fun `fecha expiracion mes invalido retorna false`() {
        assertFalse(ValidationUtils.isValidExpiryDate("1325"))
    }

    @Test
    fun `cvv valido de 3 digitos retorna true`() {
        assertTrue(ValidationUtils.isValidCvv("123"))
    }

    // --- TESTS DE CARRITO (CartViewModel) ---

    private fun createMockViewModelFactory(): TestViewModelFactory {
        val app = mockk<Application>(relaxed = true)
        val repo = mockk<ProductoRepository>(relaxed = true)
        
        // Mocks de flujos vacíos por defecto para evitar NullPointerException
        every { repo.obtenerCarrito() } returns MutableStateFlow(emptyList())
        every { repo.obtenerOrdenes() } returns MutableStateFlow(emptyList())
        every { repo.obtenerUltimaOrden() } returns MutableStateFlow(null)
        
        return TestViewModelFactory(app, repo)
    }

    @Test
    fun `carrito inicia vacio`() = runTest {
        val factory = createMockViewModelFactory()
        val viewModel = factory.create(CartViewModel::class.java)
        
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.cart.isEmpty())
    }

    // --- TESTS DE NOTICIAS (NewsViewModel) ---

    @Test
    fun `NewsViewModel carga noticias exitosamente`() = runTest {
        // Mockear ApiService
        val mockApi = mockk<ApiService>()
        val posts = listOf(Post(1, 1, "Test Title", "Test Body"))

        coEvery { mockApi.getPosts() } returns posts

        // Mockear el singleton RetrofitInstance para que devuelva nuestro mockApi
        mockkObject(RetrofitInstance)
        coEvery { RetrofitInstance.api } returns mockApi

        // Iniciar ViewModel usando la fábrica (aunque NewsViewModel no usa dependencias inyectadas aquí, 
        // usamos la fábrica para consistencia o instanciamos directo si es simple)
        val factory = createMockViewModelFactory()
        val viewModel = factory.create(NewsViewModel::class.java)

        // Avanzar el tiempo para que la corrutina se ejecute
        advanceUntilIdle()

        // Verificar
        assertEquals(1, viewModel.postList.value.size)
        assertEquals("Test Title", viewModel.postList.value[0].title)
    }

    @Test
    fun `NewsViewModel maneja error de API`() = runTest {
        val mockApi = mockk<ApiService>()
        coEvery { mockApi.getPosts() } throws Exception("API Error")

        mockkObject(RetrofitInstance)
        coEvery { RetrofitInstance.api } returns mockApi

        val factory = createMockViewModelFactory()
        val viewModel = factory.create(NewsViewModel::class.java)
        
        advanceUntilIdle()

        assertTrue(viewModel.postList.value.isEmpty())
    }
}
