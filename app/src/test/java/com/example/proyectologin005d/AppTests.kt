package com.example.proyectologin005d

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectologin005d.data.model.Post
import com.example.proyectologin005d.data.network.PostApiService
import com.example.proyectologin005d.data.network.PostRetrofit
import com.example.proyectologin005d.data.repository.NewsRepository
import com.example.proyectologin005d.data.repository.ProductoRepository
import com.example.proyectologin005d.util.ValidationUtils
import com.example.proyectologin005d.viewmodel.CartViewModel
import com.example.proyectologin005d.viewmodel.NewsViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import android.util.Log

// Fábrica de prueba para inyectar dependencias mockeadas
class TestViewModelFactory(
    private val application: Application,
    private val repository: ProductoRepository,
    private val newsRepository: NewsRepository // Add NewsRepository to factory
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel(application, repository) as T
            }
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                // Use reflection to set the private repository field or use a constructor if refactored.
                // Since we cannot change NewsViewModel constructor easily without breaking Dagger/Hilt or manual DI elsewhere if not prepared,
                // we will use the mocked Retrofit which NewsRepository uses internally.
                // Wait, NewsViewModel instantiates NewsRepository internally: private val repository = NewsRepository()
                // And NewsRepository uses PostRetrofit.api directly.
                // So mocking PostRetrofit object IS the way to go.
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
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- TESTS DE VALIDACIÓN ---

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
        val newsRepo = mockk<NewsRepository>(relaxed = true)
        
        // Mocks de flujos vacíos por defecto para evitar NullPointerException
        every { repo.obtenerCarrito() } returns MutableStateFlow(emptyList())
        every { repo.obtenerOrdenes() } returns MutableStateFlow(emptyList())
        every { repo.obtenerUltimaOrden() } returns MutableStateFlow(null)
        
        return TestViewModelFactory(app, repo, newsRepo)
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
        // Mockear ApiService (NO PostApiService)
        val mockApi = mockk<PostApiService>()
        val posts = listOf(Post(1, 1, "Test Title", "Test Body"))

        coEvery { mockApi.getPosts() } returns posts

        // Mockear el singleton PostRetrofit
        mockkObject(PostRetrofit)
        every { PostRetrofit.api } returns mockApi

        // Iniciar ViewModel usando la fábrica
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
        val mockApi = mockk<PostApiService>()
        coEvery { mockApi.getPosts() } throws Exception("API Error")

        mockkObject(PostRetrofit)
        every { PostRetrofit.api } returns mockApi

        val factory = createMockViewModelFactory()
        val viewModel = factory.create(NewsViewModel::class.java)
        
        advanceUntilIdle()

        assertTrue(viewModel.postList.value.isEmpty())
    }
}
