package com.example.proyectologin005d

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.proyectologin005d.data.local.CatalogoProductoJson
import com.example.proyectologin005d.data.model.Post
import com.example.proyectologin005d.data.network.ApiService
import com.example.proyectologin005d.data.network.RetrofitInstance
import com.example.proyectologin005d.data.repository.NewsRepository
import com.example.proyectologin005d.util.ValidationUtils
import com.example.proyectologin005d.viewmodel.CartViewModel
import com.example.proyectologin005d.viewmodel.NewsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    // --- TESTS DE CARRITO (CartViewModel) (6 Tests) ---

    @Test
    fun `carrito inicia vacio`() {
        val viewModel = CartViewModel()
        assertTrue(viewModel.uiState.value.cart.isEmpty())
    }

    @Test
    fun `agregar producto al carrito aumenta tamaño`() {
        val viewModel = CartViewModel()
        // Constructor corregido: id, nombre, precio, stock, imagen, categoria, descripcion
        val producto = CatalogoProductoJson(1, "Pastel", 1000, 10, "img", "Cat", "Rico")
        viewModel.addToCart(producto)
        assertEquals(1, viewModel.uiState.value.cart.size)
    }

    @Test
    fun `agregar mismo producto aumenta cantidad`() {
        val viewModel = CartViewModel()
        val producto = CatalogoProductoJson(1, "Pastel", 1000, 10, "img", "Cat", "Rico")
        viewModel.addToCart(producto)
        viewModel.addToCart(producto)
        assertEquals(1, viewModel.uiState.value.cart.size)
        assertEquals(2, viewModel.uiState.value.cart[0].quantity)
    }

    @Test
    fun `calcular total del carrito es correcto`() {
        val viewModel = CartViewModel()
        val producto = CatalogoProductoJson(1, "Pastel", 1000, 10, "img", "Cat", "Rico")
        viewModel.addToCart(producto)
        viewModel.addToCart(producto) // 2 * 1000 = 2000
        assertEquals(2000, viewModel.uiState.value.total)
    }

    @Test
    fun `remover producto disminuye cantidad`() {
        val viewModel = CartViewModel()
        val producto = CatalogoProductoJson(1, "Pastel", 1000, 10, "img", "Cat", "Rico")
        viewModel.addToCart(producto)
        viewModel.addToCart(producto)
        viewModel.removeFromCart(producto)
        assertEquals(1, viewModel.uiState.value.cart[0].quantity)
    }

    @Test
    fun `limpiar carrito lo deja vacio`() {
        val viewModel = CartViewModel()
        val producto = CatalogoProductoJson(1, "Pastel", 1000, 10, "img", "Cat", "Rico")
        viewModel.addToCart(producto)
        viewModel.clearCart()
        assertTrue(viewModel.uiState.value.cart.isEmpty())
    }

    // --- TESTS DE NOTICIAS (NewsViewModel) (2 Tests) ---

    @Test
    fun `NewsViewModel carga noticias exitosamente`() = runTest {
        // Mockear ApiService
        val mockApi = mockk<ApiService>()
        val posts = listOf(Post(1, 1, "Test Title", "Test Body"))
        
        coEvery { mockApi.getPosts() } returns posts

        // Mockear el singleton RetrofitInstance para que devuelva nuestro mockApi
        mockkObject(RetrofitInstance)
        coEvery { RetrofitInstance.api } returns mockApi

        // Iniciar ViewModel
        val viewModel = NewsViewModel()
        
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

        val viewModel = NewsViewModel()
        advanceUntilIdle()

        assertTrue(viewModel.postList.value.isEmpty())
    }

    // --- NUEVOS TESTS ADICIONALES (2 Tests) ---

    @Test
    fun `remover producto unico lo elimina del carrito`() {
        val viewModel = CartViewModel()
        val producto = CatalogoProductoJson(1, "Pastel", 1000, 10, "img", "Cat", "Rico")
        viewModel.addToCart(producto)
        viewModel.removeFromCart(producto)
        assertTrue(viewModel.uiState.value.cart.isEmpty())
    }

    @Test
    fun `total se actualiza al agregar diferentes productos`() {
        val viewModel = CartViewModel()
        val p1 = CatalogoProductoJson(1, "P1", 1000, 10, "img", "Cat", "D1")
        val p2 = CatalogoProductoJson(2, "P2", 2000, 10, "img", "Cat", "D2")

        viewModel.addToCart(p1)
        viewModel.addToCart(p2)

        // 1000 + 2000 = 3000
        assertEquals(3000, viewModel.uiState.value.total)
    }
}
