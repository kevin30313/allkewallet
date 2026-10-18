package com.kevin30313.alkewallet.service;

import com.kevin30313.alkewallet.dto.LoginRequest;
import com.kevin30313.alkewallet.dto.RegisterRequest;
import com.kevin30313.alkewallet.dto.UserResponseDTO;
import com.kevin30313.alkewallet.exception.UserNotFoundException;
import com.kevin30313.alkewallet.model.User;
import com.kevin30313.alkewallet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private AccountServiceClient accountServiceClient;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("kevin");
        registerRequest.setEmail("kevin@test.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("kevin@test.com");
        loginRequest.setPassword("password123");

        // Simulamos la entidad User como un Mock para evitar depender de setters ausentes
        user = mock(User.class);
        lenient().when(user.getId()).thenReturn(1L);
        lenient().when(user.getUsername()).thenReturn("kevin");
        lenient().when(user.getEmail()).thenReturn("kevin@test.com");
        lenient().when(user.getPassword()).thenReturn("encodedPassword");
    }

    @Nested
    @DisplayName("Pruebas de Registro de Usuario")
    class RegisterTests {

        @Test
        @DisplayName("Debería registrar un usuario exitosamente y crear su cuenta asociada")
        void registrarUsuario_Exito() {
            when(userRepository.findByUsername("kevin")).thenReturn(Optional.empty());
            when(userRepository.findByEmail("kevin@test.com")).thenReturn(Optional.empty());
            when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);

            UserResponseDTO response = authService.register(registerRequest);

            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getUsername()).isEqualTo("kevin");
            assertThat(response.getEmail()).isEqualTo("kevin@test.com");

            verify(userRepository, times(1)).save(any(User.class));
            verify(accountServiceClient, times(1)).createAccount(1L);
        }

        @Test
        @DisplayName("Debería lanzar excepción si el nombre de usuario ya está en uso")
        void registrarUsuario_UsernameDuplicado() {
            when(userRepository.findByUsername("kevin")).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> authService.register(registerRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("ya está en uso");

            verify(userRepository, never()).save(any(User.class));
            verify(accountServiceClient, never()).createAccount(any());
        }

        @Test
        @DisplayName("Debería lanzar excepción si el correo ya está registrado")
        void registrarUsuario_EmailDuplicado() {
            when(userRepository.findByUsername("kevin")).thenReturn(Optional.empty());
            when(userRepository.findByEmail("kevin@test.com")).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> authService.register(registerRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("ya está registrado");

            verify(userRepository, never()).save(any(User.class));
            verify(accountServiceClient, never()).createAccount(any());
        }
    }

    @Nested
    @DisplayName("Pruebas de Autenticación (Login)")
    class LoginTests {

        @Test
        @DisplayName("Debería autenticar correctamente y retornar un token JWT")
        void login_Exito() {
            Authentication authentication = mock(Authentication.class);
            when(authentication.getName()).thenReturn("kevin@test.com");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(userRepository.findByEmail("kevin@test.com")).thenReturn(Optional.of(user));
            when(jwtService.generateToken("kevin@test.com", 1L)).thenReturn("mocked-jwt-token");

            String token = authService.login(loginRequest);

            assertThat(token).isEqualTo("mocked-jwt-token");
            verify(authenticationManager, times(1)).authenticate(any());
        }

        @Test
        @DisplayName("Debería lanzar excepción si el usuario autenticado no se encuentra en la base de datos")
        void login_UsuarioNoEncontrado() {
            Authentication authentication = mock(Authentication.class);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(userRepository.findByEmail("kevin@test.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Usuario no encontrado");
        }
    }

    @Nested
    @DisplayName("Pruebas de Consulta de Perfil")
    class ProfileTests {

        @Test
        @DisplayName("Debería retornar los datos del usuario al buscar por username")
        void obtenerUsuarioPorUsername_Exito() {
            when(userRepository.findByUsername("kevin")).thenReturn(Optional.of(user));

            UserResponseDTO response = authService.getUserByUsername("kevin");

            assertThat(response).isNotNull();
            assertThat(response.getUsername()).isEqualTo("kevin");
            assertThat(response.getEmail()).isEqualTo("kevin@test.com");
        }

        @Test
        @DisplayName("Debería lanzar UserNotFoundException si el username no existe")
        void obtenerUsuarioPorUsername_NoEncontrado() {
            when(userRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> authService.getUserByUsername("noexiste"))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("Usuario no encontrado");
        }
    }
}